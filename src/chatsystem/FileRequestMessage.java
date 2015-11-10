package chatsystem;
import org.json.*;

public class FileRequestMessage extends Message{
	
	private String fileName ;
	
	public FileRequestMessage(String file)
	{
		this.setFileName(file);
	}

	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_FILE_REQUEST);
		obj.put("name", this.getFileName());
		return obj.toString();
	}
	
	
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	private String getFileName() {
		return fileName;
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
