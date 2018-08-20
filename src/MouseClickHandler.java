/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		MouseClickHandler.java
 *Description: 	This class tracks the user’s mouse movements and stores the mouse position in integer variables. If the MouseClickHandler clicks a card, it will add or remove that card from a selected cards array and highlight it accordingly
 */

//Imports
import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClickHandler implements MouseListener {

	// Declare clicked
	public boolean clicked;

	// Create constructor
	public MouseClickHandler(Canvas canvas) {
		canvas.addMouseListener(this);
	}// End of constructor

	// mouseClicked method
	public void mouseClicked(MouseEvent e) {
		// If the locotion of the mouse is referencing an object with Y value of
		// 100, the object is a card
		if (e.getComponent().getY() == 100) {
			// If the card is currently selected, deselect it, and make it's
			// selection background invisible
			if (Board.cardSelectedPics[(e.getComponent().getX() - 70) / 110].isVisible()) {
				Board.cardSelectedPics[(e.getComponent().getX() - 70) / 110].setVisible(false);
				// Remove the card the player is deselecting from the selected cards array
				for (int x = 0; x < Board.selectedCards.size(); x++) {
					if (Board.selectedCards.get(x) == Board.currentCards.get((e.getComponent().getX() - 70) / 110))
						Board.selectedCards.remove(x);
				}
				Board.selectedCards.remove(Board.currentCards.get((e.getComponent().getX() - 70) / 110));
			} // Only 3 cards can be selected at time - Ensure that the selected cards array is less than 3 before trying to select a card
			else if (Board.selectedCards.size() < 3) {
				// Set the card's selection background to be visible
				Board.cardSelectedPics[(e.getComponent().getX() - 70) / 110].setVisible(true);
				// Add the selected card to the selected Card array
				Board.selectedCards.add(Board.currentCards.get((e.getComponent().getX() - 70) / 110));
			} // End of if statement

		} else {
			// Set clicked to true
			clicked = true;

		} // End of if statement
	}// End of mouseClicked

	// Other unused mouse-events
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Unused

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Unused

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (Board.CountryPicker.size() > 0) {
			clicked = true;
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		clicked = false;

	}
}// End of mouseClickHandler



