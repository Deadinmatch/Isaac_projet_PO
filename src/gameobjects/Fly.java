package gameobjects;

import java.util.ArrayList;
import java.util.List;

import libraries.StdDraw;
import libraries.Vector2;
import resources.ImagePaths;
import resources.RoomInfos;

public class Fly extends Monster{
	
	private int degatsProj;
	private List<Projectile> crotte;
	
	private boolean listeProjRempli;
	private int cycle;

	public Fly(Vector2 position, Vector2 size, String imagePath, int life, double speed, int degatsCC, int degatsProj)
	{
		super(position, size, imagePath, life, speed, degatsCC);
		this.degatsProj = degatsProj;
	}
	
	public void updateGameObject()
	{
		move();
		if (!listeProjRempli)							//remplir les larmes une fois en debut de partie
		{
			remplirProj();
		}
		updateListeCrotte();	
		rechargement();
		updateProcessForShoot();
		enVie();
	}
	

	private void move()
	{
		Vector2 normalizedDirection = getNormalizedDirection();
		Vector2 positionAfterMoving = getPosition().addVector(normalizedDirection);
		setPosition(positionAfterMoving);
		setDirection(new Vector2());
	}

	/*
	 * *************************************************************************************
	 * DRAW
	 * *************************************************************************************
	 */
	
	public void drawGameObject()
	{
		StdDraw.picture(getPosition().getX(), getPosition().getY(), getImagePath(), getSize().getX(), getSize().getY());
		this.drawListeProjectile();
		StdDraw.setPenColor(StdDraw.WHITE);
		//StdDraw.text(getPosition().getX() - 0.025, getPosition().getY() - 0.05, Integer.toString(this.getLife()));
	}
	
	/*
	 * *************************************************************************************
	 * Moving
	 * *************************************************************************************
	 */
	
	/**
	 * Appel les methodes de deplacement en fonction de la position
	 * @param position
	 */
	public void avance(Vector2 position, boolean murHaut, boolean murBas, boolean murGauche, boolean murDroite)
	{
		if (this.isAlive())
		{
			if (this.heroEnHaut(position) && murHaut)
			{
				this.goUpNext();
			}
			else if (this.heroEnBas(position) && murBas)
			{
				this.goDownNext();
			}
			if (this.heroAGauche(position) && murGauche)
			{
				this.goLeftNext();
			}
			else if (this.heroADroite(position) && murDroite)
			{
				this.goRightNext();
			}
		}
		
	}
	
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
	 * PROJECTILE
	 * *************************************************************************************
	 */
	
	/**
	 * Remplir la liste de projectile en debut de partie
	 */
	public void remplirProj()
	{
		crotte = new ArrayList<Projectile>();
		for (int i=0 ; i<5 ; i++)
		{
			crotte.add(i, new Projectile(1, RoomInfos.TILE_SIZE.scalarMultiplication(0.2), new Vector2(0.6,0.6), 0.0175, ImagePaths.CROTTE));
			crotte.get(i).setDegats(this.degatsProj); 
		}
		this.listeProjRempli = true;
	}
	
