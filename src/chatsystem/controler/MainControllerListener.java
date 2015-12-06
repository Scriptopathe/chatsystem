package chatsystem.controler;

import chatsystem.model.User;

public interface MainControllerListener 
{
	void OnUserConnected(User usr);
	void OnUserDisconnected(User usr);
	void OnMessageReceived(User usr, String textMessage);
	void OnFileRequest(User usr, String filename, int timestamp);
	void OnFileTransferProgress(User usr, String filename, int progress, int timestamp);
	void OnFileTransferEnded(User usr, String filename, int timestamp);
	void OnLog(String text, boolean isError);
}
