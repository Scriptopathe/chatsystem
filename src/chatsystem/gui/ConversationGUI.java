package chatsystem.gui;

import java.awt.EventQueue;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import chatsystem.controler.MainController;
import chatsystem.controler.MainControllerListener;
import chatsystem.model.User;

public class ConversationGUI implements MainControllerListener, KeyListener
{
	private JFrame frame;
	private JList<UserAdapter> userlist;
	private List<UserAdapter> internalUserList;
	private JTextArea messageTextArea;
	private JTextArea inputTextArea;
	private List<GuiListener> listeners;
	
	/**
	 * Create the application.
	 */
	public ConversationGUI(List<UserAdapter> usrs) 
	{
		this.internalUserList = usrs;
		this.listeners = new ArrayList<GuiListener>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		// Création du titre.
		frame.setTitle(this.toString());
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	frame.setVisible(false);
	        }
		});
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		userlist = new JList<UserAdapter>();
		userlist.setModel(new AbstractListModel<UserAdapter>() {
			public int getSize() {
				return internalUserList.size();
			}
			public UserAdapter getElementAt(int index) {
				return internalUserList.get(index);
			}
		});
		userlist.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(userlist);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		messageTextArea = new JTextArea();
		messageTextArea.setEditable(false);
		messageTextArea.setText("");
		splitPane_1.setLeftComponent(messageTextArea);
		
		inputTextArea = new JTextArea();
		inputTextArea.setText("");
		inputTextArea.addKeyListener(this);
		splitPane_1.setRightComponent(inputTextArea);
		
		JLabel statusbar = new JLabel("Statut : Prêt.");
		frame.getContentPane().add(statusbar, BorderLayout.SOUTH);
		
		
		frame.setVisible(true);
	}
	/* ------------------------------------------------------------------------
	 * MainControllerListener
	 * --------------------------------------------------------------------- */
	@Override
	public void OnUserConnected(User usr) {

	}

	@Override
	public void OnUserDisconnected(User usr) 
	{

	}

	@Override
	public void OnMessageReceived(User usr, String textMessage) {
		// TODO Auto-generated method stub
		if(this.getUsers().contains(usr))
		{
			messageTextArea.setText(messageTextArea.getText() + usr + " says :" + textMessage+ "\n" );
		}
	}

	@Override
	public void OnFileRequest(User usr, String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnFileTransferEnded(User usr, String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnLog(String text, boolean isError) {
		// TODO Auto-generated method stub
		
	}
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
	
	public void addListener(GuiListener l) { listeners.add(l); }

	/* ------------------------------------------------------------------------
	 * KEY LISTENER
	 * --------------------------------------------------------------------- */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.inputTextArea)
		{
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if(!e.isControlDown())
				{
					for(UserAdapter usr : this.getUserAdapters())
					{
						notifySendMessage(usr.getUser(), this.inputTextArea.getText());
					}
					this.messageTextArea.setText(this.messageTextArea.getText() + "me says : " + this.inputTextArea.getText() + "\n");
					this.inputTextArea.setText("");
					e.consume();
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
	public List<UserAdapter> getUserAdapters()
	{
		return this.internalUserList;
	}
	
	public List<User> getUsers()
	{
		List<User> users = new ArrayList<User>();
		for(UserAdapter u : getUserAdapters())
		{
			users.add(u.getUser());
		}
		return users;
	}
	public void setVisible(boolean b)
	{
		this.frame.setVisible(b);
	}
	
	public String toString()
	{
		String title = "Conversation avec ";
		for(UserAdapter usr : this.internalUserList)
		{
			title += usr.getUser().getNickname() + " ";
		}
		return title;
	}
}
