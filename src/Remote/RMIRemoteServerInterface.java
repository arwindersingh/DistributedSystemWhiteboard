package Remote;
import java.awt.Color;
import java.awt.Shape;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import Server.ServerFunctions;

public interface RMIRemoteServerInterface extends Remote {
	   
	    public String getUserName() throws RemoteException;
	    public boolean validUser(String userName) throws RemoteException;	
		
		public boolean registerPaintClient(RMIRemoteClientInterface client, String userName) throws RemoteException;	
		public void uploadShape(Shape shape,Color color, String size, boolean fill)throws RemoteException;
//		public void uploadColor(Color color)throws RemoteException;
//		public void uploadSize(String size)throws RemoteException;
//		public void uploadFilled(boolean fill)throws RemoteException;
		public void sendMessage(String message)throws RemoteException;
		public boolean isManager()throws RemoteException;
		public boolean logout(String userName, boolean isCrossed)throws RemoteException;
		public boolean askPermission(RMIRemoteClientInterface client, String userName)throws RemoteException;
		public void deleteAllShapes()throws RemoteException;
		public ArrayList<Object> saveShapes()throws RemoteException;
		

}
