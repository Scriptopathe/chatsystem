package chatsystem.gui;

import chatsystem.model.User;

public class UserAdapter 
{
	private User usr;
	private Boolean connected;
	private Boolean hasNewMessages;
	
	public UserAdapter(User usr)
	{
		this.setUser(usr);
		this.setConnected(true);
		this.setHasNewMessages(false);
	}

	public User getUser() {
		return usr;
	}

	public void setUser(User usr) {
		this.usr = usr;
	}

	public Boolean isConnected() {
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	}

	public Boolean hasNewMessages() {
		return hasNewMessages;
	}

	public void setHasNewMessages(Boolean hasNewMessages) {
		this.hasNewMessages = hasNewMessages;
	}
	
	public String toString()
	{
		String s = "";
		
		if(this.hasNewMessages)
			s += "(1) ";
		
		s += getUser().getNickname();
		
		if(!this.connected)
			s += " (disconnected)";
		
		return s;
	}
}
