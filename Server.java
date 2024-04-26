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
        System.out.println("Waiting for a client ...");

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
                    gameLoop();
                    for(DataOutputStream out:outs) {
                        try {
                            out.writeUTF(boardToString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else {
                    // Stop the timer if not running
                    timer.cancel();
                }
            }
        }, 0, DELAY);


        for(int i = 0; i < PLAYERS; i++){
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket socket = clientSockets.get(finalI);
                    DataInputStream in = null;
                    try {
                        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    while(true){
                        int received = 0;
                        try {
                            received = in.readInt();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
                            default:
                                break;
                        }
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
        System.out.println("Sending board ID");
        return boardId;
    }

    public void gameLoop(){
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
            System.out.println(snake.getX()[0]);
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
                //clientes[i].gameOver();
            }
            for(int k = 1; k < snakes[i].getBodyParts(); k++){
                if ((snakes[i].getX()[0] == snakes[i].getX()[k]) && (snakes[i].getY()[0] == snakes[i].getY()[k])) {
                    snakes[i].setDirection('N');
                    //clientes[i].gameOver();
                }
            }
            for(int j = i + 1; j < PLAYERS; j++){
                for(int k = 0; k < snakes[j].getBodyParts(); k++){
                    if ((snakes[i].getX()[0] == snakes[j].getX()[k]) && (snakes[i].getY()[0] == snakes[j].getY()[k])){
                        snakes[i].setDirection('N');
                        //clientes[i].gameOver();
                        if(k == 0){
                            snakes[j].setDirection('N');
                            //clientes[j].gameOver();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(5000);
    }
}

/*
            try {
                clientSockets = serverSocket.accept();
                System.out.println("Client accepted");
                in = new DataInputStream(new BufferedInputStream(clientSockets.getInputStream()));
                out = new DataOutputStream(clientSockets.getOutputStream());
                running = true;
            } catch (IOException e) {
                System.out.println(e);
            }

            // Version singleplayer
            startGame();
            for(Snake snake:snakes){
                System.out.println(snake.getX()[0]);
            }

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (running) {
                        gameLoop();
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

            /*
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

             */