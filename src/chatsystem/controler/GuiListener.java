package chatsystem.controler;

import chatsystem.model.User;

public interface GuiListener 
{
	void onConnect(String username);
	void onDisconnect();
	void onSendMessage(User usr, String message);
	void onSendFileRequest(User usr, String path);
	void onAcceptFileRequest(User usr, int fileTimestamp);
}
