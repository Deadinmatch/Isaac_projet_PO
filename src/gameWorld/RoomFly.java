package gameWorld;

import java.util.ArrayList;
import java.util.List;

import gameobjects.Consommable;
import gameobjects.Door;
import gameobjects.Fly;
import gameobjects.Rocher;
import gameobjects.Hero;
import gameobjects.Projectile;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.FlyInfos;
import resources.ImagePaths;
import resources.RoomInfos;

public class RoomFly extends Room {
	
	private int cycle = this.tempsPause+1;
	private final int tempsPause = 30;
	private List<Fly> mouche;
	private Consommable drop = drawObjectAleatoire(new Vector2(0.3,0.3));
	private boolean isFinish;
	
	public RoomFly(Hero hero, boolean gauche, boolean bas, boolean haut, boolean droite) {
		super(hero);
		setPorteGauche(gauche);
		setPorteBas(bas);
		setPorteHaut(haut);
		setPorteDroite(droite);
	} 
	
	public void updateEntity()
	{
		getMouche().get(0).updateGameObject();
		getMouche().get(1).updateGameObject();
		getMouche().get(2).updateGameObject();
		getMouche().get(3).updateGameObject();
		attaqueDesMonstre();
		collisionDrop();
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
		drawRocher();
		drawMonster(getMouche().get(0));
		drawMonster(getMouche().get(1));
		drawMonster(getMouche().get(2));
		drawMonster(getMouche().get(3));
		if (this.isFinish())
		{
			StdDraw.picture(drop.getPosition().getX(), drop.getPosition().getY(), drop.getImagePath(), drop.getSize().getX(), drop.getSize().getY());
		}
	}
	
	public void drawMonster(Fly test)
	{
		test.drawGameObject();
		MonstreMove(test);
		test.setCycle(test.getCycle()+1);
		tirMouche();
		updateProjMonstre();
	}
	
