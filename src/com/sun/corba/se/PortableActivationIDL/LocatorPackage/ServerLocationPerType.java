package com.sun.corba.se.PortableActivationIDL.LocatorPackage;


/**
* com/sun/corba/se/PortableActivationIDL/LocatorPackage/ServerLocationPerType.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/java_re/workspace/8-2-build-macosx-x86_64/jdk8u172/10810/corba/src/share/classes/com/sun/corba/se/PortableActivationIDL/activation.idl
* Wednesday, March 28, 2018 3:39:56 PM PDT
*/

public final class ServerLocationPerType implements org.omg.CORBA.portable.IDLEntity
{
  public String hostname = null;
  public com.sun.corba.se.PortableActivationIDL.ORBPortInfo ports[] = null;

  public ServerLocationPerType ()
  {
  } // ctor

  public ServerLocationPerType (String _hostname, com.sun.corba.se.PortableActivationIDL.ORBPortInfo[] _ports)
  {
    hostname = _hostname;
    ports = _ports;
  } // ctor

} // class ServerLocationPerType
