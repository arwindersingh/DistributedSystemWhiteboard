package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import Remote.RMIRemoteServerInterface;

public class Server {
public static void main(String args[]){
	
	
        
        int port = 2600;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument " + args[0] + " must be an integer.");
                System.exit(1);
            }
        }
        
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            RMIRemoteServerInterface remoteInterface = new ServerFunctions();
            registry.rebind("serverFunctions", remoteInterface);
                                        
            System.out.println("Concurrent Whiteboard server ready at Port :  " + port);
        } catch (RemoteException e) {
            System.out.println("Whiteboard server main failed : " + e.getMessage());
        }

    }

}
