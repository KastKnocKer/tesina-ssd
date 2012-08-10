package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import RMI.SIPInterface;
import chat.Status;

public class ClientEngine {

	
	public static void Login(String username, String password){
		
		//Login mediante server SIP
		Status.getSIPAddress();
		try {
        	//host = "kastknocker.no-ip.biz";
        	//host = "192.168.1.190";
        	//host = status.getSIPAddress();
        	//host = "95.238.41.105";
        	String host = "192.168.1.113";
        	
        	System.out.println("Io, "+System.getProperty("user.name")+", Provo a connettermi a: "+host);
            Registry registry = LocateRegistry.getRegistry(host);
            SIPInterface sip = (SIPInterface) registry.lookup("SIPInterface");
            //Hello stub = (Hello) registry.lookup("Hello");
            boolean response = sip.login(username, password);
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}
	
}
