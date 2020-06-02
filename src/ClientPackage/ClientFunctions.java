package ClientPackage;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Remote.RMIRemoteClientInterface;
import Remote.RMIRemoteServerInterface;

public class ClientFunctions extends UnicastRemoteObject implements RMIRemoteClientInterface {
	
	private RMIRemoteServerInterface remoteServerInterface = null;
	private ClientGui frameTest = null;
	public static String userName = "";
	public ClientFunctions()throws RemoteException
	{
		
	}
	public ClientFunctions(RMIRemoteServerInterface remoteServerInterface)throws RemoteException
	{
		
		super();
		boolean manager = remoteServerInterface.isManager();
		if(!manager){
		String name = null;
			
		boolean validUser = false;
		while(!validUser){
			JFrame frame = new JFrame();
		    Object result = JOptionPane.showInputDialog(frame, "Enter username:");
		 //   System.out.println(result);			    
		    name = result.toString();
		    if(name.length()>1){		     
		    	validUser = remoteServerInterface.validUser(name);
				if(!validUser){
					
					informationMessage("This user name already exist, please choose other usename");
					
				}
				else{
					userName = name;
					//remoteServerInterface.askPermission(this, name);
					if(remoteServerInterface.askPermission(this, name)){
					//	System.out.println("permission granted");
						frameTest = new ClientGui(remoteServerInterface);
						remoteServerInterface.registerPaintClient(this, name);
					}else
					{
					//	System.out.println("permission not granted");
					}
					
					
				}
				
		    }
		    else{
		    	informationMessage("The user name can not be empty");
		    }
		
		}
		//frameTest = new ClientGui(remoteServerInterface);
		//remoteServerInterface.registerPaintClient(this, name);
		
		
		}
		else{
			userName = "Manager";
		//	System.out.println("in else?");
			frameTest = new ClientGui(remoteServerInterface);
			remoteServerInterface.registerPaintClient(this, "Manager");
			//frameTest = new ClientGui(remoteServerInterface);
			
		//	System.out.println("frameTest is settttt");
			
		}
	}
	public synchronized void updatePaint(Shape shape, Color color, String size, boolean fill)
	{
	//	System.out.println("Client Function recieved shape from server to be added to gui");
    	frameTest.addShape(shape,color,size,fill);
	}
	public synchronized void receiveMessage(String message)
	{
		//System.out.println("chat message recieved : "+message);
		frameTest.messageR(message);
	}
	
	public synchronized void updateUserList(String userName)
	{
	   // System.out.println("value of user name is : "+ userName);
	    if(frameTest == null){
	    //	System.out.println("frame test is null.");
	    }
		frameTest.updateUserList(userName);
	}
	
	public void informationMessage(String message)
	{
		JFrame parent = new JFrame();	
		JOptionPane.showMessageDialog(parent, message);
			
	}
	public boolean getPermission(String name)
	{
		String message = name + "  :  wants to join your white board";
		//System.out.println("starting permision sever");
		JDialog.setDefaultLookAndFeelDecorated(true);
	    int response = JOptionPane.showConfirmDialog(null, message,"Confirm",
	        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (response == JOptionPane.NO_OPTION) {
	     return false;
	    } else if (response == JOptionPane.YES_OPTION) {
          return true;
	    } else if (response == JOptionPane.CLOSED_OPTION) {
	      return false;
	    }
	 //   System.out.println("ending permision sever");
	    return false;
	}
	
	public void closeFrame(boolean isCross){
		//System.out.println("close the frame please");
		if(!isCross){
		frameTest.closeFrame();
		}
		
		
	}
	public void deleteAll(){
		
		frameTest.deleteAll();
				
		
	}


}
