package chatsystem.model;

import java.net.InetAddress;

public class User 
{
	private String nickname;
	private InetAddress ipaddr;
	
	public User(String nickname, InetAddress ipaddr)
	{
		this.nickname = nickname;
		this.ipaddr = ipaddr;
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
