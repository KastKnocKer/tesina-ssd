import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class MainSSD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("**** START - Always-ON Messenger ****");
		
		try {
    		Runtime.getRuntime().exec("rmiregistry");
    		Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
    	}
    	catch (java.io.IOException e) {
    		
    	}
		
		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
		
		
		// AVVIO PARTE SERVER
		 try {
	            Server obj = new Server();
	            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

	            // Bind the remote object's stub in the registry
	            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
	            
	            System.out.println("Registry port "+registry.REGISTRY_PORT);
	            registry.rebind("FABIO TROIA DI MERDA :D", stub);

	            System.err.println("Server ready");
	        } catch (Exception e) {
	            System.err.println("Server exception:\n" + e.toString());
	            e.printStackTrace();
	        }
		
		// AVVIO PARTE CLIENT
		
		


		String host = (args.length < 1) ? null : args[0];
		
		
        try {
        	//host = "kastknocker.no-ip.biz";
        	host = "192.168.1.190";
        	
        	System.out.println("Provo a connettermi a: "+host);
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
		
		
		
		
		Status status = new Status();
		
		XML_Configurator xml = new XML_Configurator();

	}

}
