import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
		
		//whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
		
		WhatIsMyIP wimi = new WhatIsMyIP();
		
		System.out.println("My GLOBAL IP: "+wimi.getGlobalIP());
		System.out.println("My LOCAL IP: "+wimi.getLocalIPs());
		

		
		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
		System.setProperty("java.rmi.server.hostname", wimi.getGlobalIP());
		
		
		Status status = new Status();
		status.readConfXML();
		status.writeConfXML();
		
		
		
		
		
		
		
		
		
		
		
		// AVVIO PARTE SERVER
		System.out.println("**** START - SERVER ****");
		try {
	            Server obj = new Server();
	            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 1100);

	            // Bind the remote object's stub in the registry
	            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
	            
	            System.out.println("Registry port "+registry.REGISTRY_PORT);
	            registry.rebind("Hello", stub);
	            

	            System.out.println("Server ready");
	     } catch (Exception e) {
	            System.out.println("Server exception:\n" + e.toString());
	            e.printStackTrace();
	     }
		
		// AVVIO PARTE CLIENT
		System.out.println("**** START - CLIENT ****");


		String host = (args.length < 1) ? null : args[0];
		
		
        try {
        	//host = "kastknocker.no-ip.biz";
        	//host = "192.168.1.190";
        	//host = status.getSIPAddress();
        	host = "95.238.41.105";
        	
        	System.out.println("Io, "+System.getProperty("user.name")+", Provo a connettermi a: "+host);
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello(System.getProperty("user.name").toString());
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
		
		
		
		
		
		
		XML_Configurator xml = new XML_Configurator();

	}
	

	public BufferedReader read(String url) throws Exception{
		return new BufferedReader(
			new InputStreamReader(
				new URL(url).openStream()));
	}

}
