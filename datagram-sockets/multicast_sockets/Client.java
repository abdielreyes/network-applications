import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
public class Client {
    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException{
        System.out.printf("Interfaz: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses= netint.getInetAddresses();
        for(InetAddress inetAddress: Collections.list(inetAddresses)){
            System.out.printf("InetAddress: %s\n", inetAddress);
        }
        System.out.printf("\n");
    }
    public static void main(String[] args) {
        int pto=2000;
            String hhost="230.0.0.1";
            SocketAddress remote=null;
        try{
            try{
                remote= new InetSocketAddress(hhost, pto);
            }catch(Exception e){
                e.printStackTrace();
            }//catch
						//pbtenemos las interfaces de red disponibles
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface netint: Collections.list(nets))
                displayInterfaceInformation(netint);
						//cambiamos el tipo de interfaz
            NetworkInterface ni = NetworkInterface.getByName("lo");
            DatagramChannel cl = DatagramChannel.open(StandardProtocolFamily.INET);
            cl.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            cl.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            cl.configureBlocking(false);
						//configuramos la difusion del socket
            Selector sel= Selector.open();
            cl.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            InetAddress group= InetAddress.getByName("230.0.0.1");
            cl.join(group, ni);
            ByteBuffer b = ByteBuffer.allocate(4);
            int n=0;
            while(n<100){
							//ennviamos los paquetes
                sel.select();
                Iterator<SelectionKey>it= sel.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey k = (SelectionKey)it.next();
                    it.remove();
                    if(k.isWritable()){
											//enviamos los paquetes que le corresponde a cada canal
                        DatagramChannel ch = (DatagramChannel)k.channel();
                        b.clear();
                        b.putInt(n++);
                        b.flip();
                        ch.send(b, remote);
                        continue;
                    }
                }//while
            }//while
            cl.close();
            System.out.println("Termina el envio de datagramas");
        }catch(Exception e){
            e.printStackTrace();
        }//catch   
    }//main
}

