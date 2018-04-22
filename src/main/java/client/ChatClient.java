package client;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import chat.Chat;
import chat.ChatHelper;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient
{
    static final String CMD_PREFIX = "/";
    static final String CMD_CREATE = CMD_PREFIX + "create ";
    static final String CMD_LIST   = CMD_PREFIX + "list";
    static final String CMD_JOIN   = CMD_PREFIX + "join ";
    static final String CMD_LEAVE  = CMD_PREFIX + "leave";
    static final String CMD_HELP   = CMD_PREFIX + "help";
    static final String CMD_NAME   = CMD_PREFIX + "name ";
    static final String CMD_QUIT   = CMD_PREFIX + "quit";

    static Chat connImpl;
    static String token;

    public static class Input implements Runnable {
 
    	
    	 private void initUI() {
    	        JFrame frame = new JFrame();
    	        BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
                frame.setLayout(boxLayout);

    	        final JTextField textfield = new JTextField(20);
    	        
    	        JTextField createRoom = new JTextField(20);
    	        createRoom.setToolTipText("Enter room name ");
    	        JButton createBtn = new JButton("Create Room");
    	        JTextField joinRoom = new JTextField(20);
    	        joinRoom.setToolTipText("Room name to join");
    	        JButton joinBtn = new JButton("Join Room");
    	        JButton leaveBtn = new JButton("leave Room");
    	        JButton roomListBtn = new JButton("Room List");
    	        
    	        JTextField nameField = new JTextField(20);
    	        nameField.setText(connImpl.getName(token));
    	        JButton nameBtn = new JButton("Change Name");
    	        JButton helpBtn = new JButton("View Commands");
    	        JButton quitBtn = new JButton("Exot");
    	        
    	        
    	        
    	        
    	        
    	    
    	        
    	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	        frame.add( new JLabel("Input" ) );
    	        frame.add(textfield);
    	        frame.add( new JLabel(" Create a Room" ) );
    	        frame.add(createRoom);
    	        frame.add(createBtn);
    	        frame.add( new JLabel(" Join a Room" ) );
    	        frame.add(joinRoom);
    	        frame.add(joinBtn);
    	        frame.add(leaveBtn);
    	        frame.add(roomListBtn);
    	        frame.add( new JLabel("Name" ) );
      	        frame.add(nameField);
    	        frame.add(nameBtn);
    	        frame.add(helpBtn);
    	        frame.add(quitBtn);  	        
    	        frame.setSize(300, 400);
    	        frame.setVisible(true);
    	        
    	        
    	        
    	        textfield.addActionListener(new ActionListener() {

    	            @Override
    	            public void actionPerformed(ActionEvent e) {
    	                try {
    	                    String text = textfield.getText();
    	                    InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
    	                    parse(text);
    	                    textfield.setText("");
    	                    System.err.println(text);
    	                } catch (UnsupportedEncodingException e1) {
    	                    e1.printStackTrace();
    	                }

    	            }
    	        });
    	        
    	        
    	        
    	        createBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	      
    	        joinBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	        leaveBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	        roomListBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	        nameBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	        helpBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	        quitBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	           JOptionPane.showMessageDialog(null, "test");
    	          }
    	        });
    	        
    	      
    	     
    	     
    	        
    	       
    	     
    	   
    	        
    	        
    	    }
    	
        public void run() {
        	initUI();
        }

        void parse(String str) {
            if (str.startsWith(CMD_CREATE)) {
                String name = str.substring(CMD_CREATE.length());
                connImpl.createChatRoom(token, name);
            } else if (str.startsWith(CMD_LIST)) {
                connImpl.listChatRooms(token);
            } else if (str.startsWith(CMD_JOIN)) {
                String name = str.substring(CMD_JOIN.length());
                connImpl.joinChatRoom(token, name);
            } else if (str.startsWith(CMD_LEAVE)) {
                connImpl.leaveChatRoom(token);
            } else if (str.startsWith(CMD_HELP)) {
                help();
            } else if (str.startsWith(CMD_NAME)) {
                String name = str.substring(CMD_NAME.length());
                connImpl.changeName(token, name);
            } else if (str.startsWith(CMD_QUIT)) {
                System.exit(0);
            } else {
                connImpl.sendMessage(token, str);
            }
        }

        void help() {
            String str = "Commands:\n" +
                    CMD_CREATE + "\n" +
                    CMD_LIST + "\n" +
                    CMD_JOIN + "\n" +
                    CMD_LEAVE + "\n" +
                    CMD_HELP + "\n" +
                    CMD_NAME + "\n" +
                    CMD_QUIT;
            System.out.println(str);
        }
    }

    public static class Output implements Runnable {

        public void run() {
        	
        
            
           	JFrame frame = new JFrame();
        	BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
            frame.setLayout(boxLayout);


            JTextArea ta = new JTextArea();
            TextAreaOutputStream taos = new TextAreaOutputStream( ta, 60 );
            PrintStream ps = new PrintStream( taos );
            System.setOut( ps );
            System.setErr( ps );

            
            frame.add( new JLabel(" Output" ) );
            frame.add( new JScrollPane (ta)  );

            frame.pack();
            frame.setVisible( true );
            frame.setSize(800,600);        	
            while(true) {
                String message = connImpl.receiveMessage(token);
                if (!message.isEmpty()) {
                    System.out.println(message);
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String args[])
    {
    	
    	
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");

            // Use NamingContextExt instead of NamingContext. This is
            // part of the Interoperable naming Service.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // resolve the Object Reference in Naming
            String name = "Conn";
            connImpl = ChatHelper.narrow(ncRef.resolve_str(name));

            System.out.println("Obtained a handle on server object: " + connImpl);
            token = connImpl.connect();

            new Thread(new Input()).start();
            new Thread(new Output()).start();

        } catch (Exception e) {
            System.out.println("ERROR : " + e) ;
            e.printStackTrace(System.out);
        }
    }

}