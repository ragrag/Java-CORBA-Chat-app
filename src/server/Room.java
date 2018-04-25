package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import server.Message;
import server.User;

public class Room {
    String name;	//Room name
    String word;
    int  [] marker = new int[100];
    ArrayList<String> wordList;
    boolean hangman;
    HashMap<String, User> users = new HashMap<String, User>();//Hash map of all users in room Token:User

    public Room(String name) throws FileNotFoundException {		//Constructor
        this.name = name;
        hangman = false;
        Scanner s = new Scanner(new File("words.txt"));
        wordList = new ArrayList<String>();
        while (s.hasNextLine()){
            wordList.add(s.nextLine());
        }
        s.close();
        Arrays.fill(marker,0);
        word = wordList.get((new Random()).nextInt(wordList.size()));
    }

    public String getName() {	//Get room name
        return name;
    }

    public void changeType()
    {
    	hangman = !hangman;
    }
    
    public boolean getType()
    {
    	return hangman;
    }
    public synchronized boolean addMessage(User user, Message message) { //Add message to all message queues of the users
       
    	if (hangman)
    	{
    	String temp = message.content;
        char [] ans = new char[word.length()];
        
        if (!temp.equals(word))
        {
        	for(int i=0;i<word.length();i++)
        	{
        		if(marker[i] == 1 || temp.charAt(0) == word.charAt(i))
        		{
        			marker[i] = 1;
        			ans[i] = word.charAt(i);
        		}
        		else ans[i] = '#';
        	}
        }
        else ans = temp.toCharArray();
        
    	
    	for (String s : users.keySet()) {
            users.get(s).addMessage(message.toString());
            users.get(s).addMessage(String.valueOf(ans));
        }
    	if (String.valueOf(ans).equals(word))
    	{
    		Arrays.fill(marker,0);
	        word = wordList.get((new Random()).nextInt(wordList.size()));
    	}
    	}
    	else 
    	{	
    		for (String s : users.keySet()) {
                users.get(s).addMessage(message.toString());
            }
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
