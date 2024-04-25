import java.util.Random;

public class Food {
    private int posX;
    private int posY;
    Random random;
    Food(){
        random = new Random();
        resetFoodPosition();
    }

    public void resetFoodPosition(){
        this.posX = random.nextInt(Server.unitWidth);
        this.posY = random.nextInt(Server.unitHeight);
    }
    public int getPosX(){ return posX; }
    public int getPosY(){ return posY; }
}
