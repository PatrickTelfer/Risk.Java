import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/*
 *Name: 		Pat, Rob, Stephen
 *Course: 		ICS 4U1
 *Teacher:		Mr. Rhiger
 *Project:		Risk
 *Class:		Board.java
 *Description: 	This class is the game manager. It controls all the main components 
 *				of the game including rendering the graphics and calling all other 
 *				update methods from other objects. It will hold the main data structures 
 *				(the arraylists of cards, players and countries). It will also keep 
 *				track of whose turn it is and what phase of turn that player is currently 
 *				in.
 */

public class Board implements Runnable, ActionListener {
	// Declare variables
	private BufferStrategy bs;
	private Window window;
	private Thread thread;
	private Graphics g;
	private boolean running;
	private static final int UPDATE_RATE = 40;
	private static final int BOX_WIDTH = 1285;
	private static final int BOX_HEIGHT = 1000;
	private int invisData[][];
	public static ArrayList<Country> Countries;
	public static ArrayList<Country> CountryPicker;
	public static ArrayList<Country> CountriesCopy;
	public static ArrayList<Card> Cards;
	public static ArrayList<Player> players;
	public static ArrayList<Card> usedCards;
	public static JLabel cardPics[] = new JLabel[5];
	public static JLabel cardSelectedPics[] = new JLabel[5];
	public static ArrayList<Card> currentCards;
	public static ArrayList<Card> selectedCards;
	public static int turnCounter;
	private BufferedImage invisMap;
	private int imgW;
	private int imgH;
	private BufferedImage waterMap;
	private BufferedImage labels;
	private FileReader FileRhiger;
	private MouseHoverHandler mhh;
	private MouseClickHandler mch;
	private Color hoveredColor;
	private JButton phaseButton = new JButton("Next Phase");
	private JButton cashInButton = new JButton("Cash-In Cards");
	private JButton loadButton = new JButton("Load Game");
	private JButton saveButton = new JButton("Save Game");
	private JLabel phaseLabel;
	private JLabel phaseLabel2;
	private JLabel cardBackground;
	private JTextArea continentData;
	private JTextArea cardsData;
	private int troopSpendage;
	private JLabel troopsToDeploy;
	private int initialtroops = 0;
	private int phaseInit = 0;
	private Country attackingCountry = null;
	private Country defendingCountry = null;
	private boolean battle;
	private boolean hasAttacked = false;
	private boolean hasWon = false;
	private boolean reinforced;
	private JSlider attackSlide;
	private JLabel attacker;
	private int tempAtt;
	private JSlider playerPicker;
	private JButton startGame = new JButton("Start Game");
	private JLabel playerNum;
	private boolean started = false;
	private JLabel box;
	private int numberOfPlayers;
	private boolean gameOver;

	public enum Phase {
		DEPLOY, ATTACK, REINFORCE
	}

	//Sets the inital phase
	public static Phase phase = Phase.DEPLOY;

	// The class constructor
	public Board() throws IOException {
		//Creates a window and adds a buffer strategy to it
		window = new Window("Risk", BOX_WIDTH, BOX_HEIGHT);
		window.getCanvas().createBufferStrategy(3);
		bs = window.getCanvas().getBufferStrategy();
		//Starts the turn counter at zero
		turnCounter = 0;

		
		init();

	}// End of constructor

