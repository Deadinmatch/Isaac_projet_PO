package gameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gameobjects.SpiderWeb;
import gameobjects.Hero;
import gameobjects.HpUp;
import gameobjects.Nickel;
import gameobjects.Projectile;
import gameobjects.Rocher;
import gameobjects.SpeedUp;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.RoomInfos;
import resources.ImagePaths;
import gameobjects.Wall;
import gameobjects.BlackHeart;
import gameobjects.BloodOfTheMartyr;
import gameobjects.Bomb;
import gameobjects.Coin;
import gameobjects.Consommable;
import gameobjects.Dime;
import gameobjects.Door;
import gameobjects.HalfHpUp;

public abstract class Room
{
	private Hero hero;
	private List<Wall> Mur;
	private List<Door> DoorClosed;
	private List<Door> DoorOpen;
	
	private List<Rocher> Rocher;
	private List<SpiderWeb> SpiderWeb;
	

	private boolean PorteHaut;
	private boolean PorteBas;
	private boolean PorteGauche;
	private boolean PorteDroite;

	public Room(Hero hero)
	{
		this.hero = hero;
	}


	/*
	 * Make every entity that compose a room process one step
	 */
	public void updateRoom()
	{
		makeHeroPlay();
		collisionSpiderWebThisMap(this.hero.getPosition(), this.hero.getSize());
	}
	
	public abstract void updateEntity();


	private void makeHeroPlay()
	{
		hero.updateGameObject();
	}

	/*
	 * *************************************************************************************
	 * DRAW
	 * *************************************************************************************
	 */
	/**
	 * Drawing floor
	 */
	public void drawRoom()
	{
		// For every tile, set background picture.
		for (int i = 1; i < RoomInfos.NB_TILES-1; i++)
		{
			for (int j = 1; j < RoomInfos.NB_TILES-1; j++)
			{
				Vector2 position = positionFromTileIndex(i, j);
				StdDraw.picture(position.getX(), position.getY(), ImagePaths.FLOOR, 0.12,
						0.12);	
			}
		}
		hero.drawGameObject();
	}
	
	/**
	 * Creation objet mur &
	 * Drawing Wall
	 */
	public void drawWall()
	{
		// For every tile, set wall picture.
		Mur = new ArrayList<Wall>();
		
		int index = 0;
		for (int i = 0; i < RoomInfos.NB_TILES; i++)
		{
			for (int j = 0; j < RoomInfos.NB_TILES; j++)
			{
				Vector2 position = positionFromTileIndex(i, j);
				Mur.add(index, new Wall(position, new Vector2(0.12,0.12), ImagePaths.WALL2));
				if (j==RoomInfos.NB_TILES-1 && i!=RoomInfos.NB_TILES-1)
				{
					StdDraw.picture(Mur.get(index).getPosition().getX(), Mur.get(index).getPosition().getY(), Mur.get(index).getImagePath() , Mur.get(index).getSize().getX(),
							Mur.get(index).getSize().getY());
				}
				else if (i==RoomInfos.NB_TILES-1 && j!=RoomInfos.NB_TILES-1)
				{
					StdDraw.picture(Mur.get(index).getPosition().getX(), Mur.get(index).getPosition().getY(), Mur.get(index).getImagePath() , Mur.get(index).getSize().getX(),
							Mur.get(index).getSize().getY(), -90.0);
				}
				else if (i==0 && j!=0)
				{
					StdDraw.picture(Mur.get(index).getPosition().getX(), Mur.get(index).getPosition().getY(), Mur.get(index).getImagePath() , Mur.get(index).getSize().getX(),
							Mur.get(index).getSize().getY(), 90.0);
				}
				else if (j==0 && i!=0)
				{
					StdDraw.picture(Mur.get(index).getPosition().getX(), Mur.get(index).getPosition().getY(), Mur.get(index).getImagePath() , Mur.get(index).getSize().getX(),
							Mur.get(index).getSize().getY(), 180.0);
				}
				/*
				 * Draw Angle
				 */
				if (i==0 && j==0)
					StdDraw.picture(position.getX(), position.getY(), ImagePaths.ANGLE, 0.12, 0.12, 90.0 );
				if (i==RoomInfos.NB_TILES-1 && j==0) 
					StdDraw.picture(position.getX(), position.getY(), ImagePaths.ANGLE, 0.12, 0.12, 180.0 );
				if (i==0 && j==RoomInfos.NB_TILES-1)
					StdDraw.picture(position.getX(), position.getY(), ImagePaths.ANGLE, 0.12, 0.12, 0.0 );
				if (i ==RoomInfos.NB_TILES-1 && j==RoomInfos.NB_TILES-1)
					StdDraw.picture(position.getX(), position.getY(), ImagePaths.ANGLE, 0.12, 0.12, -90.0 );
				index++;
			}
		}
		hero.drawGameObject();
	}
	
