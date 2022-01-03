import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client{
	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.getRegistry(null);
			Hello h = (Hello) reg.lookup("Hello");
			h.printMsg();
		} catch (Exception e) {
			e.printStackTrace();
			//TODO: handle exception
		}
	}
}
