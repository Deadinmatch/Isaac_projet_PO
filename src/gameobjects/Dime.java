package gameobjects;

import libraries.Vector2;

public class Dime extends Consommable {

	public Dime(Vector2 position, Vector2 size, String imagePath) {
		super(position, size, imagePath);
	}
	
	public Dime(Vector2 position, Vector2 size, String imagePath, String cle) {
		super(position, size, imagePath, cle);
	}
	

}
