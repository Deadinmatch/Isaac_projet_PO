package gameobjects;

import java.util.ArrayList;
import java.util.List;

import libraries.StdDraw;
import libraries.Vector2;
import resources.ImagePaths;
import resources.RoomInfos;


public class Hero
{
	private Vector2 position;
	private Vector2 size;
	private String imagePath;
	private double speed;
	private Vector2 direction;
	private int life;
	private int degats;
	private int coin = 10;
	private int nbBomb = 2;
	private int bouclier;
	
	private boolean AffBloodOfTheMartyr;
	
	private boolean listeLarmeRempli;
	
	private List<Projectile> larme;

	public Hero(Vector2 position, Vector2 size, double speed, String imagePath, int life, int degats)
	{
		this.position = position;
		this.size = size;
		this.speed = speed;
		this.imagePath = imagePath;
		this.direction = new Vector2();
		this.life = life;
		this.degats = degats;
	}

	public void updateGameObject()
	{
		move();
		if (!listeLarmeRempli)							//remplir les larmes une fois en debut de partie
		{
			remplirLarme();
		}
		updateListeLarme();	
		rechargement();
	}
	

	private void move()
	{
		Vector2 normalizedDirection = getNormalizedDirection();
		Vector2 positionAfterMoving = getPosition().addVector(normalizedDirection);
		setPosition(positionAfterMoving);
		direction = new Vector2();
	}

	/*
	 * *************************************************************************************
	 * DRAW
	 * *************************************************************************************
	 */
	
	public void drawGameObject()
	{
		drawLife();
		if (this.life < 7)
		{
			drawBouclier();
		}
		drawCoin();
		drawBomb();
		drawSpeed();
		if (AffBloodOfTheMartyr)
		{
			drawBloodOfTheMartyr();
		}
		//isaac
		if (this.life > 2)
		{
			StdDraw.picture(getPosition().getX(), getPosition().getY(), getImagePath(), getSize().getX(), getSize().getY());
		}
		else
		{
			StdDraw.picture(getPosition().getX(), getPosition().getY(), ImagePaths.GAPER, getSize().getX(), getSize().getY());
		}
		drawListeProjectile();
		
	}
		
