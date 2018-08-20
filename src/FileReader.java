/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		FileReader.java
 *Description: 	A class that uses a file reader to get information about the setup of 
 *				the board from a database file (setup.txt). The information includes 
 *				all relevant country information for the creation of a new game or the 
 *				loading of a saved game.
 */

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
	//Declare Scanner
	private Scanner read;
	private PrintWriter write;
	private File file;

	//Class constructor
	public FileReader() throws NumberFormatException, IOException{
	    try {
	    	file = new File("Ressources/" + "setup.txt");
			read = new Scanner (file);			
		} catch (FileNotFoundException e) {
			// Catch Block
			e.printStackTrace();
		}
	}//End of constructor
	
	/*
	 * Called when the fileReader is initialized. It reads from a database file 
	 * (setup.txt) containing all the country information and card information.
	 */
	public void setup() throws NumberFormatException, IOException{
		for (int x = 0; x < 42; x++) {
			//Reads from the file directly into the declaration of a country object
			Board.Countries.add(new Country(Integer.parseInt(read.nextLine()), read.nextLine(), read.nextLine(), readNeighbors(Integer.parseInt(read.nextLine())), read.nextLine(), readColor(), Integer.parseInt(read.nextLine()), Integer.parseInt(read.nextLine())));  
			//Reuse the data from the country object to create the corresponding Card Object
			Board.Cards.add(new Card(Board.Countries.get(x).getName(), Board.Countries.get(x).getCardType(), Board.Countries.get(x).getDetectionColor()));
		}
		read.close();
		file = new File("Ressources/" +"save.txt");
		
	}
	//Reads in three integer values from a text file, then uses them to create a color
	public Color readColor() {
		int r = Integer.parseInt(read.nextLine());
		int g = Integer.parseInt(read.nextLine());
		int b = Integer.parseInt(read.nextLine());
		//Create the country's detection color variable
		return new Color(r, g , b);		
	}
	
	//Reads uses the integer value that was read in the constructor, to read in that many string values represented the country's neighbors
	public ArrayList<String> readNeighbors(int numOfNeighbors){
		ArrayList<String> Neighbors = new ArrayList<String>();
		//Add neighbors for the amount of neighbors specified by the previous integer
		for (int x = 0; x < numOfNeighbors; x++)
			Neighbors.add(read.nextLine());
		return Neighbors;
	}
	//Saves the state of the game when the save button was clicked. Reads into a database file(state.txt) 
	public void saveGame() throws IOException{
		write = new PrintWriter(file);
		write.write(Board.turnCounter + "\n");
		write.write(Board.players.size() + "\n");
		for(Player p: Board.players) {
			write.write(p.getCountries().size() + "\n");
			for (Country c: p.getCountries()) {
				write.write(c.getName() + "\n");
				write.write(c.getTroops() + "\n");
			}
			write.write(p.getCards().size() + "\n");
			for (Card c: p.getCards()) {
				write.write(c.getCountry() + "\n");
			}
		}
		write.write(Board.usedCards.size() + "\n");
		for(Card c: Board.usedCards) {
			write.write(c.getCountry() + "\n");
		}
		
		write.close();
	}//End of saveGame
	
	//Load the state of the game when the save button was clicked. Reads from a database file (state.txt) 
	public void loadGame() throws IOException{
		read = new Scanner (file);
		Board.turnCounter = Integer.parseInt(read.nextLine());
		Board.phase = Board.Phase.DEPLOY;
		int numOfPlayers = Integer.parseInt(read.nextLine());
		
		for (int i = 0; i < numOfPlayers; i++) {
			switch (i) {
			case 0:
				Player p1 = new Player("player", Color.cyan, 0);
				Board.players.add(p1);
				break;
			case 1:
				Player p2 = new Player("player", Color.red, 1);
				Board.players.add(p2);
				break;
			case 2:
				Player p3 = new Player("player", Color.blue, 2);
				Board.players.add(p3);
				break;
			case 3:
				Player p4 = new Player("player", Color.yellow, 3);
				Board.players.add(p4);
				break;
			case 4:
				Player p5 = new Player("player", Color.green, 4);
				Board.players.add(p5);
				break;
			case 5:
				Player p6 = new Player("player", Color.magenta, 5);
				Board.players.add(p6);
				break;
			}
		}
		
		for (int x = 0; x < numOfPlayers; x++) {
			int numOfCountries = Integer.parseInt(read.nextLine());
			for (int y = 0; y < numOfCountries; y++) {
				String countryName = read.nextLine();
				for (int z = 0; z < Board.CountryPicker.size(); z++) {
					if (Board.CountryPicker.get(z).getName().equals(countryName)) {
						int troops = Integer.parseInt(read.nextLine());
						Board.CountryPicker.get(z).setTroops(troops);
						Board.players.get(x).addCountries(Board.CountryPicker.get(z));
						Board.CountryPicker.remove(z);
					}
				}
			}
			int numOfCards = Integer.parseInt(read.nextLine());
			for (int y = 0; y < numOfCards; y++) {
				String cardCountry = read.nextLine();
				for (int z = 0; z < Board.Cards.size(); z++) {
					if (Board.Cards.get(z).getCountry().equals(cardCountry)) {
						Board.players.get(x).addCard(Board.Cards.get(z));
						Board.Cards.remove(z);
					}
				}
			}
		}
		
		int numOfUsedCards = Integer.parseInt(read.nextLine());
		for (int x = 0; x < numOfUsedCards; x++) {
			String cardCountry = read.nextLine();
			for (int z = 0; z < Board.Cards.size(); z++) {
				if (Board.Cards.get(z).getCountry().equals(cardCountry)) {
					Board.usedCards.add(Board.Cards.get(z));
					Board.Cards.remove(z);
				}
			}
		}
		
		for (Player p: Board.players) {
			for (Country c: p.getCountries()) {
				new threadz(c, p.getColor(), false);
			}
		}
		read.close();
	}//End of load game

}//End of FileReader



