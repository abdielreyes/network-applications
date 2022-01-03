import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Client{
	public static void main(String[] args) {
		final int MAX_DATAGRAM_SIZE = 2000;
		final InetAddress host = InetAddress.getByName("127.0.0.1");
		try {
			DatagramSocket d = new DatagramSocket(2001);
			while(true){
				System.out.println("Write a message");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String mensaje = br.readLine();
				byte[] data = mensaje.getBytes();
				
				long bytesRestantes = data.length;
				
            int byteDeInicioActual = 0;
            int tamanio = 0;
            while(bytesRestantes > 0){                
                 DatagramPacket p = new DatagramPacket(data,byteDeInicioActual,tamanio,host,2000); //Creo un paquete de datagrama, le pongo los bytes del mensaje y el destino
                 d.send(p); //Lo envio con el socket
                 bytesRestantes-=tamanio; //Le resto lo que ya envie
                 byteDeInicioActual+=tamanio; //Le sumo lo que envie al offset
                 tamanio = Math.min(5, (int)bytesRestantes); //Obtengo el tamanio del siguiente chunk   
            }

				DatagramPacket ps = new DatagramPacket(data,data.length,host,MAX_DATAGRAM_SIZE);

				System.out.println(mensaje+" "+data.length+" "+count);
				d.send(ps);

				DatagramPacket pr = new DatagramPacket(new byte[2000],2000);
				d.receive(pr);
				System.out.println("Datagram received from "+pr.getAddress());
				String message = new String(pr.getData(),0,pr.getLength());
				System.out.println("client says: " + message);

			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
