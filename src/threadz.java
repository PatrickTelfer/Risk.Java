import java.awt.Color;

public class threadz implements Runnable {

	//Declare objects and variables
	private Thread t = new Thread(this);
	private Country country;
	private Color color;
	private boolean highlighting;
	private static int counter = 0;
	
	//Class constructor
	public threadz(Country country, Color color, boolean highlighting) {
		this.country = country;
		this.color = color;
		this.highlighting = highlighting;
		counter++;
		//Start the thread
		start();
	}
	//starts the thread
	public void start() {
		t.start();
	}
	
	public void run() {
		//Recolor a country
		recolorCountry(country, color, highlighting);
		//System.out.println("recoloring: " + country.getName());
		counter--;
	}
	
	public static void recolorCountry(Country country, Color color, boolean highlighting) {
		//If this country is only being highlighted, indicate this
		if (highlighting == true)
			country.setHighlighted(true);
		
		if (highlighting == false) {
			country.setHighlighted(false);
		}

		//For each pixel in the country's buffered image, replace the pixel color with the new pixel color
		for (int i = 0; i < country.getImg().getWidth(); i++) {
			for (int j = 0; j < country.getImg().getHeight(); j++) {

				int r = country.getImg().getRGB(i, j);
				int p = Color.black.getRGB();
				
				//Ignore the pixels where the image is black or invisible
				if (r != 16777215 && r < 0 && r != p) {
					country.getImg().setRGB(i, j, color.getRGB());
				}

			}
		}
	}
	public static int getCounter() {
		return counter;
	}

}