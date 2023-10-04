package gameobjects;

import libraries.StdDraw;
import libraries.Vector2;


public class Projectile {
	
	private int degats;
	private Vector2 portee;
	private double BulletSpeed;
	private String imagePath;
	private Vector2 direction;
	private Vector2 position;
	private Vector2 size;
	private Vector2 positionPortee;

	private boolean tirer;
	private boolean disponible = true;
	
	private boolean droite;
	private boolean gauche;
	private boolean haut;
	private boolean bas;
	
	public Projectile(int degats, Vector2 size, Vector2 portee, double BulletSpeed, String imagePath) 
	{
		this.degats = degats;
		this.size = size;
		this.portee = portee;
		this.BulletSpeed = BulletSpeed;
		this.imagePath = imagePath;
		this.direction = new Vector2();
		this.position = new Vector2();
	}
		
	/**
	 * Deplacement projectile
	 */
	
	private void isLaunched()
	{
		if (this.tirer)
		{
			this.disponible = false;
		}
	}
	

	private void move()
	{
		Vector2 normalizedDirection = getNormalizedDirection();
		Vector2 positionAfterMoving = getPosition().addVector(normalizedDirection);
		setPosition(positionAfterMoving);
		direction = new Vector2();
	}
	
	public void updateGameObject()
	{
		move();
		isLaunched();
	}
	
	/*
	 * *************************************************************************************
	 * DRAW
	 * *************************************************************************************
	 */
	
	public void drawGameObject()
	{
		StdDraw.picture(getPosition().getX(), getPosition().getY(), getImagePath(), getSize().getX(), getSize().getY());
	}
	
	
	
	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	
	public Vector2 getNormalizedDirection()
	{
		Vector2 normalizedVector = new Vector2(direction);
		normalizedVector.euclidianNormalize(BulletSpeed);
		return normalizedVector;
	}	
	
	/*
	 * *************************************************************************************
	 * DIRECTION PROJECTILE
	 * *************************************************************************************
	 */
	
	public void shootUpNext()
	{
		getDirection().addY(1);
	}
	public void shootDownNext()
	{
		getDirection().addY(-1);
	}
	public void shootLeftNext()
	{
		getDirection().addX(-1);
	}
	public void shootRightNext()
	{
		getDirection().addX(1);
	}
	
	/*
	 * *************************************************************************************
	 * CALCULE DE LA PORTEE
	 * *************************************************************************************
	 */
	
	/**
	 * Calcule la portee pour le haut et le droite
	 * @param positionCourante
	 * @param drapeau
	 * @return
	 */
	public boolean portee(Vector2 positionCourante, Vector2 drapeau)
	{
		return ((positionCourante.getX() <= drapeau.getX()) && positionCourante.getY() <= drapeau.getY());
	}
	
	/**
	 * Calcule la portee pour le bas et la gauche
	 * @param positionCourante
	 * @param drapeauAnass
	 * @return
	 */
	public boolean portee2(Vector2 positionCourante, Vector2 drapeauAnass)
	{
		return ((positionCourante.getX() >= drapeauAnass.getX()) && positionCourante.getY() >= drapeauAnass.getY());
	}
	
	
	/*
	 * *************************************************************************************
	 * Getters and Setters
	 * *************************************************************************************
	 */
	
	public int getDegats() {
		return degats;
	}

	public void setDegats(int degats) {
		this.degats = degats;
	}

	public Vector2 getPortee() {
		return portee;
	}

	public void setPortee(Vector2 portee) {
		this.portee = portee;
	}

	public double getBulletSpeed() {
		return BulletSpeed;
	}

	public void setBulletSpeed(double bulletSpeed) {
		BulletSpeed = bulletSpeed;
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public Vector2 getPositionPortee() {
		return positionPortee;
	}

	public void setPositionPortee(Vector2 positionPortee) {
		this.positionPortee = positionPortee;
	}

	public boolean isTirer() {
		return tirer;
	}

	public void setTirer(boolean tirer) {
		this.tirer = tirer;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public boolean isDroite() {
		return droite;
	}

	public void setDroite(boolean droite) {
		this.droite = droite;
	}

	public boolean isGauche() {
		return gauche;
	}

	public void setGauche(boolean gauche) {
		this.gauche = gauche;
	}

	public boolean isHaut() {
		return haut;
	}

	public void setHaut(boolean haut) {
		this.haut = haut;
	}

	public boolean isBas() {
		return bas;
	}

	public void setBas(boolean bas) {
		this.bas = bas;
	}
	
	
	
	
	
	

}