	/**
	 * Permet d'afficher les coeurs du personnage en fonction de sa vie
	 */
	public void drawLife()
	{
		if (this.life == 6)
		{
			StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.13, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.16, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.life == 5)
		{
			StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.13, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.16, 0.9, ImagePaths.HALF_HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.life == 4)
		{
			StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.13, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.16, 0.9, ImagePaths.EMPTY_HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.life == 3)
		{
			StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.13, 0.9, ImagePaths.HALF_HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.16, 0.9, ImagePaths.EMPTY_HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.life == 2)
		{
			StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.13, 0.9, ImagePaths.EMPTY_HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.16, 0.9, ImagePaths.EMPTY_HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.life == 1)
		{
			StdDraw.picture(0.1, 0.9, ImagePaths.HALF_HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.13, 0.9, ImagePaths.EMPTY_HEART_HUD, 0.04, 0.04, 0);
			StdDraw.picture(0.16, 0.9, ImagePaths.EMPTY_HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.life > 10)
		{
			StdDraw.text(0.1, 0.9, "INVINCIBLE");
		}
	}
	
	public void drawBouclier()
	{
		if (this.bouclier == 2)
		{
			StdDraw.picture(0.19, 0.9, ImagePaths.BLACK_HEART_HUD, 0.04, 0.04, 0);
		}
		else if (this.bouclier == 1)
		{
			StdDraw.picture(0.19, 0.9, ImagePaths.BLACK_HEART_HALF_HUD, 0.04, 0.04, 0);
		}
	}
	
	public void drawCoin()
	{
		StdDraw.picture(0.1, 0.8, ImagePaths.COIN);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(0.05, 0.8, Integer.toString(this.coin));
	}
	
	public void drawBloodOfTheMartyr()
	{
		StdDraw.picture(0.095, 0.7, ImagePaths.BLOOD_OF_THE_MARTYR);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(0.05, 0.7, Integer.toString(this.degats));
	}
	
	public void drawBomb()
	{
		StdDraw.picture(0.1, 0.75, ImagePaths.BOMB, 0.04, 0.04);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(0.05, 0.75, Integer.toString(this.nbBomb));
	}
	
	public void drawSpeed()
	{
		double arrondiVitesse = Math.round(this.speed*1000.0)/1000.0;
		StdDraw.picture(0.09, 0.65, ImagePaths.SPEED_INFO, 0.03, 0.03);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(0.05, 0.65, Double.toString(arrondiVitesse));
	}
	
	public void drawListeProjectile()
	{
		if (getLarme().get(0).isTirer())
		{
			getLarme().get(0).drawGameObject();
		}
		if (getLarme().get(1).isTirer())
		{
			getLarme().get(1).drawGameObject();
		}
		if (getLarme().get(2).isTirer())
		{
			getLarme().get(2).drawGameObject();
		}
		if (getLarme().get(3).isTirer())
		{
			getLarme().get(3).drawGameObject();
		}
		if (getLarme().get(4).isTirer())
		{
			getLarme().get(4).drawGameObject();
		}
	}
	
	/*
	 * *************************************************************************************
	 * LARME
	 * *************************************************************************************
	 */
	
	/**
	 * Remplir la liste de projectile en debut de partie
	 */
	public void remplirLarme()
	{
		larme = new ArrayList<Projectile>();
		for (int i=0 ; i<5 ; i++)
		{
			larme.add(i, new Projectile(1, RoomInfos.TILE_SIZE.scalarMultiplication(0.3), new Vector2(0.5,0.5), 0.0175, ImagePaths.TEAR));
			larme.get(i).setDegats(this.degats);
		}
		this.listeLarmeRempli = true;
	}
	
	/**
	 * Renvoie index de la premiere larme disponible dans la liste
	 * @return int
	 */
	public int demandeTir()
	{
		int index = -1;
		boolean fait = false;
		for (int i=0 ; i<larme.size() && !fait ; i++)
		{
			if (larme.get(i).isDisponible())
			{
				index = i;
				fait = true;
			}
		}
		return index;
	}
	
	/**
	 * recharge les larmes en fonction des cycles de jeu
	 */
	public void rechargement()
	{
	//voir avec les profs 20 larmes par cycle de rechargement = larme infinie 
	//sinon mettre condition pour bocler la boucle a 20
	//car la fonction est apelle pour un cycle de jeu donc 20
		for (int i=0 ; i<larme.size(); i++)
		{
			if (!larme.get(i).isDisponible() && !larme.get(i).isTirer() )
			{
				larme.get(i).setDisponible(true);
				larme.get(i).setHaut(false);
				larme.get(i).setBas(false);
				larme.get(i).setGauche(false);
				larme.get(i).setDroite(false);
			}
		}
	}
	
	/**
	 * Mise a jour de la liste de larme
	 */
	public void updateListeLarme()
	{
		larme.get(0).updateGameObject();
		larme.get(1).updateGameObject();
		larme.get(2).updateGameObject();
		larme.get(3).updateGameObject();
		larme.get(4).updateGameObject();
		
	}
	
	/*
	 * Shoot from key inputs.
	 */
	public void shootUpNext(int index)
	{
		larme.get(index).shootUpNext();
	}
	public void shootDownNext(int index)
	{
		larme.get(index).shootDownNext();
	}
	public void shootLeftNext(int index)
	{
		larme.get(index).shootLeftNext();
	}
	public void shootRightNext(int index)
	{
		larme.get(index).shootRightNext();
	}

	/*
	 * *************************************************************************************
	 * Moving from key inputs. Direction vector is later normalised.
	 * *************************************************************************************
	 */
	
	public void goUpNext()
	{
		getDirection().addY(1);
	}

	public void goDownNext()
	{
		getDirection().addY(-1);
	}

	public void goLeftNext()
	{
		getDirection().addX(-1);
	}

	public void goRightNext()
	{
		getDirection().addX(1);
	}

	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	
	public Vector2 getNormalizedDirection()
	{
		Vector2 normalizedVector = new Vector2(direction);
		normalizedVector.euclidianNormalize(speed);
		return normalizedVector;
	}

	/*
	 * *************************************************************************************
	 * Getters and Setters
	 * *************************************************************************************
	 */
	public Vector2 getPosition()
	{
		return position;
	}
	public void setPosition(Vector2 position)
	{
		this.position = position;
	}
	public Vector2 getSize()
	{
		return size;
	}
	public void setSize(Vector2 size)
	{
		this.size = size;
	}
	public String getImagePath()
	{
		return imagePath;
	}
	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}
	public double getSpeed()
	{
		return speed;
	}
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	public Vector2 getDirection()
	{
		return direction;
	}
	public void setDirection(Vector2 direction)
	{
		this.direction = direction;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public List<Projectile> getLarme() {
		return larme;
	}
	public void setLarme(List<Projectile> larme) {
		this.larme = larme;
	}
	public int getDegats() {
		return degats;
	}
	public void setDegats(int degats) {
		this.degats = degats;
	}
	public int getCoin() {
		return coin;
	}
	public void setCoin(int coin) {
		this.coin = coin;
	}
	public boolean isAffBloodOfTheMartyr() {
		return AffBloodOfTheMartyr;
	}
	public void setAffBloodOfTheMartyr(boolean affBloodOfTheMartyr) {
		AffBloodOfTheMartyr = affBloodOfTheMartyr;
	}
	public int getNbBomb() {
		return nbBomb;
	}
	public void setNbBomb(int nbBomb) {
		this.nbBomb = nbBomb;
	}
	public int getBouclier() {
		return bouclier;
	}
	public void setBouclier(int bouclier) {
		this.bouclier = bouclier;
	}	
	
	
}
