package chatsystem;


public abstract class Message {
	public static final int MESSAGE_TYPE_HELLO = 0;
	public static final int MESSAGE_TYPE_BYE = 1;
	public static final int MESSAGE_TYPE_MSG = 2;
	public static final int MESSAGE_TYPE_FILE = 3;
	
	/* ------------------------------------------------------------------------
	 * Fields
	 * ----------------------------------------------------------------------*/
	private String nickname;
	
	/* ------------------------------------------------------------------------
	 * Methods
	 * ----------------------------------------------------------------------*/
	public Message(String nickname) 
	{
		this.setNickname(nickname);
	}

	public abstract String toJSON();
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
