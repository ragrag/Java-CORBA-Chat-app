package server;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import server.Message;
import server.User;

public class Room {
    ReadWriteLock lock = new ReentrantReadWriteLock();
    final String name;
    HashMap<String, User> users = new HashMap<String, User>();
    ArrayList<Message> messages = new ArrayList<Message>();


    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean addMessage(User user, Message message) { //sm
        lock.writeLock().lock();
        messages.add(message);
        
        for (String s : users.keySet()) {
            users.get(s).addMessage(message.toString());
        }
        lock.writeLock().unlock();
        return true;
    }

    public ArrayList<Message> getMessages() {
        lock.readLock().lock();
        ArrayList<Message> copy = new ArrayList<Message>(messages);
        lock.readLock().unlock();
        return copy;
    }

    public void addUser(User user) {
    	lock.writeLock().lock();
        users.put(user.getToken(), user);
        user.setChatRoom(this.name);
        lock.writeLock().unlock();
    }

    public void removeUser(User user) {
        lock.writeLock().lock();
        users.remove(user.getToken());
        user.setChatRoom(null);
        lock.writeLock().unlock();
    } 
}
