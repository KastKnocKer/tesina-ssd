package client.thread;

import layout.friendslist.FriendsList_Table;
import layout.managers.LayoutReferences;
import managers.Status;
import RMI.ClientInterface;
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
		
	}

}
