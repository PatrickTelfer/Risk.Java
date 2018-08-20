import java.io.IOException;

/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Launcher.java
 *Description: 	This class will be used to make a new Board object and start its thread. 
 *				It contains the main method to begin the program.
 */

public class Launcher {
	
	public static void main(String[] args) throws IOException {
		//Start the manager
		new Board().start();
		
	}//End of Main method	
}//End of Launcher class