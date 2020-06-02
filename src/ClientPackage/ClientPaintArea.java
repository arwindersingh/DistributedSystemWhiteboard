package ClientPackage;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

import Remote.RMIRemoteServerInterface;

public class ClientPaintArea extends JComponent {
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<String> brushValue = new ArrayList<String>();
	private ArrayList<Color> colors = new ArrayList<Color>();
	private ArrayList<Boolean> filled = new ArrayList<Boolean>();
	RMIRemoteServerInterface remoteServerInterface = null;

	static Graphics2D g2 = null;
	
    Point startDragging, endDragging;
    
    static String buttonSelected = " ";
    ClientGui clientGui = null;
    static String size = "";
    static String kickUser = "";

	 static Color lineColor = Color.black;
	 static boolean fill = false;
   // Client client = null;
    public int x1,x2,y1,y2;
    public ClientPaintArea(){
    	
    }

    public ClientPaintArea(RMIRemoteServerInterface remoteServerInterface) {
    	super();
    	this.remoteServerInterface = remoteServerInterface;
	//	this.clientGui = clientGui;
	//	this.client = client;
      this.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
        	if(g2!=null){
        		g2.setColor(lineColor);
        	}
        	x1 = e.getX();
        	y1 = e.getY();
        	//buttonSelected = clientGui.buttonSelected;
        	buttonSelected = ClientGui.buttonSelected;
        	//System.out.println("new value of button : "+buttonSelected);
          startDragging = new Point(e.getX(), e.getY());
          endDragging = startDragging;
          repaint();
        }

