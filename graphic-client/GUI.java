import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;


public class GUI implements ActionListener {
    private JTextField textField;
    private JLabel label;
    private JButton button;
    private MyTextArea textArea;
    private final static String newline = "\n";
    
    public GUI() {
      
    }
    
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == button) {
            String cad=textField.getText();
            setTitle(cad);
            for (int i = 0; i < 10; i ++)
            textArea.append("hola"+"\n");
        }*/
    }
    
    public static void main(String[] args) {
    	
    
        JButton button1 = new JButton("Send");
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth=GridBagConstraints.RELATIVE;
        c.weightx = 1;
  	panel1.add(new JTextField(25), c);
        c.gridwidth=GridBagConstraints.REMAINDER;
        c.gridwidth = 1;
        c.weightx = 0.01;
        panel1.add(new JButton("Send"), c);
        
        /*JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.ipady = 100;
  	panel2.add((new MyTextArea()).getScroll(), c1);
  	c1.fill = GridBagConstraints.HORIZONTAL;
  	c.weightx = 0.5;
        c1.gridx = 1;
        c1.gridy = 0;
        JList list = new JList();
        panel2.add(new JScrollPane(list), c1);*/
        
        JPanel outputPanel = new JPanel(new MigLayout("wrap 3"));
        outputPanel.add((new MyTextArea()).getScroll(), "span 2");
        outputPanel.add(new JScrollPane(new JList()), "wrap");
        
        MyFrame frame = new MyFrame();
        frame.setContentPane(outputPanel);
        
        /*GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 0.5;
        c2.weighty = 3;
        c2.gridx = 0;
        c2.gridy = 0;
        frame.add(panel2, c2);
        c2.gridx = 0;
        c2.gridy = 1;
        frame.add(panel1, c2);
        frame.pack();
        frame.setVisible(true);*/
        
    }
    
    
}

    class MyFrame extends JFrame{
        public MyFrame(){
            Toolkit myScreen = Toolkit.getDefaultToolkit();
            Dimension screenSize = myScreen.getScreenSize();
            
            int height,width;
            height = screenSize.height;
            width = screenSize.width;
        
            setSize(width/2, height/2);
            setLocation(width/4,height/4);
        }
    }
    
    class MyTextArea extends JTextArea{
        JScrollPane scroll;
    
        public MyTextArea(){
            super(20,30);
            setLineWrap(true);
            setEditable(false);
            setVisible(true);
            
            scroll = new JScrollPane(this);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            /*for(int i = 0; i < 100; i++)
                append("hola"+"\n");*/
            
        }
        
        public JScrollPane getScroll(){
            return scroll;
        }
    }
