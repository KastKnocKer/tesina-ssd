package test;

import java.rmi.RemoteException;

import utility.WhatIsMyIP;

import Chat.Contact;
        
public class Server implements Hello {
        
    public Server() {}
    public Server(int port) {}

    public String sayHello() throws RemoteException {
    	System.out.println("Ho ricevuto una richiesta da: ");
        return "Hello, world! FEBIO";
    }
    
	public String sayHello(String nome) throws RemoteException {
    	System.out.println("Ho ricevuto una richiesta da: "+nome);
        return "Hello "+nome+" da "+System.getProperty("user.name")+" (From Global:"+new WhatIsMyIP().getGlobalIP()+" Local: "+new WhatIsMyIP().getLocalIPs()[0][0]+")";
	}

	public String sayHello(String nome, Contact userRequestor) throws RemoteException {
		System.out.println("Ho ricevuto una richiesta da: "+userRequestor.Nickname+" - GlobalIP:"+userRequestor.GlobalIP+" LocalIP:"+userRequestor.LocalIPs[0][0]);
        return "Hello "+nome+" da "+System.getProperty("user.name")+" (From Global:"+new WhatIsMyIP().getGlobalIP()+" Local: "+new WhatIsMyIP().getLocalIPs()[0][0]+")";
	}
        
	/*
    public static void main(String args[]) {
    	System.out.println("********* START SERVER *********");
    	
    	try {
    		Runtime.getRuntime().exec("rmiregistry");
    		Runtime.getRuntime().exec("rmid -J-Djava.security.policy=local.policy");
    	}
    	catch (java.io.IOException e) {}
    	
    	System.setProperty("java.rmi.server.codebase", "http://dl.dropbox.com/u/847820/SSD/");
    	
        try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            System.out.println("Registry port "+registry.REGISTRY_PORT);
            registry.rebind("FABIO TROIA DI MERDA :D", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception:\n" + e.toString());
            e.printStackTrace();
        }
    }
    */

	
}