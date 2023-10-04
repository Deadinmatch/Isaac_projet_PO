package gameWorld;

import java.util.ArrayList;
import gameobjects.Rocher;
import gameobjects.Door;
import gameobjects.Hero;
import gameobjects.Projectile;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.ImagePaths;
import resources.RoomInfos;

public class RoomSpawn extends Room {
	
	

	public RoomSpawn(Hero hero, boolean gauche, boolean bas, boolean haut, boolean droite) {
		super(hero);
		setPorteGauche(gauche);
		setPorteBas(bas);
		setPorteHaut(haut);
		setPorteDroite(droite);
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
			return true;
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(), getMur().get(44).getSize()));
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
			return true;
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(), getMur().get(4).getSize()));
		}
		
	}
	@Override
	protected boolean collisionDroite(Vector2 position, Vector2 size)
	{
		if (this.isPorteDroite())
		{
			return true;
		}
		else
		{
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(), getMur().get(76).getSize()));
		}
	}
	
	@Override
	protected void collisionSpiderWebThisMap(Vector2 position, Vector2 size) {
		
	}

	@Override
	protected Rocher collisionRocherThisMap(Vector2 position, Vector2 size) {
		
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
		return true;
	}

	@Override
	public void updateEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean attaqueDuHero(Projectile p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void killAllMonster() {
		// TODO Auto-generated method stub
		
	}

	

}