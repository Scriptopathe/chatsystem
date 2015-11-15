package chatsystem.controler;

import chatsystem.model.User;

public interface MainControllerListener 
{
	void OnUserConnected(User usr);
	void OnUserDisconnected(User usr);
	void OnMessageReceived(User usr, String textMessage);
	void OnFileRequest(User usr, String filename);
	void OnFileTransferEnded(User usr, String filename);
	void OnLog(String text, boolean isError);
}
