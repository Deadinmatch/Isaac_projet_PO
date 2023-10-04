package gameWorld;

import java.util.ArrayList;
import java.util.List;

import gameobjects.SpiderWeb;
import gameobjects.Rocher;
import gameobjects.Consommable;
import gameobjects.Door;
import gameobjects.Hero;
import gameobjects.Projectile;
import gameobjects.Spider;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.ImagePaths;
import resources.RoomInfos;
import resources.SpiderInfos;

public class RoomSpider extends Room {

	// EST CE QUE COLLISION ENTRE LES SPIDER ?

	private List<Spider> spider;
	private int cycle;
	private final int tempsPause = 30;
	private Consommable drop = drawObjectAleatoire(new Vector2(0.5, 0.6));
	private boolean isFinish;
	private double speedTmp;

	public RoomSpider(Hero hero, boolean gauche, boolean bas, boolean haut, boolean droite) {
		super(hero);
		setPorteGauche(gauche);
		setPorteBas(bas);
		setPorteHaut(haut);
		setPorteDroite(droite);
	}

	@Override
	public void updateEntity() {
		getSpider().get(0).updateGameObject();
		getSpider().get(1).updateGameObject();
		getSpider().get(2).updateGameObject();
		attaqueDesMonstre();
		collisionDrop();
	}

	public void MonstreMove(Spider test) {
		test.avance(
				(this.CollisionMurHaut(test.getPosition(), test.getSize())
						&& this.collisionHautMonstre(test.getPosition(), test.getSize())),
				(this.CollisionMurBas(test.getPosition(), test.getSize())
						&& this.collisionBasMonstre(test.getPosition(), test.getSize())),
				(this.CollisionMurGauche(test.getPosition(), test.getSize())
						&& this.collisionGaucheMonstre(test.getPosition(), test.getSize())),
				(this.CollisionMurDroite(test.getPosition(), test.getSize())
						&& this.collisionDroiteMonstre(test.getPosition(), test.getSize())));
	}

	public void AllMonsterMove() {
		MonstreMove(getSpider().get(0));
		MonstreMove(getSpider().get(1));
		MonstreMove(getSpider().get(2));
	}

	/*
	 * *****************************************************************************
	 * ********
	 * DRAW
	 * *****************************************************************************
	 * ********
	 */

	/**
	 * Creation objet rocher &
	 * Drawing Rocher
	 */
	public void drawRocher() {
		setRocher(new ArrayList<Rocher>());
		int index = 0;
		for (int i = 1; i < RoomInfos.NB_TILES - 1; i++) {
			for (int j = 1; j < RoomInfos.NB_TILES - 1; j++) {
				Vector2 position = positionFromTileIndex(i, j);
				if (i == 5 && j == 5) {
					getRocher().add(index, new Rocher(position, new Vector2(0.1, 0.1), ImagePaths.ROCK));
					StdDraw.picture(getRocher().get(index).getPosition().getX(),
							getRocher().get(index).getPosition().getY(),
							getRocher().get(index).getImagePath(), getRocher().get(index).getSize().getX(),
							getRocher().get(index).getSize().getY());
					index++;
				}
			}
		}
		getHero().drawGameObject();
	}

