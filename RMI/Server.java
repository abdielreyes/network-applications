import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server{
	public static void main(String[] args) {
		try {
			HelloImpl hi = new HelloImpl();
			Hello h = (Hello) UnicastRemoteObject.exportObject(hi,7080);
			Registry reg = LocateRegistry.getRegistry();
			reg.bind("Hello",h);
			
			
		} catch (Exception e) {
			//TODO: handle exception
			e.printStackTrace();
		}
	}
}
