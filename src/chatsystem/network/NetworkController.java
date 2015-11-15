package chatsystem.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import chatsystem.controler.MainController;
import chatsystem.messages.Message;
/**
 * Classe façade pour les services réseaux.
 */
public class NetworkController implements UDPReceiverListener
{
	public final static int PORT = 8045;
	private UDPSender udpSender;
	private UDPReceiver udpReceiver;
	private MainController mainController;
	
	public NetworkController(MainController ctrl) throws SocketException
	{
		this.udpReceiver = new UDPReceiver(PORT);
		this.udpSender = new UDPSender(PORT);
		this.mainController = ctrl;
		
		// Démarrage du receiver.
		this.udpReceiver.start();
		this.udpReceiver.addListener(this);
	}
	
	/**
	 * Envoie le message msg à l'adresse ip addr.
	 */
	public void sendMessage(InetAddress addr, Message msg) throws IOException
	{
		this.udpSender.sendMessage(addr, msg);
	}
	
	
	/* ------------------------------------------------------------------------
	 * Implémentation de UDP Receiver Listener 
	 * --------------------------------------------------------------------- */
	/**
	 * Méthode appelée lors de la réception d'un message.
	 * @param source adresse source du message.
	 * @param message message reçu.
	 */
	public void onMessageReceived(InetAddress source, String message) 
	{
		Message msg = Message.createFromJSON(message);
		mainController.processMessage(source, msg);
	}

	
}
