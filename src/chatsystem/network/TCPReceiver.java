package chatsystem.network;


import java.util.*;
import java.io.*;
import java.net.*;

/**
 * TCPReceiver a pour tâche de recevoir des données d'un socket pour
 * les transférer dans un flux de sortie.
 * @author scriptopathe
 *
 */
public class TCPReceiver extends Thread{
	
	private Socket socket;
	private OutputStream stream;
	private List<TCPReceiverListener> listeners;
	
	
	/**
	 * Crée une nouvelle instance de TCPReceiver à partir d'un socket et d'un flux
	 * de sortie.
	 * @param socket le socket sur lequel lire les données.
	 * @param stream le stream dans lequel écrire les données lues sur le socket.
	 */
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
			int totalBytes = in.available();
			int bytesRead = 0;
			int it = 0;
			int size = 0;
			while(size != -1)
			{
				size = in.read(buffer);
				if(size != -1)
				{
					out.write(buffer, 0, size);
					
					// Mise à jour des progrès de téléchargement.
					it++;
					bytesRead += size;
					if(it % 10 == 0)
						notifyProgress(this.socket.getInetAddress(), 100*bytesRead/totalBytes);
				}
				
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
	
	
	/**
	 * Ajoute un listener aux évènements de TCPReceiver.
	 */
	public void addListeners(TCPReceiverListener t)
	{
		this.listeners.add(t);
	}


	private void notifyProgress(InetAddress source, int progress) {
		
		for(TCPReceiverListener l : this.listeners)
		{
			l.onNotifyProgress(source, progress);
		}
	}
	
	
	private void notifyEnd(InetAddress source) {
		
		for(TCPReceiverListener l : this.listeners)
		{
			l.onNotifyEnd(source);
		}
	}
	
	
	
}
