package chatsystem.controler;

import java.io.*;
import java.net.*;
import java.security.Timestamp;
import java.util.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import chatsystem.messages.*;
import chatsystem.model.*;
import chatsystem.network.*;

/**
 * Contrôleur principal de l'application ChatSystem.
 * Il agit entre l'interface graphique et le controleur réseau.
 */
public class MainController implements UIListener
{
	/* ------------------------------------------------------------------------
	 * Variables
	 * ----------------------------------------------------------------------*/
	private NetworkController netControler;
	private UserList userList;
	private String nickname;
	private List<MainControllerListener> listeners;
	/**
	 * Contient les requêtes de transfert fichier entrantes, indexées par l'identifiant (timestamp)
	 * du fichier.
	 */
	private HashMap<Integer, FileRequestMessage> incomingFileRequests;
	/**
	 * Contient les requêtes de transfert fichier sortantes, indexées par l'identifiant (timestamp)
	 * du fichier.
	 */
	private HashMap<Integer, FileRequestMessage> outgoingFileRequests;
	
	/* ------------------------------------------------------------------------
	 * Constructeur
	 * ----------------------------------------------------------------------*/
	public MainController()
	{
		try 
		{
			this.netControler = new NetworkController(this);
			this.userList = new UserList();
			this.listeners = new ArrayList<MainControllerListener>();
			this.incomingFileRequests = new HashMap<Integer, FileRequestMessage>();
			this.outgoingFileRequests = new HashMap<Integer, FileRequestMessage>();
		}
		catch (IOException e) 
		{
			// TODO Une belle message box + exit !!!
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	
	/* ------------------------------------------------------------------------
	 * Messages from NI
	 * ----------------------------------------------------------------------*/
	/**
	 * Traite un message Hello entrant.
	 * @param srcAddr
	 * @param msg
	 */
	private void processHelloMessage(InetAddress srcAddr, HelloMessage hellom)
	{
		User usr = new User(hellom.getNickname(), srcAddr);
		if(getUserList().getUserByIP(usr.getIpaddr()) != null)
		{
			notifyLog("[Error] Conflicting IP address, ABORT MISSION.", true);
			return;
		}
		
		// Enregistre l'utilisateur.
		getUserList().addUser(usr);
		notifyLog("User " + usr + " added.", false);
		notifyUserConnected(usr);
		
		// On répond si besoin.
		if(hellom.getReqReply())
		{
			try 
			{
				this.netControler.sendMessage(usr.getIpaddr(), new HelloMessage(nickname, false));
			}
			catch (IOException e) 
			{
				notifyLog("[Error] Sending hello back to " + usr + ": Unable to send hello back.", true);
				e.printStackTrace();
			}
		}
	}
	/**
	 * Traite un message Bye entrant.
	 * @param srcAddr
	 * @param msg
	 */
	private void processByeMessage(InetAddress srcAddr, ByeMessage msg)
	{
		User usr = getUserList().getUserByIP(srcAddr);
		if(usr == null)
		{
			notifyLog("[Error] Trying to delete User @" + srcAddr + ": user does not exist.", true);
			return;
		}
		// Déconnexion
		notifyLog("User " + usr + " deleted.", false);
		getUserList().removeUser(getUserList().getUserByIP(srcAddr));
		notifyUserDisonnected(usr);
	}
	/**
	 * Traite un message texte entrant.
	 * @param srcAddr
	 * @param msg
	 */
	private void processTextMessage(InetAddress srcAddr, TextMessage msg)
	{
		User usr = getUserList().getUserByIP(srcAddr);
		if(usr == null)
		{
			notifyLog("[Error] Received message from User @" + srcAddr + ": user does not exist.", true);
			return;
		}
		notifyLog("Message received from " + usr + " : " + msg.toJSON(), false);
		notifyMessageReceived(usr, msg.getMessage());
	}
	/**
	 * Traite un message de requête d'envoi de fichier.
	 * @param srcAddr
	 * @param msg
	 */
	private void processFileRequestMessage(InetAddress srcAddr, FileRequestMessage msg)
	{
		User usr = getUserList().getUserByIP(srcAddr);
		notifyLog("Message received from " + usr + " : " + msg.toJSON(), false);
		notifyIncomingFileRequest(usr, msg.getFileName(), msg.getTimestamp());
		this.incomingFileRequests.put(msg.getTimestamp(), msg);
	}
	
	/**
	 * Traite un message de réponse à une requête d'envoi de fichier.
	 * @param srcAddr
	 * @param msg
	 */
	private void processFileRequestResponseMessage(InetAddress srcAddr, FileRequestResponseMessage msg)
	{
		User usr = getUserList().getUserByIP(srcAddr);
		if(!msg.isOk())
		{
			notifyLog("File transfer " + msg.getTimestamp() + " rejected.", true);
			this.outgoingFileRequests.remove(msg.getTimestamp());
			return;
		}
		FileRequestMessage acceptedFileRequest = this.outgoingFileRequests.get(msg.getTimestamp());
		if(acceptedFileRequest != null)
		{
			this.outgoingFileRequests.remove(msg.getTimestamp());
			try 
			{
				notifyLog("Sending file " + acceptedFileRequest.getFileName(), true);
				this.netControler.sendFile(srcAddr, acceptedFileRequest.getFileName());
				// TODO suivi du progrès
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			notifyLog("[Error] Received invalid FileRequestResponseMessage.", true);
		}
	}
	
	/**
	 * Traite le message entrant passé en paramètre.
	 * @param srcAddr addresse de l'émetteur du message.
	 * @param msg message envoyé par l'émetteur.
	 */
	public void processMessage(InetAddress srcAddr, Message msg)
	{
		if(msg instanceof HelloMessage)
		{
			this.processHelloMessage(srcAddr, (HelloMessage)msg);

		}
		else if(msg instanceof ByeMessage)
		{
			this.processByeMessage(srcAddr, (ByeMessage)msg);
		}
		else if(msg instanceof TextMessage)
		{
			this.processTextMessage(srcAddr, (TextMessage)msg);
		}
		else if(msg instanceof FileRequestMessage)
		{
			this.processFileRequestMessage(srcAddr, (FileRequestMessage)msg);

		}
		else if(msg instanceof FileRequestResponseMessage)
		{
			this.processFileRequestResponseMessage(srcAddr, (FileRequestResponseMessage)msg);
		}
	}
	
	/* ------------------------------------------------------------------------
	 * Messages from user
	 * ----------------------------------------------------------------------*/
	/**
	 * Connecte l'utilisateur au Chatsystem avec le nickname donné.
	 * @param nickname
	 */
	public void connect(String nickname)
	{
		try 
		{
			this.nickname = nickname;
			this.netControler.sendMessage(InetAddress.getByName("255.255.255.255"), new HelloMessage(nickname, true));
		} 
		catch (Exception e) 
		{
			System.out.println("Unable to connect.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Envoie un message texte à l'utilisateur donné.
	 * @param usr utilisateur auquel envoyer le message
	 * @param message message à envoyer
	 */
	public void sendTextMessage(User usr, String message)
	{
		try 
		{
			netControler.sendMessage(usr.getIpaddr(), new TextMessage(message));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Unable to send message " + message);
			e.printStackTrace();
		}
	}
	
	/**
	 * Déconnecte l'utilisateur du ChatSystem.
	 */
	public void disconnect()
	{
		try 
		{
			netControler.sendMessage(InetAddress.getByName("255.255.255.255"), new ByeMessage());
			netControler.dispose();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Unable to send bye message.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Envoie une requête de transfert de fichier à l'utilisateur donné.
	 * @param usr utilisateur auquel envoyer la requête.
	 * @param path chemin d'accès du fichier à envoyer.
	 */
	public void sendFileRequest(User usr, String path)
	{
		int timestamp = (int)System.currentTimeMillis();
		FileRequestMessage msg = new FileRequestMessage(path, timestamp);
		try 
		{
			netControler.sendMessage(usr.getIpaddr(), msg);
			this.outgoingFileRequests.put(timestamp, msg);
			this.notifyOutgoingFileRequest(usr, path, timestamp);
		}
		catch (IOException e) 
		{
			System.out.println("Unable to send file request.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Envoie une réponse à une requête d'envoi de fichier.
	 * @param usr utilisateur auquel envoyer la réponse.
	 * @param fileTimestamp identifiant (timestamp) du fichier à envoyer.
	 * @param accept si true : accepte le fichier, si false : refuse le fichier.
	 */
	public void sendFileRequestResponse(final User usr, final int fileTimestamp, final boolean accept)
	{
		try 
		{
			final FileRequestMessage request = this.incomingFileRequests.get(fileTimestamp);
			
			if(request == null)
			{
				notifyLog("Bad timestamp.", true);
				return;
			}
			
			netControler.sendMessage(usr.getIpaddr(), new FileRequestResponseMessage(accept, fileTimestamp));
			
			if(accept)
			{
				final MainController othis = this;
				netControler.receiveFile(usr.getIpaddr(), new FileRequestMessage(request.getFileName(), fileTimestamp), new TCPProgressListener() 
				{
					@Override
					public void onNotifyProgress(InetAddress source, int progress) {
						// TODO Auto-generated method stub
						othis.notifyFileTransferProgress(usr, request.getFileName(), progress, fileTimestamp);
					}
					
					@Override
					public void onNotifyEnd(InetAddress source) {
						// TODO Auto-generated method stub
						othis.notifyFileTransferEnded(usr, request.getFileName(), fileTimestamp);
					}
				});
			}

			this.notifyFileTransferResponse(usr, this.incomingFileRequests.get(fileTimestamp).getFileName(), fileTimestamp, accept);
			this.incomingFileRequests.remove(fileTimestamp);
			
		} 
		catch (IOException e)
		{
			System.out.println("Unable to accept file request.");
			e.printStackTrace();
		}
	}
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public UserList getUserList() {
		return userList;
	}
	
	/* ------------------------------------------------------------------------
	 * Observer implementation
	 * ----------------------------------------------------------------------*/	
	private void notifyUserConnected(User usr) {
		for(MainControllerListener l : listeners) l.OnUserConnected(usr);
	}
	private void notifyUserDisonnected(User usr) {
		for(MainControllerListener l : listeners) l.OnUserDisconnected(usr);
	}	
	private void notifyMessageReceived(User usr, String textMessage) {
		for(MainControllerListener l : listeners) l.OnMessageReceived(usr, textMessage);
	}
	private void notifyIncomingFileRequest(User usr, String filename, int timestamp) {
		for(MainControllerListener l : listeners) l.OnIncomingFileRequest(usr, filename, timestamp);
	}
	private void notifyOutgoingFileRequest(User usr, String filename, int timestamp) {
		for(MainControllerListener l : listeners) l.OnOutgoingFileRequest(usr, filename, timestamp);
	}
	private void notifyFileTransferEnded(User usr, String filename, int timestamp) {
		for(MainControllerListener l : listeners) l.OnFileTransferEnded(usr, filename, timestamp);
	}
	private void notifyFileTransferProgress(User usr, String filename, int progress, int timestamp) {
		for(MainControllerListener l : listeners) l.OnFileTransferProgress(usr, filename, progress, timestamp);
	}	
	private void notifyFileTransferResponse(User usr, String filename, int timestamp, boolean accepted) {
		for(MainControllerListener l : listeners) l.OnFileRequestResponse(usr, filename, timestamp, accepted);
	}
	private void notifyLog(String text, boolean isError) {
		for(MainControllerListener l : listeners) l.OnLog(text, isError);
	}
	public void addListener(MainControllerListener l) { 
		listeners.add(l); 
	}
	
	/* ------------------------------------------------------------------------
	 * Gui Listener
	 * --------------------------------------------------------------------- */
	@Override
	public void onConnect(String username) {
		this.connect(username);
	}

	@Override
	public void onDisconnect() {
		this.disconnect();
	}

	@Override
	public void onSendMessage(User usr, String message) {
		this.sendTextMessage(usr, message);
	}
	
	@Override
	public void onSendFileRequest(User usr, String path) 
	{
		this.sendFileRequest(usr, path);
	}
	
	@Override
	public void onAcceptFileRequest(User usr, int fileTimestamp)
	{
		this.sendFileRequestResponse(usr, fileTimestamp, true);
	}


	@Override
	public void onRejectFileRequest(User usr, int fileTimestamp) {
		this.sendFileRequestResponse(usr, fileTimestamp, false);
	}
}
