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
    Container container = new Container(); //Container Instance 


    public void setORB(ORB orb) { //Orb setter
         this.orb = orb;
     }

    
    
    public String getToken() { //Add user and receive his token
        return container.addUser();
    }


								//ChangeRoom type
	public boolean roomType(chat.User user) {
		User _user = container.getUser(user.token);
        if (!_user.getChatRoom().equals("")) {
    		container.getChatRoom(_user.getChatRoom()).changeType();
            return true;
        } 
        else  return false;
		
	}


					//getRoom type
	public boolean getRoomType(chat.User user) {
		User _user = container.getUser(user.token);
    	return container.getChatRoom(_user.getChatRoom()).getType();
	}
						//Get Room name
	public String getRoom(chat.User user) {
		User _user = container.getUser(user.token);
    	return _user.getChatRoom();
	}



	public String getName(chat.User user) { //Get user name
	     return container.getUser(user.token).getName();
	}

 
    public void sendMessage(chat.Message msg) { //Add user message to chat room
        User _user = container.getUser(msg.senderToken);
        Room chatRoom = container.getChatRoom(_user.getChatRoom());
        if (chatRoom != null) {	//Check if user is in a room (not in lobby)
            Message chatMessage = new Message(_user.getName(), msg.content);
            chatRoom.addMessage(_user, chatMessage);
        } else {
            _user.addMessage("Join a room first to send messages"); //Display that user isn't in a room
        }
    }

    public String getMessage(chat.User user) { //Receive messages from use message queue
    	User _user = container.getUser(user.token);
        String message = _user.getMessage();
            if (message != null) {
                return message;
            } 
            return "";
    }



    
    public String listChatRooms() { //Get All Chat rooms
        ArrayList<String> allRooms = container.getChatRooms();
        if ( allRooms.size() == 0 )
        {
        	return "There are currently no rooms";
        }
        String roomList = "Chat Rooms (" + allRooms.size() + ") :\n";
        for (String room : allRooms) {
            roomList += room+ "\n";
        }
        return roomList;
}

	
										//Create chat room
	public boolean createChatRoom(chat.Room room) {
        container.addChatRoom(room.name);
        return true;
	}

									//Add user to chat room
	public boolean joinChatRoom(chat.User user, chat.Room room) {
		User _user = container.getUser(user.token);
        Room chatRoom = container.getChatRoom(room.name);
        if (chatRoom != null) {
            chatRoom.addUser(_user);
            return true;
        } 
        else return false;
	}

								//Remove user from chat room
	public boolean leaveChatRoom(chat.User user) {
		User _user = container.getUser(user.token);
        if (!_user.getChatRoom().equals("")) {
    		container.getChatRoom(_user.getChatRoom()).removeUser(_user);
            return true;
        } 
        else  return false;
	}


	public boolean changeName(chat.User user) {
		container.getUser(user.token).setName(user.name);
        return true;
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
