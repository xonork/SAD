import javax.swing.*;
import java.io.*;


public class ClientThread implements Runnable{

    private DefaultListModel messages;
    private MySocket s;
    
    public ClientThread(DefaultListModel messages, MySocket s){
        this.messages = messages;
        this.s = s;
    }
    public void run(){
        String line;
                try {
                    while ((line = s.readLine()) != null) {
                        messages.addElement(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
}
