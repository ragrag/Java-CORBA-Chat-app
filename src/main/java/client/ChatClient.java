package client;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import chat.Chat;
import chat.ChatHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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

    static Chat chatImpl;
    static String token;
   static JFrame frame = new JFrame();

    public static class Input implements Runnable {
 
    	
    	 private void initUI() {
    	  
    	   

    	        final JTextField textfield = new JTextField(20);
    	        
    	        JLabel curRoom = new JLabel("Current Room : None");
    	        curRoom.setForeground(Color.red);
    	        JLabel curName= new JLabel();
    	        curName.setText("Current name : " + chatImpl.getName(token));
    	        JButton createBtn = new JButton("Create Room");
    	        JButton joinBtn = new JButton("Join Room");
    	        JButton leaveBtn = new JButton("leave Room");
    	        JButton roomListBtn = new JButton("Room List");
    	        JButton nameBtn = new JButton("Change Name");

    	        
    	        frame.add( curRoom);
       	        frame.add( curName);
    	        JLabel enterMsg = new JLabel("Enter a message");
    	        enterMsg.setFont(new Font("Verdana", Font.ITALIC,12));
    	        frame.add( enterMsg );
    	        frame.add(textfield);
    	        frame.add(nameBtn);
    	        frame.add(createBtn);
    	        frame.add( joinBtn );
    	        frame.add(leaveBtn);
    	        frame.add(roomListBtn);
    	        frame.setSize(400,600 );
    	        frame.setVisible(true);
    	        
    	        
    	        
    	        textfield.addActionListener(new ActionListener() {

    	            @Override
    	            public void actionPerformed(ActionEvent e) {
    	                try {
    	                    String text = textfield.getText();
    	                    InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
    	                    chatImpl.sendMessage(token, text);
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
    	        	  String input;
    	        	  input = JOptionPane.showInputDialog("Enter Room Name");
    	        	  
		        	if(input.equals(""))
		        			JOptionPane.showMessageDialog(null, "Failed, Make sure the field is not empty");
		        	else {
		        		if(chatImpl.createChatRoom(token, input))  {
    	        		JOptionPane.showMessageDialog(null, "Room "+ input + " Created!");
		        		}
		        		else 
    	        		JOptionPane.showMessageDialog(null, "Failed, Try Again");
    	        	  }
    	          }
    	        });
    	        
    	      
    	        joinBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	        	  String input;
    	        	  input = JOptionPane.showInputDialog("Enter Room Name");
    	        	  
    	        	  if(input.equals(""))
		        			JOptionPane.showMessageDialog(null, "Failed, Make sure the field is not empty");
		        	else {
		        		if(chatImpl.joinChatRoom(token, input)) 
		        		{
		        			
  	        		JOptionPane.showMessageDialog(null, "Joined Room "+ input + "!");
  	        		curRoom.setText("Current Room : "+input);
  	        		curRoom.setForeground(Color.green);
		        		}
		        		else 
  	        		JOptionPane.showMessageDialog(null, "Failed, Try Again");
  	        	  }
    	          }
    	        });
    	        
    	        leaveBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	        	 
		        		if(chatImpl.leaveChatRoom(token)) {
  	        		JOptionPane.showMessageDialog(null, "Left Room!");
  	        		curRoom.setText("Current Room : None");
  	        		curRoom.setForeground(Color.red);
		        		}
		        		else 
  	        		JOptionPane.showMessageDialog(null, "Failed, Try Again");
  	        	  }
    	          
    	        });
    	        
    	        roomListBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
  	        		JOptionPane.showMessageDialog(null, chatImpl.listChatRooms(token));
    	          }
    	        });
    	        
    	        nameBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	        	  
    	        	  String input;
    	        	  input = JOptionPane.showInputDialog("Enter New Name");
    	        	  if(input.equals(""))
		        			JOptionPane.showMessageDialog(null, "Failed, Make sure the field is not empty");
		        	else {
		        		if(chatImpl.changeName(token, input))  {
  	        		JOptionPane.showMessageDialog(null, "Name Changed to " + input);
  	        		curName.setText("Current Name : "+chatImpl.getName(token));
		        		}
		        		else 
  	        		JOptionPane.showMessageDialog(null, "Failed, Try Again");
  	        	  }
    	          }
    	        });
    	        
    	    }
    	
        public void run() {
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	initUI();
        }

   
     
    }

    public static class Output implements Runnable {

        public void run() {

            JTextArea ta = new JTextArea();
            TextAreaOutputStream taos = new TextAreaOutputStream( ta, 60 );
            PrintStream ps = new PrintStream( taos );
            System.setOut( ps );
            System.setErr( ps );

            frame.add( new JLabel("Output" ) );
            JScrollPane sp=  new JScrollPane (ta);
            sp.setPreferredSize(new Dimension(40,300));
            frame.add( sp  );
            frame.pack();

            while(true) {
                String message = chatImpl.receiveMessage(token);
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
    	
        BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        frame.setLayout(boxLayout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            chatImpl = ChatHelper.narrow(ncRef.resolve_str(name));

            System.out.println("Obtained a handle on server object: " + chatImpl);
            token = chatImpl.connect();
            
            new Thread(new Output()).start();
            new Thread(new Input()).start();
           

        } catch (Exception e) {
            System.out.println("ERROR : " + e) ;
            e.printStackTrace(System.out);
        }
    }

}