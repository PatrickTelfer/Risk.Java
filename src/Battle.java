/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Battle.java
 *Description: 	This program takes in the number of attacker and defenders of a 
 *				particular battle, and then outputs the winner of the battle. The 
 *				battle is based on dice rolls, with ties going to the defender.
 */

import java.util.ArrayList;
import java.util.Collections;

public class Battle {

	private static int Attackers;
	private static int Defenders;
	private static int diceReqDef;
	private static int diceReqAtt;

	// Constructor that sets the number of defenders and attackers, and how many
	// dice will be used for the battle
	public Battle(int Attacker, int Defender) {
		this.Attackers = Attacker;
		this.Defenders = Defender;

		
		//Determine the number of dice required for the defender
		if (Defender >= 2) {
			diceReqDef = 2;
		} else {
			diceReqDef = 1;
		}//End of if statement

		//Determine the number of dice required for the attacker
		if (Attacker >= 3) {
			diceReqAtt = 3;
		} else if (Attacker == 2) {
			diceReqAtt = 2;
		} else {
			diceReqAtt = 1;
		}//End of if statement

	}// End of constructor

	//Create an enum for the victor of the battle
	public enum Victor {
		//Set the possible values of victor
		ATTACKER, DEFENDER
	}

	// Returns an array list of the attacker’s dice rolls
	public static ArrayList<Integer> RollDiceAtt() {
		ArrayList<Integer> AttackersArray = new ArrayList<Integer>();

		//Randomly generate the dice rolls and add them to the array
		for (int i = 0; i < diceReqAtt; i++) {
			int number = (int) ((Math.random() * 6) + 1);
			AttackersArray.add(number);
		}//End of for loop

		return AttackersArray;
	}// End of roll dice att

	// Returns an array list of the defender’s dice rolls
	public static ArrayList<Integer> RollDiceDef() {
		ArrayList<Integer> DefendersArray = new ArrayList<Integer>();

		//Randomly generate the dice rolls and add them to the array
		for (int i = 0; i < diceReqDef; i++) {
			int number = (int) ((Math.random() * 6) + 1);
			DefendersArray.add(number);
		}//End of for loop
		
		return DefendersArray;
	}// End of dice roll def

	//Determine the highest roll for each player
	public static int CheckHighest(ArrayList<Integer> array) {
		//Declare and initialize variable for the current number
		int num = 0;
		
		//Compare the number with those in the array
		for (int y : array) {
			if (y > num) {
				num = y;
			}//End of if statement
		}//End of for loop
		
		//Return num
		return num;
	}//End of check highest

	//Determine the victor by comparing the n=dice rolled by each player
	public static Victor Compare(ArrayList<Integer> Attackers, ArrayList<Integer> Defenders) {
		//Declare and initialize variables
		Victor v;
		int numAtt = 0;
		int numDef = 0;
		
		//Determines highest roll for attackers
		for (int x : Attackers) {
			if (x > numAtt) {
				numAtt = x;
			}//End of if statement
		}//End of for loop

		//Determines highest roll for Defenders
		for (int y : Defenders) {
			if (y > numDef) {
				numDef = y;
			}//End of if statement
		}//End of for loop

		//Compare the rolls and determine the victor
		if (numDef >= numAtt) {
			v = Victor.DEFENDER;
			return v;
		} else if (numAtt > numDef) {
			v = Victor.ATTACKER;
			return v;
		}//End of if statement
		
		//Blank return statement
		return null;

	}//End of victor compare

	//Complete the required amount of rolls until no troops remain for 1 of the players
	public void BattleTroops() {
		//Run while both players have troops remaining
		while ((Attackers > 0) && (Defenders > 0)) {
			//Roll the dice
			ArrayList<Integer> AttackersRolls = Battle.RollDiceAtt();
			ArrayList<Integer> DefendersRolls = Battle.RollDiceDef();

			//Get the rolls in order to easily compare
			Collections.sort(AttackersRolls);
			Collections.sort(DefendersRolls);
			Collections.reverse(AttackersRolls);
			Collections.reverse(DefendersRolls);

			//Compare rolls in order to determine troops lost
			while (!AttackersRolls.isEmpty() && !DefendersRolls.isEmpty()) {
				//Take away 1 troop per roll lost
				if (Battle.Compare(AttackersRolls, DefendersRolls) == Victor.DEFENDER) {
					Attackers--;
				} else if (Battle.Compare(AttackersRolls, DefendersRolls) == Victor.ATTACKER) {
					Defenders--;
				}//End of if statement

				//Make the program break out of the while loop when no troops remain
				if ((Attackers == 0) || (Defenders == 0)) {
					break;
				}//End of if statement

				//Remove the highest roll, to compare the next
				AttackersRolls.remove(Integer.valueOf(Battle.CheckHighest(AttackersRolls)));
				DefendersRolls.remove(Integer.valueOf(Battle.CheckHighest(DefendersRolls)));

			}//End of Second While loop
			
			//Make the program break out of the while loop when no troops remain
			if ((Attackers == 0) || (Defenders == 0)) {
				break;
			}//End of if statement

		}//End of first while loop

	}//End of battle troops

	//Getters and setters
	public static int getAttackers() {
		return Attackers;
	}

	public static void setAttackers(int attackers) {
		Attackers = attackers;
	}

	public static int getDefenders() {
		return Defenders;
	}

	public static void setDefenders(int defenders) {
		Defenders = defenders;
	}
	
}// End of Battle


