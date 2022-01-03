import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Client{

	public  String host = "127.0.0.1";
	public  int port = 3017;
	private Socket socket;
	private HashMap<String, Product> products;
	private ShoppingCart shoppingCart;
	public Scanner sc;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public static void main(String[] args)  {
		Client c = new Client();
		System.out.println("Do you want to use default host and port? "+c.host+c.port+"\n(y/N) ");
		String op = c.sc.nextLine();
		if(op.equalsIgnoreCase("y")){
			c.init();
		}else{
			System.out.println("Host ");
			String host = c.sc.nextLine();
			System.out.println("Port ");
			int port = Integer.parseInt(c.sc.nextLine());
			c.host = host;
			c.port = port;
			c.init();
		}

	}
	public Client(){
		sc = new Scanner(System.in);
	}
	void init() {
		try{
			//inicializamos el sockets con los cual conectamos al servidor
			socket = new Socket(host, port);
			// inicializamos los flujos para el socket
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			//leemos el numero de archivos que hay e iteramos por cada uno
			int numFiles = ois.readInt();
			for (int i = 0;i<numFiles;i++){
				//leemos el nombre y tamanio
				FileDetails details = (FileDetails) ois.readObject();

				String fileName = details.getName();
				System.out.println("File Name : " + fileName);
				byte data[] = new byte[1048];
				//creamos le flujo de salida del archivo
				FileOutputStream fileOut = new FileOutputStream("./client_res/" + fileName);
				BufferedOutputStream fileBuffer = new BufferedOutputStream(fileOut);
				int count;
				int sum = 0;
				//recibimos las partes del archivo y las escribimos en el flujo
				while ((count = ois.read(data)) > 0) {
					sum += count;
					fileBuffer.write(data, 0, count);
					fileBuffer.flush();
				}
				fileBuffer.close();
			}

			//guardamos los productos que obtenemos del servidor
			products = (HashMap<String, Product>) ois.readObject();

			shoppingCart = new ShoppingCart();
			menu();
			ois.close();
			oos.close();
			socket.close();
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}

	}
	void printProducts(){
		//iteramos en los productos disponibles y los imprimimos
		ArrayList<Product> ps = new ArrayList<Product>(products.values());
		for(Product product : ps){
			System.out.println(product.toString());
		}
	}
	void addProduct(){
		//anadimos un producto al carrito de compras
		System.out.println("id: ");
		String id = "";
		id=sc.nextLine();
		System.out.println("How many products?");
		int quantity = Integer.parseInt(sc.nextLine());
		Product p = null;
		//buscamos si el producto existe
		for(Product product : products.values()){
			if(product.getId().equals(id)){
				p = product;
			}
		}

		if(p == null){
			System.out.println("Product not found");
		}else{
			//anadimos al carrito la cantidad indicada
			shoppingCart.addProduct(p, quantity);
		}
	}
	void removeProduct(){
		System.out.println("id: ");
		String id = sc.nextLine();
		System.out.println("How many products you want to remove?");
		int quantity = Integer.parseInt(sc.nextLine());
		Product p = null;
		//buscamos que el producto exista
		for(Product product: products.values()){
			if(product.getId().equals(id)){
				p = product;
			}
		}
		if(p == null){
			System.out.println("Product not found");
		}else{
			//eliminamos la cantidad de productos la cantidad indicada
			shoppingCart.removeProduct(p, quantity);
		}
	}
	void checkout(){
		ArrayList<String> toBuy = shoppingCart.checkout();

		try {
			oos.writeObject(toBuy);
			products = (HashMap<String, Product>) ois.readObject();
			shoppingCart.clear();

		} catch (Exception e) {
			System.out.println("Error buying products"+e);
		}

	}

	void menu(){
		System.out.println("\tMenu");
		System.out.println("(a)Add product");
		System.out.println("(d)Delete product");
		System.out.println("(p)See products");
		System.out.println("(s)My shopping cart");
		System.out.println("(c)Checkout");
		System.out.println("(q)Exit");

		String option = sc.nextLine();
		switch(option){
			case "a":
				addProduct();
				menu();
				break;
			case "d":
				removeProduct();
				menu();
				break;
			case "s":
				shoppingCart.printShoppingCart();
				menu();
				break;
			case "p":
				System.out.println("Products");
				printProducts();
				menu();
				break;
			case "c":
				checkout();
				menu();
				break;
			case "q":
				System.out.println("Bye!");
				break;
			default:
				menu();
		}

	}
}
