package chatsystem.controler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import chatsystem.messages.*;
import chatsystem.model.User;
import chatsystem.model.UserList;
import chatsystem.network.NetworkController;

/**
 * Contrôleur principal de l'application ChatSystem.
 * Il agit entre l'interface graphique et le controleur réseau.
 */
public class MainController 
{
	private NetworkController netControler;
	private UserList userList;
	private String nickname;

	public MainController()
	{
		try 
		{
			netControler = new NetworkController(this);
			this.userList = new UserList();
		}
		catch (SocketException e) 
		{
			// TODO Une belle message box + exit !!!
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	
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
	
	public void processMessage(InetAddress srcAddr, Message msg)
	{
		if(msg instanceof HelloMessage)
		{
			HelloMessage hellom = (HelloMessage)msg;
			User usr = new User(hellom.getNickname(), srcAddr);
			if(getUserList().getUserByIP(usr.getIpaddr()) != null)
			{
				System.out.println("Conflicting IP address, ABORT MISSION.");
				return;
			}
			
			// Enregistre l'utilisateur.
			getUserList().addUser(usr);
			System.out.println("Utilisateur " + usr + " ajouté.");
			
			// On répond si besoin.
			if(hellom.getReqReply())
			{
				try 
				{
					this.netControler.sendMessage(usr.getIpaddr(), new HelloMessage(nickname, false));
				} 
				catch (IOException e) 
				{
					System.out.println("Unable to send hello back.");
					e.printStackTrace();
				}
			}
		}
		else if(msg instanceof ByeMessage)
		{
			System.out.println("Utilisateur " + getUserList().getUserByIP(srcAddr) + " supprimé.");
			getUserList().removeUser(getUserList().getUserByIP(srcAddr));
		}
		else if(msg instanceof TextMessage)
		{
			System.out.println("Message reçu de " + getUserList().getUserByIP(srcAddr) + " : " + msg.toJSON());
		}
		else if(msg instanceof FileRequestMessage)
		{
			throw new NotImplementedException();
		}
		else if(msg instanceof FileRequestResponseMessage)
		{
			throw new NotImplementedException();
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
}
