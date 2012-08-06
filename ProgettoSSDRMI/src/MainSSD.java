import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


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
		
		


		String host = (args.length < 1) ? null : args[0];
		
		System.out.println("Provo a connettermi a: "+host);
        try {
        	//host = "kastknocker.no-ip.biz";
        	host = "192.168.1.190";
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