	public void drawSpiderWeb() {
		setSpiderWeb(new ArrayList<SpiderWeb>());
		int index = 0;
		for (int i = 1; i < RoomInfos.NB_TILES - 1; i++) {
			for (int j = 1; j < RoomInfos.NB_TILES - 1; j++) {
				Vector2 position = positionFromTileIndex(i, j);
				if (i == 2 && j == 6) {

					getSpiderWeb().add(index, new SpiderWeb(position, new Vector2(0.1, 0.1), ImagePaths.SPIDER_WEB));
					StdDraw.picture(getSpiderWeb().get(index).getPosition().getX(),
							getSpiderWeb().get(index).getPosition().getY(),
							getSpiderWeb().get(index).getImagePath(), getSpiderWeb().get(index).getSize().getX(),
							getSpiderWeb().get(index).getSize().getY());
					index++;
				}
				if (i == 2 && j == 2) {

					getSpiderWeb().add(index, new SpiderWeb(position, new Vector2(0.1, 0.1), ImagePaths.SPIDER_WEB));
					StdDraw.picture(getSpiderWeb().get(index).getPosition().getX(),
							getSpiderWeb().get(index).getPosition().getY(),
							getSpiderWeb().get(index).getImagePath(), getSpiderWeb().get(index).getSize().getX(),
							getSpiderWeb().get(index).getSize().getY());
					index++;
				}
				if (i == 4 && j == 4) {

					getSpiderWeb().add(index, new SpiderWeb(position, new Vector2(0.1, 0.1), ImagePaths.SPIDER_WEB));
					StdDraw.picture(getSpiderWeb().get(index).getPosition().getX(),
							getSpiderWeb().get(index).getPosition().getY(),
							getSpiderWeb().get(index).getImagePath(), getSpiderWeb().get(index).getSize().getX(),
							getSpiderWeb().get(index).getSize().getY());
					index++;
				}
				if (i == 6 && j == 6) {

					getSpiderWeb().add(index, new SpiderWeb(position, new Vector2(0.1, 0.1), ImagePaths.SPIDER_WEB));
					StdDraw.picture(getSpiderWeb().get(index).getPosition().getX(),
							getSpiderWeb().get(index).getPosition().getY(),
							getSpiderWeb().get(index).getImagePath(), getSpiderWeb().get(index).getSize().getX(),
							getSpiderWeb().get(index).getSize().getY());
					index++;
				}
				if (i == 6 && j == 2) {

					getSpiderWeb().add(index, new SpiderWeb(position, new Vector2(0.1, 0.1), ImagePaths.SPIDER_WEB));
					StdDraw.picture(getSpiderWeb().get(index).getPosition().getX(),
							getSpiderWeb().get(index).getPosition().getY(),
							getSpiderWeb().get(index).getImagePath(), getSpiderWeb().get(index).getSize().getX(),
							getSpiderWeb().get(index).getSize().getY());
					index++;
				}
			}
		}
		getHero().drawGameObject();
	}

	@Override
	public void drawAll() {
		super.drawRoom();
		super.drawWall();
		drawPorte();
		drawRocher();
		drawSpiderWeb();
		drawSpider();
		this.cycle++;
		if (this.isFinish()) {
			StdDraw.picture(drop.getPosition().getX(), drop.getPosition().getY(), drop.getImagePath(),
					drop.getSize().getX(), drop.getSize().getY());
		}
	}

