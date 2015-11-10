package chatsystem;


public abstract class Message {
	public static final int MESSAGE_TYPE_HELLO = 0;
	public static final int MESSAGE_TYPE_BYE = 1;
	public static final int MESSAGE_TYPE_MSG = 2;
	public static final int MESSAGE_TYPE_FILE_REQUEST = 3;
	public static final int MESSAGE_TYPE_RESP_REQUEST = 4;
	
	
	/* ------------------------------------------------------------------------
	 * Fields
	 * ----------------------------------------------------------------------*/
	
	/* ------------------------------------------------------------------------
	 * Methods
	 * ----------------------------------------------------------------------*/
	public Message() 
	{
		
	}

	public abstract String toJSON();
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/	
}
