import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    public static void main(String[] args) throws NumberFormatException, IOException {
    
        HashMap<String, MySocket> map = new HashMap<String, MySocket>();
        MyServerSocket ss = new MyServerSocket(Integer.parseInt(args[0]));
        int threadCounter = 0;
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        
        while (true) {
        
            MySocket s = ss.accept();
	    s.putNick(s.readLine());
            map.put(s.getNick(),s);
            new Thread() {
                public void run() {
                    
                    String line,myNick;
                    
                    lock.readLock().lock();
                    myNick = s.getNick();
                    System.out.println(myNick);
                    lock.readLock().unlock();
                    try {
                        while ((line = map.get(myNick).readLine()) != null)
                            for(String i : map.keySet()) {
                                map.get(i).println("\u001B[0m"+myNick + "> " + line);
                            }
                            
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            threadCounter++;
        }

    }
}
