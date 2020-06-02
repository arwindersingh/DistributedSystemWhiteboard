package ClientPackage;

import java.awt.Shape;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import Remote.RMIRemoteServerInterface;

public class Client {

	private static String serverIPAddress = "localhost";
	private static int serverPortNumber = 2600;
	private static RMIRemoteServerInterface remoteInterface = null;
	public static void main(String[] args) {
		
		try {
            Registry registry = LocateRegistry.getRegistry(serverIPAddress, serverPortNumber);
            remoteInterface = (RMIRemoteServerInterface) registry.lookup("serverFunctions");
            new ClientFunctions(remoteInterface);
            
        } catch (RemoteException e) {
                   System.out.println("Connection Fail: " + e.getMessage());
        } catch (NotBoundException e) {
                 System.out.println("No associated binding: " + e.getMessage());
        }
		
	}

}