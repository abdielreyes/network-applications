import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class UDPServidor{
	public final static int puerto = 2007;
	public final static int tam_maximo = 65507;

	public static void main(String[] args){
		int port = puerto;
		try {
			//creamos un canal de conexion
			DatagramChannel canal = DatagramChannel.open();
			canal.configureBlocking(false);
			DatagramSocket socket = canal.socket();
			SocketAddress dir = new InetSocketAddress(port);
			socket.bind(dir);
			//aceptamos conexiones entrantes
			Selector selector = Selector.open();
			canal.register(selector,SelectionKey.OP_READ);
			ByteBuffer buffer = ByteBuffer.allocateDirect(tam_maximo);

			while(true){
				//recibimos los datos del selector 
				selector.select(5000);
				Set sk = selector.selectedKeys();
				Iterator it = sk.iterator();
				while(it.hasNext()){
					//por cada item del selector recibimos los datos del cliente
					SelectionKey key = (SelectionKey)it.next();
					it.remove();
					if(key.isReadable()){
						buffer.clear();
						SocketAddress client = canal.receive(buffer);
						buffer.flip();
						int eco = buffer.getInt();
						if(eco == 1000){
							//los datos ya fueron enviados en su totalidad
							canal.close();
							System.exit(0);
						}else{
							System.out.println("Dato leido"+eco);
							buffer.flip();
							canal.send(buffer,client);
						}
					}
				}
			}
		} catch (Exception e) {
			//TODO: handle exception
			e.printStackTrace();
		}
	}
}
