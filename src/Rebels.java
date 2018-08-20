/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Rebels.java
 *Description: 	The class responsible for the generation of a randomly located neutral 
 *				army of random size, at the end of each round of turns.
 */

public class Rebels {
	//Declare variables
	private int numOfRebels;
	private boolean revolting;
	private String Location;
		
	//Class constructor
	public Rebels(){
		
	}//End of constructor

	//getters/setters

	//This method prints the numOfRebels, their location and if they are revolting
	public String toString(){
		String placeholder = "placeholder";
		return placeholder;
	}//End of toString
	
	/*
	 * After each player plays a turn, this method randomly generates a number between 
	 * one and two. If the number is two, a random number of rebels  between 1 and 6 
	 * is spawned in a random country. Rebels can only spawn in a country where the 
	 * player who owns it did not attack in the previous phase - if the country picked 
	 * is a country that attacked, the rebels do not spawn that round. Rebels cannot 
	 * spawn either if the current number of rebels isn’t zero.
	 */
	public void spawn(){
		
	}//End of spawn

}//End of Rebels
