package chatsystem;
import java.net.InetAddress;


public interface UDPReceiverListener 
{
	/**
	 * Methode appelée lors de la réception d'un message : contient l'adresse de
	 * l'expéditeur du message ainsi que le message en question.
	 * @param source
	 * @param message
	 */
	public void onMessageReceived(InetAddress source, String message);
}
