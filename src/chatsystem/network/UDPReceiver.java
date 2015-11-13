package chatsystem.network;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.nio.charset.Charset;
import java.util.List;

public class UDPReceiver extends Thread
{
	/* ------------------------------------------------------------------------
	 * Fields
	 * ----------------------------------------------------------------------*/
	private int port;
	private DatagramSocket sock;
	private List<UDPReceiverListener> listeners;
	private boolean isOK;
	
	/* ------------------------------------------------------------------------
	 * Methods
	 * ----------------------------------------------------------------------*/
	/**
	 * Crée une nouvelle instance de UDPReceiver.
	 * @param port port sur lequel écouter.
	 * @throws SocketException Lancée si le socket ne peut pas être utilisé. 
	 * (ex : port déjà en cours d'utilisation)
	 */
	public UDPReceiver(int port) throws SocketException
	{
		this.port = port;
		this.listeners = new ArrayList<UDPReceiverListener>();
		this.isOK = true;
		this.sock = new DatagramSocket(this.port);
	}
	
	@Override
	public void run() {
		byte[] buffer = new byte[4096*4];
		while(true)
		{
	        DatagramPacket data = new DatagramPacket(buffer, buffer.length); 
	        try 
	        {
				sock.receive(data);
				InetAddress srcAddr = data.getAddress();
				String str = new String(data.getData(), 0, data.getLength(), Charset.forName("UTF-8"));
				this.notifyListeners(srcAddr, str);
			} 
	        catch (IOException e) 
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	
	
	public void addListener(UDPReceiverListener l)
	{
		this.listeners.add(l);
	}
	
	
	private void notifyListeners(InetAddress source, String message)
	{
		for(UDPReceiverListener l : this.listeners)
		{
			l.onMessageReceived(source, message);
		}
	}
	
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	public boolean isOK() {
		return isOK;
	}

	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}
	
	
	
}
