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

class ChatImpl extends ChatPOA { //Server implementation 

    private ORB orb;
    Server server = new Server(); //Server (Container) Instance 

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public String connect() { //Add user when connecting
        return server.addUser();
    }

    public void sendMessage(String token, String message) { //Add user message to chat room
        User user = server.getUser(token);
        Room chatRoom = server.getChatRoom(user.getChatRoom());
        if (chatRoom != null) {	//Check if user is in a room (not in lobby)
            Message chatMessage = new Message(user.getName(), message);
            chatRoom.addMessage(user, chatMessage);
        } else {
            user.addMessage("Join a room first to send messages"); //Display that user isn't in a room
        }
    }

    public String receiveMessage(String token) { //Receive messages from use message queue
    	User user = server.getUser(token);
        String message = user.getMessage();
            if (message != null) {
                return message;
            } 
            return "";
    }

    public boolean createChatRoom(String token, String name) { //Create chat room 
        User user = server.getUser(token);
        server.addChatRoom(name);
        return true;
    }

    public String listChatRooms(String token) { //Get All Chat rooms
        User user = server.getUser(token);
            ArrayList<String> chatNames = server.getChatRooms();
            String str = "Chat Rooms:\n";
            for (String chatName : chatNames) {
                str += chatName + "\n";
            }
            return str;

    }

    public boolean joinChatRoom(String token, String name) { //Join chat room
        User user = server.getUser(token);
        Room chatRoom = server.getChatRoom(name);
        if (chatRoom != null) {
            chatRoom.addUser(user);
            return true;
        } 
        else return false;
    }

    public boolean leaveChatRoom(String token) { //User Leave chat room
        User user = server.getUser(token);
        if (!user.getChatRoom().equals("")) {
            String chatName = user.getChatRoom();
            Room chatRoom = server.getChatRoom(chatName);
            chatRoom.removeUser(user);
            return true;
        } 
        else  return false;
    }

    public boolean changeName(String token, String name) { //Change name 
        server.getUser(token).setName(name);
            return true;
    }


	public String getName(String token) { //Get user name
	     return server.getUser(token).getName();
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
            //Orb initialization
            ORB orb = ORB.init(args, null);

            //get POA Reference
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            //Create and register servlet
            ChatImpl connImpl = new ChatImpl();
            connImpl.setORB(orb);

            //Get Object reference from servlet
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(connImpl);
            Chat href = ChatHelper.narrow(ref);

            //Get naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            //Bind object to reference
            String name = "ChatCon";
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);

            System.out.println("Server started");

            //Start listening
            orb.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

     

    }
}
