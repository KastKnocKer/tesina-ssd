import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
        
public class Server implements Hello {
        
    public Server() {}

    public String sayHello() {
    	System.out.println("Ho ricevuto una richiesta da: ");
        return "Hello, world! FEBIO";
    }
        
    public static void main(String args[]) {
    	System.out.println("********* START SERVER *********");
    	
    	try {
    		Runtime.getRuntime().exec("rmiregistry");
    		Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
    	}
    	catch (java.io.IOException e) {}
    	
    	System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
    	
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
    }
}