package server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import chat.Chat;
import chat.ChatHelper;
import chat.ChatPOA;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

class ChatImpl extends ChatPOA {

    private ORB orb;
    Server server = new Server();

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public String connect() {
        return server.addUser();
    }

    public void sendMessage(String token, String message) {
        User user = server.getUser(token);
        String chatName = user.getChatRoom();
        ChatRoom chatRoom = server.getChatRoom(chatName);
        if (chatRoom != null) {
            Message chatMessage = new Message(user.getName(), message);
            chatRoom.addMessage(user, chatMessage);
        } else {
            user.addMessage("Join a room first to send messages");
        }
    }

    public String receiveMessage(String token) {
    	//  System.out.println(token);
    	User user = server.getUser(token);
      
            String message = user.getMessage();
            if (message != null) {
                return message;
            } else {
                return "";
            }
        
    }

    public boolean createChatRoom(String token, String name) {
        User user = server.getUser(token);
        if (user != null) {
            server.addChatRoom(name);
            return true;
        } else {
            return false;
        }
    }

    public String listChatRooms(String token) {
        User user = server.getUser(token);
        if (user != null) {
            ArrayList<String> chatNames = server.getChatRooms();
            String str = "Chat Rooms:\n";
            for (String chatName : chatNames) {
                str += chatName + "\n";
            }
            
            return str;
        } else {
            return "Failed, try again";
        }
    }

    public boolean joinChatRoom(String token, String name) {
        User user = server.getUser(token);
        if (user != null) {
            ChatRoom chatRoom = server.getChatRoom(name);
            if (chatRoom != null) {
                chatRoom.addUser(user);
                return true;
            } 
        } 
            return false;
        
    }

    public boolean leaveChatRoom(String token) {
        User user = server.getUser(token);
        if (user != null) {
            String chatName = user.getChatRoom();
            ChatRoom chatRoom = server.getChatRoom(chatName);
            chatRoom.removeUser(user);
            return true;
        } 
            return false;
    }

    public boolean changeName(String token, String name) {
        User user = server.getUser(token);
        if (user != null) {
            user.setName(name);;
            return true;
        } 
            return false;
    }

	@Override
	public String getName(String token) {
		 User user = server.getUser(token);
	        if (user != null) {
	            return user.getName();
	        } else {
	            return "";
	        }
			
	}
}


public class ChatServer {
    public static void main(String args[]) {
    	
    	
    	
    	JFrame frame = new JFrame();
    	BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
    	frame.setLayout(boxLayout);

        JLabel status = new JLabel("Server Started!");
        status.setFont(new Font("Verdana", Font.BOLD,30));
        status.setForeground(new Color(0,102,0));
        frame.add(status);
        frame.setSize(300,200 );
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            ChatImpl connImpl = new ChatImpl();
            connImpl.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(connImpl);
            Chat href = ChatHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            String name = "Conn";
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);

            System.out.println("Server started");

            // wait for invocations from clients
            orb.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

     

    }
}
