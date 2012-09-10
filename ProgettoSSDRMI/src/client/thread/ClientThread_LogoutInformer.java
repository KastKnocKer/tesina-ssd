package client.thread;

import java.rmi.RemoteException;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.Status;
import RMI.ClientInterface;
import RMI.SIPInterface;
import RMIMessages.RMIBasicMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import chat.ChatStatusList;
import chat.Contact;
import client.ClientEngine;

public class ClientThread_LogoutInformer extends Thread{
	
	private boolean callSIP;
	private Contact contact;

	public ClientThread_LogoutInformer(boolean callSIP,Contact contact){
		this.callSIP = callSIP;
		this.contact = contact;
		start();
	}
	
	public void run() {
		if(callSIP){
			//Comunico il mio logout al SIP
			SIPInterface sip = ClientEngine.getSIP();
			if(sip != null){
				try {
					sip.iGoOffline(new RMIBasicMessage());
				} catch (RemoteException e) {
					System.err.println("Client - ClientThread_LogoutInformer() Comunicazione al SIP");
				}
			} else return;
			
			
		}else{
			//Comunico il mio logout al client
			ClientInterface client = ClientEngine.getClient(contact.getID());
			if(client != null){
				try {
					client.iGoOffline(new RMIBasicMessage());
				} catch (RemoteException e) {
					System.err.println("Client - ClientThread_LogoutInformer() Comunicazione al Client("+contact.getID()+")");
				}
			} else return;
		}
	}

}
