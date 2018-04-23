package server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
	static int userCounter = 1;
    static final String userDefName = "User";
  

    ReadWriteLock lock = new ReentrantReadWriteLock();
    HashMap<String, User> users = new HashMap<String, User>();
    HashMap<String, Room> chatRooms = new HashMap<String, Room>();
    SecureRandom random = new SecureRandom();

    public void addChatRoom(final String name) {
        lock.writeLock().lock();
        Room chatRoom = new Room(name);
        chatRooms.put(chatRoom.getName(), chatRoom);
        lock.writeLock().unlock();
    }

    public Room getChatRoom(String name) {
        lock.readLock().lock();
        Room chatRoom = chatRooms.get(name);
        lock.readLock().unlock();
        return chatRoom;
    }

    public void removeChatRoom(String name) {
        lock.writeLock().lock();
        Room chatRoom = chatRooms.remove(name);
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
        User user = new User(token, userDefName + userCounter++ );
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
