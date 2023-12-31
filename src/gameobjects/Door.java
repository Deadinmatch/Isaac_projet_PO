package gameobjects;

import libraries.Vector2;

public class Door {
	
	private Vector2 position;
	private Vector2 size;
	private String imagePath;
	
	public Door(Vector2 position, Vector2 size, String imagePath) {
		this.position = position;
		this.size = size;
		this.imagePath = imagePath;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	public Vector2 getSize() {
		return size;
	}
	public void setSize(Vector2 size) {
		this.size = size;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	

}
