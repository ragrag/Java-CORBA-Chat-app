package server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Server { //CLass that will act as a container 
	static int userCounter = 1;		//User counter
    static final String userDefName = "User";
  

    HashMap<String, User> users = new HashMap<String, User>(); //User hashmap Token:User
    HashMap<String, Room> chatRooms = new HashMap<String, Room>(); //Users hashmao roomName:Room
    SecureRandom tokenGenerator = new SecureRandom();	//used to generate random token

    public synchronized void addChatRoom(final String name) { //Create add Chat room to hashmap
        Room chatRoom = new Room(name);
        chatRooms.put(chatRoom.getName(), chatRoom);
    }

    public synchronized Room getChatRoom(String name) { //Get Chat room from hashmap
        Room chatRoom = chatRooms.get(name);
        return chatRoom;
    }

    public synchronized  ArrayList<String> getChatRooms() {  //Get all Chat rooms from hashmap
        return  new ArrayList<String>(chatRooms.keySet());
    }

    public synchronized String addUser() { //Create and add new user to the users hashmap and return the respective token
        String token = randomString();
        User user = new User(token, userDefName + userCounter++ );
        users.put(token, user);
        return token;
    }

    public  synchronized  User getUser(String token) { //Get user by token
        return users.get(token);
    }

    String randomString() { //Funciton used to generate random token by SecureRandom
        return new BigInteger(130, tokenGenerator).toString(32);
    }
}
