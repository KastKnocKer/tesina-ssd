package test;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Chat.Contact;

public interface Hello extends Remote {
	String sayHello() throws RemoteException;
	String sayHello(String nome) throws RemoteException;
	String sayHello(String nome,Contact userRequestor) throws RemoteException;
}