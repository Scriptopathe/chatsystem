package chatsystem.network;

import java.net.InetAddress;

public interface TCPReceiverListener 
{
	/**
	 * Méthode appelée chaque 5% de l'avancement du téléchargement du fichier.
	 * @param progress progression du téléchargement (valeur entre 0 et 100)
	 * @param source adresse source du téléchargement
	 */
	public void onNotifyProgress(InetAddress source, int progress);
	/**
	 * Méthode appelée lors de la fin du téléchargement.
	 */
	public void onNotifyEnd(InetAddress source);
}
