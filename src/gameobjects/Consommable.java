package gameobjects;

import libraries.Vector2;

public class Consommable {
	private Vector2 position;
    private Vector2 size;
    private String imagePath;
    private int price;
    private String cle;

    public Consommable(Vector2 position, Vector2 size, String imagePath) {
        this.position = position;
        this.size = size;
        this.imagePath = imagePath;
    }
    
    public Consommable(Vector2 position, Vector2 size, String imagePath, String cle) {
        this.position = position;
        this.size = size;
        this.imagePath = imagePath;
        this.cle = cle;
    }

    /*
     * *************************************************************************************
     * GETTERS AND SETTERS
     * *************************************************************************************
     */
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCle() {
		return cle;
	}
	public void setCle(String cle) {
		this.cle = cle;
	}  
	
}
