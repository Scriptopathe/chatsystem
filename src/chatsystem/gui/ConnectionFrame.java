package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import chatsystem.controler.MainController;

public class ConnectionFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextArea usernameTextarea;
	/**
	 * Create the frame.
	 */
	public ConnectionFrame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 105);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		usernameTextarea = new JTextArea();
		usernameTextarea.setBounds(12, 12, 424, 25);
		contentPane.add(usernameTextarea);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		btnConnect.setBounds(12, 42, 424, 25);
		contentPane.add(btnConnect);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(usernameTextarea.getText() == "")
			return;
		MainController ctrl1 = new MainController();
		ChatGUI g = new ChatGUI(ctrl1);
		ctrl1.connect(usernameTextarea.getText());
		this.dispose();
	}
}
