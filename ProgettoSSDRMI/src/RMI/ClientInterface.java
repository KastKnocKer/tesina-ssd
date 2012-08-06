package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{
	String sayHello() throws RemoteException;
}