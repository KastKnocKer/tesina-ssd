package SIP;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SIPInterface extends Remote{
	String sayHello() throws RemoteException;
}