package Server;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import Remote.RMIRemoteClientInterface;
import Remote.RMIRemoteServerInterface;

public class ServerFunctions extends UnicastRemoteObject implements RMIRemoteServerInterface {
	String[] userNames = {"Thi", "Evelyn","Hint","Raj","James","Lily"};
  //  static ArrayList<String> allUsers = new ArrayList<String>();
    static Map<String,RMIRemoteClientInterface> userMap = new HashMap<String, RMIRemoteClientInterface>();
	static int i = 0;
	
//	ArrayList<RMIRemoteClientInterface> clients = null;
//	ArrayList<RMIRemoteClientInterface> clients =new ArrayList<RMIRemoteClientInterface>();
	
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	ArrayList<String> brushValue = new ArrayList<String>();
	ArrayList<Color> colors = new ArrayList<Color>();
	ArrayList<Boolean> filled = new ArrayList<Boolean>();
	protected ServerFunctions() throws RemoteException {
		super();
	
	}
   
	public synchronized void sendMessage(String message)throws RemoteException
	{
		if(userMap.size()>0){
			for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
			{
			    RMIRemoteClientInterface client = entry.getValue();
			    client.receiveMessage(message);
			}
		}

	}
	public synchronized String getUserName()throws RemoteException
	{
		
		i++;
		return userNames[i-1];
		
	}
	public synchronized boolean logout(String userName, boolean isCross)throws RemoteException
	{
	//	System.out.println("vvvv   "+userName );
		if(!userName.equalsIgnoreCase("Manager")){
		 RMIRemoteClientInterface closeClient = userMap.get(userName);
		 closeClient.closeFrame(isCross);
		 
		
		System.out.println(userName +"  has left the white board");
		 String userList = "";
		 userMap.remove(userName);
		  for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
			{
			    userList = userList+"\n"+entry.getKey();//		    
			}
		   for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
			{
			        RMIRemoteClientInterface allClients = entry.getValue();
			        allClients.updateUserList(userList);
			}
		  
		}
		else{
			shapes.clear();brushValue.clear();colors.clear();filled.clear();			
			 for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
				{
				        if(!entry.getKey().equalsIgnoreCase("Manager")){
				 
				        RMIRemoteClientInterface allClients = entry.getValue();
				      //  System.out.println("1");
				        allClients.closeFrame(false);
				      //  System.out.println("2");
				       
				        }
				       
				}
//			 if(!isCross){
//				 System.out.println("night 8:30  "+userName);
//				 RMIRemoteClientInterface all = userMap.get(userName);
//				
//				 all.closeFrame(false);
//				 System.out.println("night 8:30  "+userName);
//				 
//			 }
			 userMap.clear();
		}
		return isCross;
		
	}
	public synchronized boolean validUser(String userName)throws RemoteException
	{
		
		 if(!userMap.containsKey(userName)){
			 return true;
		 }else{
		return false;
		 }
		
	}
	public synchronized boolean registerPaintClient(RMIRemoteClientInterface client,String userName)throws RemoteException
	{
	 //  allUsers.add(userName);
	  // clients.add(client);
	   userMap.put(userName, client);
	   String userList = "";
	 //  System.out.println("shapes size on server is "+shapes.size());
	   for(int i =0;i <shapes.size();i++){
	   client.updatePaint(shapes.get(i), colors.get(i), brushValue.get(i), filled.get(i));
	   }

	   for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
		{
		    userList = userList+"\n"+entry.getKey();//		    
		}
	   for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
		{
		        RMIRemoteClientInterface allClients = entry.getValue();
		        allClients.updateUserList(userList);
		}

	 //  System.out.println("user reigstered is "+userName);
	   return true;
	}
	public synchronized boolean isManager()throws RemoteException
	{
	  if(userMap.size()<1){
		  return true;
	  }
	  else{
		  return false;
	  }
	}
	public synchronized void deleteAllShapes()throws RemoteException
	{
		for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
		{
			shapes.clear();brushValue.clear();colors.clear();filled.clear();	
			RMIRemoteClientInterface client = entry.getValue();
			client.deleteAll();
		       
		}
	}
	public synchronized ArrayList<Object> saveShapes()throws RemoteException
	{
		
		ArrayList<Object> allData = new ArrayList<Object>();
		allData.add(shapes);allData.add(brushValue);allData.add(colors);allData.add(filled);
		return allData;
	//	return shapes;
	}
//	public synchronized ArrayList<Shape> saveShapes()throws RemoteException
//	{
//		return shapes;
//	}
//	public synchronized ArrayList<Shape> saveShapes()throws RemoteException
//	{
//		return shapes;
//	}
//	public synchronized ArrayList<Shape> saveShapes()throws RemoteException
//	{
//		return shapes;
//	}
	
	public synchronized boolean askPermission(RMIRemoteClientInterface client,String userName)throws RemoteException
	{
		RMIRemoteClientInterface connectManager = userMap.get("Manager");
		return connectManager.getPermission(userName);
		
	//	return false;
		
		

	}
	
	public synchronized void uploadShape(Shape shape,Color color, String size, boolean fill) throws RemoteException
	{
		shapes.add(shape);colors.add(color);brushValue.add(size);filled.add(fill);
	//	System.out.println("server total number of clients are "+ clients.size());
		if(userMap.size()>0){
			for (Map.Entry<String, RMIRemoteClientInterface> entry : userMap.entrySet())
			{
				RMIRemoteClientInterface client = entry.getValue();
						
			       client.updatePaint(shape, color, size, fill);
			}
//			for(int i = 0;i<clients.size();i++){
//		       RMIRemoteClientInterface client = clients.get(i);
//		       client.updatePaint(shape, color, size, fill);
//		       System.out.println("uploading finished on server");
//			} 
		}
		
	}
	
	
	

}
