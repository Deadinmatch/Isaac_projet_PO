package gameobjects;

import libraries.Vector2;

public class Bomb extends Consommable {

	private int degats = 2;
	public Bomb(Vector2 position, Vector2 size, String imagePath) {
		super(position, size, imagePath);
	}
	public Bomb(Vector2 position, Vector2 size, String imagePath, String cle) {
		super(position, size, imagePath, cle);
	}
	
	public int getDegats() {
		return degats;
	}
	public void setDegats(int degats) {
		this.degats = degats;
	}
	
	

}
