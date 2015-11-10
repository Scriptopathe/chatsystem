package chatsystem;


import java.util.*;
import java.io.*;
import java.net.*;

public class TCPReceiver extends Thread{
	
	private Socket socket;
	private OutputStream stream;
	private List<TCPReceiverListener> listeners;
	
	
	public TCPReceiver(Socket socket, OutputStream stream)
	{
		this.socket = socket;
		this.stream = stream;
		this.listeners = new ArrayList<TCPReceiverListener>();
	}
	
	public void run()
	{
		try 
		{
			DataInputStream in = new DataInputStream(this.socket.getInputStream());
			DataOutputStream out = new DataOutputStream(this.stream);
			byte[] buffer = new byte[1024];
			int size = 0;
			while(size != -1)
			{
				size = in.read(buffer);
				if(size != -1)
					out.write(buffer, 0, size);
			}
			
			notifyEnd(this.socket.getInetAddress());
			
			socket.close();
			in.close();
			out.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(!socket.isClosed())
			{
				try 
				{
					socket.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	
	
	
	public void addListeners(TCPReceiverListener t)
	{
		this.listeners.add(t);
	}


	public void notifyProgress(InetAddress source, int progress) {
		
		for(TCPReceiverListener l : this.listeners)
		{
			l.onNotifyProgress(source, progress);
		}
	}
	
	
	public void notifyEnd(InetAddress source) {
		
		for(TCPReceiverListener l : this.listeners)
		{
			l.onNotifyEnd(source);
		}
	}
	
	
	
}