	public Consommable drawObjectAleatoire(Vector2 position)
	{
		int min = 0;
		int max = 100;
		Random random = new Random();
		int aleatoire = random.nextInt(max-min)+min;
		Consommable tmp = null;
		//rapidité
		if (aleatoire >= 0 && aleatoire < 3)
		{
			tmp = new SpeedUp(position, new Vector2(0.04,0.04), ImagePaths.SPEED_INFO, "SpeedUp");
		}
		//blood of matyr
		if (aleatoire >=3 && aleatoire < 5)
		{
			tmp = new BloodOfTheMartyr(position, new Vector2(), ImagePaths.BLOOD_OF_THE_MARTYR, "BloodOfTheMartyr");
		}
		//bombe
		if (aleatoire >=5 && aleatoire < 5)
		{
			tmp = new Bomb(position, new Vector2(0.06,0.06), ImagePaths.BOMB, "Bomb");
		}
		//coeur noir sup
		if (aleatoire >=9 && aleatoire < 19)
		{
			tmp = new BlackHeart(position, new Vector2(0.04,0.04), ImagePaths.BLACK_HEART, "BlackHeart");
		}
		//dime
		if (aleatoire >=19 && aleatoire < 35)
		{
			tmp = new Dime(position, new Vector2(0.04,0.04), ImagePaths.DIME, "Dime");
		}
		//nickel
		if (aleatoire >=35 && aleatoire < 45)
		{
			tmp = new Nickel(position, new Vector2(0.04,0.04), ImagePaths.NICKEL, "Nickel");
		}
		//coin
		if (aleatoire >=45 && aleatoire < 55)
		{
			tmp = new Coin(position, new Vector2(0.04,0.04), ImagePaths.COIN, "Coin");
		}
		//coeur
		if (aleatoire >=55 && aleatoire < 62)
		{
			tmp = new HpUp(position, new Vector2(0.05,0.05), ImagePaths.HP_UP, "HpUp");
		}
		//half coeur
		if (aleatoire >=62 && aleatoire < 80)
		{
			tmp = new HalfHpUp(position, new Vector2(0.04,0.04), ImagePaths.HALF_HEART_PICKABLE, "HalfHpUp");
		}
		if (aleatoire >= 80 && aleatoire < 100)
		{
			tmp = new Coin(new Vector2(-1.0,-1.0), new Vector2(0.09,0.09), ImagePaths.HP_UP);
		}
		return tmp;
	}
	
	/*
	 * Affichage et creation des objets portes
	 * Si la salle est termine on remplace les coord de porteferme pour ne plus bloque le personnage 
	 * dans la classe gameworld
	 */
	public abstract void drawPorte();

	/**
	 * Regroupe toute les méthodes draw
	 */
	public abstract void drawAll();
	
	
	
	/*
	 * *************************************************************************************
	 * COLLISION
	 * *************************************************************************************
	 */
	
	/**
	 * Regarde si collision entre un objet et le mur du haut
	 * @param position
	 * @param size
	 * @return
	 */
	public boolean CollisionMurHaut(Vector2 position, Vector2 size)
	{
		return (!Physics.rectangleCollision(position, size, getMur().get(17).getPosition(), getMur().get(17).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(26).getPosition(), getMur().get(26).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(35).getPosition(), getMur().get(35).getSize())
				&& (!Physics.rectangleCollision(position, size, getDoorClosed().get(2).getPosition(), getDoorClosed().get(2).getSize())  )
				&& !Physics.rectangleCollision(position, size, getMur().get(53).getPosition(), getMur().get(53).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(62).getPosition(), getMur().get(62).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(71).getPosition(), getMur().get(71).getSize()));
	}
	/**
	 * Regarde si collision entre un objet et le mur du bas
	 * @param position
	 * @param size
	 * @return
	 */
	public boolean CollisionMurBas(Vector2 position, Vector2 size)
	{
		return (!Physics.rectangleCollision(position, size, getMur().get(9).getPosition(), getMur().get(9).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(18).getPosition(), getMur().get(18).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(27).getPosition(), getMur().get(27).getSize())
				&& (!Physics.rectangleCollision(position, size, getDoorClosed().get(1).getPosition(), getDoorClosed().get(1).getSize()) )
				&& !Physics.rectangleCollision(position, size, getMur().get(45).getPosition(), getMur().get(45).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(54).getPosition(), getMur().get(54).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(63).getPosition(), getMur().get(63).getSize()));
	}
	
