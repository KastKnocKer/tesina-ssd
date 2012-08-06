import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Main_Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("**** START - Always-ON Messenger - CLIENT ****");
		
		try {
    		//Runtime.getRuntime().exec("rmiregistry");
    		Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
    	}
    	catch (java.io.IOException e) {
    		
    	}
		
		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
		
		
		String host = (args.length < 1) ? null : args[0];
		try {
        	//host = "kastknocker.no-ip.biz";
        	host = "192.168.1.190";
        	
        	System.out.println("Provo a connettermi a: "+host);
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        }catch (NotBoundException e) {
        	System.err.println("Client NotBoundException");
        	//e.printStackTrace();
        } catch (ConnectException e) {
        	System.err.println("Client ConnectException");
            //e.printStackTrace();
        } catch (Exception e) {
        	System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}

}
