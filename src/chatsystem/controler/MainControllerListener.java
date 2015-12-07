package chatsystem.controler;

import chatsystem.model.User;

public interface MainControllerListener 
{
	void OnUserConnected(User usr);
	void OnUserDisconnected(User usr);
	void OnMessageReceived(User usr, String textMessage);
	void OnOutgoingFileRequest(User usr, String filename, int timestamp);
	void OnIncomingFileRequest(User usr, String filename, int timestamp);
	void OnFileRequestResponse(User usr, String filename, int timestamp, boolean accepted);
	void OnFileTransferProgress(User usr, String filename, int progress, int timestamp);
	void OnFileTransferEnded(User usr, String filename, int timestamp);
	void OnLog(String text, boolean isError);
}
