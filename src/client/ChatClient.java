package client;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import chat.Chat;
import chat.ChatHelper;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;


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


    static Chat chatImpl;	//Server implementation instance
    static String token;	//Token used to identify different users in ther Server's Hashmap
    static JFrame frame = new JFrame();		//Jframe Initilization, Global to work on both input and output threads
    private static final PrintStream SYSTEM_OUT = System.out;	//Get input from JTextField
    public static class Input implements Runnable { //Input Thread that has JFrame buttons and input field
 
    	
    	 private void initUI() { //Function called in run() to initialize GUI when thread is started
    		 												//GUI Buttons,Labels and TextFields	
    	        final JTextField userInput = new JTextField(20);		
					
    	        JLabel curRoom = new JLabel("Current Room : None");
    	        curRoom.setForeground(Color.red);
    	        curRoom.setFont(new Font("Verdana", Font.BOLD,15));
    	        JLabel curName= new JLabel();
    	        curName.setText("Current Name : " + chatImpl.getName(new chat.User(token,"")));
    	        curName.setFont(new Font("Verdana", Font.BOLD,15));
    	        JButton createBtn = new JButton("Create Room");
    	        JButton joinBtn = new JButton("Join Room");
    	        JButton leaveBtn = new JButton("leave Room");
    	        JButton roomListBtn = new JButton("Room List");
    	        JButton nameBtn = new JButton("Change Name");
    	        
    	  
    	        						//Adding GUI elements to JFrame 
    	        frame.add( curRoom);
       	        frame.add( curName);
    	        JLabel enterMsg = new JLabel("Enter a message");
    	        enterMsg.setFont(new Font("Verdana", Font.ITALIC,13));
    	        frame.add( enterMsg );
    	        frame.add(userInput);
    	        frame.add(nameBtn);
    	        frame.add(createBtn);
    	        frame.add( joinBtn );
    	        frame.add(leaveBtn);
    	        frame.add(roomListBtn);
    	        frame.setSize(400,600 );
    	        frame.setVisible(true);
    	        		//Action Listener to detect Enter key press to read from userInput JTF	
    	        userInput.addActionListener(new ActionListener() {		

    	            @Override
    	            public void actionPerformed(ActionEvent e) {
    	                try {
    	                    String input = userInput.getText();
    	                    InputStream is = new ByteArrayInputStream(input.getBytes("UTF-8"));
    	                    if(!input.equals(""))
    	                    {	
    	                    	chatImpl.sendMessage(new chat.Message(input,token));
    	                    	userInput.setText("");
    	                    }
    	                    System.err.println(input);
    	                } catch (UnsupportedEncodingException e1) {
    	                    e1.printStackTrace();
    	                }

    	            }
    	        });
    	        							//Action Listeners on GUI Buttons
    	        createBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
    	        	  String input;
    	        	  input = JOptionPane.showInputDialog("Enter Room Name");
    	        	  
		        	if(input.equals(""))
		        			JOptionPane.showMessageDialog(null, "Failed, Make sure the field is not empty");
		        	else {
		        		if(chatImpl.createChatRoom( new chat.Room(input)))  {
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
		        		if(chatImpl.joinChatRoom(new chat.User(token,""), new chat.Room(input))) 
		        		{
		        			
  	        		JOptionPane.showMessageDialog(null, "Joined Room "+ input + "!");
  	        		curRoom.setText("Current Room : "+input);
  	        		curRoom.setForeground(new Color(0,160,0));
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
    	        	 
		        		if(chatImpl.leaveChatRoom(new chat.User(token,""))) {
  	        		JOptionPane.showMessageDialog(null, "Left Room!");
  	        		curRoom.setText("Current Room : None");
  	        		curRoom.setForeground(Color.red);
		        		}
		        		else 
  	        		JOptionPane.showMessageDialog(null, "Your are not in a room.");
  	        	  }
    	          
    	        });
    	        
    	        roomListBtn.addActionListener(new ActionListener()
    	        {
    	          public void actionPerformed(ActionEvent e)
    	          {
  	        		JOptionPane.showMessageDialog(null, chatImpl.listChatRooms());
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
		        		if(chatImpl.changeName(new chat.User(token, input)))  {
  	        		JOptionPane.showMessageDialog(null, "Name Changed to " + input);
  	        		curName.setText("Current Name : "+chatImpl.getName(new chat.User(token,"")));
  	        		frame.setTitle(input);
		        		}
		        		else 
  	        		JOptionPane.showMessageDialog(null, "Failed, Try Again");
  	        	  }
    	          }
    	        });
    	        
    	    }
    	
        public void run() {
    	try {	//Sleep after thread start to make sure output thread finishes flooding JFrame first
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	initUI();
        }

   
     
    }

    public static class Output implements Runnable {	//Output threada

        public void run() {
        								//Output Text area init
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

            while(true) {		//Keep Receiving messages from server

                String message = chatImpl.receiveMessage(new chat.User(token,""));
                if (!message.isEmpty()) {
                    System.out.println(message);
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    	System.out.println("ex");
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
            //Orb initilization
            ORB orb = ORB.init(args, null);

            //Fetching the naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            //Object Reference
            String name = "ChatCon";
            chatImpl = ChatHelper.narrow(ncRef.resolve_str(name));
            
            System.out.println("Connected ");
            token = chatImpl.connect(); //Receive token from server
            
            //Input/Output threads start
            new Thread(new Output()).start();
            new Thread(new Input()).start();
           

        } catch (Exception e) {
            System.out.println("ERROR : " + e) ;
            e.printStackTrace(System.out);
        }
        
        frame.setTitle(chatImpl.getName(new chat.User(token,""))); //Set Jframe name as user name
    }

}