package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import utility.RMIResponse;

import RMI.SIPInterface;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RMISIPBasicResponseMessage;
import RMIMessages.RequestFriendshipMessage;
import RMIMessages.ResponseLoginMessage;
import chat.Contact;
import chat.Status;

public class ClientEngine {

	
	public static ResponseLoginMessage Login(String username, String password){
		ResponseLoginMessage response = null;
		//Login mediante server SIP
		try {
			if(Status.DEBUG) System.out.println("Client - Tentativo di login username: "+username+" password: "+password);
			response = getSIP().login(username, password);
			if(response.isSUCCESS()){
				Status.setUserID(response.getLoggedContact().getID());
				Status.setEmail(response.getLoggedContact().geteMail());
				Status.setNome(response.getLoggedContact().getNome());
				Status.setCognome(response.getLoggedContact().getCognome());
				Status.setNickname(response.getLoggedContact().getNickname());
			}
			return response;
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.Login() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return new ResponseLoginMessage(false, "ClientEngine.Login() exception", null);
		}
	}
	
	public static boolean RegisterNewAccount(String nome, String cognome,String email, String nickname, String password) {
		boolean response = false;
		try {
			if(Status.DEBUG) System.out.println("Client - Tentativo registrazione nuovo utente: "+nome+", "+cognome+", "+email+", "+nickname+", ["+password+"]");
			response = getSIP().register(nome, cognome, email, nickname, password);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RegisterNewAccount() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * Richiede al sip l'intera lista dei propri conatti, aggiornando la lista dei contatti in Status
	 */
	public static boolean LoadContactsFromSIP(){
		boolean response = false;
		try {
			if(Status.DEBUG) System.out.println("Client - Richiesta lista contatti al SIP");
			ArrayList<Contact> contacts = getSIP().getMyContacts(new RMIBasicMessage());
			if(contacts != null){
				Status.setContactList(contacts);
				response = true;
			}
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.LoadContactsFromSIP() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return false;
		}
		return response;
	}
	
	/**
	 * Richiede l'amicizia ad un altro contatto
	 */
	public static RMISIPBasicResponseMessage RequestFriendship(String email){
		RMISIPBasicResponseMessage response = null;
		try {//TODO
			if(Status.DEBUG) System.out.println("Client - Richiesta di amicizia a: "+email);
			return getSIP().askFriendship(new RequestFriendshipMessage(email));
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.RequestFriendship() exception", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.Login() exception: " + e.toString());
			//e.printStackTrace();
			return new RMISIPBasicResponseMessage(false, "ClientEngine.RequestFriendship() exception");
		}
	}
	
	
	/**
	 * @return Riferimento al SIP
	 */
	public static SIPInterface getSIP(){
		Registry registry;
		SIPInterface sip = null;
		try {
			if(Status.DEBUG) System.out.println("Client - Tentativo getSIP() SIP: "+Status.getSIPAddress()+":"+Status.getSIP_Port());
			registry = LocateRegistry.getRegistry(Status.getSIPAddress());
			sip = (SIPInterface) registry.lookup("SIP");
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getSIP()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		} catch (NotBoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ClientEngine.getSIP()", JOptionPane.ERROR_MESSAGE);
			//System.err.println("ClientEngine.getSIP() exception: " + e.toString());
			//e.printStackTrace();
		}
		return sip;
	}



}
