package gameobjects;

import libraries.StdDraw;
import libraries.Vector2;
import resources.BossInfos;
import resources.ImagePaths;
import resources.RoomInfos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boss extends Monster{

    private List<Projectile> stoneProj;
    private int degatsProj;

    private boolean listeProjRempli;
    private int cycleMove;
    private int aleatoire;
    private int deplacementAlea;
    private int cycleProj;

    public Boss(Vector2 position, Vector2 size, String imagePath, int life, double speed, int degatsCC, int bossDegatsProj) 
    {
        super(position, size, imagePath, life, speed, degatsCC);
    }


    public void move()
    {
        Vector2 normalizedDirection = getNormalizedDirection();
        Vector2 positionAfterMoving = getPosition().addVector(normalizedDirection);
        setPosition(positionAfterMoving);
        setDirection(new Vector2());
    }
    
    public void updateGameObject()
    {
        move();
        this.cycleProj++;

        if( !listeProjRempli){
            remplirProj();
        }
        updateListeStonePorj();
        rechargement();
        updateProcessForShoot();
        enVie();
    }

    /*
     * *************************************************************************************
     * DRAW
     * *************************************************************************************
     */
    public void drawGameObject(){

        StdDraw.picture(getPosition().getX(),getPosition().getY(),getImagePath(),getSize().getX(),getSize().getY());
        this.drawListeProjectile();
        StdDraw.setPenColor(StdDraw.WHITE);
        //StdDraw.text(getPosition().getX() - 0.025, getPosition().getY() - 0.05, Integer.toString(this.getLife()));
    }
    
    public void drawListeProjectile()
    {
        if (getStoneProj().get(0).isTirer())
        {
            getStoneProj().get(0).drawGameObject();
        }
        if (getStoneProj().get(1).isTirer())
        {
            getStoneProj().get(1).drawGameObject();
        }
        if (getStoneProj().get(2).isTirer())
        {
            getStoneProj().get(2).drawGameObject();
        }
        if (getStoneProj().get(3).isTirer())
        {
            getStoneProj().get(3).drawGameObject();
        }
        if (getStoneProj().get(4).isTirer())
        {
            getStoneProj().get(4).drawGameObject();
        }
    }


   /*
    * *************************************************************************************
    * PROJECTILE
    * *************************************************************************************
    */

    public void remplirProj()
    {
        stoneProj = new ArrayList<Projectile>();
        for (int i=0 ; i<5 ; i++)
        {
            stoneProj.add(i, new Projectile(1, RoomInfos.TILE_SIZE.scalarMultiplication(0.5), new Vector2(0.6,0.6), BossInfos.BULLET_SPEED, ImagePaths.STONE));
            stoneProj.get(i).setDegats(BossInfos.BOSS_DEGATS_PROJ);
        }
        this.listeProjRempli = true;
    }

    public int demandeTir()
    {
        int index = -1;
        boolean fait = false;
        for (int i=0 ; i<stoneProj.size() && !fait ; i++)
        {
            if (stoneProj.get(i).isDisponible())
            {
                index = i;
                fait = true;
            }
        }
        return index;
    }

    public void rechargement()
    {
        //voir avec les profs 20 larmes par cycle de rechargement = larme infinie
        //sinon mettre condition pour bocler la boucle a 20
        //car la fonction est apelle pour un cycle de jeu donc 20
        for (int i=0 ; i<stoneProj.size(); i++)
        {
            if (!stoneProj.get(i).isDisponible() && !stoneProj.get(i).isTirer() )
            {
                stoneProj.get(i).setDisponible(true);
                stoneProj.get(i).setHaut(false);
                stoneProj.get(i).setBas(false);
                stoneProj.get(i).setGauche(false);
                stoneProj.get(i).setDroite(false);
            }
        }
    }

    public void updateListeStonePorj()
    {
        stoneProj.get(0).updateGameObject();
        stoneProj.get(1).updateGameObject();
        stoneProj.get(2).updateGameObject();
        stoneProj.get(3).updateGameObject();
        stoneProj.get(4).updateGameObject();
    }
    
    public void shootUpNext(int index)
    {
        stoneProj.get(index).shootUpNext();
    }
    public void shootDownNext(int index)
    {
        stoneProj.get(index).shootDownNext();
    }
    public void shootLeftNext(int index)
    {
        stoneProj.get(index).shootLeftNext();
    }
    public void shootRightNext(int index)
    {
        stoneProj.get(index).shootRightNext();
    }

    

    private void processForShoot(int index)
    {
        if (getStoneProj().get(index).isTirer())
        {
            if(getStoneProj().get(index).isDroite())
            {
                shootRightNext(index);
                if(!getStoneProj().get(index).portee(getStoneProj().get(index).getPosition(), getStoneProj().get(index).getPositionPortee()))
                {
                    getStoneProj().get(index).setTirer(false);
                }
            }

            else if(getStoneProj().get(index).isGauche())
            {
                shootLeftNext(index);
                if(!getStoneProj().get(index).portee2(getStoneProj().get(index).getPosition(), getStoneProj().get(index).getPositionPortee()))
                {
                    getStoneProj().get(index).setTirer(false);
                }
            }

            else if(getStoneProj().get(index).isBas())
            {
                shootDownNext(index);
                if (!getStoneProj().get(index).portee2(getStoneProj().get(index).getPosition(), getStoneProj().get(index).getPositionPortee()))
                {
                    getStoneProj().get(index).setTirer(false);
                }
            }

            else if(getStoneProj().get(index).isHaut())
            {
                shootUpNext(index);
                if (!getStoneProj().get(index).portee(getStoneProj().get(index).getPosition(), getStoneProj().get(index).getPositionPortee()))
                {
                    getStoneProj().get(index).setTirer(false);
                }
            }
        }
    }

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
            getStoneProj().get(0).setTirer(false);
            getStoneProj().get(1).setTirer(false);
            getStoneProj().get(2).setTirer(false);
            getStoneProj().get(3).setTirer(false);
            getStoneProj().get(4).setTirer(false);

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
     * DEPLACEMENT
     * *************************************************************************************
     */
    
    public void avanceCibleAndAlea(Vector2 position, boolean murHaut,boolean murBas, boolean murGauche, boolean murDroite)
    {
        if ( cycleMove == 1)
        {
            intAlea();
            int min = 0;
            int max = 10;
            Random random = new Random();
            deplacementAlea = random.nextInt(max-min)+min;
        }
            if ( deplacementAlea < 5)
            {
                if ( cycleMove <= 50 ) 
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
            else
            {
                if (cycleMove <= 50 && this.isAlive())
                {
                    //vers le haut
                    if (this.aleatoire == 1 && murHaut)
                    {
                        this.goUpNext();
                    }
                    //if(!murHaut) this.aleatoire++;
                    //vers le bas
                    if (this.aleatoire == 2 && murBas)
                    {
                        this.goDownNext();
                    }
                    //if (!murBas) this.aleatoire--;
                    //vers la gauche
                    if (this.aleatoire == 3 && murGauche)
                    {
                        this.goLeftNext();
                    }
                    //if (!murDroite) this.aleatoire--;
                    //vers la droite
                    if (this.aleatoire == 4 && murDroite)
                    {
                        this.goRightNext();
                    }
                    //if (!murGauche) this.aleatoire++;
                }
            }
        if ( cycleMove > 40)
        {
            cycleMove = 0;
        }
    }

    public void intAlea()
    {
        int min = 1;
        int max = 5;
        Random random = new Random();
        this.aleatoire = random.nextInt(max-min)+min;
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

    public void goStand(){

        getDirection().addX(0);
    }

    /*
     * *************************************************************************************
     * GETTERS AND SETTERS
     * *************************************************************************************
     */
    
    public List<Projectile> getStoneProj() {
        return stoneProj;
    }

    public void setStoneProj(List<Projectile> stoneProj) {
        this.stoneProj = stoneProj;
    }

    public int getDegatsProj() {
        return degatsProj;
    }

    public void setDegatsProj(int degatsProj) {
        this.degatsProj = degatsProj;
    }

    public boolean isListeProjRempli() {
        return listeProjRempli;
    }

    public void setListeProjRempli(boolean listeProjRempli) {
        this.listeProjRempli = listeProjRempli;
    }

    public int getCycle() {
        return cycleMove;
    }

    public void setCycle(int cycle) {
        this.cycleMove = cycle;
    }

    public int getAleatoire() {
        return aleatoire;
    }

    public void setAleatoire(int aleatoire) {
        this.aleatoire = aleatoire;
    }


	public int getCycleProj() {
		return cycleProj;
	}


	public void setCycleProj(int cycleProj) {
		this.cycleProj = cycleProj;
	}
    
}