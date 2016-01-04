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
	
	private OutputStream stream;
	private List<TCPProgressListener> listeners;
	private TCPServer tcpListener;
	private InetAddress sourceAddress;
	
	/**
	 * Crée une nouvelle instance de TCPReceiver à partir d'un socket et d'un flux
	 * de sortie.
	 * @param socket le socket sur lequel lire les données.
	 * @param stream le stream dans lequel écrire les données lues sur le socket.
	 */
	public TCPReceiver(TCPServer listener, InetAddress from, OutputStream stream)
	{
		this.tcpListener = listener;
		this.sourceAddress = from;
		this.stream = stream;
		this.listeners = new ArrayList<TCPProgressListener>();
	}
	
	public void run()
	{
		try 
		{
			// On attend que le listener nous donne un socket.
			Socket socket = null;
			while(socket == null) 
			{
				socket = this.tcpListener.popSocket(this.sourceAddress);
			}
			
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(this.stream);
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int oneKilo = 1024;
			int progressUnit = 1024 * oneKilo / bufferSize;
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
					if(it % (progressUnit) == 0)
						notifyProgress(socket.getInetAddress(), bytesRead/oneKilo);
				}
				
			}
			
			notifyEnd(socket.getInetAddress());
			
			socket.close();
			in.close();
			out.close();
			this.stream.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			/*if(!socket.isClosed())
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
			}*/
		}	
	}
	
	
	/**
	 * Ajoute un listener aux évènements de TCPReceiver.
	 */
	public void addListener(TCPProgressListener t)
	{
		this.listeners.add(t);
	}


	private void notifyProgress(InetAddress source, int progress) {
		
		for(TCPProgressListener l : this.listeners)
		{
			l.onNotifyProgress(source, progress);
		}
	}
	
	
	private void notifyEnd(InetAddress source) {
		
		for(TCPProgressListener l : this.listeners)
		{
			l.onNotifyEnd(source);
		}
	}
	
	
	
}
