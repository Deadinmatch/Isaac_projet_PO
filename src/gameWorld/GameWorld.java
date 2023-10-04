package gameWorld;

import libraries.Vector2;

import gameobjects.Hero;
import libraries.Physics;
import libraries.StdDraw;
import resources.Controls;
import resources.ImagePaths;

public class GameWorld
{
	private Room currentRoom;
	private Hero hero;	
	private int cycle;
	private RoomSpawn spawn;
	private RoomSpider room2;
	private roomShop shop;
	private RoomFly room3;
	private RoomBoss boss;
	private int compteurTricheCoin;
	
	
	// A world needs a hero
	public GameWorld(Hero hero)
	{
		this.hero = hero;
		spawn = new RoomSpawn(hero, false, false, false, true);
		room2 = new RoomSpider(hero, true, true, false, true);
		shop = new roomShop(hero, false, false, true, false);
		room3 = new RoomFly(hero, true, true, false, false);
		boss = new RoomBoss(hero, false, false, true, false);
		currentRoom = spawn;
	}
	
	public void processUserInput()
	{
		processKeysForMovement();
		processKeysForShoot();
		processKeysForCheat();
	}

	public boolean gameWin()
	{
		if (boss.isFinish())
		{
			return true;
		}
		return false;
	}
	public boolean gameOver()
	{
		if (hero.getLife() <= 0)
		{
			return true;
		}
		return false;
	}
	
	public void finalScreen()
	{
		if (hero.getLife() <= 0)
		{
			StdDraw.picture(0.5, 0.5, ImagePaths.LOSE_SCREEN, 1.0, 1.0);
		}	
	}

	public void updateGameObjects()
	{
		currentRoom.updateRoom();
		currentRoom.updateEntity();
		this.cycle++;
		this.compteurTricheCoin++;
		updateProcessForShoot();
	}

	public void drawGameObjects()
	{
		currentRoom.drawAll();
		Map(hero.getPosition(), hero.getSize());
	}
	
	/*
	 * *************************************************************************************
	 * CONTROLS MOVE & CHEAT
	 * *************************************************************************************
	 */

	/**
	 * Keys processing for movement
	 */
	private void processKeysForMovement()
	{
		if (StdDraw.isKeyPressed(Controls.goUp) && currentRoom.CollisionMurHaut(hero.getPosition(), hero.getSize()) && currentRoom.collisionHaut(hero.getPosition(), hero.getSize()) )
			hero.goUpNext();
		
		if (StdDraw.isKeyPressed(Controls.goDown) && currentRoom.CollisionMurBas(hero.getPosition(), hero.getSize()) && currentRoom.collisionBas(hero.getPosition(), hero.getSize()) )
			hero.goDownNext();
		
		if (StdDraw.isKeyPressed(Controls.goRight) 	&& currentRoom.CollisionMurDroite(hero.getPosition(), hero.getSize()) && currentRoom.collisionDroite(hero.getPosition(), hero.getSize()) )
			hero.goRightNext();
		
		if (StdDraw.isKeyPressed(Controls.goLeft) && currentRoom.CollisionMurGauche(hero.getPosition(), hero.getSize()) && currentRoom.collisionGauche(hero.getPosition(), hero.getSize()) )
			hero.goLeftNext();
	}
	
	/**
	 * Keys processing for cheat
	 */
	private void processKeysForCheat()
	{
		if (StdDraw.isKeyPressed(Controls.Invincible))
		{
			hero.setLife(999999);
		}
		if (StdDraw.isKeyPressed(Controls.SpeedUp))
		{
			hero.setSpeed(0.025);
			if (currentRoom == room2)
			{
				room2.setSpeedTmp(0.025);
			}
		}
		if (StdDraw.isKeyPressed(Controls.Puissance))
		{
			hero.setDegats(50);
			hero.remplirLarme();
		}
		if (StdDraw.isKeyPressed(Controls.KillAllMonster))
		{
			currentRoom.killAllMonster();
		}
		if (StdDraw.isKeyPressed(Controls.Money))
		{
			if (this.compteurTricheCoin > 35)
			{
				hero.setCoin(hero.getCoin() + 10);
				this.compteurTricheCoin = 0;
			}	
		}
	}
	
