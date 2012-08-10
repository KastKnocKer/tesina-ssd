package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import RMI.SIPInterface;
import chat.Status;

public class ClientEngine {

	
	public static boolean Login(String username, String password){
		boolean response = false;
		//Login mediante server SIP
		try {
			response = getSIP().login(username, password);
		} catch (RemoteException e) {
			System.err.println("ClientEngine.Login() exception: " + e.toString());
			e.printStackTrace();
		}
//		try {
//        	Registry registry = LocateRegistry.getRegistry(Status.getSIPAddress());
//            SIPInterface sip = (SIPInterface) registry.lookup("SIP");
//            response = sip.login(username, password);
//        } catch (Exception e) {
//            System.err.println("ClientEngine.Login() exception: " + e.toString());
//            e.printStackTrace();
//            return false;
//        }
		return response;
	}
	
	
	public static SIPInterface getSIP(){
		Registry registry;
		SIPInterface sip = null;
		try {
			registry = LocateRegistry.getRegistry(Status.getSIPAddress());
			sip = (SIPInterface) registry.lookup("SIP");
		} catch (RemoteException e) {
			System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			e.printStackTrace();
		}
		return sip;
	}
}
