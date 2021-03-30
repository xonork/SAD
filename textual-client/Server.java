import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    public static int threadCounter = 0;
    public static void main(String[] args) throws NumberFormatException, IOException {
    
        HashMap<String, MySocket> map = new HashMap<String, MySocket>();
        MyServerSocket ss = new MyServerSocket(Integer.parseInt(args[0]));
        
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        
        while (true) {
        
            MySocket s = ss.accept();
	    s.putNick(s.readLine());
            map.put(s.getNick(),s);
            new Thread() {
                public void run() {
                    
                    String line,myNick,myColor;
                    myColor = Colors.COLORS_LIST[threadCounter%Colors.COLORS_LIST.length];
                    
                    lock.readLock().lock();
                    myNick = s.getNick();
                    System.out.println(myNick);
                    lock.readLock().unlock();
                    
                    lock.writeLock().lock();
                    threadCounter++;
                    lock.writeLock().unlock();
                    
                    try {
                        while ((line = map.get(myNick).readLine()) != null)
                            for(String i : map.keySet()) {
                            	lock.readLock().lock();
                                map.get(i).println(myColor+myNick +"> "+Colors.RESET + line);
                                lock.readLock().unlock();
                            }
                            
                            
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            System.out.print(String.valueOf(threadCounter));
        }

    }
}
