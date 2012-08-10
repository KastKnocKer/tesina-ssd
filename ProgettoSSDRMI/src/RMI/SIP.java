package RMI;




import java.rmi.RemoteException;

import SIP.DBConnection;

public class SIP implements SIPInterface{

	public SIP() {super();}
	
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean imAlive() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean imLeaving() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean register() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean login(String username, String password) throws RemoteException {
		DBConnection dbConn = new DBConnection();
		return dbConn.login(username, password);
	}

}
