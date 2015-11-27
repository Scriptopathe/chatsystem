package chatsystem.controler;

import java.io.*;
import java.net.*;
import java.util.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tests.MainControllerTest;
import chatsystem.gui.GuiListener;
import chatsystem.messages.*;
import chatsystem.model.*;
import chatsystem.network.*;

/**
 * Contrôleur principal de l'application ChatSystem.
 * Il agit entre l'interface graphique et le controleur réseau.
 */
public class MainController implements GuiListener
{
	/* ------------------------------------------------------------------------
	 * Variables
	 * ----------------------------------------------------------------------*/
	private NetworkController netControler;
	private UserList userList;
	private String nickname;
	private List<MainControllerListener> listeners;
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
		}
		catch (SocketException e) 
		{
			// TODO Une belle message box + exit !!!
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	
	/* ------------------------------------------------------------------------
	 * Messages from NI
	 * ----------------------------------------------------------------------*/
	public void processMessage(InetAddress srcAddr, Message msg)
	{
		if(msg instanceof HelloMessage)
		{
			HelloMessage hellom = (HelloMessage)msg;
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
		else if(msg instanceof ByeMessage)
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
		else if(msg instanceof TextMessage)
		{
			User usr = getUserList().getUserByIP(srcAddr);
			if(usr == null)
			{
				notifyLog("[Error] Received message from User @" + srcAddr + ": user does not exist.", true);
				return;
			}
			notifyLog("Message received from " + usr + " : " + msg.toJSON(), false);
			notifyMessageReceived(usr, ((TextMessage)msg).getMessage());
		}
		else if(msg instanceof FileRequestMessage)
		{
			FileRequestMessage frm = (FileRequestMessage)msg;
			
			throw new NotImplementedException();
		}
		else if(msg instanceof FileRequestResponseMessage)
		{
			throw new NotImplementedException();
		}
	}
	
	/* ------------------------------------------------------------------------
	 * Messages from user
	 * ----------------------------------------------------------------------*/
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
	
	public void disconnect()
	{
		try 
		{
			netControler.sendMessage(InetAddress.getByName("255.255.255.255"), new ByeMessage());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Unable to bye message");
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
	private void notifyFileRequest(User usr, String filename) {
		for(MainControllerListener l : listeners) l.OnFileRequest(usr, filename);
	}
	private void notifyFileTransferEnded(User usr, String filename) {
		for(MainControllerListener l : listeners) l.OnFileTransferEnded(usr, filename);
	}
	private void notifyLog(String text, boolean isError) {
		for(MainControllerListener l : listeners) l.OnLog(text, isError);
	}
	public void addListener(MainControllerListener l) { listeners.add(l); }
	
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
}
