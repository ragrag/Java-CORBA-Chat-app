 package server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {

    final String token;		//User token, mapped to each user in server Hashmap
    String name;			//User name
    String chatRoom;		//Current user chatRoom
    Queue<String> messages = new LinkedList<String>();//User messeges to print in FIFO

    public User(String token, String name) {	//User Constructor
        this.token = token;
        this.name = name;
        chatRoom = "";
    }

    public String getToken() {		//Retrieve user token
        return token;
    }

    public synchronized String getName() {  //Get User name
        String name = this.name;
        return name;
    }

    public synchronized void setName(String name) {  //Set user name
        this.name = name;
    }

    public synchronized String getChatRoom() {	//Get user current chat room
        String chatRoom = this.chatRoom;
        return chatRoom;
    }

    public synchronized void setChatRoom(String chatRoom) { //Set user that room
        this.chatRoom = chatRoom;
    }

    public synchronized void addMessage(String message) { //enqueue message to the messages queue
        messages.add(message);
    }

    public synchronized String getMessage() { //dequeue message from messages queue
        String message = messages.poll();
        return message;
    }
}
