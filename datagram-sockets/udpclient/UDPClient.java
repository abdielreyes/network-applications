import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

public class UDPClient{
	public final static int puerto = 2007;
	private final static int limite = 100;
	public static void main(String[] args){
		boolean bandera = false;
		SocketAddress remoto = new InetSocketAddress("127.0.0.1",puerto);
		try {
			//creamos un canal de conexion
			DatagramChannel canal = DatagramChannel.open();
			canal.configureBlocking(false);
			canal.connect(remoto);
			Selector selector = Selector.open();
			canal.register(selector, SelectionKey.OP_WRITE);
			ByteBuffer buffer = ByteBuffer.allocateDirect(4);
			int n = 0;
			while(true){
				selector.select(5000);
				Set sk = selector.selectedKeys();
				if(sk.isEmpty() && n == limite || bandera){
					canal.close();
					break;
				}
				else{
					Iterator it = sk.iterator();
					while(it.hasNext()){
						//mandamos los datos del selector
						SelectionKey key = (SelectionKey)it.next();
						it.remove();
						if(key.isWritable()){
							//mandamos los datos
							buffer.clear();
							buffer.putInt(n);
							buffer.flip();
							canal.write(buffer);
							System.out.println("Escribiendo el dato"+n);
							n++;
							if(n==limite){
								//todos los paquetes han sido escritos
								buffer.clear();
								buffer.putInt(1000);
								buffer.flip();
								canal.write(buffer);
								bandera = true;
								key.interestOps(SelectionKey.OP_READ);
								break;
							}
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
