import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    ServerSocket s;

    public MyServerSocket(int port) throws IOException {
        s = new ServerSocket(port);
    }

    public MySocket accept() throws IOException {
        Socket ss = s.accept();
        MySocket ms = new MySocket(ss);
        return ms;
    }
}
