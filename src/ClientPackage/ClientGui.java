package ClientPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.synth.SynthSeparatorUI;

import Remote.RMIRemoteServerInterface;

public class ClientGui {

	RMIRemoteServerInterface remoteServerInterface = null;
	static String buttonSelected = "a";
	 ClientPaintArea clientPanel =null;
	 static Graphics g = null;
	 JTextArea messageHistory = null;
	 JTextArea artist_area = null;
	 JComboBox<String> kickList = null;
	 static boolean alreadySaved = false;
	 static String savedFileName = "";
	   JComboBox cb = new JComboBox();
	   static Map<String,String> map = new HashMap<String,String>();
	   JFrame frame = null;
	   JFileChooser file = null;
	  static String fileNameSelected = "abc";
	 
public ClientGui(RMIRemoteServerInterface remoteServerInterface)
{
	this.remoteServerInterface = remoteServerInterface;
	clientPanel = new ClientPaintArea(remoteServerInterface);
	getScreen();
//	kickList = new JComboBox<String>();
}
public ClientGui(){
	clientPanel = new ClientPaintArea(remoteServerInterface);
}
//    public static void main(String s[]) {
//    	new ClientGui().getScreen();
//
//}
   Graphics gg = null;
	public void getScreen(){

       frame = new JFrame("JFrame Example");
        frame.setSize(1200, 1600);
        
        JPanel mainPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
     
      
        JPanel artist = new JPanel();
        JPanel MessageHistory = new JPanel();
        JPanel chatWindow = new JPanel();
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
              try {
            	//  System.out.println("starting logout with name "+ClientFunctions.userName);
				remoteServerInterface.logout(ClientFunctions.userName, true);
			//	 System.out.println("engding logout with name "+ClientFunctions.userName);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
               
            }
        });

        

		 JMenuBar menuBar;
		 JMenu menu;
		 JMenuItem New, Open, Save, SaveAs, Close;
       
       menuBar = new JMenuBar();
       menu = new JMenu("File");

       New = new JMenuItem("New", KeyEvent.VK_T);
       Open = new JMenuItem("Open", KeyEvent.VK_T);
       Save = new JMenuItem("Save", KeyEvent.VK_T);
       SaveAs = new JMenuItem("SaveAs", KeyEvent.VK_T);
       Close = new JMenuItem("Close", KeyEvent.VK_T);
   
       menuBar.add(menu);
       menu.add(New);
       menu.add(Open);
       menu.add(Save);
       menu.add(SaveAs);
       menu.add(Close);
      
       
       Open.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {        	   
                file = new JFileChooser();
               switch (file.showOpenDialog(frame)) {
                   case JFileChooser.APPROVE_OPTION:                   
                       break;
               }
           }
       });
       
      
       
 //   File f =  file.getCurrentDirectory();
       
       JPanel panelToolBar = getPanelToolBar(menuBar);
       
       JPanel panelChat = getPanelChat();            
      
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
     //   bottomPanel.setBorder(BorderFactory.createLineBorder(Color.black, 10));          
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));    
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));

        bottomPanel.add(clientPanel);
        bottomPanel.add(panelChat);
        mainPanel.add(panelToolBar);
        mainPanel.add(bottomPanel);
        
        


        panelChat.add(artist);
        panelChat.add(MessageHistory);
        panelChat.add(chatWindow);
       
    
        frame.add(mainPanel);                
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);   
   
        artist.setBorder(BorderFactory.createTitledBorder("Artists"));
        MessageHistory.setBorder(BorderFactory.createTitledBorder("Message History"));
        chatWindow.setBorder(BorderFactory.createTitledBorder("Message "));
        
     
        
  
		artist_area = new JTextArea();
        artist_area.setEditable(false);
       
        artist.add(new JScrollPane(artist_area));
        artist_area.setColumns(30);
		artist_area.setRows(13);
		

		messageHistory = new JTextArea();
        messageHistory.setEditable(false);
        MessageHistory.add(new JScrollPane(messageHistory));
		messageHistory.setColumns(30);
		messageHistory.setRows(15);
		
		
		JTextField messageBox = new JTextField(26);
		JButton sendButton = new JButton("Send");
		
		sendButton.addActionListener(new ActionListener() { 
       	  public void actionPerformed(ActionEvent e) { 
       		try {
       			ClientFunctions a =  new ClientFunctions();       		
				remoteServerInterface.sendMessage( a.userName+"  :  "+messageBox.getText());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}       		  
       		            
       	  } 
       	} );	
		 New.addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent e) {       
	        	   JOptionPane optionPane = new JOptionPane("This will loose your previoius work. Continue?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
	        	    JDialog dialog = optionPane.createDialog(frame, "Information");
	        	    dialog.setVisible(true);
	        	    String value = optionPane.getValue().toString();
	        	 //   System.out.println("whats going on :"+value); //0 is yes 1 is no
	        	    try {
	        	    	if(value.equalsIgnoreCase("0")){
						remoteServerInterface.deleteAllShapes();
	        	    	}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	    //call 
	               
	           }
	       });
		 Open.addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent e) {        	   
	          
	                 JFrame parentFrame = new JFrame();		        	   
		        	   JFileChooser fileChooser = new JFileChooser();
		        	   fileChooser.setDialogTitle("Specify a file to save");  		        	    
		        	   int userSelection = fileChooser.showSaveDialog(parentFrame);		        	    
		        	   if (userSelection == JFileChooser.APPROVE_OPTION) {
		        	       File fileToSave = fileChooser.getSelectedFile();
		        	   
		        	       fileNameSelected = fileToSave.getAbsolutePath();
		        	       
		        	       
		        	   }
	                ArrayList<Object> result = openFromFile(fileNameSelected);
	                
	                ArrayList<Shape> shapes = ( ArrayList<Shape> ) result.get(0);
	                ArrayList<String> brushValue = (ArrayList<String> )result.get(1);
	                ArrayList<Color> colors =( ArrayList<Color> ) result.get(2);
	                ArrayList<Boolean> filled =( ArrayList<Boolean> )result.get(3);
               
	                for(int i =0;i<shapes.size();i++){
	                	try {
							remoteServerInterface.uploadShape(shapes.get(i), colors.get(i), brushValue.get(i), filled.get(i));
						} catch (RemoteException e1) {
							
							e1.printStackTrace();
						}
	                	
	                }    
//	               switch (file.showOpenDialog(frame)) {
//	                   case JFileChooser.APPROVE_OPTION:                   
//	                       break;
//	               }
	           }
	       });
		 Save.addActionListener(new ActionListener() {
				
	           @Override
	           public void actionPerformed(ActionEvent e) {        	   
	        	 //  System.out.println("night  "+ fileNameSelected);
	        	   ArrayList<Object> shapes = new ArrayList<Object>();
	        	   try {
					shapes =  (ArrayList<Object>) remoteServerInterface.saveShapes();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	   
	        	   if(!alreadySaved){
	        	     JFrame parentFrame = new JFrame();
	        	   
	        	     JFileChooser fileChooser = new JFileChooser();
	        	     fileChooser.setDialogTitle("Specify a file to save");   
	        	    
	        	     int userSelection = fileChooser.showSaveDialog(parentFrame);
	        	    
		            	//   if (userSelection == JFileChooser.APPROVE_OPTION) {
			        	       File fileToSave = fileChooser.getSelectedFile();
			        	     //  System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			        	       fileNameSelected = fileToSave.getAbsolutePath();
			        	       saveToFile(shapes,fileNameSelected);
			        	       savedFileName = fileNameSelected;
			        	       
			        	       alreadySaved = true;
	        	   }
	            	   else
	            	   {
	            		   saveToFile(shapes,savedFileName);
	            		   
	            	   }
	        	       
	          
	        	   
	           }
	       });
		 SaveAs.addActionListener(new ActionListener() {
				
	           @Override
	           public void actionPerformed(ActionEvent e) {        	   
	        	 //  System.out.println("night  "+ fileNameSelected);
	        	   ArrayList<Object> shapes = new ArrayList<Object>();
	        	   try {
					shapes =  (ArrayList<Object>) remoteServerInterface.saveShapes();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	   JFrame parentFrame = new JFrame();
	        	   
	        	   JFileChooser fileChooser = new JFileChooser();
	        	   fileChooser.setDialogTitle("Specify a file to save");   
	        	    
	        	   int userSelection = fileChooser.showSaveDialog(parentFrame);
	        	    
	        	   if (userSelection == JFileChooser.APPROVE_OPTION) {
	        	       File fileToSave = fileChooser.getSelectedFile();
	        	     //  System.out.println("Save as file: " + fileToSave.getAbsolutePath());
	        	       fileNameSelected = fileToSave.getAbsolutePath();
	        	       
	        	       saveToFile(shapes,fileNameSelected);
	        	       alreadySaved = true;
	        	   }
	           }
	       });
		 Close.addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent e) {        	   
	        	   try {
					remoteServerInterface.logout(ClientFunctions.userName, false);
					frame.dispose();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	           }
	       });
	
		chatWindow.add(messageBox);
		chatWindow.add(sendButton);
}
    
    public JPanel getPanelToolBar(JMenuBar bar)
    {
    	JPanel panelToolBar = new JPanel();
    //	panelToolBar.setBackground(Color.YELLOW);
    	panelToolBar.setPreferredSize(new Dimension(1400, 100));
    	    	
    	//panelToolBar.setBorder(BorderFactory.createLineBorder(Color.black, 10));
    	
    	JRadioButton option1 = new JRadioButton("Rectangle");
        JRadioButton option2 = new JRadioButton("Circle");       
        JRadioButton option3 = new JRadioButton("Oval");
        JRadioButton option4 = new JRadioButton("Free Hand Writing");
        JRadioButton option5 = new JRadioButton("Line");
        JRadioButton option6 = new JRadioButton("Text Input");
        JRadioButton option7 = new JRadioButton("Eraser");        
        JCheckBox checkBox = new JCheckBox("fill"); 
        
        JComboBox<String> size = new JComboBox<String>();  
        
        size.addItem("Small");
        size.addItem("Medium");
        size.addItem("Large");
        
        JButton colorDisplayButton = new JButton("");
        colorDisplayButton.setBackground(Color.black);
        
       
            
        size.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
     // 	  System.out.println( size.getSelectedItem());
      	  String selectedItem = size.getSelectedItem().toString();
      	  clientPanel.size = selectedItem;
      	  } 
      	} );
        
        JButton lineColorButton = new JButton("Pick Color");
        lineColorButton.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		 Color initialBackground = lineColorButton.getBackground();
        	     Color background = JColorChooser.showDialog(null, "Change Button Background",initialBackground);
        	     colorDisplayButton.setBackground(background);
        	     clientPanel.lineColor = background;
        	  } 
        	} );              
            
        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);
        group.add(option5);
        group.add(option6);
        group.add(option7);
        
        if(ClientFunctions.userName.equalsIgnoreCase("Manager")){
        panelToolBar.add(bar);
        }
        panelToolBar.add(option1);
        panelToolBar.add(option2); 
        panelToolBar.add(option3);
        panelToolBar.add(option4);
        panelToolBar.add(option5);
        panelToolBar.add(option6);
        panelToolBar.add(checkBox);
        panelToolBar.add(option7);
        panelToolBar.add(size);
        panelToolBar.add(lineColorButton);
        panelToolBar.add(colorDisplayButton);
        
        kickList = new JComboBox<String>();  
        kickList.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        	//  System.out.println( size.getSelectedItem());
        		  if(kickList.getSelectedItem()!=null){
        	  String selectedItem = kickList.getSelectedItem().toString();
        	  clientPanel.kickUser = selectedItem;}
        	  } 
        	} );
        panelToolBar.add(kickList);
        
        JButton kickButton = new JButton("Kick User");
        kickButton.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
      		  if(!ClientPaintArea.kickUser.equalsIgnoreCase("Manager")){
      		  map.remove(ClientPaintArea.kickUser);
      		//  kickList.remove(kickList.getSelectedIndex());
      		  clientPanel.kickUser();
      		  }
      	
      	  } 
      	} );
        if(ClientFunctions.userName.equals("Manager")){
        panelToolBar.add(kickButton);
        }
        
                   
     
        checkBox.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
      		  clientPanel.fill = checkBox.isSelected();
      		//  System.out.println(checkBox.isSelected());        		            
      	  } 
      	} ); 
        
        option1.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           buttonSelected = "reactangle";
	          }
	    });
	    option2.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	          buttonSelected = "circle";    
	          }
	    });
	    option3.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           buttonSelected = "oval";
	         }
	    });
	    option4.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           buttonSelected = "freehandwriting";
	            }
	    });
    	
	    option5.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           buttonSelected = "line";
	           }
	    });
	    option6.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           buttonSelected = "textinput";
	          }
	    });
	    option7.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	           buttonSelected = "eraser";
	          }
	    });    	
    	return panelToolBar;
    }
    
    public void getPanelPaint(JPanel panelPaint)
    {
    	panelPaint.setBackground(Color.cyan);
    //	panelPaint.setBorder(BorderFactory.createLineBorder(Color.black, 10));
    	JMenuBar connectionMenu = new JMenuBar();
    	panelPaint.add(connectionMenu);

    }
    
    public JPanel getPanelChat()
    {
    	JPanel panelChat = new JPanel();
    //	panelChat.setBackground(Color.green);
    	panelChat.setPreferredSize(new Dimension(400, 1400));
    	panelChat.setMaximumSize(new Dimension(400, 1400));
    //	panelChat.setBorder(BorderFactory.createLineBorder(Color.black, 10));
    	return panelChat;
    }

    public void addShape(Shape s,Color color, String size, boolean fill)
	{    	
		clientPanel.addShape(s, color,size, fill);
	//	System.out.println("Client guide previous shapews are aade");
	}    
    public void deleteAll()
   	{    	
   		clientPanel.deleteAll();
   	//	System.out.println("Client guide previous shapews are aade");
   	} 

	public void messageR(String message)
	{
		messageHistory.setText(messageHistory.getText()+"\n"+message);
		
	}
	public void closeFrame()
	{
		//System.out.println("disposed is called" );
	 if(frame!=null){
		 frame.dispose();
	 }
	// System.out.println("disposed second line");
		
	}
	
	public void updateUserList(String userName)
	{
		//System.out.println("all user list is : "+userName );
	   kickList.removeAllItems();
       map.clear();
    //   System.out.println(kickList.getItemCount()+"      "+map.size());
       map.put("", "");
	
		String[] parts = userName.split("\n");
	
		for(int i =0;i<parts.length;i++){
			String user = parts[i].trim();
			
		
			if(map.containsKey(user)){
		    	}else{
				
	     	kickList.addItem(user.trim());
	     	map.put(user, user);
	
			}
		}
       
		artist_area.setText(userName);
		
	}
	
	public void saveToFile(ArrayList<Object> data,String fileName)
	{
		try {
			File f = new File(fileName);
			if(f.exists() && !f.isDirectory()) { 
			    f.delete();
			  //  System.out.println("fileeee is deleted");
			}
			
			final ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(fileName));
		    fos.writeObject(data);
		    fos.close();
		  //  System.out.println("saveddddddddddddd");
		
		} catch (Exception e) {
			System.out.println(("Something went wrong while writing/saving file : "));
			System.out.println(e.getMessage());
		 
		}
	}
	public ArrayList<Object> openFromFile(String fileName)
	{
		ArrayList<Object> returnData = new ArrayList<Object>();
				try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			 returnData =(ArrayList<Object>)ois.readObject();
			
			
		}catch(Exception e)
		{
			System.out.println(("Something went wrong while reading file : "));
			System.out.println(e.getMessage());
		}
				return returnData;
			
	}
	
    
}
