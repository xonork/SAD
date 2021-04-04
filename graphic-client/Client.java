import java.io.*;
import javax.swing.*;

public class Client {

    public static void main(String[] args) throws NumberFormatException, IOException {
    	System.out.print(System.getProperty("java.classpath"));
        MySocket s = new MySocket(args[0], Integer.parseInt(args[1]), args[2]);
        s.println(s.getNick());
        new Thread() {
            public void run() {
                String line;
                BufferedReader kbd = new BufferedReader(new InputStreamReader(System.in));
                try {
                    while ((line = kbd.readLine()) != null) {
                        s.println(line);
                        System.out.print("\033[1A");
                        System.out.print("\033[2K");
                        System.out.print("\033[1G");

                    }
                    s.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        new Thread() {
            public void run() {
                String line;
                try {
                    while ((line = s.readLine()) != null) {
                        System.out.print("\033[2K");
                        System.out.print("\033[1G");
                        System.out.print("\033[0;31m" + line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
}