	@Override
	public void drawPorte() {
		setDoorClosed(new ArrayList<Door>());
		setDoorOpen(new ArrayList<Door>());

		// For every tile, set wall picture.
		int index = 0;
		for (int i = 0; i < RoomInfos.NB_TILES; i++) {
			for (int j = 0; j < RoomInfos.NB_TILES; j++) {
				Vector2 position = positionFromTileIndex(i, j);

				if (index == 4 || index == 36 || index == 44 || index == 76) {
					if (roomFinished()) {
						getDoorOpen().add(new Door(position, new Vector2(0.125, 0.125), ImagePaths.OPENED_DOOR));
						getDoorClosed()
								.add(new Door(new Vector2(-1.0, -1.0), new Vector2(0.1, 0.1), ImagePaths.CLOSED_DOOR));
					} else
						getDoorClosed().add(new Door(position, new Vector2(0.125, 0.125), ImagePaths.CLOSED_DOOR));

				}

				// Porte du haut
				if (j == RoomInfos.NB_TILES - 1 && this.isPorteHaut()) {
					if (index == 44 && !roomFinished())
						StdDraw.picture(getDoorClosed().get(2).getPosition().getX(),
								getDoorClosed().get(2).getPosition().getY(), getDoorClosed().get(2).getImagePath(),
								getDoorClosed().get(2).getSize().getX(),
								getDoorClosed().get(2).getSize().getY(), 0.0);
					else if (index == 44 && roomFinished())
						StdDraw.picture(getDoorOpen().get(2).getPosition().getX(),
								getDoorOpen().get(2).getPosition().getY(), getDoorOpen().get(2).getImagePath(),
								getDoorOpen().get(2).getSize().getX(),
								getDoorOpen().get(2).getSize().getY(), 0.0);
				}
				// Porte de droite
				else if (i == RoomInfos.NB_TILES - 1 && this.isPorteDroite()) {
					if (index == 76 && !roomFinished())
						StdDraw.picture(getDoorClosed().get(3).getPosition().getX(),
								getDoorClosed().get(3).getPosition().getY(), getDoorClosed().get(3).getImagePath(),
								getDoorClosed().get(3).getSize().getX(),
								getDoorClosed().get(3).getSize().getY(), -90.0);
					else if (index == 76 && roomFinished())
						StdDraw.picture(getDoorOpen().get(3).getPosition().getX(),
								getDoorOpen().get(3).getPosition().getY(), getDoorOpen().get(3).getImagePath(),
								getDoorOpen().get(3).getSize().getX(),
								getDoorOpen().get(3).getSize().getY(), -90.0);
				}
				// Porte de gauche
				else if (i == 0 && j != 0 && this.isPorteGauche()) {
					if (index == 4 && !roomFinished())
						StdDraw.picture(getDoorClosed().get(0).getPosition().getX(),
								getDoorClosed().get(0).getPosition().getY(), getDoorClosed().get(0).getImagePath(),
								getDoorClosed().get(0).getSize().getX(),
								getDoorClosed().get(0).getSize().getY(), 90.0);
					else if (index == 4 && roomFinished())
						StdDraw.picture(getDoorOpen().get(0).getPosition().getX(),
								getDoorOpen().get(0).getPosition().getY(), getDoorOpen().get(0).getImagePath(),
								getDoorOpen().get(0).getSize().getX(),
								getDoorOpen().get(0).getSize().getY(), 90.0);
				}
				// Porte du bas
				else if (j == 0 && i != 0 && this.isPorteBas()) {
					if (index == 36 && !roomFinished())
						StdDraw.picture(getDoorClosed().get(1).getPosition().getX(),
								getDoorClosed().get(1).getPosition().getY(), getDoorClosed().get(1).getImagePath(),
								getDoorClosed().get(1).getSize().getX(),
								getDoorClosed().get(1).getSize().getY(), 180.0);
					else if (index == 36 && roomFinished())
						StdDraw.picture(getDoorOpen().get(1).getPosition().getX(),
								getDoorOpen().get(1).getPosition().getY(), getDoorOpen().get(1).getImagePath(),
								getDoorOpen().get(1).getSize().getX(),
								getDoorOpen().get(1).getSize().getY(), 180.0);
				}
				index++;
			}
		}
		getHero().drawGameObject();
	}

	public void drawSpider() {
		getSpider().get(0).drawGameSpider();
		getSpider().get(1).drawGameSpider();
		getSpider().get(2).drawGameSpider();
		AllMonsterMove();
	}

	/*
	 * *****************************************************************************
	 * ********
	 * COLLISION
	 * *****************************************************************************
	 * ********
	 */

	@Override
	protected boolean collisionHaut(Vector2 position, Vector2 size) {
		if (this.isPorteHaut()) {
			return (!this.collisionRocherHaut(position, size)
					&& !this.collisionMonstreHaut(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(),
					getMur().get(44).getSize())
					&& !this.collisionRocherHaut(position, size)
					&& !this.collisionMonstreHaut(position, size));
		}
	}

	@Override
	protected boolean collisionBas(Vector2 position, Vector2 size) {
		if (this.isPorteBas()) {
			return (!this.collisionRocherBas(position, size)
					&& !this.collisionMonstreBas(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(36).getPosition(),
					getMur().get(36).getSize())
					&& !this.collisionRocherBas(position, size)
					&& !this.collisionMonstreBas(position, size));
		}
	}

	@Override
	protected boolean collisionGauche(Vector2 position, Vector2 size) {
		if (this.isPorteGauche()) {
			return (!this.collisionRocherGauche(position, size)
					&& !this.collisionMonstreGauche(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(),
					getMur().get(4).getSize())
					&& !this.collisionRocherGauche(position, size)
					&& !this.collisionMonstreGauche(position, size));
		}
	}

