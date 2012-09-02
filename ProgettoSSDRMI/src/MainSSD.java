import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.ClientThread;

import layout.homeframe.Home_Frame;
import utility.WhatIsMyIP;
import RMI.Client;
import RMI.ClientInterface;
import RMI.SIP;
import RMI.SIPInterface;
import chat.Status;


public class MainSSD {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("**** START - Always-ON Messenger ****");
		
		try {
			Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
    		Runtime.getRuntime().exec("rmiregistry");
    	} catch (java.io.IOException e) {
    		System.out.println("Attenzione: rmid e rmiregistry non trovati!");
    		//System.exit(0);
    	}
		
		// System.setProperty("java.rmi.server.codebase", "https://dl.dropbox.com/u/852592/SSD/");		//Repository FABIO
		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");			//Repository KKK
		
		//TODO tentativo qua sotto fallito
		//Timeout per le chiamate ad oggetto remoto
		System.setProperty("sun.rmi.transport.connectionTimeout", "2000");
		System.setProperty("sun.rmi.transport.tcp.handshakeTimeout", "2000");
		System.setProperty("sun.rmi.dgc.client.gcInterval", "2000");
		
		
		//Inizializzo la classe statica Status
		Status status = new Status();
		
		//	Acquisisco i miei indirizzi IP e li carico sulla classe Status
		WhatIsMyIP wimi = new WhatIsMyIP();
		Status.setGlobalIP(wimi.getGlobalIP());
		Status.setLocalIP(wimi.getLocalIPs()[0][0]);
		if(Status.SUPER_DEBUG){
			System.out.println("My GLOBAL IP: "+wimi.getGlobalIP());
			String[][] localIPs = wimi.getLocalIPs();
			for(int i=0; i<localIPs.length; i++){
				System.out.println("My LOCAL IP: "+wimi.getLocalIPs()[i][0]);
			}
		}
		
		
		//	Carico i dati locali
		status.readConfXML();	//File locale di configurazione
		
		System.err.println("CLIENT TYPE: "+Status.getType());
		
		if( Status.getType() == Status.TYPE_SIP ){
			StarterSIP();
		}else if( Status.getType() == Status.TYPE_CLIENT ){
			StarterClient();
		}else if( Status.getType() == Status.TYPE_SIPCLIENT ){
			StarterSIP();
			StarterClient();
		}
			
			
		
		
		
		return;
//		
//		System.out.println("**** START - Always-ON Messenger ****");
//		
//		try {
//    		Runtime.getRuntime().exec("rmiregistry");
//    		Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
//    	}
//    	catch (java.io.IOException e) {
//    		
//    	}
//		
//		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
//		
//		//	Acquisisco i miei indirizzi IP
//		WhatIsMyIP wimi = new WhatIsMyIP();
//		
//		System.out.println("My GLOBAL IP: "+wimi.getGlobalIP());
//		String[][] localIPs = wimi.getLocalIPs();
//		for(int i=0; i<localIPs.length; i++){
//			System.out.println("My LOCAL IP: "+wimi.getLocalIPs()[i][0]);
//		}
//		
//		System.setProperty("java.rmi.server.hostname", wimi.getGlobalIP());
//		
//		
//		
//		
//		
//		Status status = new Status();
//		status.localUser = new Contact(System.getProperty("user.name").toString(), "N", "C", "@", wimi.getGlobalIP(), wimi.getLocalIPs());
//		status.readConfXML();
//		status.writeConfXML();
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		// AVVIO PARTE SERVER
//		System.out.println("**** START - SERVER ****");
//		try {
//	            Server obj = new Server();
//	            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 1100);
//
//	            // Bind the remote object's stub in the registry
//	            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
//	            
//	            System.out.println("Registry port "+registry.REGISTRY_PORT);
//	            registry.rebind("Hello", stub);
//	            
//
//	            System.out.println("Server ready");
//	     } catch (Exception e) {
//	            System.out.println("Server exception:\n" + e.toString());
//	            e.printStackTrace();
//	     }
//		
//		// AVVIO PARTE CLIENT
//		System.out.println("**** START - CLIENT ****");
//
//
//		String host = (args.length < 1) ? null : args[0];
//		
//		
//        try {
//        	host = "kastknocker.no-ip.biz";
//        	//host = "192.168.1.190";
//        	//host = status.getSIPAddress();
//        	//host = "95.238.41.105";
//        	
//        	System.out.println("Io, "+System.getProperty("user.name")+", Provo a connettermi a: "+host);
//            Registry registry = LocateRegistry.getRegistry(host);
//            Hello stub = (Hello) registry.lookup("Hello");
//            String response = stub.sayHello(System.getProperty("user.name").toString(),status.localUser);
//            System.out.println("response: " + response);
//        } catch (Exception e) {
//            System.err.println("Client exception: " + e.toString());
//            e.printStackTrace();
//        }
//		
//		
//		
//		
//		
//		
//		
//		XML_Configurator xml = new XML_Configurator();

	}
	
	
	
	private static boolean StarterSIP(){
		System.out.println("*** SIP is starting ***");
		try {
            SIP sip = new SIP();
            SIPInterface stub = (SIPInterface) UnicastRemoteObject.exportObject(sip, Status.getSIP_Port());
            // Registro il SIP nel RMIREGISTRY
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            //System.out.println("Registry port "+registry.REGISTRY_PORT);
            registry.rebind("SIP", stub);
            System.out.println("*** SIP Server ready ***");
		} catch (Exception e) {
            System.out.println("SIP Server exception:\n" + e.toString());
            JOptionPane.showMessageDialog(null, e.getMessage(), "SIP Server exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
            System.out.println("EXIT FORZATO");
            System.exit(0);
		}
		return true;
	}
	
	private static boolean StarterClient(){
		System.out.println("*** Client is starting ***");
		
		try {
            Client client = new Client();
            ClientInterface stub = (ClientInterface) UnicastRemoteObject.exportObject(client, Status.getClient_Port());
            // Registro il SIP nel RMIREGISTRY
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            //System.out.println("Registry port "+registry.REGISTRY_PORT);
            registry.rebind("Client", stub);
            System.out.println("*** Client obj ready ***");
		} catch (Exception e) {
            System.out.println("Client obj exception:\n" + e.toString());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Client obj exception", JOptionPane.ERROR_MESSAGE);
            System.out.println("EXIT FORZATO");
            System.exit(0);
		}
		// imposto visualizzazione con look and feel del sistema operativo in uso 
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					System.err.println("Impossibile impostare L&F di sistema");
				}
				
				
				/**
				 * Mostro l'Home Frame
				 * @author Fabio Pierazzi 
				 */
				// places the application on the Swing Event Queue 
				SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	
			            	Home_Frame hf = new Home_Frame(); 
			            	hf.setVisible(true);
			            }
				});
		ClientThread ct = new ClientThread();
		//ct.start();
		return true;
	}
}
