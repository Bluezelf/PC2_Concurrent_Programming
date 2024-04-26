// A Java program for a Client
import java.io.*;
import java.net.*;

public class Client {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    Client(String address, int port){
        try {
            boolean runClient = true;
            socket = new Socket(address, port);
            System.out.println("Connected");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            ClientFrame frame = new ClientFrame();
            ClientPanel panel = new ClientPanel();
            frame.add(panel);
            frame.setVisible(true);


            while(runClient){
                String received = in.readUTF();
                if(received.matches("GameOver(.*)")){
                    int score = Integer.parseInt(received.substring(8));
                    panel.gameOver(score);
                    runClient = false;
                    socket.close();
                    //int toSend = panel.sendKeyCode();
                    //out.writeInt(toSend);
                }
                else{
                    panel.stringToBoard(received);
                    int toSend = panel.sendKeyCode();
                    out.writeInt(toSend);
                }

            }

        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) throws Exception {
        Client cliente = new Client("127.0.0.1", 5000);
    }

}
