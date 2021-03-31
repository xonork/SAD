import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    public static void main(String[] args) throws NumberFormatException, IOException {
        ReentrantLock lock = new ReentrantLock();
        ConcurrentHashMap < String, MySocket > map = new ConcurrentHashMap < String, MySocket > ();
        MyServerSocket ss = new MyServerSocket(Integer.parseInt(args[0]));

        while (true) {

            MySocket s = ss.accept();
            Worker w = new Worker(lock, map, s);
            Thread wThread = new Thread(w);
            wThread.start();
        }

    }
}
