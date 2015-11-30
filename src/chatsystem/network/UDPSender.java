package chatsystem.network;
import java.util.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;

import chatsystem.messages.Message;
/*
 * TODO pense bête
 * Pour le fichier, le FileMessage et lancera un thread pour chaque connection TCP.
 * (TCPSender)
 */
public class UDPSender 
{
	/* ------------------------------------------------------------------------
	 * Fields
	 * ----------------------------------------------------------------------*/
	private int port;
	private DatagramSocket socket;
	
	/* ------------------------------------------------------------------------
	 * Methods
	 * ----------------------------------------------------------------------*/
	public UDPSender(int port) throws SocketException 
	{
		this.port = port;
		this.socket = new DatagramSocket();
	}	
	
	/**
	 * 
	 * @param msg message à envoyer
	 * @param addr adresse destination
	 * @throws IOException 
	 */
	public void sendMessage(InetAddress addr, Message msg) throws IOException
	{
		String msgStr = msg.toJSON();
		byte[] buf = msgStr.getBytes(Charset.forName("UTF-8"));
		socket.send(new DatagramPacket(buf, buf.length, addr, this.port));
	}

	public void dispose() {
		this.socket.close();
	}
}
