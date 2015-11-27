package chatsystem.gui;

import chatsystem.model.User;

public interface GuiListener 
{
	void onConnect(String username);
	void onDisconnect();
	void onSendMessage(User usr, String message);
}