	@Override
	protected boolean collisionDroite(Vector2 position, Vector2 size) {
		if (this.isPorteDroite()) {
			return (!this.collisionRocherDroite(position, size)
					&& !this.collisionMonstreDroite(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(),
					getMur().get(76).getSize())
					&& !this.collisionRocherDroite(position, size)
					&& !this.collisionMonstreDroite(position, size));
		}
	}

	private boolean collisionHautMonstre(Vector2 position, Vector2 size) {
		if (this.isPorteHaut()) {
			return (!this.collisionRocherHaut(position, size)
					&& !this.collisionDesMonstreHautAvecHero(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(44).getPosition(),
					getMur().get(44).getSize())
					&& !this.collisionRocherHaut(position, size)
					&& !this.collisionDesMonstreHautAvecHero(position, size));
		}

	}

	private boolean collisionBasMonstre(Vector2 position, Vector2 size) {
		if (this.isPorteBas()) {
			return (!this.collisionRocherBas(position, size)
					&& !this.collisionDesMonstreBasAvecHero(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(36).getPosition(),
					getMur().get(36).getSize())
					&& !this.collisionRocherBas(position, size)
					&& !this.collisionDesMonstreBasAvecHero(position, size));
		}

	}

	private boolean collisionGaucheMonstre(Vector2 position, Vector2 size) {
		if (this.isPorteGauche()) {
			return (!this.collisionRocherGauche(position, size)
					&& !this.collisionDesMonstreGaucheAvecHero(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(4).getPosition(),
					getMur().get(4).getSize())
					&& !this.collisionRocherGauche(position, size)
					&& !this.collisionDesMonstreGaucheAvecHero(position, size));
		}

	}

	private boolean collisionDroiteMonstre(Vector2 position, Vector2 size) {
		if (this.isPorteDroite()) {
			return (!this.collisionRocherDroite(position, size)
					&& !this.collisionDesMonstreDroiteAvecHero(position, size));
		} else {
			return (!Physics.rectangleCollision(position, size, getMur().get(76).getPosition(),
					getMur().get(76).getSize())
					&& !this.collisionRocherDroite(position, size)
					&& !this.collisionDesMonstreDroiteAvecHero(position, size));
		}
	}

