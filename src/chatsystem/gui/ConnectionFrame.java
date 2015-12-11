package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import chatsystem.controler.ChatSettings;
import chatsystem.controler.MainController;
import chatsystem.controler.MainControllerListener;
import chatsystem.model.User;

public class ConnectionFrame extends JFrame implements ActionListener, KeyListener {

	private JPanel contentPane;
	private JTextArea usernameTextarea;
	private ChatSettings settings;
	/**
	 * Create the frame.
	 */
	public ConnectionFrame(ChatSettings settings) {
		this.settings = settings;
		setTitle("Système de chat - connexion");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 105);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		usernameTextarea = new JTextArea();
		usernameTextarea.setBounds(12, 12, 424, 25);
		usernameTextarea.addKeyListener(this);
		contentPane.add(usernameTextarea);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		btnConnect.setBounds(12, 42, 424, 25);
		contentPane.add(btnConnect);
	}

	/**
	 * Crée la connection.
	 */
	void performConnection()
	{
		if(usernameTextarea.getText() == "")
			return;
		MainController ctrl1 = new MainController(settings);
		ChatGUI g = new ChatGUI(ctrl1, this);
		
		ctrl1.addListener(new MainControllerListener() {
			
			@Override
			public void OnUserDisconnected(User usr) {
				System.out.println("User " + usr + " disconnected");
				
			}
			@Override
			public void OnFileRequestResponse(User usr, String filename, int timestamp,
					boolean accepted) {

				System.out.println("User " + usr + " sent file request response.");
				
			}
			@Override
			public void OnUserConnected(User usr) {
				// TODO Auto-generated method stub
				System.out.println("User " + usr + " connected");
				
			}
			
			@Override
			public void OnMessageReceived(User usr, String textMessage) {
				// TODO Auto-generated method stub
				System.out.println("User " + usr + " says : " + textMessage);
			}
			
			@Override
			public void OnLog(String text, boolean isError) {
				// TODO Auto-generated method stub
				System.out.println("[CtrlLog] " + text);
			}

			@Override
			public void OnIncomingFileRequest(User usr, String filename, int timestamp) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnFileTransferEnded(User usr, String filename,
					int timestamp) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnFileTransferProgress(User usr, String filename,
					int progress, int timestamp) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void OnOutgoingFileRequest(User usr, String filename, int timestamp) {
				// TODO Auto-generated method stub
				
			}

		});
		
		ctrl1.connect(usernameTextarea.getText());
		
		this.setVisible(false);
		
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		performConnection();
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getSource() == this.usernameTextarea)
		{
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				performConnection();
				e.consume();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