	/*
	 * *************************************************************************************
	 * GENERATION MAP
	 * *************************************************************************************
	 */
	private void Map(Vector2 position, Vector2 size)
	{
		if (currentRoom.roomFinished())
		{
			//porte de gauche
			if (Physics.rectangleCollision(position, size, currentRoom.getDoorOpen().get(0).getPosition(), currentRoom.getDoorOpen().get(0).getSize())
					&& StdDraw.isKeyPressed(Controls.goLeft))
			{
				if (currentRoom == room2)
				{
					hero.setPosition(new Vector2(0.85,0.5));
					currentRoom = spawn;
				}
				else if (currentRoom == room3)
				{
					hero.setPosition(new Vector2(0.85,0.5));
					currentRoom = room2;
				}
			}
			//porte du bas
			else if (Physics.rectangleCollision(position, size, currentRoom.getDoorOpen().get(1).getPosition(), currentRoom.getDoorOpen().get(1).getSize())
					&& StdDraw.isKeyPressed(Controls.goDown))
			{
				if (currentRoom == room2)
				{
					hero.setPosition(new Vector2(0.5,0.85));
					shop.drawAll();
					currentRoom = shop;
				}
				else if (currentRoom == room3)
				{
					hero.setPosition(new Vector2(0.5,0.85));
					if(!boss.isFinish())
					{
						boss.CreationList();
						boss.updateEntity();
						boss.drawAll();
					}
					currentRoom = boss;
				}
			}
			//porte du haut
			else if (Physics.rectangleCollision(position, size, currentRoom.getDoorOpen().get(2).getPosition(), currentRoom.getDoorOpen().get(2).getSize())
					&& StdDraw.isKeyPressed(Controls.goUp))
			{
				if (currentRoom == shop)
				{
					hero.setPosition(new Vector2(0.5,0.15));
					currentRoom = room2;
				}
				else if (currentRoom == boss)
				{
					hero.setPosition(new Vector2(0.5,0.15));
					currentRoom = room3;
				}
			}
			//porte de droite
			else if(Physics.rectangleCollision(position, size, currentRoom.getDoorOpen().get(3).getPosition(), currentRoom.getDoorOpen().get(3).getSize())
					&& StdDraw.isKeyPressed(Controls.goRight))
			{
				if (currentRoom == spawn)
				{
					hero.setPosition(new Vector2(0.15,0.5));
					if (!room2.isFinish())
					{
						room2.setSpeedTmp(hero.getSpeed());
						room2.CreationList();
						room2.updateEntity();
						room2.drawAll();
					}
					currentRoom = room2;
				}
				else if(currentRoom == room2)
				{
					hero.setPosition(new Vector2(0.15,0.5));
					if (!room3.isFinish())
					{
						room3.CreationList();
						room3.updateEntity();
						room3.drawAll();
					}
					currentRoom = room3;
				}
			}
		}
	}

	/*
	 * *************************************************************************************
	 * PROJECTILE
	 * *************************************************************************************
	 */
	