	// Starts the thread
	public void run() {
		//Only update the program if no threadz are running
		while (running) {
			if (threadz.getCounter() == 0) {
				try {
					update();
				} catch (IOException e2) {
					//Catch
					e2.printStackTrace();
					stop();
				}
			}
			try {
				Thread.sleep(1000 / UPDATE_RATE); // Controls the FPS (Frames
													// per Second)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}// End of run

	// Starts the program
	public void start() {
		running = true;
		run();
	}// End of start

	// Stops the program
	public void stop() {
		//If the program has an error this will stop the program
		try {
			running = false;
			g.dispose();
			thread.join();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}// End of stop

	// Called once when the object is instantiated
	public void init() throws IOException {
		//Initialize all the arrayLists
		Countries = new ArrayList<Country>();
		CountriesCopy = new ArrayList<Country>(Countries.size());
		Cards = new ArrayList<Card>();
		players = new ArrayList<Player>();
		currentCards = new ArrayList<Card>();
		selectedCards = new ArrayList<Card>();
		usedCards = new ArrayList<Card>();
		
		//Declare the file Reader and read-in all the card and country data
		FileRhiger = new FileReader();
		FileRhiger.setup();
		
		//Make the countryPicker arrayList identical to the the countries arrayList
		CountryPicker = Countries;

		//Adds the mouseClickhandler and mouseHoverHandler to the canvas
		mhh = new MouseHoverHandler(window.getCanvas());
		mch = new MouseClickHandler(window.getCanvas());
		
		
		box = new JLabel();
		window.getPanel().add(box);
		box.setBounds(0, 0, 1300, 800);
		
		playerNum = new JLabel();
		window.getPanel().add(playerNum);
		playerNum.setBounds(100,50,500,50);
		playerNum.setFont(new Font("Vani",25,25));
		
		playerPicker = new JSlider(2,6,2);
		window.getPanel().add(playerPicker);
		playerPicker.setBounds(100,100,500,100);
		playerPicker.setBorder(BorderFactory.createBevelBorder(0));
		playerPicker.setBackground(new Color (0,153,255));
		
	
		window.getPanel().add(startGame);
		startGame.addActionListener(this);
		
		window.getPanel().add(startGame);
		startGame.setBounds(650, 100, 150, 100);
		startGame.setFont(new Font("Vani", Font.BOLD, 20));
		startGame.setBorder(BorderFactory.createBevelBorder(0));
		startGame.setBackground(new Color(204,153,255));
	
		
		//Loads in all the background images of the map, the invis map will not be rendered but will be used to detect hovering
		invisMap = ImageIO.read(new File ("Ressources/" +"ColorMapFinal.png"));
		waterMap = ImageIO.read(new File ("Ressources/" +"gMap200.png"));
		labels = ImageIO.read(new File ("Ressources/" + "gLabels.png"));
		//waterMap = ImageIO.read(getClass().getResourceAsStream("Ressources/" +"gMap200.png"));
		
		
		//Makes a 2D array of the pixels in the invisMap, (all of its pixels will be stored here)
		imgW = invisMap.getWidth();
		imgH = invisMap.getHeight();
		invisData = new int[imgW][imgH];
		
		//Fills the arrayList with all the pixels RGB values of the invismap image
		for (int i = 0; i < imgW - 1; i++) {
			for (int j = 0; j < imgH - 1; j++) {
				invisData[i][j] = invisMap.getRGB(i, j);

			}
		}
		//Loop that creates JLabels to display the cards of the player
		for (int x = 0; x < 5; x++) {
			cardPics[x] = new JLabel("");
			window.getPanel().add(cardPics[x]);
			cardPics[x].setBounds(70 + (x * 110), 100, 100, 150);
		}
		//Loop that creates JLabels to display when a card has been selected
		for (int x = 0; x < 5; x++) {
			cardSelectedPics[x] = new JLabel("");
			cardSelectedPics[x].setBackground(Color.BLUE);
			window.getPanel().add(cardSelectedPics[x]);
			cardSelectedPics[x].setBounds(68 + (x * 110), 98, 104, 154);
			cardSelectedPics[x].setOpaque(true);
			cardSelectedPics[x].setVisible(false);
		}

		

		//Adds a label to display how many troops a player can display at that moment
		troopsToDeploy = new JLabel();
		window.getPanel().add(troopsToDeploy);
		troopsToDeploy.setFont(new Font("Vani", Font.PLAIN, 20));
		troopsToDeploy.setBounds(200, 40, 300, 50);

		// Add phase button
		phaseButton.setVisible(false);
		phaseButton.addActionListener(this);
		window.getPanel().add(phaseButton);
		phaseButton.setFont(new Font("Vani", Font.BOLD, 14));
		phaseButton.setBounds(60, 20, 120, 50);
		phaseButton.setVerticalTextPosition(SwingConstants.CENTER);
		phaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
		phaseButton.setBackground(Color.white);
		
		// Add loadGame button
		loadButton.setVisible(true);
		loadButton.addActionListener(this);
		window.getPanel().add(loadButton);
		loadButton.setFont(new Font("Vani", Font.BOLD, 20));
		loadButton.setBounds(820, 100, 120, 100);
		loadButton.setVerticalTextPosition(SwingConstants.CENTER);
		loadButton.setHorizontalTextPosition(SwingConstants.CENTER);
		loadButton.setBorder(BorderFactory.createBevelBorder(0));
		loadButton.setBackground(new Color(153, 153, 255));
		

		// Add saveGame button
		saveButton.setVisible(true);
		saveButton.addActionListener(this);
		window.getPanel().add(saveButton);
		saveButton.setFont(new Font("Vani", Font.BOLD, 14));
		saveButton.setBounds(610, 20, 120, 50);
		saveButton.setVerticalTextPosition(SwingConstants.CENTER);
		saveButton.setHorizontalTextPosition(SwingConstants.CENTER);

		// Create text fields with info on the card cash-in bonuses
		cardsData = new JTextArea(
				"Continent	Troop\n	Bonus:  \n Kjersia	     2\n Estoveria	     5\n Moa	     6\n Shilov	     3\n Tormudd	     3\n Eschilles	     7");
		window.getPanel().add(cardsData);
		cardsData.setFont(new Font("Vani", Font.BOLD, 17));
		cardsData.setBounds(800, 20, 200, 250);
		cardsData.setBorder(BorderFactory.createBevelBorder(1));
		cardsData.setBackground(new Color(135, 190, 250));
		cardsData.setEditable(false);

		// Create text fields with info on the continents bonuses
		continentData = new JTextArea(
				"Card Set	Troop\n	Bonus: \n 3-Soldiers	     3\n 3-Horses	     5\n 3-Cannons	     7\n 1-of-Each-Kind   10");
		window.getPanel().add(continentData);
		continentData.setFont(new Font("Vani", Font.BOLD, 17));
		continentData.setBounds(1050, 20, 200, 250);
		continentData.setBorder(BorderFactory.createBevelBorder(1));
		continentData.setBackground(new Color(135, 190, 250));
		continentData.setEditable(false);

		//Add phase button
		cashInButton.setVisible(false);
		cashInButton.addActionListener(this);
		window.getPanel().add(cashInButton);
		cashInButton.setFont(new Font("Vani", Font.BOLD, 25));
		cashInButton.setText("<html>Cash<br /> ~In~ <br /> Cards</html>");
		cashInButton.setBounds(620, 100, 100, 150);
		cashInButton.setBorder(BorderFactory.createBevelBorder(0));

		//Add a background area to the cards section of the bottom panel
		cardBackground = new JLabel("");
		window.getPanel().add(cardBackground);
		cardBackground.setBounds(60, 90, 670, 170);
		cardBackground.setBackground(new Color(135, 190, 250));
		cardBackground.setBorder(BorderFactory.createBevelBorder(1));
		cardBackground.setOpaque(true);

		//Add phase label that dsiplays the turn of the user and the phase they are currently in
		phaseLabel = new JLabel();
		window.getPanel().add(phaseLabel);
		phaseLabel.setFont((new Font("Impact", Font.BOLD, 20)));
		phaseLabel.setBounds(200, 10, 300, 50);
		phaseLabel2 = new JLabel();
		window.getPanel().add(phaseLabel2);
		phaseLabel2.setFont((new Font("Impact", Font.BOLD, 20)));
		phaseLabel2.setBounds(203, 13, 300, 50);
		
		attackSlide = new JSlider(0,10,5);
		window.getPanel().add(attackSlide);
		attackSlide.setBounds(740, 90, 50, 150);
		attackSlide.setOrientation(JSlider.VERTICAL);
		attackSlide.setVisible(false);
		
		attacker = new JLabel("");
		window.getPanel().add(attacker);
		attacker.setFont(new Font("impact",18,18));
		attacker.setBounds(740,40,100,50);
		attacker.setVisible(false);
		
		//Initializes the graphics
		g = bs.getDrawGraphics();

		//Shuffle the deck
		Collections.shuffle(Cards);			
		
		
	}// End of init
	

	// FOR TESTING PURPOSES ONLY, DIVIDES ALL COUNTRIES TO THE PLAYERS AT THE START OF THE GAME
	public void divyCountries() {
		while (CountryPicker.size() > 0) {
			for (int x = 0; x < 6; x++) {
				if (CountryPicker.size() > 0) {
					new threadz(CountryPicker.get(0), players.get(x).getColor(), false);
					CountryPicker.get(0).setTroops(1);
					players.get(x).addCountries(CountryPicker.get(0));
					CountryPicker.remove(0);
				}
			}
		}

	}

	//This method is run everytime a button or a card on the screen is clicked
	public void actionPerformed(ActionEvent e) {
		//If the phaseButton is clicked
		JButton btn = (JButton) e.getSource();
		if (btn.getText().equals("Next Phase")) {
			
			//Every time the phase switches, the phase must initialize - this counter ensures it only initializes once
			phaseInit = 0;
			
			//This switch statement switches the phase from the current one to the next
			switch (phase) {
			case DEPLOY:
				phase = Phase.ATTACK;
				break;
			case ATTACK:
				phase = Phase.REINFORCE;
				break;
			case REINFORCE:
				phase = Phase.DEPLOY;
				
				do {					
					turnCounter++;
					if (turnCounter >= players.size()) {
						turnCounter = 0;
					}
				}while(players.get(turnCounter).getCountries().size() == 0);
				
				break;
			default:
				break;
			}
			//Call the phaseManager method which will call whatever method the current phase is in
			//Ex: if in the attack phase it will call attack();
			try {
				phaseManager();
			} catch (IOException e1) {
				//Catch
				e1.printStackTrace();
			}
			
			//Else if the cash in button is clicked
		} else if (btn.getText().equals("<html>Cash<br /> ~In~ <br /> Cards</html>")) {
			saveButton.setVisible(false);
	
			
			int cannon = 0;
			int horse = 0;
			int soldier = 0;
			//Goes through the cards that the player has selected
			for (int x = 0; x < selectedCards.size(); x++) {
				if (selectedCards.get(x).getCardType().equals("Soldier"))
					soldier++;
				if (selectedCards.get(x).getCardType().equals("Horse"))
					horse++;
				if (selectedCards.get(x).getCardType().equals("Cannon"))
					cannon++;
			}
			//Based on the combination of cards picked, that will give players a bonus to their troop spendage
			if (soldier == 3) {
				troopSpendage += 3;
				cashIn();
			} else if (horse == 3) {
				troopSpendage += 5;
				cashIn();
			} else if (cannon == 3) {
				troopSpendage += 7;
				cashIn();
			} else if (cannon == 1 && horse == 1 && soldier == 1) {
				troopSpendage += 10;
				cashIn();
			}
		} else if (btn.getText().equals("Save Game")) {
			saveButton.setVisible(false);
			try {
				FileRhiger.saveGame();
			} catch (IOException e1) {
				// Catch
				e1.printStackTrace();
			}
		} else if (btn.getText().equals("Load Game")) {
			try {
				FileRhiger.loadGame();
			} catch (IOException e1) {
				// Catch
				e1.printStackTrace();
			}
			phaseButton.setVisible(true);
			try {
				deploy();
			} catch (IOException e1) {
				//Catch
				e1.printStackTrace();
			}
			
			saveButton.setVisible(false);
			continentData.setVisible(true);
			cardsData.setVisible(true);
			cardBackground.setVisible(true);
			phaseLabel.setVisible(true);
			phaseLabel2.setVisible(true);
			
			playerPicker.setVisible(false);
			playerNum.setVisible(false);
			startGame.setVisible(false);
			loadButton.setVisible(false);
			loadButton.setVisible(false);
			started = true;
			
		} else if (btn.getText().equals("Start Game")) {
			started = true;
			
			saveButton.setVisible(false);
			continentData.setVisible(true);
			cardsData.setVisible(true);
			cardBackground.setVisible(true);
			phaseLabel.setVisible(true);
			phaseLabel2.setVisible(true);
			
			playerPicker.setVisible(false);
			playerNum.setVisible(false);
			startGame.setVisible(false);
			loadButton.setVisible(false);
			
			numberOfPlayers = playerPicker.getValue();
			
			
			for (int i = 0; i < numberOfPlayers; i++) {
				switch (i) {
				case 0:
					Player p1 = new Player("player", Color.cyan, 0);
					players.add(p1);
					break;
				case 1:
					Player p2 = new Player("player", Color.red, 1);
					players.add(p2);
					break;
				case 2:
					Player p3 = new Player("player", Color.blue, 2);
					players.add(p3);
					break;
				case 3:
					Player p4 = new Player("player", Color.yellow, 3);
					players.add(p4);
					break;
				case 4:
					Player p5 = new Player("player", Color.green, 4);
					players.add(p5);
					break;
				case 5:
					Player p6 = new Player("player", Color.magenta, 5);
					players.add(p6);
					break;
				}
			}
			//Sets up how many troops the players can divy out at the start of the game
			for (int i = 0; i < players.size(); i++) {
				initialtroops = 1 * players.size();
			}
			
			
		}
	}
	//This method handles what happens when a user has a correct card set and has chosen to cash-in their cards
	public void cashIn() {
		//Updates the troops to deploy text
		troopsToDeploy.setText("Troops to Deploy: " + troopSpendage);
		
		//Only delete cards while there are cards to delete
		while (selectedCards.size() > 0) {
			//Loop for the amount of cards in the player's hand
			for (int x = 0; x < currentCards.size(); x++) {
				//Only loop while neither arrayList is empty - catch for when cards have been deleted
				if (selectedCards.size() > 0 && currentCards.size() > 0) {
					//if this card in the player's hand is selected, it is being cashed-in. Therefore, it must be removed from the player's hand
					if (currentCards.get(x) == selectedCards.get(0)) {
						//Remove the card from the player, remove the card from having been selected, and add the card to the used cards araryList
						currentCards.remove(x);
						usedCards.add(selectedCards.get(0));
						selectedCards.remove(0);
					}
				}
			}
		}
		//Sets the player's cards as the updated current cards
		players.get(turnCounter).setCards(currentCards);
		// Refresh the cards images and the selectedCard background images
		for (int x = 0; x < 5; x++) {
			cardPics[x].setIcon(null);
			if (cardPics[x].getMouseListeners() != null)
				cardPics[x].removeMouseListener(mch);
			cardSelectedPics[x].setVisible(false);
			cardSelectedPics[x].setBackground(players.get(turnCounter).getColor());
		}

		// Draw the player's cards
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.BOLD, 20));
		g.drawString("Player " + (turnCounter + 1) + "'s Cards", 105, 750);
		BufferedImage img = null;
		//Add an image to each card label, for as many cards as the player owns
		for (int y = 0; y < players.get(turnCounter).getCards().size(); y++) {
			cardPics[y].addMouseListener(mch);
			cardPics[y].setEnabled(true);
			try {
				img = ImageIO.read(
						//read in the card from a seperate resources file
						new File ("Ressources/" + "Cards/" + players.get(turnCounter).getCards().get(y).getCountry() + "Card.png"));
			} catch (IOException e1) {
				// Catch
				e1.printStackTrace();
			}
			cardPics[y].setIcon(new ImageIcon(recolorCard(img, y)));
		}
		//Disable and hide the cash-in button after use since it can only be used once per turn. 
		cashInButton.setVisible(false);
		//Disable the phase button since the player must spend bonus troops before moving on.
		phaseButton.setEnabled(false);
	}

	//Takes in the buffered image of a card and its index number, it will recolor the country in
	//the card to the color of the player that owns that country
	public BufferedImage recolorCard(BufferedImage img, int indexNumber) {
		Color color = null;
		//Gets the color of the player who owns that country
		for (Player p : players) {
			for (Country c : p.getCountries()) {
				if (currentCards.get(indexNumber).getDetectionColor() == c.getDetectionColor()) {
					color = p.getColor();
				}
			}
		}

		//Recolors the country in that card with the color found above
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {

				int r = img.getRGB(i, j);

				if (r == currentCards.get(indexNumber).getDetectionColor().getRGB()) {
					img.setRGB(i, j, color.getRGB());
				}

			}
		}
		//Returns the new Recolored image
		return img;
	}

	// Updates the turns and other important game information
	public void update() throws IOException {
		//Gets the x and y of the mouse relative to the frame
		int y = mhh.mouseY;
		int x = mhh.mouseX;

		//Makes sure the x and y doesn't go out of bounds
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}

		
		
		
		//Creates the hoveredColor based on the pixel rgb that coresponds to the x and y of the screen to the 2D array of the invisMap
		if ((y < imgH && x < imgW)) {
			hoveredColor = new Color(invisData[x][y]);
		}
		
		if (started == false) {
			
			saveButton.setVisible(false);
			
			continentData.setVisible(false);
			cardsData.setVisible(false);
			cardBackground.setVisible(false);
			phaseLabel.setVisible(false);
			phaseLabel2.setVisible(false);
			
			playerNum.setText("SELECT THE NUMBER OF PLAYER: " + playerPicker.getValue());
			
		}
		//If there has been no countries picked then it needs a player to select a country
		else if (CountryPicker.size() > 0 && mch.clicked) {
			pickCountry();
			
			//Once all countries have been selected it will allow the player to spread the initial troops
		} else if (initialtroops > 0 && mch.clicked) {
			initialTroopSpread();
			
			//Once both of the above have been satisfied, it can start calling the phases (deploy, attack, etc.)
		} else if (mch.clicked) {
			phaseManager();
		}

		// Set phase label based on what phase of the game it is in, as well as how many countries that player has yet to deploy
		if(players.size() > 0) {
			if (CountryPicker.size() > 0) {
				phaseLabel.setText("Player " + (turnCounter + 1) + " - Select Country");
				phaseLabel.setForeground(players.get(turnCounter).getColor());
				phaseLabel2.setText("Player " + (turnCounter + 1) + " - Select Country");
			} else if (initialtroops > 0) {
				phaseLabel.setText("Player " + (turnCounter + 1) + " - Place Initial Troops");
				phaseLabel.setForeground(players.get(turnCounter).getColor());
				phaseLabel2.setText("Player " + (turnCounter + 1) + " - Place Initial Troops");
				troopsToDeploy.setText((initialtroops - 1) / players.size() + 1 + " Troops Remaining");
			} else {
				phaseLabel.setText("Player " + (turnCounter + 1) + " - " + phase);
				phaseLabel.setForeground(players.get(turnCounter).getColor());
				phaseLabel2.setText("Player " + (turnCounter + 1) + " - " + phase);
			}
		}
		
		if (players.size() > 0) {
			for (Player p : players) {
				if (p.getCountries().size() == 42) {
					
					endGame(p.getPlayerNum());
					
				}
			}
			
			
		}
		//render the game base on chanegs made in the update class
		render();
		
	}// End of update

