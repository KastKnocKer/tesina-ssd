import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Chat.Contact;


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
		
		//	Acquisisco i miei indirizzi IP
		WhatIsMyIP wimi = new WhatIsMyIP();
		
		System.out.println("My GLOBAL IP: "+wimi.getGlobalIP());
		String[][] localIPs = wimi.getLocalIPs();
		for(int i=0; i<localIPs.length; i++){
			System.out.println("My LOCAL IP: "+wimi.getLocalIPs()[i][0]);
		}
		
		System.setProperty("java.rmi.server.hostname", wimi.getGlobalIP());
		
		
		
		
		
		Status status = new Status();
		status.localUser = new Contact(System.getProperty("user.name").toString(), "N", "C", "@", wimi.getGlobalIP(), wimi.getLocalIPs());
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
        	host = "kastknocker.no-ip.biz";
        	//host = "192.168.1.190";
        	//host = status.getSIPAddress();
        	//host = "95.238.41.105";
        	
        	System.out.println("Io, "+System.getProperty("user.name")+", Provo a connettermi a: "+host);
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello(System.getProperty("user.name").toString(),status.localUser);
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
		
		
		
		
		
		
		
		XML_Configurator xml = new XML_Configurator();

	}
	
	
	
	private boolean StarterSIP(){
		return true;
	}
	
	private boolean StarterClient(){
		return true;
	}
	
}
