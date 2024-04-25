import java.awt.*;

public class Snake {
    private int playerNumber;
    private final int[] X;
    private final int[] Y;
    private char direction;
    private int bodyParts;
    private Color headColor;
    private Color tailColor;

    public Snake(int playerNumber){
        this.playerNumber = playerNumber;
        this.X = new int[Server.unitWidth * Server.unitHeight];
        this.Y = new int[Server.unitWidth * Server.unitHeight];
        this.bodyParts = 6;

        switch (playerNumber){
            case 1:
                this.direction = 'R';
                this.headColor = new Color(190,0,0);
                this.tailColor = new Color(190,0,0,160);
                for(int i = 0; i < bodyParts; i++){
                    X[i] = bodyParts - i - 1;
                    Y[i] = 0;
                }
                 break;
            case 2:
                this.direction = 'D';
                this.headColor = new Color(0,190,0);
                this.tailColor = new Color(0,190,0,160);
                for(int i = 0; i < bodyParts; i++){
                    X[i] = Server.unitWidth - 1;
                    Y[i] = bodyParts - i - 1;
                }
                break;
            case 3:
                this.direction = 'L';
                this.headColor = new Color(0,0,190);
                this.tailColor = new Color(0,0,190,160);
                for(int i = 0; i < bodyParts; i++){
                    X[i] = Server.unitWidth - bodyParts + i;
                    Y[i] = Server.unitHeight - 1;
                }
                break;
            case 4:
                this.direction = 'U';
                this.headColor = new Color(190,190,0);
                this.tailColor = new Color(190,190,0,160);
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
            default:
                break;
        }
    }

    public void grow() {
        bodyParts++;
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

    public Color getHeadColor(){ return headColor; }

    public Color getTailColor(){ return tailColor; }
}
