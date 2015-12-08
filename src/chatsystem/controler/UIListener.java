package chatsystem.controler;

import chatsystem.model.User;

/**
 * Interface définissant les messages envoyés par l'interface utilisateur.
 * @author scriptopathe
 */
public interface UIListener 
{
	/**
	 * Méthode appelée lorsque l'utilisateur désire se connecter.
	 * @param username nom d'utilisateur choisi
	 */
	void onConnect(String username);
	/**
	 * Méthode appelée lorsque l'utilisateur souhaite se déconnecter.
	 */
	void onDisconnect();
	/**
	 * Méthode appelée lorsque l'utilisateur souhaite envoyer un message.
	 * @param usr utilisateur auquel envoyer le message
	 * @param message contenu du message à envoyer
	 */
	void onSendMessage(User usr, String message);
	/**
	 * Méthode appelée lorsque l'utilisateur souhaite envoyer un fichier.
	 * @param usr utilisateur auquel envoyer un fichier
	 * @param path chemin d'accès du fichier
	 */
	void onSendFileRequest(User usr, String path);
	/**
	 * Méthode appelée lorsque l'utilisateur souhaite accepter une requête
	 * d'envoi de fichier.
	 * @param usr utilisateur émetteur du fichier
	 * @param fileTimestamp idenfitiant du fichier à accepter.
	 */
	void onAcceptFileRequest(User usr, int fileTimestamp);
	/**
	 * Méthode appelée lorsque l'utilisateur souhaite refuser une requête
	 * d'envoi de fichier.
	 * @param usr utilisateur émetteur du fichier
	 * @param fileTimestamp idenfitiant du fichier à refuser.
	 */
	void onRejectFileRequest(User usr, int fileTimestamp);
}
