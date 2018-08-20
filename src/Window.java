/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Window.java
 *Description: 	This class makes a JFrame and Canvas object for later use.
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Toolkit;

public class Window {

	//Declare and initialize variables
	private int width, height;
	private String title;
	private JFrame frame;
	private ScrollPane scrollPane;
	private Canvas canvas;
	private Dimension gamDim = new Dimension(width, height);
	private JPanel bottomPanel;
	private JPanel bigPanel;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	//Create constructor
	public Window(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		init();
	}//End of constructor

	//Initialize components of the JFrame
	public void init(){
		//Retrieve the height and width of the monitor
		int width2 = (int) screenSize.getWidth();
		int height2 = (int) screenSize.getHeight();
		
		//If the height and width of the monitor is larger than the game dimensions, set the JFrame dimension to the game dimensions
		frame = new JFrame(title);
		if (width2 > 1307) 
			width2 = 1294;
//		
		if (height2 > 1050)
			height2 = 1035;
		//Initialize the frame
		frame.setSize(width2, height2);		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLayout(null);		
		
		//Initialize the canvas
		canvas = new Canvas();
		canvas.setSize(width, height);
		canvas.setMaximumSize(gamDim);
		canvas.setMinimumSize(gamDim);
		canvas.setPreferredSize(gamDim);		
		
		//Initialize the overall container
		bigPanel = new JPanel();
		bigPanel.setBounds(0, 0, width, height);
		bigPanel.setPreferredSize(new Dimension(width, height));
		bigPanel.setLayout(null);
		
		//Initialize the scroll pane
		scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scrollPane.add(bigPanel);
		scrollPane.setSize(new Dimension(width2-5,height2-25));
		
		//Initialize the lower panel of the game
		bottomPanel = new JPanel();
		bottomPanel.setBounds(0,720,1280,280);
		bottomPanel.setLayout(null);
		bottomPanel.setBackground(new Color(135, 206, 235));
		
		//Add the contents to the big Panel
		bigPanel.add(bottomPanel);
		bigPanel.add(canvas);
		
		//Add the scroll bars to to the frame
		frame.add(scrollPane);
		frame.setVisible(true);
	}//End of init
	
	//Get frame
	public JFrame getFrame(){
		return frame;
	}//End of getFrame
	
	//Get canvas
	public Canvas getCanvas(){
		return canvas;
	}//End of getCanvas
	
	public JPanel getPanel() {
		return bottomPanel;
	}

}//End of Window