	private boolean collisionMonstreHaut(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getSpider().get(0).getPosition(),
				getSpider().get(0).getSize())) {
			double y = position.getY() - getSpider().get(0).getPosition().getY();
			if (y < 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(1).getPosition(),
				getSpider().get(1).getSize())) {
			double y = position.getY() - getSpider().get(1).getPosition().getY();
			if (y < 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(2).getPosition(),
				getSpider().get(2).getSize())) {
			double y = position.getY() - getSpider().get(2).getPosition().getY();
			if (y < 0) {
				return true;
			}
		}
		return false;
	}

	protected boolean collisionMonstreBas(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getSpider().get(0).getPosition(),
				getSpider().get(0).getSize())) {
			double y = position.getY() - getSpider().get(0).getPosition().getY();
			if (y > 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(1).getPosition(),
				getSpider().get(1).getSize())) {
			double y = position.getY() - getSpider().get(1).getPosition().getY();
			if (y > 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(2).getPosition(),
				getSpider().get(2).getSize())) {
			double y = position.getY() - getSpider().get(2).getPosition().getY();
			if (y > 0) {
				return true;
			}
		}
		return false;
	}

	protected boolean collisionMonstreGauche(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getSpider().get(0).getPosition(),
				getSpider().get(0).getSize())) {
			double x = position.getX() - getSpider().get(0).getPosition().getY();
			if (x > 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(1).getPosition(),
				getSpider().get(1).getSize())) {
			double x = position.getX() - getSpider().get(1).getPosition().getY();
			if (x > 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(2).getPosition(),
				getSpider().get(2).getSize())) {
			double x = position.getX() - getSpider().get(2).getPosition().getY();
			if (x > 0) {
				return true;
			}
		}
		return false;
	}

	protected boolean collisionMonstreDroite(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getSpider().get(0).getPosition(),
				getSpider().get(0).getSize())) {
			double x = position.getX() - getSpider().get(0).getPosition().getY();
			if (x < 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(1).getPosition(),
				getSpider().get(1).getSize())) {
			double x = position.getX() - getSpider().get(1).getPosition().getY();
			if (x < 0) {
				return true;
			}
		}
		if (Physics.rectangleCollision(position, size, getSpider().get(2).getPosition(),
				getSpider().get(2).getSize())) {
			double x = position.getX() - getSpider().get(2).getPosition().getY();
			if (x < 0) {
				return true;
			}
		}
		return false;
	}

	private boolean collisionDesMonstreHautAvecHero(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize())) {
			double y = position.getY() - getHero().getPosition().getX();
			if (y < 0) {
				return true;
			}
		}
		return false;
	}

	protected boolean collisionDesMonstreBasAvecHero(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize())) {
			double y = position.getY() - getHero().getPosition().getX();
			if (y > 0) {
				return true;
			}
		}
		return false;
	}

	protected boolean collisionDesMonstreGaucheAvecHero(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize())) {
			double x = position.getX() - getHero().getPosition().getX();
			if (x > 0) {
				return true;
			}
		}
		return false;
	}

	protected boolean collisionDesMonstreDroiteAvecHero(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getHero().getPosition(), getHero().getSize())) {
			double x = position.getX() - getHero().getPosition().getX();
			if (x < 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Rocher collisionRocherThisMap(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getRocher().get(0).getPosition(),
				getRocher().get(0).getSize())) {
			return getRocher().get(0);
		}
		return new Rocher(new Vector2(-1.0, -1.0), new Vector2(0.1, 0.1), ImagePaths.ROCK);
	}

	@Override
	protected void collisionSpiderWebThisMap(Vector2 position, Vector2 size) {
		if (Physics.rectangleCollision(position, size, getSpiderWeb().get(0).getPosition(),
				getSpiderWeb().get(0).getSize())
				|| Physics.rectangleCollision(position, size, getSpiderWeb().get(1).getPosition(),
						getSpiderWeb().get(1).getSize())
				|| Physics.rectangleCollision(position, size, getSpiderWeb().get(2).getPosition(),
						getSpiderWeb().get(2).getSize())
				|| Physics.rectangleCollision(position, size, getSpiderWeb().get(3).getPosition(),
						getSpiderWeb().get(3).getSize())
				|| Physics.rectangleCollision(position, size, getSpiderWeb().get(4).getPosition(),
						getSpiderWeb().get(4).getSize())) {
			getHero().setSpeed(this.speedTmp / 3);
		} else {
			getHero().setSpeed(this.speedTmp);
		}
	}

	private void collisionDrop() {
		if (roomFinished()) {
			if (Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), drop.getPosition(),
					drop.getSize())) {
				if (drop.getCle().equals("Bomb")) {
					getHero().setNbBomb(getHero().getNbBomb() + 1);
					drop.setPosition(new Vector2(-1.0, -1.0));
				}
				if (drop.getCle().equals("BloodOfTheMartyr")) {
					getHero().setDegats(getHero().getDegats() + 1);
					getHero().remplirLarme();
					drop.setPosition(new Vector2(-1.0, -1.0));
				}
				if (drop.getCle().equals("Coin")) {
					getHero().setCoin(getHero().getCoin() + 1);
					drop.setPosition(new Vector2(-1.0, -1.0));
				}
				if (drop.getCle().equals("Dime")) {
					getHero().setCoin(getHero().getCoin() + 5);
					drop.setPosition(new Vector2(-1.0, -1.0));
				}
				if (drop.getCle().equals("Nickel")) {
					getHero().setCoin(getHero().getCoin() + 10);
					drop.setPosition(new Vector2(-1.0, -1.0));
				}
				if (drop.getCle().equals("HalfHpUp")) {
					if (getHero().getLife() < 6) {
						getHero().setLife(getHero().getLife() + 1);
						drop.setPosition(new Vector2(-1.0, -1.0));
					}
				}
				if (drop.getCle().equals("HpUp")) {
					if (getHero().getLife() < 5) {
						getHero().setLife(getHero().getLife() + 2);
						drop.setPosition(new Vector2(-1.0, -1.0));
					} else if (getHero().getLife() == 5) {
						getHero().setLife(getHero().getLife() + 1);
						drop.setPosition(new Vector2(-1.0, -1.0));
					}
				}
				if (drop.getCle().equals("BlackHeart")) {
					if (getHero().getBouclier() < 2) {
						getHero().setBouclier(2);
						drop.setPosition(new Vector2(-1.0, -1.0));
					}
				}
				if (drop.getCle().equals("SpeedUp")) {
					this.speedTmp += 0.005;
					getHero().setSpeed(this.speedTmp);
					drop.setPosition(new Vector2(-1.0, -1.0));
				}
			}
		}
	}

	/*
	 * *****************************************************************************
	 * ********
	 * AUTRE METHODE
	 * *****************************************************************************
	 * ********
	 */

	@Override
	public boolean roomFinished() {
		if ((getSpider().get(0).getLife() <= 0)
				&& (getSpider().get(1).getLife() <= 0)
				&& (getSpider().get(2).getLife() <= 0)) {
			this.isFinish = true;
			;
		}
		return this.isFinish;
	}

	public void CreationList() {
		spider = new ArrayList<Spider>();
		for (int i = 0; i < 3; i++) {
			Vector2 tempon = CreatVectorAleatoire();
			if ((tempon.getX() != 5 || tempon.getY() != 5)
					&& (tempon.getX() != 1 || tempon.getY() != 4)) {
				spider.add(i, new Spider(tempon, SpiderInfos.SPIDER_SIZE, ImagePaths.SPIDER, SpiderInfos.SPIDER_LIFE,
						SpiderInfos.SPIDER_SPEED, SpiderInfos.SPIDER_DEGATS));
			} else {
				i--;
			}
		}
	}

	@Override
	public boolean attaqueDuHero(Projectile p) {
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getSpider().get(0).getPosition(),
				getSpider().get(0).getSize())) {
			getSpider().get(0).setLife(getSpider().get(0).getLife() - p.getDegats());
			return true;
		}
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getSpider().get(1).getPosition(),
				getSpider().get(1).getSize())) {
			getSpider().get(1).setLife(getSpider().get(1).getLife() - p.getDegats());
			return true;
		}
		if (Physics.rectangleCollision(p.getPosition(), p.getSize(), getSpider().get(2).getPosition(),
				getSpider().get(2).getSize())) {
			getSpider().get(2).setLife(getSpider().get(2).getLife() - p.getDegats());
			return true;
		}
		return false;
	}

	public void attaqueDesMonstre() {
		if ((Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(), getSpider().get(0).getPosition(),
				getSpider().get(0).getSize()))
				|| Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(),
						getSpider().get(1).getPosition(), getSpider().get(1).getSize())
				|| Physics.rectangleCollision(getHero().getPosition(), getHero().getSize(),
						getSpider().get(2).getPosition(), getSpider().get(2).getSize())) {
			if (this.cycle > this.tempsPause) {
				if (getHero().getBouclier() > 0) {
					getHero().setBouclier(getHero().getBouclier() - getSpider().get(0).getDegatsCC());
				} else {
					getHero().setLife(getHero().getLife() - getSpider().get(0).getDegatsCC());
				}
				this.cycle = 0;
			}
		}
	}

	@Override
	public void killAllMonster() {
		getSpider().get(0).setLife(0);
		getSpider().get(1).setLife(0);
		getSpider().get(2).setLife(0);
	}

	/*
	 * *****************************************************************************
	 * ********
	 * GETTERS AND SETTERS
	 * *****************************************************************************
	 * ********
	 */

	public List<Spider> getSpider() {
		return spider;
	}

	public void setSpider(List<Spider> spider) {
		this.spider = spider;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public double getSpeedTmp() {
		return speedTmp;
	}

	public void setSpeedTmp(double speedTmp) {
		this.speedTmp = speedTmp;
	}

}
