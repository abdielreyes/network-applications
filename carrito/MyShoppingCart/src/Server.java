import javax.lang.model.element.NestingKind;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



public class Server{
	
	private ServerSocket serverSocket;
	private Socket socket;
	public int port = 3017;
	public String host = "127.0.0.1";
	private Scanner sc;
	private Database db;
	public static void main(String[] args){
		Server s = new Server();
		s.menu();

	}
	public Server(){
		sc = new Scanner(System.in);
		db = new Database();
	}
       

	void menu(){
		System.out.println("\tMenu");
		System.out.println("(c)Create product");
		System.out.println("(d)Delete product");
		System.out.println("(r)Restock product");
		System.out.println("(p)List products");
		System.out.println("(s)Run server");
		System.out.println("(q)Exit");

		String option = sc.nextLine();
		switch(option){
			case "d":
				deleteProduct();
				menu();
				break;
			case "r":
				restockProduct();
				menu();
				break;
			case "c":
				createProduct();
				menu();
				break;
			case "p":
				listProducts();
				menu();
				break;
			case "q":
				break;
			case "s":
				init();
				break;
			default: break;

		}
	}
        public void listProducts(){
            System.out.println(db.printProducts());
            
        }
	void restockProduct(){
		//Por medio del id, mandamos una cantidad nueva de stock
		System.out.println("id:");
		String id = sc.nextLine();
		System.out.println("How many products to stock?");
		int quantity = Integer.parseInt(sc.nextLine());
		Product p = db.restockProduct(id, quantity);
		if(p != null){
			System.out.println(p.getName()+" succesfully stocked!");
		}else{
			System.out.println("Error stocking product!");
		}
	}
	void deleteProduct(){
		//De aqui se borra un producto de la bd, no es lo mismo que stock 0
		System.out.println("id:");
		String id = sc.nextLine();
		if(db.deleteProduct(id)){
			System.out.println("Product deleted");
		}
	}
	void init()  {
		try{
			System.out.println("Do you want to use default port? "+port+"\n(y/N) ");

			String op = sc.nextLine();
			if(!op.equalsIgnoreCase("y")){
				System.out.println("Port ");
				int p = Integer.parseInt(sc.nextLine());
				port = p;
			}
			while(true){

				//iniciamos los sockets y conectamos con el cliente
				serverSocket = new ServerSocket(port);
				System.out.println("Waiting for connections");
				socket = serverSocket.accept();
				System.out.println("Connection from:\n"+serverSocket);

				//creamos los flujos
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				//Para mandar las imagenes, primero las listamos en un array
				File folder = new File("./server_res");
				File[] listOfFiles = folder.listFiles();
				//mandamos la cantidad de archivos
				oos.writeInt(listOfFiles.length);

				for(File file: listOfFiles){
					//Por cada archivo, creamos sus detalles(nombre y tamanio) y los mandamos
					byte[] data = new byte[1048];
					FileDetails details = new FileDetails();
					details.setDetails(file.getName(), file.length());

					oos.writeObject(details);
					oos.flush();
					//Creamos streams para obtener los bytes del archivo
					FileInputStream fileStream = new FileInputStream(file);
					BufferedInputStream fileBuffer = new BufferedInputStream(fileStream);

					int count;
					//partimos el archivo en bloques de bytes que se van enviando por partes.
					while ((count = fileBuffer.read(data)) > 0) {
						oos.write(data, 0, count);
						oos.flush();
					}
					fileBuffer.close();
					fileStream.close();
				}
				//mandamos los productos al cliente
				db = new Database();
				oos.writeObject(db.getProducts());
				//leemos los productos que compro
				ArrayList<String> boughtProducts = (ArrayList<String>)ois.readObject();
				for(String p : boughtProducts){
					db.buyProduct(p,1);
				}
				//le devolvemos los productos actualizados
				oos.writeObject(db.getProducts());

				socket.close();
				oos.close();
				ois.close();
				serverSocket.close();

			}
		}catch(Exception e){
			System.out.println("Error connecting client" );
			e.printStackTrace();
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			init();
		}
	}
	

	public void createProduct(){
		//Para crear un producto, leemos los datos necesarios
		String name;
		String description;
		double price;
		int stock;
		Scanner sc = new Scanner(System.in);
		System.out.println("Name: ");
		name = sc.nextLine();
		System.out.println("Description: ");
		description = sc.nextLine();
		System.out.println("Price: ");
		price = sc.nextDouble();
		System.out.println("Stock: ");
		stock = sc.nextInt();
		String id = UUID.randomUUID().toString();
		//Copiamos el archivo del producto en los archivos del servidor
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(new JFrame("Choose File"));
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				Files.copy(selectedFile.toPath(),
						new File("./server_res/"+id+"_"+name+".jpg").toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				//creamos el producto en la bd
				Product p = new Product(id,name,description,id+"_"+name+".jpg",price,stock);
				db.addProduct(p); 
				
			} catch (Exception e) {
				System.out.println("Error copying file"+e);
				System.out.println("Product not saved");
			}

		}

	}
}