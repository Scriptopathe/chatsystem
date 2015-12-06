package chatsystem.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Permet l'envoi de données depuis un flux d'entrée vers un socket.
 * @author scriptopathe
 *
 */
public class TCPSender extends Thread {
	private int dstPort;
	private InetAddress addr;
	private InputStream stream;
	private List<TCPProgressListener> listeners;
	
	/**
	 * Crée une nouvelle instance de TCPSender à partir d'une adresse, d'un port et
	 * d'un flux d'entrée.
	 * @param port port sur lequel envoyer les données.
	 * @param addr addresse vers laquelle envoyer les données.
	 * @param stream stream duquel seront lues les données à écrire dans le socket.
	 */
	public TCPSender(int port, InetAddress addr, InputStream stream)
	{
		this.dstPort = port;
		this.addr = addr;
		this.stream = stream;
		this.listeners = new ArrayList<TCPProgressListener>();
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
			
			notifyEnd(socket.getInetAddress());
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
	
	/**
	 * Ajoute un listener aux évènements de TCPSender.
	 */
	public void addListener(TCPProgressListener t)
	{
		this.listeners.add(t);
	}
	
	private void notifyProgress(InetAddress source, int progress) 
	{
		for(TCPProgressListener l : this.listeners)
		{
			l.onNotifyProgress(source, progress);
		}
	}
	
	private void notifyEnd(InetAddress source) 
	{
		for(TCPProgressListener l : this.listeners)
		{
			l.onNotifyEnd(source);
		}
	}
}
