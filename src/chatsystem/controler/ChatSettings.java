package chatsystem.controler;

public class ChatSettings 
{
	/**
	 * Port sur lequel s'effectueront toutes les connexions entrantes.
	 * -1 : defaut
	 */
	private int inPort;
	/**
	 * Port sur lequel s'effectueront toutes les connexions sortantes.
	 * -1 : defaut
	 */
	private int outPort;
	
	public ChatSettings(int inPort, int outPort)
	{
		this.inPort = inPort;
		this.outPort = outPort;
	}
	
	public int getInPort() { return this.inPort; }
	public int getOutPort() { return this.outPort; }
}
