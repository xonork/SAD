import java.util.concurrent.*;
import java.lang.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;

public class Worker implements Runnable {
    private ConcurrentHashMap < String, MySocket > map;
    private MySocket s;
    private ReentrantLock lock;
    private static int threads = 0;
    private String myColor;

    public Worker(ReentrantLock lock, ConcurrentHashMap < String, MySocket > map, MySocket s) {
        this.map = map;
        this.s = s;
        this.lock = lock;
    }

    public void broadcast(String line) {
        try {
            for (String i: map.keySet()) {
                map.get(i).println(s.getNick() + "> "  + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            s.putNick(s.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put(s.getNick(), s);

        String line;

        lock.lock();
        this.myColor = Colors.COLORS_LIST[threads % Colors.COLORS_LIST.length];
        threads++;
        lock.unlock();

        try {
            while ((line = map.get(s.getNick()).readLine()) != null)
                broadcast(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

