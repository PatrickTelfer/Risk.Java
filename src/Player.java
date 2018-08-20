
/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Player.java
 *Description: 	This class holds all information relevant to each individual player, 
 *				such as what countries and cards they own, as well as their color 
 *				and name.
 */

import java.awt.Color;
import java.util.ArrayList;

public class Player {
	// Declare variables and objects
	private Color color;
	private String name;
	private ArrayList<Country> countries;
	private ArrayList<Card> cards;
	private int playerNum;
	private boolean myTurn;

	// Class constructor
	public Player(String name, Color color, int playerNum) {
		this.name = name;
		this.color = color;
		this.playerNum = playerNum;
		countries = new ArrayList<Country>();
		cards = new ArrayList<Card>();
	}// End of constructor

	// Outputs all the countries and cards owned by the player
	public String toString() {
		String str = "Countries: " + countries + " \nCards: " + cards;
		return str;
	}// End of toString

	//Add a country to this player
	public void addCountries(Country country) {
		this.countries.add(country);
	}

	//Add a card to this player
	public void addCard(Card card) {
		this.cards.add(card);
	}	
	
	// getters/setters
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	public boolean checkOwn(String string) {
		for (Country c : countries) {
			if (c.getName().equals(string)) {
				return true;
			}
		}
		return false;

	}
}// End of Player
