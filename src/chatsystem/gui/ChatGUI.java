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
	private List<User> internalUserList;
	private JButton createConversationButton;
	private JList<ConversationGUI2> conversationsList;
	private List<ConversationGUI2> internalConversationsList;
	private List<GuiListener> listeners;
	private JMenuItem mntmDisconnect;
	private MainController mainController;
	private ConnectionFrame connectionFrame;
	
	/**
	 * Create the application.
	 */
	public ChatGUI(MainController ctrl, ConnectionFrame connect) 
	{
		internalUserList = new ArrayList<User>();
		internalConversationsList = new ArrayList<ConversationGUI2>();
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		connectedUserList = new JList<User>();
		connectedUserList.setModel(new AbstractListModel<User>() {
			public int getSize() {
				return internalUserList.size();
			}
			public User getElementAt(int index) {
				return internalUserList.get(index);
			}
		});
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
		conversationsList = new JList<ConversationGUI2>();
		conversationsList.setModel(new AbstractListModel<ConversationGUI2>() {
			public int getSize() {
				return internalConversationsList.size();
			}
			public ConversationGUI2 getElementAt(int index) {
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
	/* ------------------------------------------------------------------------
	 * MainControllerListener
	 * --------------------------------------------------------------------- */
	@Override
	public void OnUserConnected(User usr) {
		// TODO Auto-generated method stub
		internalUserList.add(usr);
		connectedUserList.updateUI();
	}

	@Override
	public void OnUserDisconnected(User usr) {
		// TODO Auto-generated method stub
		internalUserList.remove(usr);
		connectedUserList.updateUI();
	}

	@Override
	public void OnMessageReceived(User usr, String textMessage) {
		// TODO si on veut afficher un truc
	}

	@Override
	public void OnFileRequest(User usr, String filename, int timestamp) {
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
			notifyDisconnect();
			this.frame.dispose();
			this.connectionFrame.setVisible(true);
		}
		else if(e.getSource() == this.createConversationButton)
		{
			List<User> connectedUsers = this.connectedUserList.getSelectedValuesList();
			ConversationGUI2 c = null;
			for(ConversationGUI2 conv : this.internalConversationsList)
			{
				boolean isOK = true;
				// On vérifie que la conversation n'existe pas déja.
				if(connectedUsers.size() == conv.getUserAdapters().size())
				{
					for(UserAdapter userAdapter : conv.getUserAdapters())
					{
						if(!connectedUsers.contains(userAdapter.getUser()))
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
				List<UserAdapter> adapters = new ArrayList<UserAdapter>();
				for(User u : connectedUsers)
					adapters.add(new UserAdapter(u));
				
				c = new ConversationGUI2(adapters);
				mainController.addListener(c);
				c.addListener(mainController);
				this.internalConversationsList.add(c);
				this.conversationsList.updateUI();
			}
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
