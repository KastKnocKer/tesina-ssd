package RMI;
import java.rmi.RemoteException;

import RMIMessages.RMIBasicResponseMessage;
import RMIMessages.RequestHowAreYou;
import RMIMessages.ResponseHowAreYou;
import client.ClientEngine;
import chat.Contact;
import chat.Message;


public class Client implements ClientInterface{

	public Client() {}
	
	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMSG(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Contact whoAreYou() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	public ResponseHowAreYou howAreYou(RequestHowAreYou rhay) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RMIBasicResponseMessage sendMessageToContact(Message[] chatMsgs) throws RemoteException {
		for(Message msg : chatMsgs)ClientEngine.receiveMessageFromContact(msg);
		return new RMIBasicResponseMessage(true, "OK");
	}

}
