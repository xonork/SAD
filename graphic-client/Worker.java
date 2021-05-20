import java.util.concurrent.*;
import java.lang.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;

public class Worker implements Runnable {
    private ConcurrentHashMap < String, MySocket > map;
    private MySocket s;
    private ReentrantLock lock;
    private static int threads = 0;


    public Worker(ReentrantLock lock, ConcurrentHashMap < String, MySocket > map, MySocket s) {
        this.map = map;
        this.s = s;
        this.lock = lock;
    }

    public void broadcast(String line) {
        try {
            for (String i: map.keySet()) {
                map.get(i).println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            s.putNick(s.readLine());
            String newNick = "0," + s.getNick();
            broadcast(newNick);
            map.put(s.getNick(), s);
            s.println("0," + map.keySet());


        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        lock.lock();
        threads++;
        lock.unlock();

        try {
            while ((line = map.get(s.getNick()).readLine()) != null)
                broadcast("1," + s.getNick() + " -> " + line);
            broadcast("2," + s.getNick());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
