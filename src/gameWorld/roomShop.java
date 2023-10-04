package gameWorld;

import java.util.ArrayList;
import java.util.List;

import gameobjects.BloodOfTheMartyr;
import gameobjects.Bomb;
import gameobjects.Decoration;
import gameobjects.Door;
import gameobjects.Rocher;
import gameobjects.Hero;
import gameobjects.HpUp;
import gameobjects.Projectile;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.ImagePaths;
import resources.RoomInfos;

public class roomShop extends Room {
	
	private List<HpUp> hpUp;
	private List<BloodOfTheMartyr> blood;
	private List<Bomb> bomb;
	private Decoration deco;
	
	private int cycle;
	
	public roomShop(Hero hero, boolean gauche, boolean bas, boolean haut, boolean droite) {
		super(hero);
		setPorteGauche(gauche);
		setPorteBas(bas);
		setPorteHaut(haut);
		setPorteDroite(droite);
	} 
	
	@Override
	public void updateEntity() 
	{
		collisionHpUp();
		collisionBlood();
		collisionBomb();
	}
	/*
	 * *************************************************************************************
	 * DRAW
	 * *************************************************************************************
	 */
	
	public void drawAll()
	{
		super.drawRoom();
		super.drawWall();
		drawPorte();
		drawAndInitialiseHpUP();
		drawAndInitialiseBlood();
		drawAndInitialiseBomb();
		drawDeco();
		this.cycle++;
	}
	
	@Override
	public void drawPorte()
	{
		setDoorClosed(new ArrayList<Door>());
		setDoorOpen(new ArrayList<Door>());
		
		// For every tile, set wall picture.
		int index = 0;
		for (int i = 0; i < RoomInfos.NB_TILES; i++)
		{
			for (int j = 0; j < RoomInfos.NB_TILES; j++)
			{
				Vector2 position = positionFromTileIndex(i, j);
			
				if (index == 4 || index == 36 || index == 44 || index == 76)
				{
					if (roomFinished())
						{
						getDoorOpen().add(new Door(position, new Vector2(0.125,0.125), ImagePaths.OPENED_DOOR));
						getDoorClosed().add(new Door(new Vector2(-1.0,-1.0), new Vector2(0.1,0.1), ImagePaths.CLOSED_DOOR));
						}
					else getDoorClosed().add(new Door(position, new Vector2(0.125,0.125), ImagePaths.CLOSED_DOOR));
					
				}
				
				//Porte du haut
				if (j==RoomInfos.NB_TILES-1 && this.isPorteHaut())
				{
					if (index == 44 && !roomFinished()) StdDraw.picture(getDoorClosed().get(2).getPosition().getX(), getDoorClosed().get(2).getPosition().getY(), getDoorClosed().get(2).getImagePath() , getDoorClosed().get(2).getSize().getX(),
							getDoorClosed().get(2).getSize().getY(), 0.0);
					else if (index == 44 && roomFinished()) StdDraw.picture(getDoorOpen().get(2).getPosition().getX(), getDoorOpen().get(2).getPosition().getY(), getDoorOpen().get(2).getImagePath() , getDoorOpen().get(2).getSize().getX(),
							getDoorOpen().get(2).getSize().getY(), 0.0);
				}
				//Porte de droite
				else if (i==RoomInfos.NB_TILES-1 && this.isPorteDroite())
				{
					if (index == 76 && !roomFinished()) StdDraw.picture(getDoorClosed().get(3).getPosition().getX(), getDoorClosed().get(3).getPosition().getY(), getDoorClosed().get(3).getImagePath() , getDoorClosed().get(3).getSize().getX(),
							getDoorClosed().get(3).getSize().getY(), -90.0);
					else if (index == 76 && roomFinished()) StdDraw.picture(getDoorOpen().get(3).getPosition().getX(), getDoorOpen().get(3).getPosition().getY(), getDoorOpen().get(3).getImagePath() , getDoorOpen().get(3).getSize().getX(),
							getDoorOpen().get(3).getSize().getY(), -90.0);
				}
				//Porte de gauche
				else if (i==0 && j!=0 && this.isPorteGauche())
				{
					if (index == 4 && !roomFinished()) StdDraw.picture(getDoorClosed().get(0).getPosition().getX(), getDoorClosed().get(0).getPosition().getY(), getDoorClosed().get(0).getImagePath() , getDoorClosed().get(0).getSize().getX(),
							getDoorClosed().get(0).getSize().getY(), 90.0);
					else if (index == 4 && roomFinished()) StdDraw.picture(getDoorOpen().get(0).getPosition().getX(), getDoorOpen().get(0).getPosition().getY(), getDoorOpen().get(0).getImagePath() , getDoorOpen().get(0).getSize().getX(),
							getDoorOpen().get(0).getSize().getY(), 90.0);
				}
				//Porte du bas
				else if (j==0 && i!=0 && this.isPorteBas())
				{
					if (index == 36 && !roomFinished()) StdDraw.picture(getDoorClosed().get(1).getPosition().getX(), getDoorClosed().get(1).getPosition().getY(), getDoorClosed().get(1).getImagePath() , getDoorClosed().get(1).getSize().getX(),
							getDoorClosed().get(1).getSize().getY(), 180.0);
					else if (index == 36 && roomFinished()) StdDraw.picture(getDoorOpen().get(1).getPosition().getX(), getDoorOpen().get(1).getPosition().getY(), getDoorOpen().get(1).getImagePath() , getDoorOpen().get(1).getSize().getX(),
							getDoorOpen().get(1).getSize().getY(), 180.0);
				}
				index++;
			}
		}
		getHero().drawGameObject();
	}
	
