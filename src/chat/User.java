package chat;


/**
* chat/User.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Chat.idl
* Wednesday, April 25, 2018 9:39:15 PM EET
*/

public final class User implements org.omg.CORBA.portable.IDLEntity
{
  public String token = null;
  public String name = null;

  public User ()
  {
  } // ctor

  public User (String _token, String _name)
  {
    token = _token;
    name = _name;
  } // ctor

} // class User
