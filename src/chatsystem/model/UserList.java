package chatsystem.model;

import java.net.InetAddress;
import java.util.*;

public class UserList 
{
	private List<User> userList;
	
	public UserList()
	{
		this.userList = new ArrayList<User>();
	}
	
	
	/**
	 * Ajout un nouvel user
	 * @param u nouvel user à ajouter à la liste userList
	 */
	public void addUser(User u)
	{
		this.userList.add(u);
	}
	
	/**
	 * Supprime un user	
	 * @param u user à supprimer
	 */
	public void removeUser(User u)
	{
		this.userList.remove(u);
	}
	
	/**
	 * Recherche d'un utilisateur par son nickname
	 * @param nickname
	 * @return 
	 */
	public User getUserByNickname(String nickname)
	{		
		for(User u : this.userList)
		{
			if(u.getNickname().equals(nickname))
				return u;
		}

		return null;
	}
	
	
	public User getUserByIP(InetAddress ip)
	{
		String ipId = toId(ip);
		for(User u : this.userList)
		{
			if(toId(u.getIpaddr()).equals(ipId))
				return u;
		}
		return null;
	}
	
	
	private String toId(InetAddress addr)
	{
		String addname = addr.toString();
		return addname.split("/")[1];
	}
}
