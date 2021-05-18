import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MySocket {
    private Socket s;
    private String clientNick;
    private PrintWriter out;
    private BufferedReader in ;
    
    public MySocket(String address, int port, String clientName) throws IOException {
        s = new Socket(address, port);
        this.clientNick = clientName;
        out = new PrintWriter(s.getOutputStream(), true); in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    public MySocket(Socket s) throws IOException {
        this.s = s;
        out = new PrintWriter(s.getOutputStream(), true); in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    public void println(String str) throws IOException {
        out.println(str);
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public String getNick() {
        return clientNick;
    }

    public void shutdownOutput() throws IOException {
        s.shutdownOutput();
    }

    public void putNick(String nick) {
        clientNick = nick;
    }
}