        public void mouseReleased(MouseEvent e) {
        
        	Shape newShape = null;
        	
        	if(buttonSelected.equals("reactangle")){
        	  newShape = makeRectangle(startDragging.x, startDragging.y, e.getX(), e.getY());
        	}
        	else if(buttonSelected.equals("circle"))  	{
        		newShape = makeCircle(startDragging.x, startDragging.y, e.getX(), e.getY());
        	}
        	else if(buttonSelected.equals("oval"))  	{
        		newShape = makeOval(startDragging.x, startDragging.y, e.getX(), e.getY());
        	}
        	else if(buttonSelected.equals("freehandwriting"))  	{
        		//newShape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
        	}
        	else if(buttonSelected.equals("line"))  	{
        		newShape = makeLine(startDragging.x, startDragging.y, e.getX(), e.getY());
        	}
        	else if(buttonSelected.equals("textinput"))  	{
        		// = makeRectangle(startDragging.x, startDragging.y, e.getX(), e.getY());
        		
        	
        	}
        	else if(buttonSelected.equals("eraser"))  	{
        		//newShape = makeOval(startDragging.x, startDragging.y, e.getX(), e.getY());
        	}
        	if(newShape != null){
        		
             shapes.add(newShape);
             brushValue.add(size);
             colors.add(lineColor);
           //  colors.add(fillcolor);
             filled.add(fill);
         //    valueArray[0] = "";valueArray[1] = ""; valueArray[2] = "";
             
           //  newS = newShape;
           //  System.out.println("shape added to locals shapes");
             try {
				remoteServerInterface.uploadShape(newShape, lineColor, size, fill);
				
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	}
                 	
          startDragging = null;
          endDragging = null;
          repaint();
        }
      });

      this.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
          endDragging = new Point(e.getX(), e.getY());
          if(buttonSelected.equals("freehandwriting")) {
    	      x2 = e.getX();
    	      y2 = e.getY();
          Shape newShape = makeLine(x1, y1, x2, y2);
          shapes.add(newShape);
          brushValue.add(size);
          colors.add(lineColor);
      	  filled.add(fill);    
      	try {
			remoteServerInterface.uploadShape(newShape, lineColor, size, fill);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
          
          
        //  System.out.println("shape added to locals shapes");
          x1 = x2;
          y1 = y2;
      }
          
          if(buttonSelected.equals("eraser")) {
    	      x2 = e.getX();
    	      y2 = e.getY();
          Shape newShape = makeLine(x1, y1, x2, y2);
          shapes.add(newShape);
          brushValue.add(size);
          colors.add(Color.white);
          filled.add(fill);
        	try {
    			remoteServerInterface.uploadShape(newShape, Color.white, size, fill);
    		} catch (RemoteException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
          }
       //   System.out.println("shape added to locals shapes");
          x1 = x2;
          y1 = y2;
          }
      
      }
      });
    }
    
    public void paintComponent(Graphics g) {    
    
    	g2 = (Graphics2D) g;     
    //	drawImage();
    	
    	for(int i =0; i<shapes.size();i++){
    		
       	   setGValue(g2,i);
       	   if(filled.get(i)){
       	      g2.fill(shapes.get(i));
       	    }else{
               g2.draw(shapes.get(i));   
       	     }
    	}        
        
         if (startDragging != null && endDragging != null && buttonSelected.equals("reactangle")) {
             Shape r = makeRectangle(startDragging.x, startDragging.y, endDragging.x, endDragging.y);          
             g2.draw(r);
         }
         
         if (startDragging != null && endDragging != null && buttonSelected.equals("circle")) {
             Shape r = makeCircle(startDragging.x, startDragging.y, endDragging.x, endDragging.y);     
             g2.draw(r);
           }      
         
         if (startDragging != null && endDragging != null && buttonSelected.equals("textinput")) {
             Shape r = makeRectangle(startDragging.x, startDragging.y, endDragging.x, endDragging.y);     
             g2.draw(r);
           }
         if (startDragging != null && endDragging != null && buttonSelected.equals("line")) {
             Shape r  =new Line2D.Double(startDragging.x, startDragging.y, endDragging.x, endDragging.y);
                 
             g2.draw(r);
           }
         if (startDragging != null && endDragging != null && buttonSelected.equals("oval")) {   
             Shape r = new Ellipse2D.Double(Math.min(startDragging.x, endDragging.x), Math.min(startDragging.y, endDragging.y), Math.abs(startDragging.x - endDragging.x), Math.abs(startDragging.y - endDragging.y));
             g2.draw(r);
           }
         repaint();
        
    }

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
      return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    
    private Ellipse2D.Double makeOval(int x1, int y1, int x2, int y2) {
        return new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    	 
      }
    private Ellipse2D.Double makeCircle(int x1, int y1, int x2, int y2) {
	    double radius = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	    return new Ellipse2D.Double(Math.min(x1,x2) - radius, Math.min(y1,y2) - radius, 2.0 * radius, 2.0 * radius);
    }
    private Line2D.Float makeLine(int x1,int y1,int x3,int y3) {
	    return new Line2D.Float(x1,y1,x3,y3);
    }
    
    public void addShape(Shape s,Color color, String size, boolean fill)
    {
    	shapes.add(s);
    	colors.add(color);
    	brushValue.add(size);
    	filled.add(fill);    	
    //	System.out.println("shapes are added  from previous : ");
    }

    public void setGValue(Graphics2D g, int i)
    {
    	if(brushValue.get(i).equalsIgnoreCase("Small")){
    	 g2.setStroke(new BasicStroke(5));
    	}else if (brushValue.get(i).equalsIgnoreCase("Medium")){
    		g2.setStroke(new BasicStroke(12));
    	}else if (brushValue.get(i).equalsIgnoreCase("Large")){
    		g2.setStroke(new BasicStroke(20));
    	}
       g2.setColor(colors.get(i));
    	     	
    }
    public void drawImage()
    {
    	//System.out.println("drawing texgt image");
    	 Image img1 = Toolkit.getDefaultToolkit().getImage("file.gif");
 		 g2.drawImage(img1, 2, 4, this);
    }
    
    public void deleteAll()
    {
    	shapes.clear();brushValue.clear();colors.clear();filled.clear();
    }
    
    public void kickUser(){
    	try {
			remoteServerInterface.logout(kickUser, false);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
  }


