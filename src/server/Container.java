package server;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

public class Container { //CLass that will act as a container 
	int userCounter = 1;		//User counter
    String userDefName = "User";
  

    HashMap<String, User> users = new HashMap<String, User>(); //User hashmap Token:User
    HashMap<String, Room> chatRooms = new HashMap<String, Room>(); //Users hashmao roomName:Room

    public synchronized void addChatRoom(final String name) { //Create add Chat room to hashmap
        try {
			chatRooms.put(name, new Room(name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public synchronized Room getChatRoom(String name) { //Get Chat room from hashmap
        return chatRooms.get(name);
    }

    public synchronized  ArrayList<String> getChatRooms() {  //Get all Chat rooms from hashmap
        return  new ArrayList<String>(chatRooms.keySet());
    }

    public synchronized String addUser() { //Create and add new user to the users hashmap and return the respective token
        String token = generateToken();
        User user = new User(token, userDefName + userCounter++ );
        users.put(token, user);
        return token;
    }

    public  synchronized  User getUser(String token) { //Get user by token
        return users.get(token);
    }

    String generateToken() { //Funciton used to generate random token by SecureRandom
        return new BigInteger(124, new SecureRandom()).toString(32);
    }
}
