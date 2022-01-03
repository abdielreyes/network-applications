import java.net.*;
import java.io.*;
/*
		Reyes Rodriguez Enrique Abdiel
		3CV17 ACR
 */
public class Client {
	public static void main(String args[]) {
		try {

			Socket sClient = new Socket("localhost", 1234);//creamos el socket cliente

			BufferedReader inStream = new BufferedReader(new InputStreamReader(sClient.getInputStream()));//creamos el flujo de entrada, proveniente del cliente 
			PrintWriter outStream = new PrintWriter(sClient.getOutputStream(),true);//Creamos el flujo de salida del cliente y le hacemos el flush automatico

			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));//Creamos el flujo de entrada del usuario
			
			System.out.print("~> ");
			String messageOut = userInput.readLine(); //Leemos una linea del usuario
			outStream.println(messageOut);//Esta se envia por el flujo de salida
			String messageIn = inStream.readLine();//Esperamos a recibir datos por el flujo de entrada para poder imprimirlos
			System.out.println(messageIn + " <~");
				
			//Cerramos sockets y limpiamos flujos
			sClient.close();
			inStream.close();
			outStream.flush();
			outStream.close();


		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

	}
}
