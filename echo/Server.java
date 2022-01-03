import java.net.*;
import java.io.*;
/*
    Reyes Rodriguez Enrique Abdiel
    3CV17 ACR
 */
public class Server {
	public static void main(String args[]) {
		try {

			ServerSocket sServer = new ServerSocket(1234); //Se crea el socket servidor en el puerto 1234
			System.out.println("Waiting for client");

			Socket sClient = sServer.accept(); //Creamos un socket cliente para poder recibir
			System.out.println("Connection stablished from: " + sClient.getInetAddress() +":"+ sClient.getPort());

			BufferedReader inStream = new BufferedReader(new InputStreamReader(sClient.getInputStream())); //Creamos el flujo de entrada
			PrintWriter outStream = new PrintWriter(sClient.getOutputStream(),true);//Creamos el flujo de salida y establecemos que tenga un flush automatico 

			
			String messageIn = inStream.readLine(); //Del cliente esperamos a recibir una linea
			System.out.println("client ~> "+ messageIn);
			outStream.println(messageIn);//Esa misma linea recibida, la enviamos denuevo
			
			//Cerramos sockets y limpiamos flujos
			sClient.close();
			sServer.close();
			inStream.close();
			outStream.flush();
			outStream.close();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
