import java.net.*;
import java.io.*;
public class Server {
	public static void main(String[] args) {
		try {
			DatagramSocket d = new DatagramSocket(2000);
			while(true){

				DatagramPacket pr = new DatagramPacket(new byte[2000],2000);
				d.receive(pr);
				System.out.println("Datagram received from "+pr.getAddress());
				String message = new String(pr.getData(),0,pr.getLength());
				System.out.println("client says: " + message);

				System.out.println("Write a message");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String mensaje = br.readLine();
				byte[] data = mensaje.getBytes();
				DatagramPacket ps = new DatagramPacket(data,data.length,InetAddress.getByName("127.0.0.1"),2001);

				d.send(ps);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
