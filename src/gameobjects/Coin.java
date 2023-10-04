package gameobjects;

import libraries.Vector2;

public class Coin extends Consommable {
    public Coin(Vector2 position, Vector2 size, String imagePath) {
        super(position, size, imagePath);
    }
    public Coin(Vector2 position, Vector2 size, String imagePath, String cle) {
        super(position, size, imagePath, cle);
    }
}
