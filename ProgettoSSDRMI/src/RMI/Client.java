package RMI;

import java.rmi.RemoteException;

import Chat.Contact;
import Chat.Message;

public class Client implements ClientInterface{

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

	@Override
	public Contact howAreYou() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}