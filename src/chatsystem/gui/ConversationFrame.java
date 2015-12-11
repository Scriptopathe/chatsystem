package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import chatsystem.controler.UIListener;
import chatsystem.controler.MainControllerListener;
import chatsystem.model.User;

import java.util.*;



public class ConversationFrame extends JFrame implements MainControllerListener, KeyListener
{

	private volatile JPanel contentPane;
	private volatile JTextArea inputTextArea;
	private volatile JTextArea messageTextArea;
	// private volatile JList<FileTransfer> fileList;
	private volatile FileTransferPanelManager fileList;
	private volatile List<FileTransfer> fileTransfers;
	private volatile List<User> userList;
	private volatile List<UIListener> listeners;
	

	/**
	 * Create the frame.
	 */
	public ConversationFrame(List<User> users) 
	{
		this.fileTransfers = new ArrayList<FileTransfer>();
		this.listeners = new ArrayList<UIListener>();
		this.userList = users;
		this.initializeComponents();
		this.setTitle(this.toString());
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
		
		fileList = new FileTransferPanelManager();
		fileList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileTransfer t = (FileTransfer)e.getSource();
				ConversationFrame.this.accept(t.timestamp);
			}
		});
		/*fileList = new JList<FileTransfer>();
		fileList.setCellRenderer(new FileTransfertCellRenderer());
		fileList.setModel(new AbstractListModel<FileTransfer>() {
			public int getSize() {
				return fileTransfers.size();
			}
			public FileTransfer getElementAt(int index) {
				return fileTransfers.get(index);
			}
		});*/
		
		contentPane.add(fileList, BorderLayout.EAST);
		fileList.setSize(200, -1);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane2 = new JScrollPane();
		panel.add(scrollPane2, BorderLayout.CENTER);
		inputTextArea = new JTextArea();
		inputTextArea.setText("\\file banana.txt");
		scrollPane2.setViewportView(inputTextArea);
		inputTextArea.setColumns(10);
		inputTextArea.addKeyListener(this);
		
		Panel panel_1 = new Panel();
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton sendMessageButton = new JButton("Send Message");
		sendMessageButton.setPreferredSize(new Dimension(150, 25));
		panel_1.add(sendMessageButton, BorderLayout.NORTH);
		
		JButton sendFileButton = new JButton("Send File");
		sendMessageButton.setPreferredSize(new Dimension(150, 25));
		panel_1.add(sendFileButton, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		messageTextArea = new JTextArea();
		scrollPane.setViewportView(messageTextArea);
		messageTextArea.setEditable(false);
		this.setVisible(true);
	}
	
	/** 
	 * Ajoute un message dans la conversation.
	 * @param userSrc utilisateur qui a envoyé le message
	 * @param msg contenu du message
	 */
	private synchronized void addMessage(User userSrc, String msg)
	{
		String prompt;
		if(userSrc == null)
			prompt = "I say ";
		else
			prompt = userSrc + " says ";
		
		messageTextArea.append(prompt + " : " + msg + "\n");
		messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
	}
	
	/**
	 * Retourne le nom de la conversation.
	 */
	@Override
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
	private void markAccepted(int fileTimestamp, boolean accepted)
	{
		for(FileTransfer transfer : this.fileTransfers)
			if(transfer.timestamp == fileTimestamp)
				transfer.accepted = accepted;
	}
	
	private void markProgress(int fileTimestamp, int progress)
	{
		for(FileTransfer transfer : this.fileTransfers)
			if(transfer.timestamp == fileTimestamp)
				transfer.progress = progress;
	}
	
	private void markEnded(int fileTimestamp)
	{
		for(FileTransfer transfer : this.fileTransfers)
			if(transfer.timestamp == fileTimestamp)
				transfer.ended = true;
	}
	
	private void updateFileList()
	{
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() 
		    {
		    	ConversationFrame.this.fileList.refresh();
		    }
		});
	}
	
	private void addTransfer(FileTransfer ft)
	{
		this.fileTransfers.add(ft);
		this.fileList.addFileTransfer(ft);
		this.updateFileList();
	}
	
	@Override
	public synchronized void OnIncomingFileRequest(User usr, String filename, int timestamp) {
		if(this.getUsers().contains(usr))
			this.addTransfer(new FileTransfer(true, timestamp, filename));
	}
	
	@Override
	public synchronized void OnOutgoingFileRequest(User usr, String filename, int timestamp) 
	{
		if(this.getUsers().contains(usr))
			this.addTransfer(new FileTransfer(false, timestamp, filename));
	}
	
	@Override
	public synchronized void OnFileTransferEnded(User usr, String filename, int timestamp) 
	{
		if(this.getUsers().contains(usr))
		{
			this.addMessage(usr, "File transfert : " + filename + " complete.");
			this.markEnded(timestamp);
			this.updateFileList();	
		}
	}

	@Override
	public synchronized void OnFileTransferProgress(User usr, String filename, int progress, int timestamp) 
	{
		if(!this.getUsers().contains(usr))
			return;
		this.markProgress(timestamp, progress);
		this.updateFileList();
	}
	
	@Override
	public synchronized void OnMessageReceived(User usr, String textMessage) 
	{
		if(this.getUsers().contains(usr))
		{
			this.addMessage(usr, textMessage);
		}
	}

	@Override
	public synchronized void OnFileRequestResponse(User usr, String filename, int timestamp, boolean accepted) 
	{
		System.out.println("Accepted !!!");
		if(!this.getUsers().contains(usr))
			return;
		
		this.markAccepted(timestamp, accepted);
		this.updateFileList();
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
		for(UIListener l : listeners) l.onConnect(username);
	}
	private void notifyDisconnect() {
		for(UIListener l : listeners) l.onDisconnect();
	}
	private void notifySendMessage(User usr, String message) {
		for(UIListener l : listeners) l.onSendMessage(usr, message);
	}
	private void notifySendFileRequest(User usr, String path) 
	{
		for(UIListener l : listeners) l.onSendFileRequest(usr, path);
	}
	private void notifyAcceptFileRequest(User usr, int timestamp) 
	{
		for(UIListener l : listeners) l.onAcceptFileRequest(usr, timestamp);
	}
	private void notifyRejectFileRequest(User usr, int timestamp) 
	{
		for(UIListener l : listeners) l.onRejectFileRequest(usr, timestamp);
	}
	public void addListener(UIListener l) { listeners.add(l); }
	
	/* ------------------------------------------------------------------------
	 * KEY LISTENER
	 * --------------------------------------------------------------------- */
	/**
	 * Demande l'acceptation d'un transfert de fichier estampillé par fileTimestamp.
	 * @param fileTimestamp timestamp du fichier à accepter.
	 */
	private synchronized void accept(int fileTimestamp)
	{
		if(getFileTransfer(fileTimestamp) == null)
		{
			this.addMessage(null, "Error : no such file to accept (ID = " + fileTimestamp + ").");
		}
		else
		{
			for(User usr : this.getUsers())
			{
				notifyAcceptFileRequest(usr, fileTimestamp);
			}
			
			this.addMessage(this.getUsers().get(0), " has accepted your file : " + getFileTransfer(fileTimestamp).filename);
			this.markAccepted(fileTimestamp, true);
			this.updateFileList();
		}
		this.inputTextArea.setText("");
	}
	
	/**
	 * Demande le rejet d'un transfert de fichier estampillé par fileTimestamp.
	 * @param fileTimestamp timestamp du fichier à rejeter.
	 */
	private  synchronized void reject(int fileTimestamp)
	{
		for(User usr : this.getUsers())
		{
			this.notifyRejectFileRequest(usr, fileTimestamp);
		}
		
		// Statut du transfert : refusé et terminé.
		this.addMessage(this.getUsers().get(0), " has rejected your file : " + getFileTransfer(fileTimestamp).filename);
		this.markAccepted(fileTimestamp, false);
		this.markEnded(fileTimestamp);
		this.updateFileList();
		this.inputTextArea.setText("");
	}
	
	/**
	 * Demande l'envoi du fichier dont le nom est donné en paramètre.
	 * @param filename
	 */
	private synchronized void sendFile(String filename)
	{
		if(this.getUsers().size() > 1)
		{
			this.messageTextArea.append("[Error] Cannot send file to more than one user. \n");
			this.inputTextArea.setText("");
			return;
		}
		
		for(User usr : this.getUsers())
		{
			notifySendFileRequest(usr, filename);
		}
		this.addMessage(this.getUsers().get(0), "File transfert request sent. ");
		this.inputTextArea.setText("");
	}
	
	/**
	 * Demande l'envoi du message donné aux utilisateurs de la conversation.
	 * @param message
	 */
	private synchronized void sendMessage(String message)
	{
		for(User usr : this.getUsers())
		{
			notifySendMessage(usr, message);
		}
		this.addMessage(null, message);
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
						text = text.replaceFirst("\\\\accept", "").trim();
						int timestamp;
						try
						{
							timestamp = Integer.parseInt(text);						
						}
						catch(NumberFormatException ex)
						{
							this.messageTextArea.append("Bad file id. " + "\n");
							this.inputTextArea.setText("");
							return;
						}
						accept(timestamp);
						e.consume();
					}
					else if(text.startsWith("\\reject"))
					{
						text = text.replaceFirst("\\\\reject", "").trim();
						int timestamp;
						try
						{
							timestamp = Integer.parseInt(text);						
						}
						catch(NumberFormatException ex)
						{
							this.messageTextArea.append("Bad file id. " + "\n");
							this.inputTextArea.setText("");
							return;
						}
						reject(timestamp);
						e.consume();
					}
					else
					{
						// Send message.
						sendMessage(this.inputTextArea.getText());
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
