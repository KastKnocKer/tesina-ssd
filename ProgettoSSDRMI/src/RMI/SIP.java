package RMI;

import java.rmi.RemoteException;

public class SIP implements SIPInterface{

	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean imAlive() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean imLeaving() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean register() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
