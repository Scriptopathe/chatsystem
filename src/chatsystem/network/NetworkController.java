package chatsystem.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import chatsystem.controler.MainController;
import chatsystem.messages.FileRequestMessage;
import chatsystem.messages.Message;
/**
 * Classe façade pour les services réseaux.
 */
public class NetworkController implements UDPReceiverListener
{	
	public final static int PORT = 8045;
	public final static boolean VERBOSE = true;
	private UDPSender udpSender;
	private UDPReceiver udpReceiver;
	private MainController mainController;
	private TCPListener tcpListener;
	
	public NetworkController(MainController ctrl) throws IOException
	{
		this.udpReceiver = new UDPReceiver(PORT);
		this.udpSender = new UDPSender(PORT);
		this.tcpListener = new TCPListener(PORT);
		this.mainController = ctrl;
		
		// Démarrage du receiver.
		this.udpReceiver.start();
		this.udpReceiver.addListener(this);
		this.tcpListener.start();
	}
		
	/**
	 * Envoie le message msg à l'adresse ip addr.
	 */
	public void sendMessage(InetAddress addr, Message msg) throws IOException
	{
		if(VERBOSE)
			System.out.println("[Network] Sent message : " + msg.toJSON());
		
		this.udpSender.sendMessage(addr, msg);
	}
	
	/** Envoie le fichier à l'addresse ip addr */
	public void sendFile(InetAddress addr, String path, TCPProgressListener listener) throws FileNotFoundException
	{
		if(VERBOSE)
			System.out.println("[Network] Starting sending file " + path);
		
		FileInputStream stream = new FileInputStream(path);
		TCPSender sender = new TCPSender(PORT, addr, stream);
		sender.addListener(listener);
		sender.start();
		
		if(VERBOSE)
			System.out.println("[Network] Started sending file " + path);
	}
	
	/**
	 * Lance la réception du fichier correspond à la file request donnée, depuis l'addresse source donnée.
	 */
	public void receiveFile(InetAddress from, final FileRequestMessage fileRequest, TCPProgressListener listener)
	{
		try 
		{
			String outputPath = new File(fileRequest.getFileName()).getName();
			FileOutputStream stream = new FileOutputStream("downloads/" + outputPath);
			TCPReceiver receiver = new TCPReceiver(tcpListener, from, stream);
			
			receiver.start();
			receiver.addListener(listener);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(VERBOSE)
			System.out.println("[Network] Received message " + message);
		
		Message msg = Message.createFromJSON(message);
		mainController.processMessage(source, msg);
	}

	public void dispose() {
		this.udpReceiver.dispose();
		this.udpSender.dispose();
		this.tcpListener.dispose();
		
	}
}
