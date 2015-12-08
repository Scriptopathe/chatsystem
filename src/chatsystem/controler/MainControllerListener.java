package chatsystem.controler;

import chatsystem.model.User;

/**
 * Interface définissant les messages envoyés par le controlleur principal à ses
 * observeurs.
 * @author scriptopathe
 */
public interface MainControllerListener 
{
	/**
	 * Méthode appelée lorsqu'un utilisateur se connecte au ChatSystem.
	 * @param usr utilisateur nouvellement connecté.
	 */
	void OnUserConnected(User usr);
	/**
	 * Méthode appelée lorsqu'un utilisateur se déconnecte du ChatSystem.
	 * @param usr utilisateur nouvellement déconnecté.
	 */
	void OnUserDisconnected(User usr);
	/**
	 * Méthode appelée lors de la réception d'un message text.
	 * @param usr utilisateur émetteur du message.
	 * @param textMessage contenu du message.
	 */
	void OnMessageReceived(User usr, String textMessage);
	/**
	 * Méthode appelée lorsqu'une requête de transfert de fichier sortant est envoyée.
	 * @param usr utilisateur destinataire du fichier
	 * @param filename nom du fichier
	 * @param timestamp identifiant du fichier
	 */
	void OnOutgoingFileRequest(User usr, String filename, int timestamp);
	/**
	 * Méthode appelée lorsqu'une requête de transfert de fichier entrant est reçue.
	 * @param usr utilisateur destinataire du fichier
	 * @param filename nom du fichier
	 * @param timestamp identifiant du fichier
	 */
	void OnIncomingFileRequest(User usr, String filename, int timestamp);
	/**
	 * Méthode appelée lors de la réception d'une réponse à une requête de
	 * transfert de fichier.
	 * @param usr utilisateur émetteur de la réponse
	 * @param filename nom du fichier 
	 * @param timestamp identifiant du fichier
	 * @param accepted valeur indiquant si le transfert est accepté ou refusé.
	 */
	void OnFileRequestResponse(User usr, String filename, int timestamp, boolean accepted);
	/**
	 * Méthode appelée à intervalle régulier pour signaler la progression d'un transfert de fichier
	 * en cours.
	 * @param usr utilisateur concerné par le transfert de fichier
	 * @param filename nom du fichier
	 * @param progress progression du transfert (bytes)
	 * @param timestamp identifiant du fichier.
	 */
	void OnFileTransferProgress(User usr, String filename, int progress, int timestamp);
	/**
	 * Méthode appelée lorsqu'un transfert de fichier est terminé.
	 * @param usr utilisateur concerné par le transfert de fichier
	 * @param filename nom du fichier
	 * @param timestamp identifiant du fichier.
	 */
	void OnFileTransferEnded(User usr, String filename, int timestamp);
	void OnLog(String text, boolean isError);
}
