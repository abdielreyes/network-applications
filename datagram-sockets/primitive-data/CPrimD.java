import java.net.*;
import java.io.*;

public class CPrimD {
	public static void main(String[] args) {
		try {
				
			int pto = 2000;
			InetAddress dst = InetAddress.getByName("127.0.0.1");
			DatagramSocket cl = new DatagramSocket();
			//creamos el socket cliente para enviar datos
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//baos para enviar paquetes de bytes en trozos
			DataOutputStream dos = new DataOutputStream(baos);
			dos.write(4);
			dos.flush();
			dos.writeFloat(4.1f);
			dos.flush();
			dos.writeLong(72);
			dos.flush();
			byte[] b = baos.toByteArray();
			DatagramPacket p = new DatagramPacket(b,b.length,dst,pto);
			cl.send(p);
			cl.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
