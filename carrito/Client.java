import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import com.itextpdf.*;

public class Client{
	private final String host = "127.0.0.1";
	private final int port = 3017;
	private Socket socket;
	private HashMap<String, Product> products;
	private ShoppingCart shoppingCart;
	private Scanner sc;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public static void main(String[] args){
		Client c = new Client();
		c.init();
	}
	public Client(){
		sc = new Scanner(System.in);
	}
	void init(){
		try{
			socket = new Socket(host, port);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			products = (HashMap<String, Product>) ois.readObject();
			shoppingCart = new ShoppingCart();
			menu();
			ois.close();
			oos.close();
			socket.close();
		}catch(Exception e){
			System.out.println(e);
		}

	}
	void printProducts(){
		ArrayList<Product> ps = new ArrayList<Product>(products.values());
		for(Product product : ps){
			System.out.println(product.toString());
		}
	}
	void addProduct(){
		System.out.println("id: ");
		String id = "";
		id=sc.nextLine();
		System.out.println("How many products?");
		int quantity = Integer.parseInt(sc.nextLine());
		Product p = null;
		for(Product product : products.values()){
			if(product.getId().equals(id)){
				p = product;
			}
		}
		if(p == null){
			System.out.println("Product not found");
		}else{
			shoppingCart.addProduct(p, quantity);
		}
	}
	void removeProduct(){
		System.out.println("id: ");
		String id = sc.nextLine();
		System.out.println("How many products you want to remove?");
		int quantity = Integer.parseInt(sc.nextLine());
		Product p = null;
		for(Product product: products.values()){
			if(product.getId().equals(id)){
				p = product;
			}
		}
		if(p == null){
			System.out.println("Product not found");
		}else{
			shoppingCart.removeProduct(p, quantity);
		}
	}
	void checkout(){
		ArrayList<String> toBuy = shoppingCart.checkout();
		System.out.println("Proceed with checkout? yN");
		String op = sc.nextLine();
		if(op.equalsIgnoreCase("y")){
			try {
				oos.writeObject(toBuy);
				products = (HashMap<String, Product>) ois.readObject();
				shoppingCart.clear();

			} catch (Exception e) {
				System.out.println("Error buying products"+e);
			}
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
