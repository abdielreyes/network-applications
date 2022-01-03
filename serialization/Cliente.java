import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {

        Cliente cliente = new Cliente();

        Persona persona = cliente.solicitarDatos();

        System.out.println("Datos originales de la persona: "+persona.toString());

        cliente.serializarPersona(persona);

        Persona personaDeserializada = cliente.deserializarPersona();
        
        System.out.println("Datos de la persona despues de serializar: "+personaDeserializada.toString());

        cliente.enviarPersonaSerializadaPorSocket();
    }


    private Persona solicitarDatos(){
        Scanner scn = new Scanner(System.in);
        System.out.println("Ingrese el nombre de la persona: ");
        String nombre = scn.next();
        System.out.println("Ingrese la altura de la persona: ");
        float altura = scn.nextFloat();
        System.out.println("Ingrese la edad de la persona: ");
        int edad = scn.nextInt();
        System.out.println("Ingrese la peso de la persona: ");
        double peso = scn.nextDouble();
        System.out.println("Ingrese si la persona es casada (true/false): ");
        boolean casado = scn.nextBoolean();
        System.out.println("Ingrese la nacionalidad de la persona: ");
        String nacionalidad = scn.next();

        Persona persona = new Persona(nombre, altura, edad, peso, casado, nacionalidad);
        return persona;
    }

    private void serializarPersona(Persona persona){
        System.out.println("Serializando persona...");
        try {
            FileOutputStream fos =new FileOutputStream("persona.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(persona);
            oos.close();
            fos.close();
            System.out.println("Persona serializada correctamente");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Persona deserializarPersona(){
        System.out.println("Deserializando persona...");
        try {
            FileInputStream fis = new FileInputStream("persona.ser");
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

    private void enviarPersonaSerializadaPorSocket(){
        try {
            Socket socketCliente = new Socket("127.0.0.1", 40920);
            File archivoSerializadoPersona = new File("persona.txt");
            long tamanoArchivo = archivoSerializadoPersona.length();

            DataOutputStream dos = new DataOutputStream(socketCliente.getOutputStream());
            DataInputStream dis = new DataInputStream(new FileInputStream(archivoSerializadoPersona));

            dos.writeLong(tamanoArchivo);
            dos.flush();

            byte[] bufferEnvio = new byte[1024];
            long bytesEnviados = 0;
            int bytesLeidos;
            while (bytesEnviados < tamanoArchivo) {
                bytesLeidos = dis.read(bufferEnvio);
                dos.write(bufferEnvio, 0, bytesLeidos);
                dos.flush();
                bytesEnviados += bytesLeidos;
            }
            System.out.println("Archivo de persona enviado");
            dis.close();
            dos.close();
            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