	/**
	 * Renvoie index de la premiere larme disponible dans la liste
	 * @return int
	 */
	public int demandeTir()
	{
		int index = -1;
		boolean fait = false;
		for (int i=0 ; i<crotte.size() && !fait ; i++)
		{
			if (crotte.get(i).isDisponible())
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
		for (int i=0 ; i<crotte.size(); i++)
		{
			if (!crotte.get(i).isDisponible() && !crotte.get(i).isTirer() )
			{
				crotte.get(i).setDisponible(true);
				crotte.get(i).setHaut(false);
				crotte.get(i).setBas(false);
				crotte.get(i).setGauche(false);
				crotte.get(i).setDroite(false);
			}
		}
	}
	
	/**
	 * Mise a jour de la liste de larme
	 */
	public void updateListeCrotte()
	{
		crotte.get(0).updateGameObject();
		crotte.get(1).updateGameObject();
		crotte.get(2).updateGameObject();
		crotte.get(3).updateGameObject();
		crotte.get(4).updateGameObject();
		
	}
	
	/*
	 * Shoot from key inputs.
	 */
	public void shootUpNext(int index)
	{
		crotte.get(index).shootUpNext();
	}
	public void shootDownNext(int index)
	{
		crotte.get(index).shootDownNext();
	}
	public void shootLeftNext(int index)
	{
		crotte.get(index).shootLeftNext();
	}
	public void shootRightNext(int index)
	{
		crotte.get(index).shootRightNext();
	}
	
	public void drawListeProjectile()
	{
		if (getCrotte().get(0).isTirer())
		{
			getCrotte().get(0).drawGameObject();
		}
		if (getCrotte().get(1).isTirer())
		{
			getCrotte().get(1).drawGameObject();
		}
		if (getCrotte().get(2).isTirer())
		{
			getCrotte().get(2).drawGameObject();
		}
		if (getCrotte().get(3).isTirer())
		{
			getCrotte().get(3).drawGameObject();
		}
		if (getCrotte().get(4).isTirer())
		{
			getCrotte().get(4).drawGameObject();
		}
	}
	
	/**
	 * Appel les fonctions pour lancer le projectile
	 * @param index
	 */
	private void processForShoot(int index)
	{
		if (getCrotte().get(index).isTirer())
		{
			if(getCrotte().get(index).isDroite())
			{
				shootRightNext(index);
				if(!getCrotte().get(index).portee(getCrotte().get(index).getPosition(), getCrotte().get(index).getPositionPortee()))
				{
					getCrotte().get(index).setTirer(false);
				}
			}
			
			else if(getCrotte().get(index).isGauche())
			{
				shootLeftNext(index);
				if(!getCrotte().get(index).portee2(getCrotte().get(index).getPosition(), getCrotte().get(index).getPositionPortee()))
				{
					getCrotte().get(index).setTirer(false);
				}
			}
			
			else if(getCrotte().get(index).isBas())
			{
				shootDownNext(index);
				if (!getCrotte().get(index).portee2(getCrotte().get(index).getPosition(), getCrotte().get(index).getPositionPortee()))
				{
					getCrotte().get(index).setTirer(false);
				}
			}
			
			else if(getCrotte().get(index).isHaut())
			{
				shootUpNext(index);
				if (!getCrotte().get(index).portee(getCrotte().get(index).getPosition(), getCrotte().get(index).getPositionPortee()))
				{
					getCrotte().get(index).setTirer(false);
				}
			}
		}	
	}
	
	/**
	 * Mise a du processus de tire pour chaque projectile
	 */
	private void updateProcessForShoot()
	{
		if (this.isAlive())
		{
			processForShoot(0);
			processForShoot(1);
			processForShoot(2);
			processForShoot(3);
			processForShoot(4);
		}
		else
		{
			getCrotte().get(0).setTirer(false);
			getCrotte().get(1).setTirer(false);
			getCrotte().get(2).setTirer(false);
			getCrotte().get(3).setTirer(false);
			getCrotte().get(4).setTirer(false);
			
		}
		
	}
	
	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	
	public Vector2 getNormalizedDirection()
	{
		Vector2 normalizedVector = new Vector2(getDirection());
		normalizedVector.euclidianNormalize(getSpeed());
		return normalizedVector;
	}

	/*
	 * *************************************************************************************
	 * GETTERS ANS SETTERS
	 * *************************************************************************************
	 */
	public int getDegatsProj() {
		return degatsProj;
	}
	public void setDegatsProj(int degatsProj) {
		this.degatsProj = degatsProj;
	}
	public List<Projectile> getCrotte() {
		return crotte;
	}
	public void setCrotte(List<Projectile> crotte) {
		this.crotte = crotte;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	

}
