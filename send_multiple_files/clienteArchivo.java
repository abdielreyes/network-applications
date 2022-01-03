import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;
/*
 Reyes Rodriguez Enrique Abdiel
3CV11
*/


public class clienteArchivo {

    public static void main(String[] args) {
        try {
            Socket cl = new Socket("localhost", 7001);//creamos el socket cliente 
            JFileChooser jf = new JFileChooser(); 
            jf.setMultiSelectionEnabled(true); 
            int r = jf.showOpenDialog(null);//abrimos una ventana de seleccion de archivos

            if (r == JFileChooser.APPROVE_OPTION) { 
                File[] f = jf.getSelectedFiles(); 
                int numberFiles = f.length;//numero de archivos

                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                dos.writeInt(numberFiles);//mandamos al servidor el numero de archivos

                for (int i = 0; i < numberFiles; i++) {
                    String archivo = f[i].getAbsolutePath(); 
                    String nombre = f[i].getName(); 
                    long tam = f[i].length(); 
                    DataInputStream dis = new DataInputStream(new FileInputStream(archivo)); 
					//enviamos su nombre y su tamanio
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();

                    long enviados = 0;
                    int porcentaje, n;
					// enviamos los bits de archivo al servidor
                    while (enviados < tam) {
                        byte[] b = new byte[1024]; 
                        n = dis.read(b);
                        dos.write(b, 0, n);//mandamos al servidor una porcion del archivo 
                        dos.flush();
                        enviados = enviados + n;//acarreamos un contador para saber cuando se escribio el archiov
                        porcentaje = (int) (enviados * 100 / tam);
                         
                        System.out.print("sent " + nombre + ": " + porcentaje + "% \r");
					
                    }
					System.out.println("\n");
                    System.out.print("\nFile"+f[i].getName()+" sent");
                    dis.close();
                }
                dos.close(); 
                cl.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