	public void drawAndInitialiseHpUP()
	{
		hpUp = new ArrayList<HpUp>();
		int index = 0;
		
		for (int i = 1; i < RoomInfos.NB_TILES-1; i++)
		{
			for (int j = 1; j < RoomInfos.NB_TILES-1 ; j++) 
			{
				Vector2 position = positionFromTileIndex(i,j);
				if ( i == 2 && j == 4)
				{
					getHpUp().add(index,new HpUp(position,new Vector2(0.09,0.09),ImagePaths.HP_UP));
					getHpUp().get(index).setPrice(2);
					StdDraw.picture(getHpUp().get(index).getPosition().getX(), getHpUp().get(index).getPosition().getY(),
							getHpUp().get(index).getImagePath(), getHpUp().get(index).getSize().getX(), getHpUp().get(index).getSize().getY());
					StdDraw.picture(position.getX(), position.getY() -0.05, ImagePaths.COIN);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(position.getX() - 0.025, position.getY() - 0.05, Integer.toString(getHpUp().get(0).getPrice()));
					index++;
				}
			}
		}
		for (int i=0 ; i<30 ; i++)
		{
			getHpUp().add(index,new HpUp(getHpUp().get(0).getPosition(),new Vector2(0.09,0.09),ImagePaths.HP_UP));
			getHpUp().get(index).setPrice(2);
			index++;
		}
		getHero().drawGameObject();
	}
	
	public void drawAndInitialiseBlood()
	{
		blood = new ArrayList<BloodOfTheMartyr>();
		int index = 0;
		for (int i = 1; i < RoomInfos.NB_TILES-1; i++)
		{
			for (int j = 1; j < RoomInfos.NB_TILES-1 ; j++) 
			{
				Vector2 position = positionFromTileIndex(i,j);
				if ( i == 6 && j == 4)
				{
					getBlood().add(index,new BloodOfTheMartyr(position,new Vector2(0.09,0.09),ImagePaths.BLOOD_OF_THE_MARTYR));
					getBlood().get(index).setPrice(15);
					StdDraw.picture(getBlood().get(index).getPosition().getX(), getBlood().get(index).getPosition().getY(),
							getBlood().get(index).getImagePath(), getBlood().get(index).getSize().getX(), getBlood().get(index).getSize().getY());
					StdDraw.picture(position.getX(), position.getY() -0.05, ImagePaths.COIN);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(position.getX() - 0.03, position.getY() - 0.05, Integer.toString(getBlood().get(0).getPrice()));
					index++;
				}
			}
		}
		for (int i=0 ; i<30 ; i++)
		{
			getBlood().add(index,new BloodOfTheMartyr(getBlood().get(0).getPosition(),new Vector2(0.09,0.09),ImagePaths.BLOOD_OF_THE_MARTYR));
			getBlood().get(index).setPrice(15);
			index++;
		}
		getHero().drawGameObject();
	}
	
	public void drawAndInitialiseBomb()
	{
		bomb = new ArrayList<Bomb>();
		int index = 0;
		for (int i = 1; i < RoomInfos.NB_TILES-1; i++)
		{
			for (int j = 1; j < RoomInfos.NB_TILES-1 ; j++) 
			{
				Vector2 position = positionFromTileIndex(i,j);
				if ( i == 4 && j == 4)
				{
					getBomb().add(index,new Bomb(position,new Vector2(0.06,0.06),ImagePaths.BOMB));
					getBomb().get(index).setPrice(3);
					StdDraw.picture(getBomb().get(index).getPosition().getX(), getBomb().get(index).getPosition().getY(),
							getBomb().get(index).getImagePath(), getBomb().get(index).getSize().getX(), getBomb().get(index).getSize().getY());
					StdDraw.picture(position.getX(), position.getY() -0.05, ImagePaths.COIN);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(position.getX() - 0.03, position.getY() - 0.05, Integer.toString(getBomb().get(0).getPrice()));
					index++;
				}
			}
		}
		for (int i=0 ; i<30 ; i++)
		{
			getBomb().add(index,new Bomb(getBlood().get(0).getPosition(),new Vector2(0.06,0.06),ImagePaths.BOMB));
			getBomb().get(index).setPrice(3);
			index++;
		}
		getHero().drawGameObject();
	}
	