	public void drawAllMonster()
	{
		drawMonster(getMouche().get(0));
		drawMonster(getMouche().get(1));
		drawMonster(getMouche().get(2));
		drawMonster(getMouche().get(3));
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
					else if (index == 36 && roomFinished()) StdDraw.picture(getDoorOpen().get(1).getPosition().getX(), getDoorOpen().get(1).getPosition().getY(), ImagePaths.BOSS_DOOR , getDoorOpen().get(1).getSize().getX(),
							getDoorOpen().get(1).getSize().getY(), 180.0);
				}
				index++;
			}
		}
		getHero().drawGameObject();
	}
	
	/**
	 * Creation objet rocher &
	 * Drawing Rocher
	 */
	public void drawRocher()
	{
		setRocher(new ArrayList<Rocher>());
		int index = 0;
		for (int i = 1; i < RoomInfos.NB_TILES-1; i++)
		{
			for (int j = 1; j < RoomInfos.NB_TILES-1; j++)
			{
				Vector2 position = positionFromTileIndex(i, j);
				if (i==4 && j==5)
				{
					getRocher().add(index, new Rocher(position, new Vector2(0.1,0.1), ImagePaths.ROCK));
					StdDraw.picture(getRocher().get(index).getPosition().getX(), getRocher().get(index).getPosition().getY(), 
							getRocher().get(index).getImagePath(), getRocher().get(index).getSize().getX(), getRocher().get(index).getSize().getY());
					index++;
				}	
				if (i==3&& j==4)
				{
					getRocher().add(index, new Rocher(position, new Vector2(0.1,0.1), ImagePaths.ROCK));
					StdDraw.picture(getRocher().get(index).getPosition().getX(), getRocher().get(index).getPosition().getY(), 
							getRocher().get(index).getImagePath(), getRocher().get(index).getSize().getX(), getRocher().get(index).getSize().getY());
					index++;
				}
				if (i==4 && j==3)
				{
					getRocher().add(index, new Rocher(position, new Vector2(0.1,0.1), ImagePaths.ROCK));
					StdDraw.picture(getRocher().get(index).getPosition().getX(), getRocher().get(index).getPosition().getY(), 
							getRocher().get(index).getImagePath(), getRocher().get(index).getSize().getX(), getRocher().get(index).getSize().getY());
					index++;
				}
				if (i==5 && j==4)
				{
					getRocher().add(index, new Rocher(position, new Vector2(0.1,0.1), ImagePaths.ROCK));
					StdDraw.picture(getRocher().get(index).getPosition().getX(), getRocher().get(index).getPosition().getY(), 
							getRocher().get(index).getImagePath(), getRocher().get(index).getSize().getX(), getRocher().get(index).getSize().getY());
					index++;
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
			return !this.collisionRocherHaut(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(), getMur().get(44).getSize())
					&& !this.collisionRocherHaut(position, size));
		}
			
	}
	@Override
	protected boolean collisionBas(Vector2 position, Vector2 size)
	{
		if (this.isPorteBas())
		{
			return !this.collisionRocherBas(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(36).getPosition(), getMur().get(36).getSize())
					&& !this.collisionRocherBas(position, size));
		}
		
	}
	@Override
	protected boolean collisionGauche(Vector2 position, Vector2 size)
	{
		if (this.isPorteGauche())
		{
			return !this.collisionRocherGauche(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(), getMur().get(4).getSize())
					&& !this.collisionRocherGauche(position, size));
		}
		
	}
	@Override
	protected boolean collisionDroite(Vector2 position, Vector2 size)
	{
		if (this.isPorteDroite())
		{
			return !this.collisionRocherDroite(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(), getMur().get(76).getSize())
					&& !this.collisionRocherDroite(position, size));
		}
	}
	
	

	@Override
	protected void collisionSpiderWebThisMap(Vector2 position, Vector2 size) {
		
	}

	@Override
	protected Rocher collisionRocherThisMap(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getRocher().get(0).getPosition(), getRocher().get(0).getSize()))
		{
			return getRocher().get(0);
		}
		if (Physics.rectangleCollision(position, size, getRocher().get(1).getPosition(), getRocher().get(1).getSize()))
		{
			return getRocher().get(1);
		}
		if (Physics.rectangleCollision(position, size, getRocher().get(2).getPosition(), getRocher().get(2).getSize()))
		{
			return getRocher().get(2);
		}
		if (Physics.rectangleCollision(position, size, getRocher().get(3).getPosition(), getRocher().get(3).getSize()))
		{
			return getRocher().get(3);
		}
		return new Rocher(new Vector2(-1.0,-1.0), new Vector2(0.1,0.1), ImagePaths.ROCK);
	}
	
	private void collisionDrop()
	{
		if (roomFinished())
		{
			if(Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), drop.getPosition(), drop.getSize()))
			{
				if (drop.getCle().equals("Bomb"))
				{
					getHero().setNbBomb(getHero().getNbBomb()+1);
					drop.setPosition(new Vector2(-1.0,-1.0));
				}
				if (drop.getCle().equals("BloodOfTheMartyr"))
				{
					getHero().setDegats(getHero().getDegats()+1);
					getHero().remplirLarme();
					drop.setPosition(new Vector2(-1.0,-1.0));
				}
				if (drop.getCle().equals("Coin"))
				{
					getHero().setCoin(getHero().getCoin()+1);
					drop.setPosition(new Vector2(-1.0,-1.0));
				}
				if (drop.getCle().equals("Dime"))
				{
					getHero().setCoin(getHero().getCoin()+5);
					drop.setPosition(new Vector2(-1.0,-1.0));
				}
				if (drop.getCle().equals("Nickel"))
				{
					getHero().setCoin(getHero().getCoin()+10);
					drop.setPosition(new Vector2(-1.0,-1.0));
				}
				if (drop.getCle().equals("HalfHpUp"))
				{
					if (getHero().getLife() < 6)
					{
						getHero().setLife(getHero().getLife()+1);
						drop.setPosition(new Vector2(-1.0,-1.0));
					}
				}
				if (drop.getCle().equals("HpUp"))
				{
					if (getHero().getLife() < 5)
					{
						getHero().setLife(getHero().getLife()+2);
						drop.setPosition(new Vector2(-1.0,-1.0));
					}
					else if (getHero().getLife() == 5)
					{
						getHero().setLife(getHero().getLife()+1);
						drop.setPosition(new Vector2(-1.0,-1.0));
					}
				}
				if (drop.getCle().equals("BlackHeart"))
				{
					if (getHero().getBouclier() < 2)
					{
						getHero().setBouclier(2);
						drop.setPosition(new Vector2(-1.0,-1.0));
					}	
				}
				if (drop.getCle().equals("SpeedUp"))
				{
					getHero().setSpeed(getHero().getSpeed() + 0.005);
					drop.setPosition(new Vector2(-1.0,-1.0));
				}
			}
		}
	}
	
	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	
	@Override
	public boolean roomFinished()
	{
		if ((getMouche().get(0).getLife() <= 0)
			&& (getMouche().get(1).getLife() <= 0)
			&& (getMouche().get(2).getLife() <= 0)
			&& (getMouche().get(3).getLife() <= 0))
		{
			
			this.isFinish = true;;
		}
		return this.isFinish;
	}
	
	public void MonstreMove(Fly test)
	{
		test.avance(getHero().getPosition(), this.CollisionMurHaut(test.getPosition(), test.getSize())
				, this.CollisionMurBas(test.getPosition(), test.getSize())
				, this.CollisionMurGauche(test.getPosition(), test.getSize())
				, this.CollisionMurDroite(test.getPosition(), test.getSize()));
	}
	
	public void CreationList()
	{
		mouche = new ArrayList<Fly>();
		for (int i = 0; i < 4; i++)
		{
			Vector2 tempon = CreatVectorAleatoire();
			if (true)
			{
				mouche.add(i, new Fly(tempon, FlyInfos.FLY_SIZE, ImagePaths.FLY, FlyInfos.FLY_LIFE, FlyInfos.FLY_SPEED, FlyInfos.FLY_DEGATCC, FlyInfos.FLY_DEGAT_PROJ));
			}
		}
	}
	
	public void attaqueDesMonstre()
	{
		if ((Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getMouche().get(0).getPosition(), getMouche().get(0).getSize()))
			|| Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getMouche().get(1).getPosition(), getMouche().get(1).getSize())
			|| Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getMouche().get(2).getPosition(), getMouche().get(2).getSize())
			|| Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getMouche().get(3).getPosition(), getMouche().get(3).getSize()))
		{
			if (this.cycle > this.tempsPause)
			{
				if (getHero().getBouclier() > 0)
				{
					getHero().setBouclier(getHero().getBouclier() - getMouche().get(0).getDegatsCC());
				}
				else
				{
					getHero().setLife(getHero().getLife() - getMouche().get(0).getDegatsCC());
				}
				this.cycle = 0;
			}
		}
		this.cycle++;
	}
	
	@Override
	public boolean attaqueDuHero(Projectile p)
	{
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getMouche().get(0).getPosition(), getMouche().get(0).getSize()))
		{
			getMouche().get(0).setLife(getMouche().get(0).getLife() - p.getDegats());
			return true;
		}
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getMouche().get(1).getPosition(), getMouche().get(1).getSize()))
		{
			getMouche().get(1).setLife(getMouche().get(1).getLife() - p.getDegats());
			return true;
		}
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getMouche().get(2).getPosition(), getMouche().get(2).getSize()))
		{
			getMouche().get(2).setLife(getMouche().get(2).getLife() - p.getDegats());
			return true;
		}
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getMouche().get(3).getPosition(), getMouche().get(3).getSize()))
		{
			getMouche().get(3).setLife(getMouche().get(3).getLife() - p.getDegats());
			return true;
		}
		return false;
	}
	@Override
	public void killAllMonster()
	{
		getMouche().get(0).setLife(0);
		getMouche().get(1).setLife(0);
		getMouche().get(2).setLife(0);
		getMouche().get(3).setLife(0);
	}

	/*
	 * *************************************************************************************
	 * PROJECTILE MONSTRE
	 * *************************************************************************************
	 */
	private void tirMouche()
	{
		processMonsterForShoot(getMouche().get(0));
		processMonsterForShoot(getMouche().get(1));
		processMonsterForShoot(getMouche().get(2));
		processMonsterForShoot(getMouche().get(3));
	}
	/**
	 * Permet de tire les larmes en appuyant sur les touches 
	 */
	private void processMonsterForShoot(Fly test)
	{
		if (test.getCycle() > 50)
		{
			//TIR VERS LE HAUT
			if ((test.gety() > test.getx()) && (test.gety() > 0))
			{
				int index = test.demandeTir();
				if (index != -1)
				{
					test.getCrotte().get(index).setTirer(true);
					test.getCrotte().get(index).setPosition(test.getPosition());
					test.getCrotte().get(index).setHaut(true);
					test.getCrotte().get(index).setPositionPortee(test.getPosition().addVector(test.getCrotte().get(index).getPortee()));
				}
				test.setCycle(0);
			}
			//TIR VERS LE BAS
			if ((test.gety() < test.getx()) && (test.gety() < 0))
			{
				int index = test.demandeTir();
				if (index != -1)
				{
					test.getCrotte().get(index).setTirer(true);
					test.getCrotte().get(index).setPosition(test.getPosition());
					test.getCrotte().get(index).setBas(true);
					test.getCrotte().get(index).setPositionPortee(test.getPosition().subVector(test.getCrotte().get(index).getPortee()));
				}
				test.setCycle(0);
			}
			//TIR VERS LA DROITE
			if ((test.gety() < test.getx()) && (test.getx() > 0))
			{		
				int index = test.demandeTir();
				if (index != -1)
				{
					test.getCrotte().get(index).setTirer(true);
					test.getCrotte().get(index).setPosition(test.getPosition());
					test.getCrotte().get(index).setDroite(true);
					test.getCrotte().get(index).setPositionPortee(test.getPosition().addVector(test.getCrotte().get(index).getPortee()));
				}
				test.setCycle(0);
			}
			//TIR VERS LA GAUCHE
			if ((test.gety() > test.getx()) && (test.getx() < 0))
			{	
				int index = test.demandeTir();
				if (index != -1)
				{
					test.getCrotte().get(index).setTirer(true);
					test.getCrotte().get(index).setPosition(test.getPosition());
					test.getCrotte().get(index).setGauche(true);
					test.getCrotte().get(index).setPositionPortee(test.getPosition().subVector(test.getCrotte().get(index).getPortee()));
				}
				test.setCycle(0);
			}
		}
	}
	
	private void ProjMonstreCollision(int index, Fly test)
	{
		if (!CollisionMurHaut(test.getCrotte().get(index).getPosition(), test.getCrotte().get(index).getSize())
			|| !CollisionMurBas(test.getCrotte().get(index).getPosition(), test.getCrotte().get(index).getSize())
			|| !CollisionMurGauche(test.getCrotte().get(index).getPosition(), test.getCrotte().get(index).getSize())
			|| !CollisionMurDroite(test.getCrotte().get(index).getPosition(), test.getCrotte().get(index).getSize())
			|| Physics.rectangleCollision(test.getCrotte().get(index).getPosition(), test.getCrotte().get(index).getSize(), getHero().getPosition(), getHero().getSize()))
		{
			if (Physics.rectangleCollision(test.getCrotte().get(index).getPosition(), test.getCrotte().get(index).getSize(), getHero().getPosition(), getHero().getSize())
				&& test.getCrotte().get(index).isTirer())
			{
				if (getHero().getBouclier() > 0)
				{
					getHero().setBouclier(getHero().getBouclier() - test.getCrotte().get(index).getDegats());
				}
				else
				{
					getHero().setLife(getHero().getLife() - test.getCrotte().get(index).getDegats());
				}	
			}
			test.getCrotte().get(index).setTirer(false);
		}
	}
	
	private void updateProjMonstre()
	{
		ProjMonstreCollision(0, getMouche().get(0));
		ProjMonstreCollision(0, getMouche().get(1));
		ProjMonstreCollision(0, getMouche().get(2));
		ProjMonstreCollision(0, getMouche().get(3));
		
		ProjMonstreCollision(1, getMouche().get(0));
		ProjMonstreCollision(1, getMouche().get(1));
		ProjMonstreCollision(1, getMouche().get(2));
		ProjMonstreCollision(1, getMouche().get(3));
		
		ProjMonstreCollision(2, getMouche().get(0));
		ProjMonstreCollision(2, getMouche().get(1));
		ProjMonstreCollision(2, getMouche().get(2));
		ProjMonstreCollision(2, getMouche().get(3));
		
		ProjMonstreCollision(3, getMouche().get(0));
		ProjMonstreCollision(3, getMouche().get(1));
		ProjMonstreCollision(3, getMouche().get(2));
		ProjMonstreCollision(3, getMouche().get(3));
		
		ProjMonstreCollision(4, getMouche().get(0));
		ProjMonstreCollision(4, getMouche().get(1));
		ProjMonstreCollision(4, getMouche().get(2));
		ProjMonstreCollision(4, getMouche().get(3));
		
	}

	
	
	/*
	 * *************************************************************************************
	 * GETTERS AND SETTERS
	 * *************************************************************************************
	 */
	
	public List<Fly> getMouche() {
		return mouche;
	}
	public void setMouche(List<Fly> mouche) {
		this.mouche = mouche;
	}
	public boolean isFinish() {
		return isFinish;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
	
}
