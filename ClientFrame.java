//import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import javax.swing.*;
import java.awt.*;

public class ClientFrame extends JFrame {
    final int WIDTH = 700;
    final int HEIGHT = 700;
    ClientFrame(){
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setResizable(false);
        this.pack();
        //this.setVisible(true);
        this.setLocationRelativeTo(null);
        /*
        //this.add(new GamePanel());
        this.add(new ClientePanel());
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
         */
    }
}
