package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JList;
import java.awt.Panel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

public class ConversationFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea txtInputtextfield;
	private JTextArea messageArea;
	private JList fileList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConversationFrame frame = new ConversationFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConversationFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		fileList = new JList();
		fileList.setModel(new AbstractListModel() {
			String[] values = new String[] {"SPACE FILLER"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		contentPane.add(fileList, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane2 = new JScrollPane();
		panel.add(scrollPane2, BorderLayout.CENTER);
		txtInputtextfield = new JTextArea();
		txtInputtextfield.setText("inputTextField");
		scrollPane2.setViewportView(txtInputtextfield);
		txtInputtextfield.setColumns(10);
		
		Panel panel_1 = new Panel();
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JButton sendMessageButton = new JButton("Send Message");
		panel_1.add(sendMessageButton, BorderLayout.NORTH);
		
		JButton sendFileButton = new JButton("Send File");
		panel_1.add(sendFileButton, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		messageArea = new JTextArea();
		scrollPane.setViewportView(messageArea);
		messageArea.setEditable(false);
	}
}
