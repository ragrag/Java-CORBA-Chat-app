package chat;

/**
* chat/MessageHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Chat.idl
* Wednesday, April 25, 2018 9:39:15 PM EET
*/

public final class MessageHolder implements org.omg.CORBA.portable.Streamable
{
  public chat.Message value = null;

  public MessageHolder ()
  {
  }

  public MessageHolder (chat.Message initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = chat.MessageHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    chat.MessageHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return chat.MessageHelper.type ();
  }

}
