import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.Random;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.TimerTask;
import java.util.Timer;

public class Server{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    public static int unitWidth = 30;
    public static int unitHeight = 30;
    int PLAYERS;
    int execution_counter = 100;
    final int DELAY = 70;
    Snake[] snakes;
    Food[] foods;
    Cliente[] clientes;
    boolean running = false;
    private Timer timer;
    private Random random;
    int received;
    int foodX;
    int foodY;
    Snake snake;

    Server(int port){
        try{
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            try {
                socket = server.accept();
                System.out.println("Client accepted");
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
                running = true;
            } catch (IOException e) {
                System.out.println(e);
            }

            // Version singleplayer
            random = new Random();
            timer = new Timer();
            foodX = random.nextInt(unitWidth);
            foodY = random.nextInt(unitHeight);
            snake = new Snake(3);



            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (running) {
                        snake.move();
                        if ((snake.getX()[0] == foodX) && (snake.getY()[0] == foodY)) {
                            snake.grow();
                            foodX = random.nextInt(unitWidth);
                            foodY = random.nextInt(unitHeight);
                        }
                        try {
                            out.writeUTF(boardToString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        // Stop the timer if not running
                        timer.cancel();
                    }
                }
            }, 0, DELAY);

            new Thread(() -> {
                while(true){
                    try{
                        received = in.readInt();
                        switch (received){
                            case 37:
                                snake.setDirection('L');
                                break;
                            case 38:
                                snake.setDirection('U');
                                break;
                            case 39:
                                snake.setDirection('R');
                                break;
                            case 40:
                                snake.setDirection('D');
                                break;
                            default:
                                break;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();



        } catch (IOException e){
            System.out.println(e);
        }
    }

    public String boardToString(){
        String boardId = "";
        for(int i = 0; i < snake.getBodyParts(); i++){
            boardId = boardId + String.format("%d,%d,%d;", 1,snake.getX()[i], snake.getY()[i]);
        }
        boardId = boardId + String.format("%d,%d,%d", 5,foodX, foodY);
        System.out.println("Sending board ID");
        return boardId;
    }

    public void setPLAYERS(){
        // Definir numero de jugadores
        PLAYERS = 1;
        snakes = new Snake[PLAYERS];
        for(int i = 0; i < PLAYERS; i++){
            snakes[i] = new Snake(i+1);
        }
    }

    public void waitPlayers(){
        // Funcion para esperar que se conecten N jugadores
    }
    public void startGame(){
        foods = new Food[PLAYERS];
        for(Food food: foods){
            food = new Food();
        }
        running = true;
        System.out.println("awa");
    }

    public void moveSnakes(){
        for(Snake snake:snakes){
            snake.move();
        }
    }

    public void checkFood(){
        for(Snake snake:snakes){
            for(Food food:foods){
                if((snake.getX()[0] == food.getPosX()) && (snake.getY()[0] == food.getPosY())){
                    snake.grow();
                    food.resetFoodPosition();
                }
            }
        }
    }

    public void checkCollision(){
        for(int i = 0; i < PLAYERS; i++){
            if ((snakes[i].getX()[0] < 0) || (snakes[i].getX()[0] > unitWidth) || (snakes[i].getY()[0] < 0) || (snakes[i].getY()[0] > unitHeight)){
                snakes[i].setDirection('N');
                clientes[i].gameOver();
            }
            for(int k = 1; k < snakes[i].getBodyParts(); k++){
                if ((snakes[i].getX()[0] == snakes[i].getX()[k]) && (snakes[i].getY()[0] == snakes[i].getY()[k])) {
                    snakes[i].setDirection('N');
                    clientes[i].gameOver();
                }
            }
            for(int j = i + 1; j < PLAYERS; j++){
                for(int k = 0; k < snakes[j].getBodyParts(); k++){
                    if ((snakes[i].getX()[0] == snakes[j].getX()[k]) && (snakes[i].getY()[0] == snakes[j].getY()[k])){
                        snakes[i].setDirection('N');
                        clientes[i].gameOver();
                        if(k == 0){
                            snakes[j].setDirection('N');
                            clientes[j].gameOver();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        Server server = new Server(5000);
    }
}
