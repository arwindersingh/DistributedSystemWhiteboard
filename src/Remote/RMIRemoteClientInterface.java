package Remote;

import java.awt.Color;
import java.awt.Shape;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIRemoteClientInterface extends Remote{
	public void updatePaint(Shape shape,Color color, String size, boolean fill)throws RemoteException;
	public void receiveMessage(String message)throws RemoteException;
	public void updateUserList(String userName)throws RemoteException;
	public void closeFrame(boolean isCross)throws RemoteException;
	public boolean getPermission(String name)throws RemoteException;
	public void deleteAll()throws RemoteException;
	

}