	/**
	 * Regarde si collision entre un objet et le mur de gauche
	 * @param position
	 * @param size
	 * @return
	 */
	public boolean CollisionMurGauche(Vector2 position, Vector2 size)
	{
		return (!Physics.rectangleCollision(position, size, getMur().get(1).getPosition(), getMur().get(1).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(2).getPosition(), getMur().get(2).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(3).getPosition(), getMur().get(3).getSize())
				&& (!Physics.rectangleCollision(position, size, getDoorClosed().get(0).getPosition(), getDoorClosed().get(0).getSize()) )
				&& !Physics.rectangleCollision(position, size, getMur().get(5).getPosition(), getMur().get(5).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(6).getPosition(), getMur().get(6).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(7).getPosition(), getMur().get(7).getSize()));
	}
	/**
	 * Regarde si collision entre un objet et le mur de droite
	 * @param position
	 * @param size
	 * @return
	 */
	public boolean CollisionMurDroite(Vector2 position, Vector2 size)
	{
		return (!Physics.rectangleCollision(position, size, getMur().get(73).getPosition(), getMur().get(73).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(74).getPosition(), getMur().get(74).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(75).getPosition(), getMur().get(75).getSize())
				&& (!Physics.rectangleCollision(position, size, getDoorClosed().get(3).getPosition(), getDoorClosed().get(3).getSize()) )
				&& !Physics.rectangleCollision(position, size, getMur().get(77).getPosition(), getMur().get(77).getSize())
				&& !Physics.rectangleCollision(position,size, getMur().get(78).getPosition(), getMur().get(78).getSize())
				&& !Physics.rectangleCollision(position, size, getMur().get(79).getPosition(), getMur().get(79).getSize()));
	}
	
	/**
	 * Regarde si collision avec un object et dans quelle direction
	 * @param postion
	 * @param size
	 * @return boolean
	 */
	protected abstract boolean collisionHaut(Vector2 position, Vector2 size);
	protected abstract boolean collisionBas(Vector2 position, Vector2 size);
	protected abstract boolean collisionGauche(Vector2 position, Vector2 size);
	protected abstract boolean collisionDroite(Vector2 position, Vector2 size);
	
	
	
