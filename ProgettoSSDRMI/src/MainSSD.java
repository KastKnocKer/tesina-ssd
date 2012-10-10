import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

import client.thread.ClientThread;
import client.thread.ClientThread_SipRequestor;
import managers.FileConfManager;
import managers.Status;
import utility.WhatIsMyIP;
import RMI.SIP;
import RMI.SIPInterface;

/**
 * Main dell'applicazione 
 * 
 * @author Andrea Castelli, Fabio Pierazzi
 *
 */
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
    	}
		
		System.setProperty("java.rmi.server.codebase", "https://dl.dropbox.com/u/852592/SSD/");		// Repository FABIO
//		System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");		// Repository KKK
		
		Status.unbindSIP();
		Status.unbindClient();
		
		
		
		
		
		
		//Inizializzo la classe statica Status
		Status status = new Status();
		
		//	Acquisisco i miei indirizzi IP e li carico sulla classe Status
		WhatIsMyIP wimi = new WhatIsMyIP();
		Status.setGlobalIP(wimi.getGlobalIP());
		Status.setLocalIP(wimi.getStdLocalIP());
		System.out.println("STATUS IPs: Global:"+Status.getGlobalIP()+"  Local:"+Status.getLocalIP());
		
		/** Riga di codice necessaria a far funzionare il tutto su Internet, 
		 * mettendo l'IP globale al posto dell'ip locale. 
		 * NOTA: PROVOCA MALFUNZIONAMENTI IN LAN!!!	 */
		if(Status.isDebuginlan()){
			if(Status.getLocalIP() != null){
				System.setProperty("java.rmi.server.hostname", Status.getLocalIP());
			}else
				System.err.println("IP NON VALIDO!!!!");
		}else{
			if(Status.getGlobalIP() != null){
				System.setProperty("java.rmi.server.hostname", Status.getGlobalIP());
			}else
				System.err.println("IP NON VALIDO!!!!");
		}
		
		
		
		
		
		Status.setLocalIP(wimi.getStdLocalIP());
		if(Status.SUPER_DEBUG){
			System.out.println("My GLOBAL IP: "+Status.getGlobalIP());
			String[][] localIPs = wimi.getLocalIPs();
			for(int i=0; i<localIPs.length; i++){
				System.out.println("My LOCAL IP: "+wimi.getLocalIPs()[i][0]);
			}
		}
		
		
		//	Carico i dati locali
		FileConfManager.readConfXML();	//File locale di configurazione
		
		System.err.println("CLIENT TYPE: "+Status.getType());
		
		if( Status.getType() == Status.TYPE_SIP ){
			Status.startSIP();
		}else if( Status.getType() == Status.TYPE_CLIENT ){
			Status.startClient();
		}else if( Status.getType() == Status.TYPE_SIPCLIENT ){
			Status.startSIP();
			Status.startClient();
		}
			
			
		
		//Avvio i demoni
		ClientThread ct = new ClientThread();
		ct.start();
		ClientThread_SipRequestor ctsr = new ClientThread_SipRequestor();
		ctsr.start();
		
		return;
	}
	
	
	
//	private static boolean StarterSIP(){
//		System.out.println("*** SIP is starting ***");
//		try {
//            SIP sip = new SIP();
//            SIPInterface stub = (SIPInterface) UnicastRemoteObject.exportObject(sip, Status.getSIP_Port());
//            // Registro il SIP nel RMIREGISTRY
//            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
//            //System.out.println("Registry port "+registry.REGISTRY_PORT);
//            registry.rebind("SIP", stub);
//            System.out.println("*** SIP Server ready ***");
//		} catch (Exception e) {
//            System.out.println("SIP Server exception:\n" + e.toString());
//            JOptionPane.showMessageDialog(null, e.getMessage(), "SIP Server exception", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace(); 
//            System.out.println("EXIT FORZATO");
//            System.exit(0);
//		}
//		return true;
//	}

}
