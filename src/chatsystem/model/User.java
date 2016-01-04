package chatsystem.model;

import java.net.InetAddress;
import java.util.*;

public class User 
{
	private String nickname;
	private InetAddress ipaddr;
	static List<User> pool = new ArrayList<User>();
	
	private User(String nickname, InetAddress ipaddr)
	{
		this.nickname = nickname;
		this.ipaddr = ipaddr;
	}
	
	public static User create(String nickname, InetAddress addr)
	{
		User u = null;
		for(User usr : pool)
		{
			if(usr.getNickname().equals(nickname) && usr.ipaddr.equals(addr))
			{
				u = usr;
				break;
			}
		}
		
		if(u == null)
		{
			u = new User(nickname, addr);
			pool.add(u);
		}
		
		return u;
	}
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/	
	
	public String getNickname()
	{
		return nickname;
	}
	
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	
	
	public InetAddress getIpaddr()
	{
		return ipaddr;
	}
	
	public void setIpaddr(InetAddress ipaddr) 
	{
		this.ipaddr = ipaddr;
	}
	
	public String toString()
	{
		return nickname + "@" + ipaddr.toString();
	}
}
