package server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

	
    String user;  //Message sender
    String content; //Message content
    Date date;	//Message date (Auto-set on new)
    
    public Message(String user, String content) { //Message Constructor
        this.user = user;
        this.content = content;
        this.date = new Date();
    }

    public String toString() { //Message to string
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date) + " - " + user + ": " + content;
    }
}
