
import java.io.Serializable;
import java.util.UUID;

public class Product implements Serializable {


	private double price;
	private int stock;
	private String id;
	private String name;
	private String description;
	private String image;
	public Product(String name, String description,String image, double price, int stock ) {
		super();
		this.id = UUID.randomUUID().toString();
		this.price = price;
		this.stock = stock;
		this.name = name;
		this.description = description;
		this.image = image;
	}
	public Product(String id,String name, String description,String image, double price, int stock ) {
		super();
		this.id = id;
		this.price = price;
		this.stock = stock;
		this.name = name;
		this.description = description;
		this.image = image;
	}
	

	public double getPrice(){
		return price;
	}
	public int getStock(){
		return stock;
	}
	public String getName(){
		return name;
	}
	public String getDescription(){
		return description;
	}
	public String getId(){
		return id;
	}
	public String getImage(){
		return image;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public void setStock(int stock){
		this.stock = stock;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setId(String id){
		this.id = id;
	}
	@Override
    public String toString() {
        return "id:" + id +",\n name:"+name+",\n description:"+description+",\n image:"+image+",\n stock:"+stock+",\n price:"+price+"";
    }

}
