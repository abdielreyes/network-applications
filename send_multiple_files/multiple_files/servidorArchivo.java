import java.net.*;
import java.io.*;
/*
 Reyes Rodriguez Enrique Abdiel

*/
public class servidorArchivo {

    public static void main(String[] args) {
            try {

                ServerSocket s = new ServerSocket(7001);
                System.out.println("Waiting for client");
                Socket cl = s.accept();//Esperamos por la conexion de un cliente
                System.out.println("Stablished connection from " + cl.getInetAddress() + ":" + cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());  
                
				int archivos = dis.readInt();//Numero de archivos recibidos
                //nos ha llegado un archivo
				for (int i = 0; i < archivos; i++) {
                    String nombre = dis.readUTF();//nombre del archivo
                    System.out.println("File:" + nombre);

                    long tam = dis.readLong();//tamanio del archivo
                    
					DataOutputStream dos = new DataOutputStream(new FileOutputStream( new File(nombre)));//establece el flujo con el nombre del archivo
                    
					long bytes_received = 0;
                    while (bytes_received < tam) { 
                        byte[] b = new byte[1024]; 
                        int n = dis.read(b);//leemos los bytes del archivo del cliente
                        dos.write(b, 0, n);//los escribimos en el archivo
                        dos.flush();
                        bytes_received = bytes_received + n;
                    }
                    dos.close();
                }
                dis.close();
                cl.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        
    }

}