	public void drawDeco()
	{
		for (int i=1 ; i<RoomInfos.NB_TILES-1; i++)
		{
			for (int j=0 ; j<RoomInfos.NB_TILES-1 ; j++)
			{
				Vector2 position = positionFromTileIndex(i,j);
				if (i==7 && j==7)
				{
					deco = new Decoration(position, new Vector2(0.1,0.1), ImagePaths.SHOP_DECO);
					StdDraw.picture(deco.getPosition().getX(), deco.getPosition().getY(), deco.getImagePath(), deco.getSize().getX(), deco.getSize().getY());
				}
			}
		}
		getHero().drawGameObject();
	}
	
	
	/*
	 * *************************************************************************************
	 * COLLISION
	 * *************************************************************************************
	 */
	
	@Override
	protected boolean collisionHaut(Vector2 position, Vector2 size)
	{
		if (this.isPorteHaut())
		{
			return !this.collisionDeco();
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(), getMur().get(44).getSize())
					&& !this.collisionDeco());
		}
			
	}
	@Override
	protected boolean collisionBas(Vector2 position, Vector2 size)
	{
		if (this.isPorteBas())
		{
			return true;
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(36).getPosition(), getMur().get(36).getSize()));
		}
		
	}
	@Override
	protected boolean collisionGauche(Vector2 position, Vector2 size)
	{
		if (this.isPorteGauche())
		{
			return !this.collisionDeco();
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(), getMur().get(4).getSize())
					&& !this.collisionDeco());
		}
		
	}
	@Override
	protected boolean collisionDroite(Vector2 position, Vector2 size)
	{
		if (this.isPorteDroite())
		{
			return !this.collisionDeco();
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(), getMur().get(76).getSize())
					&& !this.collisionDeco());
		}
	}
	
	

	@Override
	protected Rocher collisionRocherThisMap(Vector2 position, Vector2 size) {
		return null;
	}

	@Override
	protected void collisionSpiderWebThisMap(Vector2 position, Vector2 size) {
		
	}
	
	private void collisionHpUp()
	{
		if (Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getHpUp().get(0).getPosition(), getHpUp().get(0).getSize())
			&& getHero().getCoin() >= getHpUp().get(0).getPrice() && this.cycle > 35)
		{
			if (getHero().getLife() <= 4)
			{
				getHero().setLife(getHero().getLife() + 2);
				getHero().setCoin(getHero().getCoin() - getHpUp().get(0).getPrice());
				this.cycle = 0;
			}
			else if (getHero().getLife() == 5)
			{
				getHero().setLife(6);
				getHero().setCoin(getHero().getCoin() - getHpUp().get(0).getPrice());
				this.cycle = 0;
			}
		}
	}
	
	private void collisionBomb()
	{
		if (Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getBomb().get(0).getPosition(), getBomb().get(0).getSize())
			&& getHero().getCoin() >= getBomb().get(0).getPrice() && this.cycle > 35)
		{
			getHero().setNbBomb(getHero().getNbBomb()+1);
			getHero().setCoin(getHero().getCoin() - getBomb().get(0).getPrice());
			this.cycle = 0;
		}
	}
	
	private void collisionBlood()
	{
		if (Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getBlood().get(0).getPosition(), getBlood().get(0).getSize())
			&& getHero().getCoin() >= getBlood().get(0).getPrice() && this.cycle > 35)
		{
			getHero().setDegats(getHero().getDegats() + 1);
			getHero().remplirLarme();
			getHero().setCoin(getHero().getCoin() - getBlood().get(0).getPrice());
			getHero().setAffBloodOfTheMartyr(true);
			this.cycle = 0;
		}
	}
	
	private boolean collisionDeco()
	{
		if (Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), deco.getPosition(), deco.getSize()))
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

	@Override
	public boolean roomFinished()
	{
		return true;
	}
	@Override
	public boolean attaqueDuHero(Projectile p) {
		return false;
	}
	@Override
	public void killAllMonster() {
		
	}
	
	/*
	 * *************************************************************************************
	 * GETTERS AND SETTERS
	 * *************************************************************************************
	 */
	public List<HpUp> getHpUp() {
		return hpUp;
	}
	public void setHpUp(List<HpUp> hpUp) {
		this.hpUp = hpUp;
	}
	public List<BloodOfTheMartyr> getBlood() {
		return blood;
	}
	public void setBlood(List<BloodOfTheMartyr> blood) {
		this.blood = blood;
	}
	public List<Bomb> getBomb() {
		return bomb;
	}
	public void setBomb(List<Bomb> bomb) {
		this.bomb = bomb;
	}
	

	
}
