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

import java.util.ArrayList;

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
        if (user != null) {
            String chatName = user.getChatRoom();
            ChatRoom chatRoom = server.getChatRoom(chatName);
            if (chatRoom != null) {
                Message chatMessage = new Message(user.getName(), message);
                chatRoom.addMessage(user, chatMessage);
            } else {
                user.addMessage("Error: You cannot send a message within the lobby.");
            }
        } else {
            System.out.println("Invalid token given for sending message.");
        }
    }

    public String receiveMessage(String token) {
        User user = server.getUser(token);
        if (user != null) {
            String message = user.getMessage();
            if (message != null) {
                return message;
            } else {
                return "";
            }
        } else {
            System.out.println("Invalid token given for receiving message.");
            return "";
        }
    }

    public boolean createChatRoom(String token, String name) {
        User user = server.getUser(token);
        if (user != null) {
            server.addChatRoom(name);
            user.addMessage("Notice: Created " + name + ".");
            return true;
        } else {
            System.out.println("Invalid token given for creating chat room.");
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
            System.out.println("Invalid token given for listing chat rooms.");
            return "Failed, try again";
        }
    }

    public boolean joinChatRoom(String token, String name) {
        User user = server.getUser(token);
        if (user != null) {
            ChatRoom chatRoom = server.getChatRoom(name);
            if (chatRoom != null) {
                user.addMessage("Notice: Joined " + name +  ".");
                chatRoom.addUser(user);
                return true;
            } else {
                user.addMessage("Error: The specified channel does not exist.");
                return false;
            }
        } else {
            System.out.println("Invalid token given for joining chat room.");
            return false;
        }
    }

    public boolean leaveChatRoom(String token) {
        User user = server.getUser(token);
        if (user != null) {
            String chatName = user.getChatRoom();
            ChatRoom chatRoom = server.getChatRoom(chatName);
            chatRoom.removeUser(user);
            user.addMessage("Notice: Left " + chatName +  ".");
            return true;
        } else {
            System.out.println("Invalid token given for leaving chat room.");
            return false;
        }
    }

    public boolean changeName(String token, String name) {
        User user = server.getUser(token);
        if (user != null) {
            user.setName(name);
            user.addMessage("Notice: Changed name to " + name +  ".");
            return true;
        } else {
            System.out.println("Invalid token given for changing name.");
            return false;
        }
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

            System.out.println("server.ConnServer ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server.ConnServer Exiting ...");

    }
}
