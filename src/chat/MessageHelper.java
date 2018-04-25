package chat;


/**
* chat/MessageHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Chat.idl
* Wednesday, April 25, 2018 9:39:15 PM EET
*/

abstract public class MessageHelper
{
  private static String  _id = "IDL:chat/Message:1.0";

  public static void insert (org.omg.CORBA.Any a, chat.Message that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static chat.Message extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "content",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[1] = new org.omg.CORBA.StructMember (
            "senderToken",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (chat.MessageHelper.id (), "Message", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static chat.Message read (org.omg.CORBA.portable.InputStream istream)
  {
    chat.Message value = new chat.Message ();
    value.content = istream.read_string ();
    value.senderToken = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, chat.Message value)
  {
    ostream.write_string (value.content);
    ostream.write_string (value.senderToken);
  }

}
