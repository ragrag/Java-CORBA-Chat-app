package server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatRoom {
    ReadWriteLock lock = new ReentrantReadWriteLock();
    final String name;
    HashMap<String, User> users = new HashMap<String, User>();
    ArrayList<Message> messages = new ArrayList<Message>();
    boolean expired = false;

    public ChatRoom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMessage(User user, Message message) {
        lock.writeLock().lock();
        if (!expired) {
            messages.add(message);
            for (String s : users.keySet()) {
                users.get(s).addMessage(message.toString());
            }
        } else {
            user.addMessage("Error: Chat room has been deleted.");
        }
        lock.writeLock().unlock();
    }

    public ArrayList<Message> getMessages() {
        lock.readLock().lock();
        ArrayList<Message> copy = new ArrayList<Message>(messages);
        lock.readLock().unlock();
        return copy;
    }

    public void addUser(User user) {
        lock.writeLock().lock();
        if (!expired) {
            users.put(user.getToken(), user);
            user.setChatRoom(this.name);
            for (Message message : messages) {
                user.addMessage(message.toString());
            }
        } else {
            user.addMessage("Error: Chat room has been deleted.");
        }
        lock.writeLock().unlock();
    }

    public void removeUser(User user) {
        lock.writeLock().lock();
        users.remove(user.getToken());
        user.setChatRoom(null);
        lock.writeLock().unlock();
    }

    public void expire() {
        lock.writeLock().lock();
        expired = true;
        for (String s : users.keySet()) {
            User user = users.get(s);
            user.setChatRoom(null);
            user.addMessage("Notice: Your current chat room has been deleted");
        }
        lock.writeLock().unlock();
    }



 
}
