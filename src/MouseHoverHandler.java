/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		MouseHoverHandler.java
 *Description: 	This class tracks the user’s mouse movements and stores the mouse 
 *				position in integer variables.
 */

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseHoverHandler implements MouseMotionListener {
		//Declare Variables
		public int mouseY;
		public int mouseX;
	//Constructor that adds that mouse handler to the JFrame's canvas
	public MouseHoverHandler(Canvas canvas){
		canvas.addMouseMotionListener(this);
	}//End of constructor

	//When the mouse is moved, store mouse’s position in x,y int variables
	public void mouseDragged(MouseEvent e) {
		mouseX  = e.getX();
		mouseY = e.getY();
	}

	//Unimplemented method
	public void mouseMoved(MouseEvent e) {
		mouseX  = e.getX();
		mouseY = e.getY();
		
	}//End of mouse moved	

}//End of mouse hover handler 