package chatsystem.messages;

import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


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
	
	
	/**
	 * Crée une instance de Message à partir du string json passé en paramètre.
	 */
	public static Message createFromJSON(String jsonstr)
	{
		JSONObject jobj = new JSONObject(jsonstr);
		int type = jobj.getInt("type");
		Message msg;
		switch(type)
		{
		case MESSAGE_TYPE_HELLO:
			msg = new HelloMessage(
				jobj.getString("nickname"), 
				jobj.getBoolean("reqReply")
			);
			break;
		case MESSAGE_TYPE_BYE:
			msg = new ByeMessage();
			break;

		case MESSAGE_TYPE_MSG:
			msg = new TextMessage(jobj.getString("message"));
			break;

		case MESSAGE_TYPE_FILE_REQUEST:
			msg = new FileRequestMessage(jobj.getString("name"));
			break;

		case MESSAGE_TYPE_RESP_REQUEST:
			msg = new FileRequestResponseMessage(jobj.getBoolean("ok"));
			break;
		default:
			throw new NotImplementedException();
		}
		return msg;
	}
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/	
}
