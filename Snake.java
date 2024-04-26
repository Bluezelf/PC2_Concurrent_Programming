import java.awt.*;

public class Snake {
    private int playerNumber;
    private final int[] X;
    private final int[] Y;
    private char direction;
    private int bodyParts;
    private int score = 0;
    boolean alive;

    public Snake(int playerNumber){
        this.playerNumber = playerNumber;
        this.X = new int[Server.unitWidth * Server.unitHeight];
        this.Y = new int[Server.unitWidth * Server.unitHeight];
        this.bodyParts = 6;
        this.alive = true;

        switch (playerNumber){
            case 1:
                this.direction = 'R';
                for(int i = 0; i < bodyParts; i++){
                    X[i] = bodyParts - i - 1;
                    Y[i] = 0;
                }
                 break;
            case 2:
                this.direction = 'D';
                for(int i = 0; i < bodyParts; i++){
                    X[i] = Server.unitWidth - 1;
                    Y[i] = bodyParts - i - 1;
                }
                break;
            case 3:
                this.direction = 'L';
                for(int i = 0; i < bodyParts; i++){
                    X[i] = Server.unitWidth - bodyParts + i;
                    Y[i] = Server.unitHeight - 1;
                }
                break;
            case 4:
                this.direction = 'U';
                for(int i = 0; i < bodyParts; i++){
                    X[i] = 0;
                    Y[i] = Server.unitHeight - bodyParts + i;
                }
                break;
        }
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }

        switch (direction) {
            case 'U':
                Y[0] -= 1;
                break;
            case 'D':
                Y[0] += 1;
                break;
            case 'L':
                X[0] -= 1;
                break;
            case 'R':
                X[0] += 1;
                break;
            case 'N':
                break;
        }
    }

    public void grow() {
        bodyParts++;
        score++;
    }

    public void killSnake(){

    }
    public int[] getX() {
        return X;
    }

    public int[] getY() {
        return Y;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public int getBodyParts() {
        return bodyParts;
    }
    public void setBodyParts(int bodyParts) { this.bodyParts = bodyParts; }
    public int getScore(){ return score; }
    public boolean isAlive(){ return alive; }

}