	public void endGame(int playerNum) throws IOException {
		for (Player p : players) {
			for (Country c : p.getCountries()) {
				new threadz(c,Color.white,false);
			}
				
		}
		
		gameOver = true;
		
		
//		JOptionPane.showMessageDialog(window.getFrame(), "Player " + (playerNum+1) + " Wins!");
//		
//		System.exit(0);

		//g.setColor(players.get(playerNum).getColor());
		
		
	}
	
	public void phaseManager() throws IOException {
		//Based on what phase the game is in, it will call the appropriate method
		
		
		switch (phase) {
		case DEPLOY:
			deploy();
			break;
		case ATTACK:
			attack();
			break;
		case REINFORCE:
			reinforce();
			break;
		default:
			break;
		}
	}

	//Determines how many troops a player gets to deploy
	public void calcTroops() {
		//Based on how many troops the player owns this will add to their spendage with a minimum of 2
		troopSpendage = (players.get(turnCounter).getCountries().size()) / 3;
		if (troopSpendage < 3) {
			troopSpendage = 3;
		}
		//If a player owns an entire continent, they will get a continent bonus to their spendage
		//these counters record how much of a continent the player owns
		int Kjersia = 0;
		int Estoveria = 0;
		int Moa = 0;
		int Shilov = 0;
		int Tormudd = 0;
		int Eschilles = 0;
		
		//increment the corresponding continent counter based on the continent value stored in the country objects the player owns
		for (Country c:players.get(turnCounter).getCountries()) {
			switch (c.getContinent()) {
			case "Kjersia": Kjersia++;
				break;
			case "Estoveria": Estoveria++;
				break;
			case "Moa": Moa++;
				break;
			case "Shilov": Shilov ++;
				break;
			case "Tormudd": Tormudd++;
				break;
			case "Eschilles": Eschilles++;
				break;
			}
		}
		//If the player owns all countries in a continent (each continent has different numbers of countries), give them a corresponding troop bonus as decided by the rules of the game
		if (Kjersia == 4) {
			troopSpendage += 2;
		}
		if (Estoveria == 8) {
			troopSpendage += 5;
		}
		if (Moa == 9) {
			troopSpendage += 6;
		}
		if (Shilov == 4) {
			troopSpendage += 3;
		}
		if (Tormudd == 5) {
			troopSpendage += 3;
		}
		if (Eschilles == 12) {
			troopSpendage += 7;
		}

		//If a player owns the card and owns the coresponding country on that card they get one extra troop
		for(Card C: players.get(turnCounter).getCards()) 
			for(Country co: players.get(turnCounter).getCountries())
				if (C.getCountry().equals(co.getName()))
					troopSpendage++;		
		
		//Updates the display of how many troops the player has to deploy
		troopsToDeploy.setText("Troops to Deploy: " + troopSpendage);
	}
	//At the beginnning of the game, the players must divide troops amongst countries they own
	public void initialTroopSpread() throws IOException {
		
		mch.clicked = false;
		
		//Get the name of the country they are clicking
		String s = "";
		for (Country c : players.get(turnCounter).getCountries()) {
			if (hoveredColor.equals(c.getDetectionColor())) {
				s = c.getName();
			}
			//If the player own that country, increment that country's troops, and deincrement the total amount of remaining troops
			if (hoveredColor.equals(c.getDetectionColor()) && players.get(turnCounter).checkOwn(s)) {
				c.setTroops(c.getTroops() + 1);
				initialtroops--;

				//incrememnt the turn counter to the next player
				
				do {					
					turnCounter++;
					if (turnCounter >= players.size()) {
						turnCounter = 0;
					}
				}while(players.get(turnCounter).getCountries().size() == 0);
				
			}

		}
		//if there are no remaining troops left to deploy, start the game by running the first deploy
		if (initialtroops == 0) {
			try {
				render();
			} catch (IOException e) {
				// Catch
				e.printStackTrace();
			}
			turnCounter = 0;
			phaseButton.setVisible(true);
			deploy();
		}

	}
	//In this phase, the player can deploy their troops and cash-in cards
	public void deploy() throws IOException {
		//This runs once at the beginning of the deploy method - afterwards, the phaseInit is incremented
		if (phaseInit == 0) {
			saveButton.setVisible(true);
			
			//First, draw the images of all players' countries to ensure everything is correctly drawn
			for (Player player : players) {
				for (Country ca : player.getCountries()) {
					if (ca.isHighlighted()) {
						new threadz(ca, player.getColor(), false);
					}
				}
			}
			//calculate how many troops this player has yet to deploy
			troopsToDeploy.setVisible(true);
			calcTroops();
			phaseButton.setVisible(true);
			phaseButton.setEnabled(false);

			// Set up the cash-in button
			cashInButton.setVisible(true);
			cashInButton.setBackground(players.get(turnCounter).getColor());
			selectedCards = new ArrayList<Card>();
			currentCards = players.get(turnCounter).getCards();
			//Force the player to cash in cards if they have 5 by disabling the phase button
			if (currentCards.size() == 5) {
				phaseButton.setEnabled(false);
			}

			// Refresh the cards by removing all their icons
			for (int x = 0; x < 5; x++) {
				cardPics[x].setIcon(null);
				if (cardPics[x].getMouseListeners() != null)
					cardPics[x].removeMouseListener(mch);
				cardSelectedPics[x].setVisible(false);
				cardSelectedPics[x].setBackground(players.get(turnCounter).getColor());
			}

			// Draw the player's cards by matching their JLabel icons to the cards that the player owns
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.BOLD, 20));
			g.drawString("Player " + (turnCounter + 1) + "'s Cards", 105, 750);
			BufferedImage img = null;
			for (int y = 0; y < players.get(turnCounter).getCards().size(); y++) {
				cardPics[y].addMouseListener(mch);
				cardPics[y].setEnabled(true);
				try {
					img = ImageIO.read(
							//Get the image from a separate file
							new File ("Ressources/" +"Cards/" + players.get(turnCounter).getCards().get(y).getCountry() + "Card.png"));
				} catch (IOException e1) {
					// Catch
					e1.printStackTrace();
				}
				cardPics[y].setIcon(new ImageIcon(recolorCard(img, y)));
			}
		}
		mch.clicked = false;
		//If the player still has troops to deploy
		if (troopSpendage > 0) {
			String s = "";
			//Get the name of the country they clicked
			for (Country c : players.get(turnCounter).getCountries()) {
				if (hoveredColor.equals(c.getDetectionColor())) {
					s = c.getName();
				}
				//Ensure this country is one the current player owns
				if (hoveredColor.equals(c.getDetectionColor()) && players.get(turnCounter).checkOwn(s)) {
					//Increment the countries' troops for each click
					c.setTroops(c.getTroops() + 1);
					//update how many troops the player has yet to deploy to the user
					troopSpendage--;
					saveButton.setVisible(false);
					troopsToDeploy.setText("Troops to Deploy: " + troopSpendage);
				}

			}
			//Once all troops are deployed, and player has less than 5 cards, enable the next phase button to move on
			if (troopSpendage == 0 && players.get(turnCounter).getCards().size() < 5)
				phaseButton.setEnabled(true);
		}
		//increment phaseInit so deploy only initializes once
		phaseInit++;
	}
	//In this phase, the player can select countries to attack
	public void attack() {
		mch.clicked = false;
		
		//Makes the troopsToDeploy and cashinButton not visible
		//This code will only be called once per attack()
		if (phaseInit == 0) {
			troopsToDeploy.setVisible(false);
			cashInButton.setVisible(false);
			//Removes the mouse listeners from the cards and makes them invisible
			for (int x = 0; x < 5; x++) {
				if (cardPics[x].getMouseListeners() != null)
					cardPics[x].removeMouseListener(mch);
				cardSelectedPics[x].setVisible(false);
			}
		}
		phaseInit++;

		//This code will determine when a battle will happen
		String s = "";
		for (Player p : Board.players) {
			//get the name of the country that has been clicked
			for (Country c : p.getCountries()) {
				if (hoveredColor.equals(c.getDetectionColor())) {
					s = c.getName();
				}
				//Ensure this country is one the current player owns and the country has more than one troop
				if (hoveredColor.equals(c.getDetectionColor()) && players.get(turnCounter).checkOwn(s)
						&& c.getTroops() > 1) {
					//Dehighlight any countries that were previously selected
					for (Player player : players) {
						for (Country ca : player.getCountries()) {
							if (ca.isHighlighted()) {
								new threadz(ca, player.getColor(), false);
							}
						}
					}
					//Highlight the new selected country, and its neighboring countries
					new threadz(c, players.get(turnCounter).getColor().brighter().brighter().brighter(), true);
					c.HighlightNeighbours();
					//the attacking country is the first clicked country
					attackingCountry = c;
					tempAtt = attackingCountry.getTroops();
					
					attackSlide.setVisible(true);
					attackSlide.setMaximum(attackingCountry.getTroops()-1);
					attackSlide.setMinimum(1);
					attackSlide.setValue(attackSlide.getMaximum());
					attacker.setVisible(true);
					
					
					
				} else if (hoveredColor.equals(c.getDetectionColor()) && c.isHighlighted() && c.getName().equals(attackingCountry.getName()) == false && checkO(c) == false) {
					//The defending country is the second clicked country, and it must be highlighted
					defendingCountry = c;
					battle = true;
					hasAttacked = true;
					attackSlide.setVisible(false);
					attacker.setVisible(false);
				}

			}

		}

		//If a battle has been determined to happen
		if (battle == true) {
			
			//Makes sure the attacking country has more than one troop and that it is attacking a neighbour
			if (attackingCountry.getTroops() > 1 && defendingCountry.isHighlighted()) {
				//Makes a new battle between the attacker and defender then updates their troops after the battle 
				Battle battle1 = new Battle(attackSlide.getValue(), defendingCountry.getTroops());
				battle1.BattleTroops();
				attackingCountry.setTroops(Battle.getAttackers());
				defendingCountry.setTroops(Battle.getDefenders());
			}

			//Determines the array index of the defending country in the players array
			int defNum = 0;
			for (Player po : players) {
				for (Country co : po.getCountries()) {
					if (defendingCountry.getName().equals(co.getName())) {
						defNum = po.getPlayerNum();
					}
				}
			}

			//These two ifs ensure that the troops never go into the negative
			if (attackingCountry.getTroops() < 0) {
				attackingCountry.setTroops(0);
			}
			if (defendingCountry.getTroops() < 0) {
				defendingCountry.setTroops(0);
			}

			
			// If Attackers lose
			if (attackingCountry.getTroops() == 0) {
				//Unhighlight its neighbors
				attackingCountry.unHighlightNeighbours();
				
				attackingCountry.setTroops(tempAtt - attackSlide.getValue());
				//Updates the defenders based on the defenders left
				defendingCountry.setTroops(defendingCountry.getTroops());

				
				//Ensures the troops never go negative or less than zero
				if (defendingCountry.getTroops() <= 0) {
					defendingCountry.setTroops(1);
				}
			}

			//If defenders lose
			if (defendingCountry.getTroops() == 0) {
				//Unhighlights the defending country
				for (Player p: players) {
					for (Country c: p.getCountries()) {
						if (c.getName().equals(defendingCountry.getName())){
							c.setHighlighted(false);
						}
					}
				}
				//Recolor the defenders country to the attackers color
				new threadz(defendingCountry, players.get(turnCounter).getColor(), false);
				attackingCountry.unHighlightNeighbours();				
				
				//Updates the troop numbers
			
				defendingCountry.setTroops(attackingCountry.getTroops());
				attackingCountry.setTroops(tempAtt - attackSlide.getValue());
				if (attackingCountry.getTroops() <= 0) {
					attackingCountry.setTroops(1);
				}

				//Removes the defender from the defenders country arraylist
				players.get(defNum).getCountries().remove(defendingCountry);
				//Adds the defender country to the attackers country arrayList.
				players.get(turnCounter).getCountries().add(defendingCountry);
				hasWon = true;
				
				
				if (players.get(defNum).getCountries().size() == 0) {
					File fanfare = new File("Ressources/" + "fanfare2.wav");
					playSound(fanfare);
					
					
					
					JOptionPane.showMessageDialog(window.getFrame(), "Player " + (defNum+1) + " has been Eliminated!");
					
					
					
					
					
					while(players.get(defNum).getCards().size() > 0) {
						usedCards.add(players.get(defNum).getCards().get(0));
						players.get(defNum).getCards().remove(0);
					}
				}
				
			}

		} // End of i

		battle = false;
		
	}
	public void playSound(File sound) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
			
			Thread.sleep(clip.getMicrosecondLength()/1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkO(Country c) {
		//This method makes sure that a player does not attack itself, runs through
		//the attackers countries and if it sees itself it will return true and be unable to attack
		for (Country ca : players.get(turnCounter).getCountries()) {
			if (c.equals(ca)) {
				return true;
			} 	
		}
		
		return false;
		
	}

	// this phase gives the player a card if they attacked and won at least one
	// battle in the previous phase
	public void reinforce() {

		mch.clicked = false;
		
		if (phaseInit == 0) {
			
			
			attackSlide.setVisible(false);
			attacker.setVisible(false);
			reinforced = false;
			// If run out of cards, fill the cards array with the usedCards
			if (Cards.size() == 0) {
				Cards = usedCards;
				usedCards = new ArrayList<Card>();
				Collections.shuffle(Cards);
			}
			// Dehighlight all countries that were previously highlighted
			
			for (Player pa : players) {
				for (Country ca : pa.getCountries()){
					if (ca.isHighlighted()) {
						new threadz(ca, pa.getColor(), false);
					}
				}
			}
			

			// If a player has attacked that turn and won at least one battle it will win a
			// card
			if (hasAttacked == true && hasWon == true) {
				// Give that player a card.
				players.get(turnCounter).addCard(Cards.get(0));
				Cards.remove(0);
				hasAttacked = false;
				hasWon = false;

				// Refresh the cards
				for (int x = 0; x < 5; x++) {
					cardPics[x].setIcon(null);
					if (cardPics[x].getMouseListeners() != null)
						cardPics[x].removeMouseListener(mch);
					cardSelectedPics[x].setVisible(false);
					cardSelectedPics[x].setBackground(players.get(turnCounter).getColor());
				}

				// Draw the player's cards
				g.setColor(Color.WHITE);
				g.setFont(new Font("Impact", Font.BOLD, 20));
				g.drawString("Player " + (turnCounter + 1) + "'s Cards", 105, 750);
				BufferedImage img = null;
				for (int y = 0; y < players.get(turnCounter).getCards().size(); y++) {
					cardPics[y].addMouseListener(mch);
					cardPics[y].setEnabled(true);
					try {
						img = ImageIO.read(
								// Read in the image from a separate file
								new File ("Ressources/" +"Cards/" + players.get(turnCounter).getCards().get(y).getCountry()
										+ "Card.png"));
					} catch (IOException e1) {
						// Catch
						e1.printStackTrace();
					}
					cardPics[y].setIcon(new ImageIcon(recolorCard(img, y)));
				}
			}
		}

		String s = "";
		for (Player p : Board.players) {
			// get the name of the country that has been clicked
			for (Country c : p.getCountries()) {
				if (hoveredColor.equals(c.getDetectionColor())) {
					s = c.getName();
				}
				// Ensure this country is one the current player owns and the country has more
				// than one troop
				if (hoveredColor.equals(c.getDetectionColor()) && players.get(turnCounter).checkOwn(s)&& c.getTroops() > 1 && c.isHighlighted() == false && reinforced == false) {
					// Dehighlight any countries that were previously selected
					for (Player player : players) {
						for (Country ca : player.getCountries()) {
							if (ca.isHighlighted()) {
								new threadz(ca, player.getColor(), false);
							}
						}
					}
					// Highlight the new selected country, and its neighboring countries
					new threadz(c, players.get(turnCounter).getColor().brighter().brighter().brighter(), true);
					c.highlightOwned();
					attackingCountry = c;
					reinforced = true;
				} else if (hoveredColor.equals(c.getDetectionColor()) && c.isHighlighted()
						&& c.getName().equals(attackingCountry.getName()) 
						== false && checkO(c) == true
						&& attackingCountry.getTroops() > 1) {

	
					defendingCountry = c;

					attackingCountry.setTroops(attackingCountry.getTroops() - 1);
					defendingCountry.setTroops(defendingCountry.getTroops() + 1);

					// new threadz(attackingCountry,
					// players.get(turnCounter).getColor().brighter().brighter().brighter(), false);

					if (attackingCountry.getTroops() == 1) {
						attackingCountry.unHighlightNeighbours();

					}

				}

			}

		}

		phaseInit++;
	}

	//This method runs while the initial countries have yet to be claimed
	private void pickCountry() {
		mch.clicked = false;
		loadButton.setVisible(false);
		
		//creates a temporary country to be moved around
		Country tempC = null;

		//ensure the country that was clicked has yet to be claimed
		for (int j = 0; j < CountryPicker.size(); j++) {
			//Add this country to the player's arrayList
			if (hoveredColor.equals(CountryPicker.get(j).getDetectionColor())) {
				CountryPicker.get(j).setClicked(true);
				tempC = CountryPicker.get(j);
				//colors the chosen country to the selecting player's color
				for (int i = 0; i < CountryPicker.size(); i++) {
					if (CountryPicker.get(i).isClicked() == true) {
						 threadz.recolorCountry(CountryPicker.get(i), players.get(turnCounter).getColor(), false );
						CountryPicker.remove(i);
						tempC.setTroops(1);
						players.get(turnCounter).addCountries(tempC);
					}
				}
				// Move on to the next player's turn

				turnCounter++;
				if (turnCounter >= players.size()) {
					turnCounter = 0;
				}
		
			}
		}
		//if there are no countriies left, reset the turn so player one moves first
		if (CountryPicker.size() == 0) {
			turnCounter = 0;
		}

	}


	// Places all graphical elements on the window of the game screen.
	public void render() throws IOException {
		BufferedImage bi = null;
		// Wipes the screen, always above the objects you're animating
		Color bgC = new Color(135, 206, 235);
		g.setColor(bgC);
		g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);

		if (players.size() > 0) {
			// Allows hovering in the pick country part of the game
			for (Country x : Countries) {
				if (hoveredColor.equals(x.getDetectionColor())) {
					new threadz(x, players.get(turnCounter).getColor(), false);
					bi = x.getImg();
				}
			}
		}
		//Draws the waterMap and hovered country
		g.drawImage(waterMap, 0, 0, null);
		g.drawImage(bi, 0, 0, null);

		//Draws the countries
		for (Player x : players) {
			for (Country k : x.getCountries()) {
				if (x.getCountries().size() > 0)
					g.drawImage(k.getImg(), 0, 0, null);
			}
		}

		//Draws the labels
		g.drawImage(labels, 0, 0, null);
	
		//Draws how many troops are on each country
		drawTroopNumbers();
		if (gameOver == true) {
			g.setFont(new Font("Vani",Font.BOLD, 150));
			g.setColor(Color.BLACK);
			g.drawString("Player " + (turnCounter+1) + " Wins!", 160, 430);
			
			
			g.setColor(players.get(turnCounter).getColor());
			g.drawString("Player " + (turnCounter+1) + " Wins!", 150, 420);
			
		}
		

		attacker.setText("ATT: "+attackSlide.getValue());
		
		bs.show();

	}// End of render
	
	public void drawTroopNumbers() {
		g.setFont(new Font("Vani", Font.BOLD, 14));
		g.setColor(Color.black);

		//Draws the troop numbers of all the countries ( the formatting changes based on if its a 2 digit number or not )
		for (Player x : players) {
			for (Country y : x.getCountries()) {
				g.drawString(y.getTroops() > 10 ? "" + y.getTroops() : " " + y.getTroops(), y.getxPos(), y.getyPos());
			}
		}
	}

}// End of Board




