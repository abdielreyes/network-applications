import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JFileChooser;
import javax.swing.JFrame;



public class Server{
	
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream InputStream;
	private ObjectOutputStream OutputStream;
	private static final int port = 3017;
	private Scanner sc;
	private Database db;
	public static void main(String[] args){
		Server s = new Server();
	//	s.createProduct();
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
			case "s":
				init();
				break;
			default: break;

		}
	}
	void restockProduct(){
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
		System.out.println("id:");
		String id = sc.nextLine();
		if(db.deleteProduct(id)){
			System.out.println("Product deleted");
		}
	}
	void init(){
		try{
			while(true){
				serverSocket = new ServerSocket(port);
				System.out.println("Waiting for connections");
				socket = serverSocket.accept();
				System.out.println("Connection from:\n"+serverSocket);

				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

				Database db = new Database();
				oos.writeObject(db.getProducts());

				ArrayList<String> boughtProducts = (ArrayList<String>)ois.readObject();
				for(String p : boughtProducts){
					db.buyProduct(p,1);
				}
				oos.writeObject(db.getProducts());

				socket.close();
				oos.close();
				serverSocket.close();

			}
		}catch(Exception e){
			System.out.println("Error connecting client");
			init();
		}
	}
	

	public void createProduct(){
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
		sc.close();
		String id = UUID.randomUUID().toString();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(new JFrame("Choose File"));
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				Files.copy(selectedFile.toPath(),
						new File("./server_res/"+id+name).toPath(),
						StandardCopyOption.REPLACE_EXISTING); 
				Product p = new Product(id,name,description,id+selectedFile.getName(),price,stock);
				db.addProduct(p); 
				
			} catch (Exception e) {
				System.out.println("Error copying file"+e);
				System.out.println("Product not saved");
			}

		}

	}
}
