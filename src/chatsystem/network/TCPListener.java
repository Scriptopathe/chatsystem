package chatsystem.network;

import java.io.*;
import java.util.*;
import java.net.*;

import chatsystem.model.UserList;

/**
 * TCPListener enregistre les connexions entrantes, et permet de les récupérer
 * via getSocket().
 * @author scriptopathe
 *
 */
public class TCPListener extends Thread
{
	private int sourcePort;
	private ServerSocket socket;
	private HashMap<String, Socket> acceptedSockets;
	
	public TCPListener(int port) throws IOException
	{
		this.sourcePort = port;
		this.socket = new ServerSocket(port);
		this.acceptedSockets = new HashMap<String, Socket>();
	}
	
	/**
	 * Transforme une addresse IP en un ID qui permet de l'identifier
	 * dans la hashmap.
	 */
	private String toId(InetAddress addr)
	{
		String addname = addr.toString();
		return addname.split("/")[1];
	}
	
	/**
	 * Obtient le socket enregistré correspondant à l'addresse IP donnée.
	 * @return le socket correspondant à l'IP source donnée s'il a été enregistré
	 * null sinon.
	 */
	public Socket popSocket(InetAddress addr)
	{
		String addrname = toId(addr);
		if(acceptedSockets.containsKey(addrname))
		{
			Socket sock = acceptedSockets.get(addrname);
			acceptedSockets.remove(sock);
			return sock;
			
		}
		return null;
	}
	
	
	public void run()
	{
		while(true)
		{	
			try 
			{
				Socket sock = this.socket.accept();
				this.acceptedSockets.put(toId(sock.getInetAddress()), sock);
			}
			catch (IOException e) 
			{
				break;
			}
		}
	}

	public void dispose() {
		try 
		{
			this.socket.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	
	
}
