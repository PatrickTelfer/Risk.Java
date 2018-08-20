/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Country.java
 *Description: 	This class stores the information necessary to be stored in each Country 
 *				object such as what its detection color is (used to allow mouseover 
 *				highlighting), the color displayed, it’s name, its position on the map, 
 *				who its owned by, the number of troops stationed on that country, if it 
 *				is being hovered over and its image.
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Country {
	//Declare objects
	private String name;
	private String continent;
	private ArrayList<String> Neighbours;
	private Color detectionColor;
	private String cardType;
	private BufferedImage img;

	//Declare variables
	private boolean highlighted;
	private boolean isClicked;
	private int xPos;
	private int yPos;
	private int troops;
	private int countryNum;

	// Class constructor
	public Country(int countryNum, String name, String continent, ArrayList<String> Neighbours, String cardType,
			Color detectionColor, int xPos, int yPos) throws IOException {
		//Initialize and instantiate variable and objects from the constructor
		this.countryNum = countryNum;
		this.name = name;
		this.continent = continent;
		this.Neighbours = Neighbours;
		this.detectionColor = detectionColor;
		this.cardType = cardType;
		this.troops = 0;
		//These numbers reference the location the troop number will be displayed
		this.xPos = xPos;
		this.yPos = yPos;
		//Retrieve the countries corresponding png image
		img = (ImageIO.read(new File ("Ressources/" +"countries/" + name + ".png")));
	}// End of constructor

	/*
	 * When the player clicks one of the countries (must be a country they own),
	 * it highlights the neighbouring countries it can interact with - either to
	 * attack or fortify, depending on the phase.
	 */
	public void HighlightNeighbours() {
		int counter1 = 0;
		while (threadz.getCounter() !=0) {
			//Wait until all recoloring has finished
			//System.out.println(threadz.getCounter());
			if (Board.Cards.size() > 0)
				Collections.shuffle(Board.Cards);
			else
				Collections.shuffle(Board.usedCards);
			counter1++;
			if (counter1 > 150000)
				break;
		}		
		//Declare and initialize playerNum
		int playerNum = 0;
		
		//Nested for loop for players and countries
		for (Player p : Board.players) {
			for (Country c : p.getCountries()) {
				if (c.getName() == name) {
					//Get the name of the player
					playerNum = p.getPlayerNum();
				}//End of if statement
			}//End of first for loop
		}//End of second for loop

		//Nested for loop for the neighbours of the highlighted country
		for (String s : Neighbours) {
			for (Player pl : Board.players) {
				//Prevent the neighbours owned by the same player from being highlighted
				if (pl.getPlayerNum() != playerNum) {
					for (Country co : pl.getCountries()) {
						//Make the neighbouring countries darker
						if (s.equals(co.getName()))
							new threadz(co, pl.getColor().darker().darker().darker(), true);
					}//End of for loop
				}//End of if statement
			}//End of first for loop
		}//End of second for loop

	}// End of highlight neighbours

	public void unHighlightNeighbours() {
			int counter1 = 0;
		while (threadz.getCounter() !=0) {
				//Wait until all recoloring has finished
				
			if (Board.Cards.size() > 0)
				Collections.shuffle(Board.Cards);
			else
				Collections.shuffle(Board.usedCards);
				counter1++;
				if (counter1 > 150000)
					break;
			}		
			//Declare and initialize playerNum
			int playerNum = 0;
			
			//Nested for loop for players and countries
			for (Player p : Board.players) {
				for (Country c : p.getCountries()) {
					if (c.getName() == name) {
						//Get the name of the player
						playerNum = p.getPlayerNum();
					}//End of if statement
				}//End of first for loop
			}//End of second for loop

			//Nested for loop for the neighbours of the highlighted country
			for (String s : Neighbours) {
				for (Player pl : Board.players) {
					//Prevent the neighbours owned by the same player from being highlighted
					if (pl.getPlayerNum() != playerNum) {
						for (Country co : pl.getCountries()) {
							//Make the neighbouring countries darker
							if (s.equals(co.getName())) {
								if(co.highlighted == true) {
									new threadz(co, pl.getColor().brighter().brighter(), false);
									//System.out.println("Unhighlighted " + co.getName());
								}
							}
						}//End of for loop
					}//End of if statement
				}//End of first for loop
			}//End of second for loop
		
	}
	
	public void highlightOwned() {

			int counter1 = 0;
			while (threadz.getCounter() !=0) {
				//Wait until all recoloring has finished
				//System.out.println(threadz.getCounter());
				if (Board.Cards.size() > 0)
					Collections.shuffle(Board.Cards);
				else
					Collections.shuffle(Board.usedCards);
				counter1++;
				if (counter1 > 150000)
					break;
			}		
			//Declare and initialize playerNum
			int playerNum = 0;
			
			//Nested for loop for players and countries
			for (Player p : Board.players) {
				for (Country c : p.getCountries()) {
					if (c.getName() == name) {
						//Get the name of the player
						playerNum = p.getPlayerNum();
					}//End of if statement
				}//End of first for loop
			}//End of second for loop

			//Nested for loop for the neighbours of the highlighted country
			for (String s : Neighbours) {
				for (Player pl : Board.players) {
					//Prevent the neighbours owned by the same player from being highlighted
					if (pl.getPlayerNum() == playerNum) {
						for (Country co : pl.getCountries()) {
							//Make the neighbouring countries darker
							if (s.equals(co.getName()))
								new threadz(co, pl.getColor().darker().darker().darker(), true);
						}//End of for loop
					}//End of if statement
				}//End of first for loop
			}//End of second for loop

		}// End of highlight ownded
		
	
	public void unhighlightOwned() {

		int counter1 = 0;
		while (threadz.getCounter() !=0) {
			//Wait until all recoloring has finished
			//System.out.println(threadz.getCounter());
			if (Board.Cards.size() > 0)
				Collections.shuffle(Board.Cards);
			else
				Collections.shuffle(Board.usedCards);
			counter1++;
			if (counter1 > 150000)
				break;
		}		
		//Declare and initialize playerNum
		int playerNum = 0;
		
		//Nested for loop for players and countries
		for (Player p : Board.players) {
			for (Country c : p.getCountries()) {
				if (c.getName() == name) {
					//Get the name of the player
					playerNum = p.getPlayerNum();
				}//End of if statement
			}//End of first for loop
		}//End of second for loop

		//Nested for loop for the neighbours of the highlighted country
		for (String s : Neighbours) {
			for (Player pl : Board.players) {
				//Prevent the neighbours owned by the same player from being highlighted
				if (pl.getPlayerNum() == playerNum) {
					for (Country co : pl.getCountries()) {
						//Make the neighbouring countries darker
						if (s.equals(co.getName()))
							new threadz(co, pl.getColor().brighter().brighter().brighter(), false);
					}//End of for loop
				}//End of if statement
			}//End of first for loop
		}//End of second for loop

	}// End of highlight neighbours
	
	// Outputs all the variables of the player class
	public String toString() {
		String info = name + " " + troops;
		return info;
	}// End of toString

	
	//Getters and setters of the Country class
	public BufferedImage getImg() {
		return img;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public String getContinent() {
		return continent;
	}

	public int getCountryNum() {
		return countryNum;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getNeighbours() {
		return Neighbours;
	}

	public Color getDetectionColor() {
		return detectionColor;
	}

	public String getCardType() {
		return cardType;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public int getTroops() {
		return troops;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	
}// End of Country




