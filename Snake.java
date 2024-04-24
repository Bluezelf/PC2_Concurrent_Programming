import java.awt.*;

public class Snake {
    private final int[] X;
    private final int[] Y;
    private char direction;
    private int bodyParts;
    private int bodyLength;

    public Snake(int x, int y, int gameUnits) {
        X = new int[gameUnits];
        Y = new int[gameUnits];
        X[0] = x;
        Y[0] = y;
        direction = 'R';
        bodyParts = 6;
        bodyLength = 1;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }

        switch (direction) {
            case 'U':
                Y[0] -= GamePanel.UNIT_SIZE;
                break;
            case 'D':
                Y[0] += GamePanel.UNIT_SIZE;
                break;
            case 'L':
                X[0] -= GamePanel.UNIT_SIZE;
                break;
            case 'R':
                X[0] += GamePanel.UNIT_SIZE;
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

    public int getBodyLength() {
        return bodyLength;
    }
}
