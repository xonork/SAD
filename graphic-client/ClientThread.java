import javax.swing.*;
import java.io.*;
import java.util.*;


public class ClientThread implements Runnable {

    private DefaultListModel messages;
    private DefaultListModel usernames;
    private ArrayList < String > users;
    private MySocket s;

    public ClientThread(DefaultListModel messages, DefaultListModel usernames, MySocket s, ArrayList < String > users) {
        this.messages = messages;
        this.usernames = usernames;
        this.users = users;
        this.s = s;
    }
    public void run() {
        String line;
        try {
            while ((line = s.readLine()) != null) {
                String[] splitLine = line.split(",", 2);
                switch (splitLine[0]) {
                    case "0":
                        if ((splitLine[1].indexOf("[") == 0) && (splitLine[1].lastIndexOf("]") == splitLine[1].length() - 1)) {
                            splitLine[1] = splitLine[1].substring(1, splitLine[1].length() - 1);
                            String[] totalUsers;
                            if (splitLine[1].contains(",")) {
                                totalUsers = splitLine[1].split(",");
                            } else {
                                totalUsers = new String[1];
                                totalUsers[0] = splitLine[1];
                            }
                            System.out.println(totalUsers[0]);
                            for (int i = 0; i < totalUsers.length; i++) {
                                if (totalUsers[i] != "") {
                                    users.add(totalUsers[i].replaceAll(" ", ""));
                                }
                            }
                        } else {
                            users.add(splitLine[1]);
                        }
                        Collections.sort(users, String.CASE_INSENSITIVE_ORDER);
                        usernames.removeAllElements();
                        for (int i = 0; i < users.size(); i++) {
                            usernames.add(i, users.get(i));
                        }
                        break;
                    case "1":
                        messages.addElement(splitLine[1]);
                        break;
                    case "2":
                        users.remove(new String(splitLine[1]));
                        usernames.removeAllElements();
                        for (int i = 0; i < users.size(); i++) {
                            usernames.add(i, users.get(i));
                        }



                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
