import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE_W = WIDTH / Server.unitWidth;
    static final int UNIT_SIZE_H = HEIGHT / Server.unitHeight;
    int sentKeyCode;
    int finalScore;
    int[][] coloredTiles = null;
    boolean gameOver = false;
    Color[] colors = {new Color(190,0,0,160),
            new Color(0,190,0,160),
            new Color(0,0,190,160),
            new Color(190,190,0,160),
            Color.white};
    ClientPanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                sentKeyCode = e.getKeyCode();
            }
        });
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(!gameOver){
            drawGame(g);
        }
        else{
            drawGameOver(g);
        }
    }

    public void drawGame(Graphics g){
        for(int i = 0; i < Server.unitWidth; i++){
            g.drawLine(i*UNIT_SIZE_W,0,i*UNIT_SIZE_W, HEIGHT);
        }
        for(int i = 0; i < Server.unitHeight; i++){
            g.drawLine(0,i*UNIT_SIZE_H, WIDTH, i*UNIT_SIZE_H );
        }
        if(coloredTiles != null){

            for(int[] tile : coloredTiles){
                g.setColor(colors[tile[0] - 1]);
                g.fillRect(tile[1] * UNIT_SIZE_W, tile[2] * UNIT_SIZE_H, UNIT_SIZE_W, UNIT_SIZE_H);
            }
        }
    }
    public void drawGameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Century", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Puntaje: " + finalScore, 100, 100);
    }

    public void stringToBoard(String boardId){
        String[] elements = boardId.split(";");
        coloredTiles = new int[elements.length][3];
        for(int i = 0; i < elements.length; i++){
            String[] coords = elements[i].split(",");
            int[] tile = new int[3];
            for(int j = 0 ; j < 3; j++){
                tile[j] = Integer.parseInt(coords[j]);
            }
            coloredTiles[i] = tile;
        }
        repaint();
    }

    public void gameOver(int score){
        finalScore = score;
        gameOver = true;
        repaint();
    }
    public int sendKeyCode(){
        return this.sentKeyCode;
    }

}
