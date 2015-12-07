package chatsystem.gui;


public class FileTransfer {
	
	public int timestamp;
	public int progress;
	public boolean ended;
	public String filename;
	public boolean accepted;
	public boolean isIncoming;
	
	public FileTransfer(boolean isIncoming, int timestamp, String filename)
	{
		this.timestamp = timestamp;
		this.progress=0;
		this.ended = false;
		this.filename = filename;
		this.isIncoming = isIncoming;
		this.accepted = false;
	}
	
	
	public String toString()
	{
		if(!this.accepted)
		{
			if(this.ended)
				return this.filename + " : rejected";
			
			if(this.isIncoming)
			{
				return this.filename + " : accept ? ID = " + timestamp;
			}
			else
			{
				return this.filename + " : not accepted yet.";
			}
		}
		if(this.ended)
		{
			return this.filename +" : finished";
		}
		else
		{
			return this.filename + " : " + this.progress + " %";
		}
	}
	
	
}
