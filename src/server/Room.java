package server;
import java.util.HashMap;


import server.Message;
import server.User;

public class Room {
    final String name;	//Room name
    HashMap<String, User> users = new HashMap<String, User>();//Hash map of all users in room Token:User

    public Room(String name) {		//Constructor
        this.name = name;
    }

    public String getName() {	//Get room name
        return name;
    }

    public synchronized boolean addMessage(User user, Message message) { //Add message to all message queues of the users
        for (String s : users.keySet()) {
            users.get(s).addMessage(message.toString());
        }
        return true;
    }


    public synchronized void addUser(User user) { //Add user to room hashmap
        users.put(user.getToken(), user);
        user.setChatRoom(this.name);
    }

    public synchronized void removeUser(User user) { //Remove user from room hashmap
        users.remove(user.getToken());
        user.setChatRoom(null);

    } 
}
