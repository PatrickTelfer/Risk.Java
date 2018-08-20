import java.awt.Color;

/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Card.java
 *Description: 	This class stores all the information necessary for each card Object 
 *				such as which country it displays, what type it is and if it has been 
 *				used.
 */

public class Card {
	//Declare variables
	private String cardType;
	public Color getDetectionColor() {
		return detectionColor;
	}

	private String country;
	private Color detectionColor;
	private boolean used;
	
	//Class constructor
	public Card(String country, String type, Color color){
		this.country = country;
		this.cardType = type;
		this.detectionColor = color;
	}//End of constructor

	//getters/setters
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
	
	//Outputs the card type and country on the card.
	public String toString(){
		String placeholder = " " + country + ", " + cardType;
		return placeholder;
	}//End of toString

}//End of Card
