package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import chatsystem.controler.GuiListener;
import chatsystem.controler.MainControllerListener;
import chatsystem.model.User;

import java.util.*;



public class ConversationFrame extends JFrame implements MainControllerListener, KeyListener
{

	private JPanel contentPane;
	private JTextArea inputTextArea;
	private JTextArea messageTextArea;
	private JList<FileTransfer> fileList;
	private List<FileTransfer> fileTransfers;
	private List<User> userList;
	private List<GuiListener> listeners;
	

	/**
	 * Create the frame.
	 */
	public ConversationFrame(List<User> users) 
	{
		this.fileTransfers = new ArrayList<FileTransfer>();
		this.listeners = new ArrayList<GuiListener>();
		this.userList = users;
		this.initializeComponents();
		
	}
	
	private void initializeComponents()
	{
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	ConversationFrame.this.setVisible(false);
	        }
		});
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		fileList = new JList<FileTransfer>();
		fileList.setModel(new AbstractListModel<FileTransfer>() {
			public int getSize() {
				return fileTransfers.size();
			}
			public FileTransfer getElementAt(int index) {
				return fileTransfers.get(index);
			}
		});
		contentPane.add(fileList, BorderLayout.EAST);
		fileList.setSize(200, -1);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane2 = new JScrollPane();
		panel.add(scrollPane2, BorderLayout.CENTER);
		inputTextArea = new JTextArea();
		inputTextArea.setText("inputTextField");
		scrollPane2.setViewportView(inputTextArea);
		inputTextArea.setColumns(10);
		inputTextArea.addKeyListener(this);
		
		Panel panel_1 = new Panel();
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton sendMessageButton = new JButton("Send Message");
		panel_1.add(sendMessageButton, BorderLayout.NORTH);
		
		JButton sendFileButton = new JButton("Send File");
		panel_1.add(sendFileButton, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		messageTextArea = new JTextArea();
		scrollPane.setViewportView(messageTextArea);
		messageTextArea.setEditable(false);
		this.setVisible(true);
	}
	
	private void addMessage(User userSrc, String msg)
	{
		messageTextArea.append(userSrc + " says : " + msg + "\n");
	}
	
	public String toString()
	{
		String title = "Conversation avec ";
		for(User usr : this.userList)
		{
			title += usr.getNickname() + " ";
		}
		return title;
	}
	
	
	/* ------------------------------------------------------------------------
	 * MainControllerListener
	 * --------------------------------------------------------------------- */
	
	@Override
	public void OnIncomingFileRequest(User usr, String filename, int timestamp) {
		// TODO Auto-generated method stub
		messageTextArea.setText(messageTextArea.getText() + usr + " File transfert request : " + filename + ". ID = " + timestamp + "\n");
		this.fileTransfers.add(new FileTransfer(true, timestamp, filename));
		this.fileList.updateUI();
	}
	
	@Override
	public void OnOutgoingFileRequest(User usr, String filename, int timestamp) {
		// TODO Auto-generated method stub
		this.fileTransfers.add(new FileTransfer(false, timestamp, filename));
		this.fileList.updateUI();
	}
	
	@Override
	public void OnFileTransferEnded(User usr, String filename, int timestamp) 
	{
		messageTextArea.setText(messageTextArea.getText() + usr + " File transfert : " + filename + " complete.\n");
		this.getFileTransfer(timestamp).ended = true;
		this.fileList.updateUI();
	}
	@Override
	public void OnFileTransferProgress(User usr, String filename, int progress, int timestamp) {
		messageTextArea.setText(messageTextArea.getText() + usr + " File transfert : " + filename + " [progress=" + progress + "%.\n");
		this.getFileTransfer(timestamp).progress = progress;
		this.fileList.updateUI();
	}
	
	@Override
	public void OnMessageReceived(User usr, String textMessage) {
		if(this.getUsers().contains(usr))
		{
			this.addMessage(usr, textMessage);
		}
	}

	@Override
	public void OnFileRequestResponse(User usr, String filename, int timestamp, boolean accepted) 
	{
		this.getFileTransfer(timestamp).ended = true;
		this.fileList.updateUI();
	}
	
	@Override
	public void OnLog(String text, boolean isError) {}
	@Override
	public void OnUserConnected(User usr) {}
	@Override
	public void OnUserDisconnected(User usr) {}
	
	/* ------------------------------------------------------------------------
	 * Gui Listener
	 * --------------------------------------------------------------------- */
	
	private void notifyConnect(String username) {
		for(GuiListener l : listeners) l.onConnect(username);
	}
	private void notifyDisconnect() {
		for(GuiListener l : listeners) l.onDisconnect();
	}
	private void notifySendMessage(User usr, String message) {
		for(GuiListener l : listeners) l.onSendMessage(usr, message);
	}
	private void notifySendFileRequest(User usr, String path) 
	{
		for(GuiListener l : listeners) l.onSendFileRequest(usr, path);
	}
	private void notifyAcceptFileRequest(User usr, int timestamp) 
	{
		for(GuiListener l : listeners) l.onAcceptFileRequest(usr, timestamp);
	}
	private void notifyRejectFileRequest(User usr, int timestamp) 
	{
		for(GuiListener l : listeners) l.onRejectFileRequest(usr, timestamp);
	}
	public void addListener(GuiListener l) { listeners.add(l); }
	
	/* ------------------------------------------------------------------------
	 * KEY LISTENER
	 * --------------------------------------------------------------------- */
	private void accept(int fileTimestamp)
	{
		for(User usr : this.getUsers())
		{
			notifyAcceptFileRequest(usr, fileTimestamp);
		}
		this.getFileTransfer(fileTimestamp).accepted = true;
		this.fileList.updateUI();
		this.messageTextArea.setText(this.messageTextArea.getText() + "File transfert accepted . " + "\n");
		this.inputTextArea.setText("");
	}
	
	private void reject(int fileTimestamp)
	{
		for(User usr : this.getUsers())
		{
			this.notifyRejectFileRequest(usr, fileTimestamp);
		}
		this.getFileTransfer(fileTimestamp).accepted = true;
		this.fileList.updateUI();
		this.messageTextArea.setText(this.messageTextArea.getText() + "File transfert accepted . " + "\n");
		this.inputTextArea.setText("");
	}
	
	private void sendFile(String filename)
	{
		if(this.getUsers().size() > 1)
		{
			this.messageTextArea.setText("[Error] Cannot send file to more than one user. \n");
			this.inputTextArea.setText("");
			return;
		}
		
		for(User usr : this.getUsers())
		{
			notifySendFileRequest(usr, filename);
		}
		this.messageTextArea.setText(this.messageTextArea.getText() + "File transfert request sent. " + "\n");
		this.inputTextArea.setText("");
	}
	
	private void sendMessage()
	{
		for(User usr : this.getUsers())
		{
			notifySendMessage(usr, this.inputTextArea.getText());
		}
		this.messageTextArea.setText(this.messageTextArea.getText() + "me says : " + this.inputTextArea.getText() + "\n");
		this.inputTextArea.setText("");	
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.inputTextArea)
		{
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if(!e.isControlDown())
				{
					String text = this.inputTextArea.getText();
					if(text.startsWith("\\file"))
					{
						text = text.replaceFirst("\\\\file", "").trim();
						sendFile(text);
						e.consume();
					}
					else if(text.startsWith("\\accept"))
					{
						text = text.replaceFirst("\\\\refuse", "").trim();
						int timestamp;
						try
						{
							timestamp = Integer.parseInt(text);						
						}
						catch(NumberFormatException ex)
						{
							this.messageTextArea.setText(this.messageTextArea.getText() + "Bad file id. " + "\n");
							this.inputTextArea.setText("");
							return;
						}
						accept(timestamp);
						e.consume();
					}
					else if(text.startsWith("\\refuse"))
					{
						text = text.replaceFirst("\\\\refuse", "").trim();
						int timestamp;
						try
						{
							timestamp = Integer.parseInt(text);						
						}
						catch(NumberFormatException ex)
						{
							this.messageTextArea.setText(this.messageTextArea.getText() + "Bad file id. " + "\n");
							this.inputTextArea.setText("");
							return;
						}
						reject(timestamp);
						e.consume();
					}
					else
					{
						// Send message.
						sendMessage();
						e.consume();
					}

				}
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	/* ------------------------------------------------------------------------
	 * Getters
	 * --------------------------------------------------------------------- */
	private FileTransfer getFileTransfer(int timestamp)
	{
		for(FileTransfer transfer : this.fileTransfers)
		{
			if(transfer.timestamp == timestamp)
				return transfer;
		}
		return null;
	}
	
	public List<User> getUsers()
	{
		return this.userList;
	}
}
