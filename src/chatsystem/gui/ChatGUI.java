package chatsystem.gui;

import java.awt.EventQueue;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import chatsystem.controler.GuiListener;
import chatsystem.controler.MainController;
import chatsystem.controler.MainControllerListener;
import chatsystem.model.User;

public class ChatGUI implements MainControllerListener, ActionListener, MouseListener
{
	private JFrame frame;
	private JList<User> connectedUserList;
	private UserListModel connectedUserListModel;
	private JButton createConversationButton;
	private JList<ConversationFrame> conversationsList;
	private List<ConversationFrame> internalConversationsList;
	private List<GuiListener> listeners;
	private JMenuItem mntmDisconnect;
	private MainController mainController;
	private ConnectionFrame connectionFrame;
	
	class UserListModel extends DefaultListModel<User>
	{
	    public void update() 
	    {
	        this.fireContentsChanged(this, 0, this.getSize()-1);
	    }
	}
	/**
	 * Create the application.
	 */
	public ChatGUI(MainController ctrl, ConnectionFrame connect) 
	{
		connectedUserListModel = new UserListModel();
		internalConversationsList = new ArrayList<ConversationFrame>();
		mainController = ctrl;
		listeners = new ArrayList<GuiListener>();
		connectionFrame = connect;
		
		ctrl.addListener(this);
		this.addListener(ctrl);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Le système de chat !");
		frame.setBounds(100, 100, 450, 300);		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	        	ChatGUI.this.disconnect();
	        	System.exit(0);
	        }
		});
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		connectedUserList = new JList<User>();
		connectedUserList.setModel(connectedUserListModel);
		connectedUserList.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(connectedUserList);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.025);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		

		createConversationButton = new JButton("Creer une conversation.");
		createConversationButton.addActionListener(this);
		splitPane_1.setLeftComponent(createConversationButton);
		
		// Liste des conversations.
		conversationsList = new JList<ConversationFrame>();
		conversationsList.setModel(new AbstractListModel<ConversationFrame>() {
			public int getSize() {
				return internalConversationsList.size();
			}
			public ConversationFrame getElementAt(int index) {
				return internalConversationsList.get(index);
			}
		});
		conversationsList.addMouseListener(this);
		
		splitPane_1.setRightComponent(conversationsList);
		
		JLabel statusbar = new JLabel("Statut : Prêt.");
		frame.getContentPane().add(statusbar, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(this);
		
		mnFile.add(mntmDisconnect);
		frame.setVisible(true);
	}
	
	private void disconnect()
	{
		notifyDisconnect();
		this.frame.dispose();
		this.connectionFrame.setVisible(true);
		for(ConversationFrame conv : this.internalConversationsList)
		{
			conv.dispose();
		}
	}
	
	private void connect()
	{
		List<User> connectedUsers = this.connectedUserList.getSelectedValuesList();
		ConversationFrame c = null;
		for(ConversationFrame conv : this.internalConversationsList)
		{
			boolean isOK = true;
			// On vérifie que la conversation n'existe pas déja.
			if(connectedUsers.size() == conv.getUsers().size())
			{
				for(User user : conv.getUsers())
				{
					if(!connectedUsers.contains(user))
					{
						isOK = false;
					}
				}
			}
			else
				isOK = false;
			
			if(isOK)
				c = conv;
		}
		
		
		// Si la conversation existe : on l'affiche
		if(c != null)
		{
			c.setVisible(true);
		}
		else
		{
			// Sinon on la crée.
			List<User> adapters = new ArrayList<User>();
			for(User u : connectedUsers)
				adapters.add(u);
			
			c = new ConversationFrame(adapters);
			mainController.addListener(c);
			c.addListener(mainController);
			this.internalConversationsList.add(c);
			this.conversationsList.updateUI();
		}
	}
	
	/* ------------------------------------------------------------------------
	 * MainControllerListener
	 * --------------------------------------------------------------------- */
	@Override
	public void OnUserConnected(User usr) {
		// TODO Auto-generated method stub
		connectedUserListModel.addElement(usr);
		connectedUserListModel.update();
	}

	@Override
	public void OnUserDisconnected(User usr) {
		// TODO Auto-generated method stub
		connectedUserListModel.removeElement(usr);
		connectedUserListModel.update();
	}
	@Override
	public void OnOutgoingFileRequest(User usr, String filename, int timestamp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnFileRequestResponse(User usr, String filename, int timestamp,
			boolean accepted) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnMessageReceived(User usr, String textMessage) {
		// TODO si on veut afficher un truc
	}

	@Override
	public void OnIncomingFileRequest(User usr, String filename, int timestamp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnFileTransferProgress(User usr, String filename, int progress,
			int timestamp) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnFileTransferEnded(User usr, String filename, int timestamp) {
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
	 * Action Listener
	 * --------------------------------------------------------------------- */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.mntmDisconnect)
		{
			this.disconnect();
		}
		else if(e.getSource() == this.createConversationButton)
		{
			this.connect();
		}
	}

	/* ------------------------------------------------------------------------
	 * Mouse Listener
	 * --------------------------------------------------------------------- */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(this.conversationsList.getSelectedValue() != null)
			this.conversationsList.getSelectedValue().setVisible(true);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}








}
