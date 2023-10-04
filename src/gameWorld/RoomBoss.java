package gameWorld;

import java.util.ArrayList;
import gameobjects.*;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.BossInfos;
import resources.ImagePaths;
import resources.RoomInfos;


public class RoomBoss extends Room {


	private Boss bigBoss;
	private int cycle;
	private final int tempsPause = 30;
	private int tempsTir = 50;
	private boolean isFinish;

	public RoomBoss(Hero hero, boolean gauche, boolean bas, boolean haut, boolean droite) {
		super(hero);
		setPorteGauche(gauche);
		setPorteBas(bas);
		setPorteHaut(haut);
		setPorteDroite(droite);
	} 
	
	public void updateEntity()
	{
		getBigBoss().updateGameObject();
		attaqueDuBoss();
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
		drawMonster();
	}

	public void drawMonster(){
		getBigBoss().drawGameObject();
		MonsterMove();
		getBigBoss().setCycle(getBigBoss().getCycle()+1);
		tirCailloux();
		updateProjMonstre();
	}

	public void MonsterMove()
	{
		getBigBoss().avanceCibleAndAlea(getHero().getPosition(),(this.CollisionMurHaut(getBigBoss().getPosition(),getBigBoss().getSize()) && this.collisionHautMonstre(getBigBoss().getPosition(),getBigBoss().getSize()))
				, (this.CollisionMurBas(getBigBoss().getPosition(), getBigBoss().getSize()) && this.collisionBasMonstre(getBigBoss().getPosition(), getBigBoss().getSize()))
				, (this.CollisionMurGauche(getBigBoss().getPosition(), getBigBoss().getSize()) && this.collisionGaucheMonstre(getBigBoss().getPosition(), getBigBoss().getSize()))
				, (this.CollisionMurDroite(getBigBoss().getPosition(), getBigBoss().getSize()) && this.collisionDroiteMonstre(getBigBoss().getPosition(), getBigBoss().getSize())));

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
			return !this.collisionMonstreHaut(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(), getMur().get(44).getSize())
					&& !this.collisionMonstreHaut(position, size));
		}

	}
	@Override
	protected boolean collisionBas(Vector2 position, Vector2 size)
	{
		if (this.isPorteBas())
		{
			return !this.collisionMonstreBas(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(36).getPosition(), getMur().get(36).getSize())
					&& !this.collisionMonstreBas(position, size));
		}

	}
	@Override
	protected boolean collisionGauche(Vector2 position, Vector2 size)
	{
		if (this.isPorteGauche())
		{
			return !this.collisionMonstreGauche(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(), getMur().get(4).getSize())
					&& !this.collisionMonstreGauche(position, size));
		}

	}
	@Override
	protected boolean collisionDroite(Vector2 position, Vector2 size)
	{
		if (this.isPorteDroite())
		{
			return !this.collisionMonstreDroite(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(), getMur().get(76).getSize())
					&& !this.collisionMonstreDroite(position, size));
		}
	}

	private boolean collisionHautMonstre(Vector2 position, Vector2 size)
	{
		if (this.isPorteHaut())
		{
			return !this.collisionDesMonstreHautAvecHero(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(), getMur().get(44).getSize())
					&& !this.collisionDesMonstreHautAvecHero(position, size));
		}

	}
	private boolean collisionBasMonstre(Vector2 position, Vector2 size)
	{
		if (this.isPorteBas())
		{
			return !this.collisionDesMonstreBasAvecHero(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(36).getPosition(), getMur().get(36).getSize())
					&& !this.collisionDesMonstreBasAvecHero(position, size));
		}

	}
	private boolean collisionGaucheMonstre(Vector2 position, Vector2 size)
	{
		if (this.isPorteGauche())
		{
			return  !this.collisionDesMonstreGaucheAvecHero(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(), getMur().get(4).getSize())
					&& !this.collisionDesMonstreGaucheAvecHero(position, size));
		}

	}
	private boolean collisionDroiteMonstre(Vector2 position, Vector2 size)
	{
		if (this.isPorteDroite())
		{
			return !this.collisionDesMonstreDroiteAvecHero(position, size);
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(), getMur().get(76).getSize())
					&& !this.collisionDesMonstreDroiteAvecHero(position, size));
		}
	}

	private boolean collisionMonstreHaut(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getBigBoss().getPosition(), getBigBoss().getSize()))
		{
			double y = position.getY() - getBigBoss().getPosition().getY();
			if (y < 0)
			{
				return true;
			}
		}

		return false;
	}
	protected boolean collisionMonstreBas(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getBigBoss().getPosition(), getBigBoss().getSize()))
		{
			double y = position.getY() - getBigBoss().getPosition().getY();
			if (y > 0)
			{
				return true;
			}
		}
		return false;
	}
	protected boolean collisionMonstreGauche(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getBigBoss().getPosition(), getBigBoss().getSize()))
		{
			double x = position.getX() - getBigBoss().getPosition().getY();
			if (x > 0)
			{
				return true;
			}
		}
		return false;
	}
	protected boolean collisionMonstreDroite(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getBigBoss().getPosition(), getBigBoss().getSize()))
		{
			double x = position.getX() - getBigBoss().getPosition().getY();
			if (x < 0)
			{
				return true;
			}
		}
		return false;
	}

	private boolean collisionDesMonstreHautAvecHero(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize()))
		{
			double y = position.getY() - getHero().getPosition().getX();
			if (y < 0)
			{
				return true;
			}
		}
		return false;
	}
	protected boolean collisionDesMonstreBasAvecHero(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize()))
		{
			double y = position.getY() - getHero().getPosition().getX();
			if (y > 0)
			{
				return true;
			}
		}
		return false;
	}
	protected boolean collisionDesMonstreGaucheAvecHero(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize()))
		{
			double x = position.getX() - getHero().getPosition().getX();
			if (x > 0)
			{
				return true;
			}
		}
		return false;
	}
	protected boolean collisionDesMonstreDroiteAvecHero(Vector2 position, Vector2 size)
	{
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize()))
		{
			double x = position.getX() - getHero().getPosition().getX();
			if (x < 0)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected void collisionSpiderWebThisMap(Vector2 position, Vector2 size) {	
	}

	@Override
	protected Rocher collisionRocherThisMap(Vector2 position, Vector2 size) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * *************************************************************************************
	 * AUTRE METHODE
	 * *************************************************************************************
	 */
	@Override
	public boolean roomFinished()
	{
		if ( getBigBoss().getLife() <= 0)
		{
			StdDraw.picture(0.5, 0.5, ImagePaths.WIN_SCREEN, 1.0, 1.0);
			this.isFinish = true;
		}
		return this.isFinish;
	}

	public void attaqueDuBoss() 
	{
		//dgetBigBoss().get(0).setSpeed(HeroInfos.ISAAC_SPEED*2);
		if ((Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getBigBoss().getPosition(), getBigBoss().getSize())))
		{
			if (this.cycle > this.tempsPause)
			{
				if (getHero().getBouclier() > 0)
				{
					getHero().setBouclier(getHero().getBouclier() - getBigBoss().getDegatsCC());
				}
				else
				{
					getHero().setLife(getHero().getLife() - getBigBoss().getDegatsCC());
				}
				this.cycle = 0;
			}
		}
		this.cycle++;
	}

	@Override
	public boolean attaqueDuHero(Projectile p)
	{
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getBigBoss().getPosition(), getBigBoss().getSize()))
		{
			getBigBoss().setLife(getBigBoss().getLife() - p.getDegats());
			getBigBoss().setSpeed(BossInfos.BOSS_SPEED+=0.0005);
			//this.tempsTir = this.tempsTir - 2;
			getBigBoss().setDegatsCC(BossInfos.BOSS_DEGATS+=1);
			
			return true;
		}
		return false;
	}

	public void CreationList()
	{
		Vector2 tmp = CreatVectorAleatoire();
		bigBoss = new Boss(tmp,BossInfos.BOSS_SIZE,ImagePaths.BOSS,BossInfos.BOSS_LIFE,BossInfos.BOSS_SPEED,BossInfos.BOSS_DEGATS,BossInfos.BOSS_DEGATS_PROJ);
	}
	
	@Override
	public void killAllMonster()
	{
		getBigBoss().setLife(0);
	}


	/*
	 * *************************************************************************************
	 * PROJECTILE
	 * *************************************************************************************
	 */
	public void tirCailloux()
	{
		processMonsterForShoot();
	}
	private void processMonsterForShoot()
	{
		if (getBigBoss().getCycleProj() > this.tempsTir)
		{
			//TIR VERS LE HAUT
			if ((getBigBoss().gety() > getBigBoss().getx()) && (getBigBoss().gety() > 0))
			{
				int index = getBigBoss().demandeTir();
				if (index != -1)
				{
					getBigBoss().getStoneProj().get(index).setTirer(true);
					getBigBoss().getStoneProj().get(index).setPosition(getBigBoss().getPosition());
					getBigBoss().getStoneProj().get(index).setHaut(true);
					getBigBoss().getStoneProj().get(index).setPositionPortee(getBigBoss().getPosition().addVector(getBigBoss().getStoneProj().get(index).getPortee()));
				}

				getBigBoss().setCycleProj(0);
			}
			//TIR VERS LE BAS
			if ((getBigBoss().gety() < getBigBoss().getx()) && (getBigBoss().gety() < 0))
			{
				int index = getBigBoss().demandeTir();
				if (index != -1)
				{
					getBigBoss().getStoneProj().get(index).setTirer(true);
					getBigBoss().getStoneProj().get(index).setPosition(getBigBoss().getPosition());
					getBigBoss().getStoneProj().get(index).setBas(true);
					getBigBoss().getStoneProj().get(index).setPositionPortee(getBigBoss().getPosition().subVector(getBigBoss().getStoneProj().get(index).getPortee()));
				}
				getBigBoss().setCycleProj(0);
			}
			//TIR VERS LA DROITE
			if ((getBigBoss().gety() < getBigBoss().getx()) && (getBigBoss().getx() > 0))
			{
				int index = getBigBoss().demandeTir();
				if (index != -1)
				{
					getBigBoss().getStoneProj().get(index).setTirer(true);
					getBigBoss().getStoneProj().get(index).setPosition(getBigBoss().getPosition());
					getBigBoss().getStoneProj().get(index).setDroite(true);
					getBigBoss().getStoneProj().get(index).setPositionPortee(getBigBoss().getPosition().addVector(getBigBoss().getStoneProj().get(index).getPortee()));
				}
				getBigBoss().setCycleProj(0);
			}
			//TIR VERS LA GAUCHE
			if ((getBigBoss().gety() > getBigBoss().getx()) && (getBigBoss().getx() < 0))
			{
				int index = getBigBoss().demandeTir();
				if (index != -1)
				{
					getBigBoss().getStoneProj().get(index).setTirer(true);
					getBigBoss().getStoneProj().get(index).setPosition(getBigBoss().getPosition());
					getBigBoss().getStoneProj().get(index).setGauche(true);
					getBigBoss().getStoneProj().get(index).setPositionPortee(getBigBoss().getPosition().subVector(getBigBoss().getStoneProj().get(index).getPortee()));
				}
				getBigBoss().setCycleProj(0);
			}
		}
	}

	private void ProjMonstreCollision(int index)
	{
		if (!CollisionMurHaut(getBigBoss().getStoneProj().get(index).getPosition(), getBigBoss().getStoneProj().get(index).getSize())
				|| !CollisionMurBas(getBigBoss().getStoneProj().get(index).getPosition(), getBigBoss().getStoneProj().get(index).getSize())
				|| !CollisionMurGauche(getBigBoss().getStoneProj().get(index).getPosition(), getBigBoss().getStoneProj().get(index).getSize())
				|| !CollisionMurDroite(getBigBoss().getStoneProj().get(index).getPosition(), getBigBoss().getStoneProj().get(index).getSize())
				|| Physics.rectangleCollision(getBigBoss().getStoneProj().get(index).getPosition(), getBigBoss().getStoneProj().get(index).getSize(), getHero().getPosition(), getHero().getSize()))
		{
			if (Physics.rectangleCollision(getBigBoss().getStoneProj().get(index).getPosition(), getBigBoss().getStoneProj().get(index).getSize(), getHero().getPosition(), getHero().getSize())
					&& getBigBoss().getStoneProj().get(index).isTirer())
			{
				getHero().setLife(getHero().getLife() - getBigBoss().getStoneProj().get(index).getDegats());
			}
			getBigBoss().getStoneProj().get(index).setTirer(false);
		}
	}

	private void updateProjMonstre(){
		ProjMonstreCollision(0);
		ProjMonstreCollision(1);
		ProjMonstreCollision(2);
		ProjMonstreCollision(3);
		ProjMonstreCollision(4);
	}

	/*
	 * *************************************************************************************
	 * GETTERS AND SETTERS
	 * *************************************************************************************
	 */

	public int getTempsPause() {
		return tempsPause;
	}
	public Boss getBigBoss() {
		return bigBoss;
	}
	public void setBigBoss(Boss bigBoss) {
		this.bigBoss = bigBoss;
	}
	public boolean isFinish() {
		return isFinish;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
}
