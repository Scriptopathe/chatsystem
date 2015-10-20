package chatsystem;
import java.util.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
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
	public UDPSender(int port)
	{
		this.port = port;
		try 
		{
			socket = new DatagramSocket();
		}
		catch (SocketException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
