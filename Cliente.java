// A Java program for a Client
import java.io.*;
import java.net.*;

public class Cliente{
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    Cliente(String address, int port){
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            ClientFrame frame = new ClientFrame();
            TestPanel panel = new TestPanel();
            frame.add(panel);
            frame.setVisible(true);


            while(true){
                String received = in.readUTF();
                panel.stringToBoard(received);
                int toSend = panel.sendKeyCode();
                out.writeInt(toSend);
            }

        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) throws Exception {
        Cliente cliente = new Cliente("127.0.0.1", 5000);
    }


    public void gameOver(){

    }

}
