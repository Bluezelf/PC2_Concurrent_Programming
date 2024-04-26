import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Server{
    // Variables for server - client interaction
    private ArrayList<Socket> clientSockets = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private ArrayList<DataOutputStream> outs = new ArrayList<>();
    private int connections = 0;

    // Game Units
    public static int unitWidth = 30;
    public static int unitHeight = 30;
    Snake[] snakes;
    Food[] foods;
    int PLAYERS = 2;

    // Game Loop
    private Timer timer;
    final int DELAY = 70;
    boolean running = false;
    int[] keyPresses;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        System.out.println("Waiting for " + PLAYERS + " players...");

        while(connections != PLAYERS) {
            Socket socket = serverSocket.accept();
            clientSockets.add(socket);
            outs.add(new DataOutputStream(socket.getOutputStream()));
            connections++;
            System.out.println("Connections = " + connections);
        }

        startGame();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    try {
                        gameLoop();
                        for(int i = 0; i < PLAYERS; i++){
                            if(outs.get(i) != null){
                                outs.get(i).writeUTF(boardToString());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(connections == 0){
                        running = false;
                    }
                } else {
                    // Stop the timer if not running
                    timer.cancel();
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, DELAY);


        for(int i = 0; i < PLAYERS; i++){
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean runThread = true;
                    Socket socket = clientSockets.get(finalI);
                    DataInputStream in;
                    try {
                        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                        while(runThread){
                            int received = 0;
                            received = in.readInt();
                            switch (received){
                                case 37:
                                    snakes[finalI].setDirection('L');
                                    break;
                                case 38:
                                    snakes[finalI].setDirection('U');
                                    break;
                                case 39:
                                    snakes[finalI].setDirection('R');
                                    break;
                                case 40:
                                    snakes[finalI].setDirection('D');
                                    break;
                            }
                        }
                    } catch (IOException e) {
                        runThread = false;
                    }

                }
            }).start();
        }
    }

    public String boardToString(){
        String boardId = "";
        for(int i = 0; i < PLAYERS; i++){
            for(int j = 0; j < snakes[i].getBodyParts(); j++){
                boardId = boardId + String.format("%d,%d,%d;", i+1,snakes[i].getX()[j], snakes[i].getY()[j]);
            }
        }
        for(int i = 0; i < PLAYERS; i++){
            boardId = boardId + String.format("%d,%d,%d;",5,foods[i].getPosX(), foods[i].getPosY());
        }
        return boardId;
    }

    public void gameLoop() throws IOException {
        moveSnakes();
        checkFood();
        checkCollision();
    }

    public void startGame(){
        // Get Food
        foods = new Food[PLAYERS];
        snakes = new Snake[PLAYERS];
        for(int i = 0; i < PLAYERS; i++){
            foods[i] = new Food();
            snakes[i] = new Snake(i+1);
        }

        running = true;
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

    public void checkCollision() throws IOException {
        for(int i = 0; i < PLAYERS; i++){
            if(snakes[i].isAlive()){
                if ((snakes[i].getX()[0] < 0) || (snakes[i].getX()[0] > unitWidth) || (snakes[i].getY()[0] < 0) || (snakes[i].getY()[0] > unitHeight)){
                    killSnake(i);
                }
                for(int k = 1; k < snakes[i].getBodyParts(); k++){
                    if ((snakes[i].getX()[0] == snakes[i].getX()[k]) && (snakes[i].getY()[0] == snakes[i].getY()[k])) {
                        killSnake(i);
                    }
                }
                for(int j = i + 1; j < PLAYERS; j++){
                    for(int k = 0; k < snakes[j].getBodyParts(); k++){
                        if ((snakes[i].getX()[0] == snakes[j].getX()[k]) && (snakes[i].getY()[0] == snakes[j].getY()[k])){
                            killSnake(i);
                        }
                    }
                }
            }
        }
    }

    public void killSnake(int id) throws IOException {
        outs.get(id).writeUTF("GameOver" + snakes[id].getScore());
        outs.set(id, null);
        snakes[id].alive = false;
        snakes[id].setBodyParts(0);
        snakes[id].setDirection('N');
        connections--;
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server(5000);
    }
}