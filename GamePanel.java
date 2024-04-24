import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;

    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private Snake snake;
    private int foodX;
    private int foodY;
    private int foodEaten;
    private boolean running = false;
    private Timer timer;
    private Random random;

    GamePanel() {
        random = new Random();
        snake = new Snake(0, 0, GAME_UNITS);

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        addFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < snake.getBodyParts(); i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(0, 180, 9, 160));
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Century", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Puntaje: " + foodEaten, (WIDTH - metrics.stringWidth("Puntaje: " + foodEaten)) / 2, 100);

        } else {
            gameOver(g);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int) WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt((int) HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        snake.move();
    }

    public void checkFood() {
        if ((snake.getX()[0] == foodX) && (snake.getY()[0] == foodY)) {
            snake.grow();
            foodEaten++;
            addFood();
        }
    }

    public void checkCollision() {
        for (int i = snake.getBodyParts(); i > 0; i--) {
            if ((snake.getX()[0] == snake.getX()[i]) && (snake.getY()[0] == snake.getY()[i])) {
                running = false;
            }
        }

        if ((snake.getX()[0] < 0) || (snake.getX()[0] > WIDTH) || (snake.getY()[0] < 0) || (snake.getY()[0] > HEIGHT)) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        FontMetrics metrics;
        g.setColor(Color.red);
        g.setFont(new Font("Century", Font.BOLD, 60));
        metrics = getFontMetrics(g.getFont());
        g.drawString("PERDISTE :(", (WIDTH - metrics.stringWidth("PERDISTE :(")) / 2, HEIGHT / 2);

        g.setFont(new Font("Century", Font.BOLD, 30));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Puntaje: " + foodEaten, (WIDTH - metrics.stringWidth("Puntaje: " + foodEaten)) / 2, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snake.getDirection() != 'R') {
                        snake.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snake.getDirection() != 'L') {
                        snake.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snake.getDirection() != 'D') {
                        snake.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snake.getDirection() != 'U') {
                        snake.setDirection('D');
                    }
                    break;
            }
        }
    }
}
