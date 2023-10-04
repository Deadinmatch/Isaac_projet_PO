package gameobjects;

import libraries.Vector2;

public class HpUp extends Consommable {
    public HpUp(Vector2 position, Vector2 size, String imagePath) {
        super(position, size, imagePath);
    }
    
    public HpUp(Vector2 position, Vector2 size, String imagePath, String cle) {
        super(position, size, imagePath, cle);
    }
 
}
