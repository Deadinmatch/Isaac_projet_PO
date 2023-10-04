package gameobjects;

import libraries.Vector2;

public class Monster 
{
	private Vector2 position;
	private Vector2 size;
	private String imagePath;
	private double speed;
	private Vector2 direction;
	private int life;
	private int degatsCC;

	private boolean alive = true;
	
	private double y;
	private double x;
	
	public Monster(Vector2 position, Vector2 size, String imagePath, int life, double speed, int degatsCC)
	{
		this.position = position;
		this.size = size;
		this.imagePath = imagePath;
		this.life = life;
		this.speed = speed;
		this.degatsCC = degatsCC;
		this.direction = new Vector2();
	}
	
	/*
	 * *************************************************************************************
	 * DEPLACEMENT VERS LE HERO
	 * *************************************************************************************
	 */
	
	protected boolean heroEnHaut(Vector2 position)
	{
		this.y = position.getY() - this.position.getY();
		if (this.y > 0)
		{
			return true;
		}
		return false;
	}
	protected boolean heroEnBas(Vector2 position)
	{
		this.y = position.getY() - this.position.getY();
		if (this.y < 0)
		{
			return true;
		}
		return false;
	}
	protected boolean heroAGauche(Vector2 position)
	{
		this.x = position.getX() - this.position.getX();
		if (this.x < 0)
		{
			return true;
		}
		return false;
	}
	protected boolean heroADroite(Vector2 position)
	{
		this.x = position.getX() - this.position.getX();
		if (this.x > 0)
		{
			return true;
		}
		return false;
	}
	
	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	
	public boolean enVie()
	{
		if (this.life <= 0)
		{
			this.alive = false;
			this.setPosition(new Vector2(-5.0,-5.0));
		}
		return this.alive;
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
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public Vector2 getDirection() {
		return direction;
	}
	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getDegatsCC() {
		return degatsCC;
	}
	public void setDegatsCC(int degatsCC) {
		this.degatsCC = degatsCC;
	}

	public double gety() {
		return y;
	}
	public void sety(double y) {
		this.y = y;
	}
	public double getx() {
		return x;
	}
	public void setx(double x) {
		this.x = x;
	}
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	

}