	/**
	 * Permet de tire les larmes en appuyant sur les touches 
	 */
	private void processKeysForShoot()
	{
		if (cycle > 20)
		{
			if (StdDraw.isKeyPressed(Controls.shootUp))
			{
				int index = hero.demandeTir();
				if (index != -1)
				{
					hero.getLarme().get(index).setTirer(true);
					hero.getLarme().get(index).setPosition(hero.getPosition());
					hero.getLarme().get(index).setHaut(true);
					hero.getLarme().get(index).setPositionPortee(hero.getPosition().addVector(hero.getLarme().get(index).getPortee()));
				}
				cycle = 0;
			}
			
			if (StdDraw.isKeyPressed(Controls.shootDown))
			{
				int index = hero.demandeTir();
				if (index != -1)
				{
					hero.getLarme().get(index).setTirer(true);
					hero.getLarme().get(index).setPosition(hero.getPosition());
					hero.getLarme().get(index).setBas(true);
					hero.getLarme().get(index).setPositionPortee(hero.getPosition().subVector(hero.getLarme().get(index).getPortee()));
				}
				cycle = 0;
			}
			
			if (StdDraw.isKeyPressed(Controls.shootRight))
			{		
				int index = hero.demandeTir();
				if (index != -1)
				{
					hero.getLarme().get(index).setTirer(true);
					hero.getLarme().get(index).setPosition(hero.getPosition());
					hero.getLarme().get(index).setDroite(true);
					hero.getLarme().get(index).setPositionPortee(hero.getPosition().addVector(hero.getLarme().get(index).getPortee()));
				}
				cycle = 0;
			}
			
			if (StdDraw.isKeyPressed(Controls.shootLeft))
			{	
				int index = hero.demandeTir();
				if (index != -1)
				{
					hero.getLarme().get(index).setTirer(true);
					hero.getLarme().get(index).setPosition(hero.getPosition());
					hero.getLarme().get(index).setGauche(true);
					hero.getLarme().get(index).setPositionPortee(hero.getPosition().subVector(hero.getLarme().get(index).getPortee()));
				}
				cycle = 0;
			}
		}
	}
	
	/**
	 * Appel les fonctions pour lancer le projectile
	 * @param index
	 */
	private void processForShoot(int index)
	{
		if (hero.getLarme().get(index).isTirer())
		{
			if(hero.getLarme().get(index).isDroite())
			{
				hero.shootRightNext(index);
				if(currentRoom.attaqueDuHero(hero.getLarme().get(index)) 
					|| !hero.getLarme().get(index).portee(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getPositionPortee())
					|| !currentRoom.CollisionMurDroite(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize())
					|| (!currentRoom.collisionDroite(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize())  && currentRoom != room3))
				{
					
					hero.getLarme().get(index).setTirer(false);
				}
			}
			
			else if(hero.getLarme().get(index).isGauche())
			{
				hero.shootLeftNext(index);
				if(currentRoom.attaqueDuHero(hero.getLarme().get(index))
					|| !hero.getLarme().get(index).portee2(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getPositionPortee())
					|| (!currentRoom.collisionGauche(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize())  && currentRoom != room3)
					|| !currentRoom.CollisionMurGauche(hero.getLarme().get(index).getPosition(),hero.getLarme().get(index).getSize())  )
				{
					hero.getLarme().get(index).setTirer(false);
				}
			}
			
			else if(hero.getLarme().get(index).isBas())
			{
				hero.shootDownNext(index);
				if (currentRoom.attaqueDuHero(hero.getLarme().get(index))
					|| !hero.getLarme().get(index).portee2(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getPositionPortee())
					|| (!currentRoom.collisionBas(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize())  && currentRoom != room3)
					|| !currentRoom.CollisionMurBas(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize())  )
				{
					hero.getLarme().get(index).setTirer(false);
				}
			}
			
			else if(hero.getLarme().get(index).isHaut())
			{
				hero.shootUpNext(index);
				if (currentRoom.attaqueDuHero(hero.getLarme().get(index)) 
					|| !hero.getLarme().get(index).portee(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getPositionPortee())
					|| (!currentRoom.collisionHaut(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize())  && currentRoom != room3)
					|| !currentRoom.CollisionMurHaut(hero.getLarme().get(index).getPosition(), hero.getLarme().get(index).getSize()))
				{
					hero.getLarme().get(index).setTirer(false);
				}
			}
		}	
	}
	
	/**
	 * Mise a du processus de tire pour chaque projectile
	 */
	private void updateProcessForShoot()
	{
		processForShoot(0);
		processForShoot(1);
		processForShoot(2);
		processForShoot(3);
		processForShoot(4);
	}
	
}