	/**
	 * Renvoie si collision avec un rocher qui est a notre gauche 
	 * @param position
	 * @param size
	 * @return boolean
	 */
	protected boolean collisionRocherGauche(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, collisionRocherThisMap(position, size).getPosition(), collisionRocherThisMap(position, size).getSize()))
		{
			double x = position.getX() - collisionRocherThisMap(position, size).getPosition().getX();
			if (x > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoie si collision avec un rocher qui est a notre bas
	 * @param position
	 * @param size
	 * @return boolean
	 */
	protected boolean collisionRocherBas(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, collisionRocherThisMap(position, size).getPosition(), collisionRocherThisMap(position, size).getSize()))
		{
			double y = position.getY() - collisionRocherThisMap(position, size).getPosition().getY();
			if (y > 0)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Renvoie si collision avec un rocher qui est a notre haut
	 * @param position
	 * @param size
	 * @return boolean
	 */
	protected boolean collisionRocherHaut(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, collisionRocherThisMap(position, size).getPosition(), collisionRocherThisMap(position, size).getSize()))
		{
			double y = position.getY() - collisionRocherThisMap(position, size).getPosition().getY();
			if (y < 0)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Renvoie si collision avec un rocher qui est a notre droite
	 * @param position
	 * @param size
	 * @return boolean
	 */
	protected boolean collisionRocherDroite(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, collisionRocherThisMap(position, size).getPosition(), collisionRocherThisMap(position, size).getSize()))
		{
			double x = position.getX() - collisionRocherThisMap(position, size).getPosition().getX();
			if (x < 0)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Renvoie le rocher avec lequel nous sommes en collision sinon un rocher hors de la map
	 * @param position
	 * @param size
	 * @return Rocher
	 */
	protected abstract Rocher collisionRocherThisMap(Vector2 position, Vector2 size);
	
	
	/**
	 * Change la vitesse du hero si collision avec une toile d araignee
	 * @param position
	 * @param size
	 */
	protected abstract void collisionSpiderWebThisMap(Vector2 position, Vector2 size);
	
	/*
	 * *************************************************************************************
	 * TRICHE
	 * *************************************************************************************
	 */
	/**
	 * Triche tue les monstres
	 */
	public abstract void killAllMonster();
	
	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	
	/**
	 * Convert a tile index to a 0-1 position.
	 * 
	 * @param indexX
	 * @param indexY
	 * @return Vector2
	 */
	protected static Vector2 positionFromTileIndex(int indexX, int indexY)
	{
		return new Vector2(indexX * RoomInfos.TILE_WIDTH + RoomInfos.HALF_TILE_SIZE.getX(),
				indexY * RoomInfos.TILE_HEIGHT + RoomInfos.HALF_TILE_SIZE.getY());
	}

	protected Vector2 CreatVectorAleatoire(){

		int min = 1;
		int max = 7;
		Random random = new Random();
		int aleatoire = random.nextInt(max-min)+min;
		int aleatoire2 = random.nextInt(max-min)+min;
		Vector2 position = null;

		for (int i = 1; i < RoomInfos.NB_TILES-1; i++)
		{
			for (int j = 1; j < RoomInfos.NB_TILES-1 ; j++)
			{
					if ( i == aleatoire && j == aleatoire2)  // && ( i != 5 || j != 5)
					{
						position = positionFromTileIndex(i,j);
					}
			}
		}
		if ( position == null)
		{
			position = RoomInfos.POSITION_CENTER_OF_ROOM;
		}
		return position;
	}
	
	/**
	 * Baisse la vie des monstres si collision avec larme et sert de collibox pour la larme avec un monstre
	 * @param p
	 * @return boolean
	 */
	public abstract boolean attaqueDuHero(Projectile p);
	
	

	/**
	 * Retourne si la salle est finie ou pas
	 * @return boolean
	 */
	public abstract boolean roomFinished();
	/*
	 * *************************************************************************************
	 * Getters and Setters
	 * *************************************************************************************
	 */
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero = hero;
	}
	public List<Wall> getMur() {
		return Mur;
	}
	public void setMur(List<Wall> mur) {
		Mur = mur;
	}
	public List<Door> getDoorClosed() {
		return DoorClosed;
	}
	public void setDoorClosed(List<Door> doorClosed) {
		DoorClosed = doorClosed;
	}
	public List<Door> getDoorOpen() {
		return DoorOpen;
	}
	public void setDoorOpen(List<Door> doorOpen) {
		DoorOpen = doorOpen;
	}
	public boolean isPorteHaut() {
		return PorteHaut;
	}
	public void setPorteHaut(boolean porteHaut) {
		PorteHaut = porteHaut;
	}
	public boolean isPorteBas() {
		return PorteBas;
	}
	public void setPorteBas(boolean porteBas) {
		PorteBas = porteBas;
	}
	public boolean isPorteGauche() {
		return PorteGauche;
	}
	public void setPorteGauche(boolean porteGauche) {
		PorteGauche = porteGauche;
	}
	public boolean isPorteDroite() {
		return PorteDroite;
	}
	public void setPorteDroite(boolean porteDroite) {
		PorteDroite = porteDroite;
	}
	public List<SpiderWeb> getSpiderWeb() {
		return SpiderWeb;
	}
	public void setSpiderWeb(List<SpiderWeb> spiderWeb) {
		SpiderWeb = spiderWeb;
	}
	public List<Rocher> getRocher() {
		return Rocher;
	}
	public void setRocher(List<Rocher> rocher) {
		Rocher = rocher;
	}
	

	
	
	
	
	
	
	
	
	
}
