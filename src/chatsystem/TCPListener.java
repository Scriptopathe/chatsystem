package chatsystem;

import java.io.*;
import java.util.*;
import java.net.*;

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
	
	private String toId(InetAddress addr)
	{
		String addname = addr.toString();
		return addname.split("/")[1];
	}
	
	public Socket getSocket(InetAddress addr)
	{
		String addrname = toId(addr);
		if(acceptedSockets.containsKey(addrname))
			return acceptedSockets.get(addrname);
		return null;
	}
	
	@Override
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
