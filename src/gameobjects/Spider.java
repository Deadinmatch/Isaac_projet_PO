package gameobjects;

import java.util.Random;

import libraries.StdDraw;
import libraries.Vector2;

public class Spider extends Monster {
	
	private int cycle;
	private int aleatoire;

	public Spider(Vector2 position, Vector2 size, String imagePath, int life, double speed, int degatsCC) {
		super(position, size, imagePath, life, speed, degatsCC);
	}
	
	public void updateGameObject()
    {
        move();
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
	 * DEPLACEMENT
	 * *************************************************************************************
	 */
    public void avance(boolean murHaut, boolean murBas, boolean murGauche, boolean murDroite)
    {
		if (cycle == 0)
		{
			intAlea();
		}
		if (cycle > 10 && this.isAlive())
		{
			//vers le haut
			if (this.aleatoire == 1 && murHaut)
			{
				this.goUpNext();
			}
			if(!murHaut) this.aleatoire++;
			//vers le bas
			if (this.aleatoire == 2 && murBas)
			{
				this.goDownNext();
			}
			if (!murBas) this.aleatoire--;
			//vers la gauche
			if (this.aleatoire == 3 && murGauche)
			{
				this.goLeftNext();
			}
			if (!murDroite) this.aleatoire--;
			//vers la droite
			if (this.aleatoire == 4 && murDroite)
			{
				this.goRightNext();
			}
			if (!murGauche) this.aleatoire++;
		}
		if (cycle > 15)
		{
			cycle = 0;
		}
		else
		{
			this.cycle++;
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
     * DRAW
     * *************************************************************************************
     */

    public void drawGameSpider()
    {

        StdDraw.picture(getPosition().getX(), getPosition().getY(), getImagePath(), getSize().getX(), getSize().getY());
        StdDraw.setPenColor(StdDraw.WHITE);
		//StdDraw.text(getPosition().getX() - 0.025, getPosition().getY() - 0.05, Integer.toString(this.getLife()));
    }
    
    /*
     * *************************************************************************************
     * AUTRE METHODE
     * *************************************************************************************
     */
    public Vector2 getNormalizedDirection()
    {
        Vector2 normalizedVector = new Vector2(this.getDirection());
        normalizedVector.euclidianNormalize(this.getSpeed());
        return normalizedVector;
    }
    
    public void intAlea()
    {
    	int min = 1;
		int max = 5;
		Random random = new Random();
		this.aleatoire = random.nextInt(max-min)+min;
    }
}
