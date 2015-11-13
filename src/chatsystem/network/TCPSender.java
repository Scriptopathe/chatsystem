package chatsystem.network;

import java.io.*;
import java.net.*;



public class TCPSender extends Thread {
	private int dstPort;
	private InetAddress addr;
	private InputStream stream;
	
	public TCPSender(int port, InetAddress addr, InputStream stream)
	{
		this.dstPort = port;
		this.addr = addr;
		this.stream = stream;
	}
	
	
	@Override
	public void run() {
		try 
		{
			int size = 0;
			byte[] buffer = new byte[1024];
			Socket socket = new Socket(addr, dstPort);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(this.stream);
			
			// On stream du fichier vers le socket.
			while(size != -1)
			{
				size = in.read(buffer);
				if(size != -1)
					out.write(buffer, 0, size);
			}
			
			socket.close();
			in.close();
			out.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
