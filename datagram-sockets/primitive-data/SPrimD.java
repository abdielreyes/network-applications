import java.net.*;
import java.io.*;

public class SPrimD{
	public static void main (String args[]){
		try {
			DatagramSocket s = new DatagramSocket(2000);
			System.out.println("Servidor iniciado,  esperando cliente");
			for(;;){
				//siempre recibe datos de clientes que se van conectando
				DatagramPacket p = new DatagramPacket(new byte[2000],2000);
				s.receive(p);
				System.out.println("Datagrama recibido desde"+p.getAddress()+":"+p.getPort());
				DataInputStream dis= new DataInputStream(new ByteArrayInputStream(p.getData()));
				//recibir los datos del cliente
				int x = dis.readInt();
				float f = dis.readFloat();
				long z = dis.readLong();
				System.out.println("\n\nEntero:"+ x + " Flotante:"+f+" Entero largo:"+z);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
