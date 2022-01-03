import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.escucharConexiones();
    }


 	private void escucharConexiones(){
        try {
            ServerSocket serverSocket = new ServerSocket(40920);
            while(true){
                System.out.println("Esperando conexiones...");
                try {
                    Socket socketCliente = serverSocket.accept();
                    System.out.println("Conexión establecida desde" + socketCliente.getInetAddress() + ":" + socketCliente.getPort());
                    recibirArchivoPersonaSerializada(socketCliente);
                    Persona persona = deserializarPersonaRecibida();
                    System.out.println("Datos persona serializada recibidos: "+persona.toString());
                    socketCliente.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void recibirArchivoPersonaSerializada(Socket socketCliente){
        System.out.println("Recibiendo archivo persona serializada...");
        try{
            DataInputStream dis = new DataInputStream(socketCliente.getInputStream());

            byte[] bufferRecepcion = new byte[1024];
            long tamanoArchivo = dis.readLong();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("personaRecibidaServidor.txt"));
            long bytesRecibidos = 0;
            int bytesEscritos = 0;
    
            while (bytesRecibidos < tamanoArchivo) {
                bytesEscritos = dis.read(bufferRecepcion);
                dos.write(bufferRecepcion, 0, bytesEscritos);
                dos.flush();
                bytesRecibidos = bytesRecibidos + bytesEscritos;
            }
            dos.close();
            dis.close();
            System.out.println("Archivo persona serializada recibido correctamente");
        }catch(IOException e){
            e.printStackTrace();
        }
    
    }

    private Persona deserializarPersonaRecibida(){
        System.out.println("Deserializando persona...");
        try {
            FileInputStream fis = new FileInputStream("personaRecibidaServidor.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Persona persona = (Persona) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Persona deserializada correctamente");
            return persona;
         } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
            return null;
         }
    }
}
