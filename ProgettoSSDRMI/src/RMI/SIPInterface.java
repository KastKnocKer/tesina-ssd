package RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SIPInterface extends Remote{
	String sayHello() throws RemoteException;
	
	/**
	 * Il client indica al SIP il mio stato
	 */
	boolean imAlive() throws RemoteException;
	
	/**
	 * Il client indica al SIP di volersi disconnettere
	 */
	boolean imLeaving() throws RemoteException;
	
	
	//TODO
	/**
	 * Registrazione del client presso il SIP
	 */
	boolean register(String nome, String cognome,String email, String nickname, String password) throws RemoteException;
	
	/**
	 * Login del client presso il SIP
	 */
	boolean login(String username, String password) throws RemoteException;
	
	
}