package chat;


/**
* chat/Message.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Chat.idl
* Wednesday, April 25, 2018 9:39:15 PM EET
*/

public final class Message implements org.omg.CORBA.portable.IDLEntity
{
  public String content = null;
  public String senderToken = null;

  public Message ()
  {
  } // ctor

  public Message (String _content, String _senderToken)
  {
    content = _content;
    senderToken = _senderToken;
  } // ctor

} // class Message
