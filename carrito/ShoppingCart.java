import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class ShoppingCart{

	private HashMap<String,Item> cart;
	private double total;
	public ShoppingCart(){
		cart = new HashMap<String,Item>();
		total = 0.0;
	}
	void addProduct(Product p, int quantity){

		if(cart.containsKey(p.getId())){
			if((cart.get(p.getId()).quantity+quantity)<=p.getStock()){
				cart.get(p.getId()).quantity += quantity;
				System.out.println("Product(s) added!");
			}else{
				System.out.println("Not enough stock!");
			}
		}else{
			if(quantity<=p.getStock()){
				Item i = new Item(p,quantity);
				cart.put(i.id, i);
			}else{
				System.out.println("Not enough stock!");
			}
		}
	}
	void removeProduct(Product p,int quantity){
		if(cart.containsKey(p.getId())){
			if((cart.get(p.getId()).quantity-quantity)>0){
				cart.get(p.getId()).quantity -= quantity;
				System.out.println("Product(s) removed!");
			}else{
				cart.remove(p.getId());
				System.out.println("Deleting product from your cart");
			}
		}else{
			System.out.println("This product is not in your cart!");
		}
	}
	void printShoppingCart(){
		ArrayList<Item> products = new ArrayList<Item>(cart.values());
		if(products.size() == 0){
			System.out.println("Your cart is empty!");
		}
		for(Item i :products){
			System.out.println(i.toString());
		}
	}
	void clear(){
		this.cart = new HashMap<String, Item>();
	}
	ArrayList<String> checkout(){
		ArrayList<Item> products = new ArrayList<Item>(cart.values());
		ArrayList<String> toBuy = new ArrayList<String>();

		for(Item i: products){
			for(int j = 1;j<=i.quantity;j++){
				toBuy.add(i.id);
			}
			System.out.println(i.product.getName());
			System.out.println("\tQuantity:"+i.quantity+"\n\t"+"Price:"+i.price);
			double subtotal = i.quantity * i.price;
			System.out.println("\tSubtotal:"+subtotal);
			total += subtotal;
		}
		System.out.println("Total: "+total);
		return toBuy;
	}
	ArrayList<Item> getItems(){
		return new ArrayList<Item>(cart.values());
	}
	private class Item{
		int quantity;
		double price;
		Product product;
		String id;
		Item(Product p,int q){
			quantity = q;
			product = p;
			price = product.getPrice();
			id = product.getId();
		}
		@Override
		public String toString(){
			return "quantity: "+quantity+"\nProduct: "+product.toString();
		}
	}
	

}
