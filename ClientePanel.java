import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class ClientePanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    boolean running;
    Snake[] snakes;
    Food[] foods;
    int keyCode;
    ClientePanel(){
        this.running = false;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ClientePanel.AnotherKeyAdapter());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillOval(0,0,UNIT_SIZE,UNIT_SIZE);
        /*
        for(Food food:foods){
            g.fillOval(food.getPosX(), food.getPosY(), UNIT_SIZE, UNIT_SIZE);
        }

        for(Snake snake:snakes){
            for (int i = 0; i < snake.getBodyParts(); i++) {
                if (i == 0) {
                    g.setColor(snake.getHeadColor());
                    g.fillRect(snake.getX()[i], snake.getY()[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(snake.getTailColor());
                    g.fillRect(snake.getX()[i], snake.getY()[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

        }
        */
    }
    private class AnotherKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    // send L to server
                    break;
                case KeyEvent.VK_RIGHT:
                    // send R to server
                    break;
                case KeyEvent.VK_UP:
                    // send U to server
                    break;
                case KeyEvent.VK_DOWN:
                    // send D to server
                    break;
            }
        }
    }
}
