import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;

public class GUI {

	private Set<String> users = new HashSet<>();
	private static String host;
	private static int port = 12345;
	private String user;
	private String state;
	private static MySocket s;

	private JFrame frame = new JFrame(s.getNick());
 	private JTextField textField = new JTextField(75);
	private JList messageList, userList;
	private static DefaultListModel messages, usernames;
	private JButton sendButton;
	
	public GUI() {
	
	this.messages = new DefaultListModel();
	this.messageList = new JList(messages);
	this.messageList.setBackground(new Color(180, 233, 184));
	
	TitledBorder titleUsers = new TitledBorder("Users Online");
	titleUsers.setTitleColor(new Color(91, 120, 25));
	this.usernames = new DefaultListModel();
	this.userList = new JList(usernames);
	this.userList.setBackground(new Color(223, 233, 180));
	this.userList.setBorder(titleUsers);
	
	messageList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	textField.setEditable(true);
	
	SendListener sendListener = new SendListener();
	sendButton = new JButton("Send");
	sendButton.addActionListener(sendListener);
	textField.addActionListener(sendListener);
	
	
	JPanel buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
	buttonPane.add(textField);
	buttonPane.add(Box.createHorizontalStrut(5));
	buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
	buttonPane.add(Box.createHorizontalStrut(5));
	buttonPane.add(sendButton);
	buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	
	
	frame.getContentPane().add(buttonPane, BorderLayout.SOUTH);
	frame.getContentPane().add(new JScrollPane(userList), BorderLayout.WEST);
	frame.getContentPane().add(new JScrollPane(messageList), BorderLayout.CENTER);
	frame.pack();
	
	}
	
	class SendListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        String text = textField.getText();
	        
	        if(!text.equals("")){
	            try{
	                s.println(text);
	            } catch (IOException i) {
                    i.printStackTrace();
                    }
	            textField.setText("");
	        }
	    }
	}
	
	private static void createAndShowGUI() {
	    GUI gui = new GUI();
	    gui.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gui.frame.setVisible(true);
	    ClientThread clientThread = new ClientThread(messages, s);
	    Thread cThread = new Thread(clientThread);
	    cThread.start();
	}
	public static void main(String[] args) {
	    try{
	        s = new MySocket(args[0], Integer.parseInt(args[1]), args[2]);
	        s.println(s.getNick());
	    } catch (IOException e) {
                    e.printStackTrace();
                }
	    
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            createAndShowGUI();
	        }
	
	    });
	}
}
