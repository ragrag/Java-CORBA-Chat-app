package server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    static final String DEFAULT_NAME = "User";
    static int userCounter = 1;

    ReadWriteLock lock = new ReentrantReadWriteLock();
    HashMap<String, User> users = new HashMap<String, User>();
    HashMap<String, ChatRoom> chatRooms = new HashMap<String, ChatRoom>();
    SecureRandom random = new SecureRandom();

    public void addChatRoom(final String name) {
        lock.writeLock().lock();
        ChatRoom chatRoom = new ChatRoom(name);
        chatRooms.put(chatRoom.getName(), chatRoom);
        lock.writeLock().unlock();
    }

    public ChatRoom getChatRoom(String name) {
        lock.readLock().lock();
        ChatRoom chatRoom = chatRooms.get(name);
        lock.readLock().unlock();
        return chatRoom;
    }

    public void removeChatRoom(String name) {
        lock.writeLock().lock();
        ChatRoom chatRoom = chatRooms.remove(name);
        lock.writeLock().unlock();
    }

    public ArrayList<String> getChatRooms() {
        lock.readLock().lock();
        ArrayList<String> names = new ArrayList<String>(chatRooms.keySet());
        lock.readLock().unlock();
        return names;
    }

    public String addUser() {
        lock.writeLock().lock();
        String token = randomString();
        User user = new User(token, DEFAULT_NAME + userCounter++ );
        users.put(user.getToken(), user);
        lock.writeLock().unlock();
        return token;
    }

    public User getUser(String token) {
        lock.readLock().lock();
        User user = users.get(token);
        lock.readLock().unlock();
        return user;
    }

    String randomString() {
        return new BigInteger(130, random).toString(32);
    }
}
