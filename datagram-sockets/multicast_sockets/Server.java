import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class Server {

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException{
        //Obtenemos los datos de la interfaz de red a utilizar
        System.out.printf("Interfaz: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for(InetAddress inetAddress: Collections.list(inetAddresses)){
            System.out.printf("InetAddress: %s\n", inetAddress);
        }
        System.out.printf("\n");
    }
    public static void main(String[] args) {
        try{
            int pto=2000;
            Enumeration<NetworkInterface> nets =NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface netint: Collections.list(nets))
                displayInterfaceInformation(netint);
            NetworkInterface ni = NetworkInterface.getByName("lo");
            InetSocketAddress dir= new InetSocketAddress(pto);
            //configuramos la difusion del socket
            DatagramChannel s =DatagramChannel.open(StandardProtocolFamily.INET);
            s.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            s.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            InetAddress group = InetAddress.getByName("230.0.0.1");
            s.join(group, ni);
            s.configureBlocking(false);
            //inicializamos el socket
            s.socket().bind(dir);
            //creamos los selectores para el multicast
            Selector sel= Selector.open();
            s.register(sel, SelectionKey.OP_READ);
            ByteBuffer b = ByteBuffer.allocate(4);
            System.out.println("Servidor listo.. Esperando datagramas...");
            while(true){
                sel.select();
                Iterator<SelectionKey>it= sel.selectedKeys().iterator();
                while(it.hasNext()){
                    //recibimos los datos recibidos por los iteradores de el canal correspondiente
                    SelectionKey k = (SelectionKey)it.next();
                    it.remove();
                    if(k.isReadable()){
                        DatagramChannel ch = (DatagramChannel)k.channel();
                        b.clear();
                        SocketAddress emisor = ch.receive(b);
                        b.flip();
                        InetSocketAddress d = (InetSocketAddress)emisor;
                        //dato recibido
                        System.out.println("Datagrama recibido desde "+ d.getAddress()+":"+d.getPort());
                        System.out.println("Dato: "+b.getInt());
                        continue;
                    }
                }//while
            }//while
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}

