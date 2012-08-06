import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Main_SIP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("**** START - Always-ON Messenger - SIP ****");
		
		try {
    		//Runtime.getRuntime().exec("rmiregistry");
    		Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
    	}
    	catch (java.io.IOException e) {
    		
    	}
		
		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
		
		
		try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            
            System.out.println("Registry port "+registry.REGISTRY_PORT);
            registry.rebind("FABIO TROIA DI MERDA :D", stub);

            System.err.println("Server ready");
        } catch (ConnectException e) {
        	System.err.println("SIP ConnectException");
            //e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server exception:\n" + e.toString());
            e.printStackTrace();
        }
		
		
	}

}
