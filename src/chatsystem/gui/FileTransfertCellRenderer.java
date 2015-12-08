package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;

/**
 * 
 */
@SuppressWarnings("serial")
public class FileTransfertCellRenderer extends JPanel implements ListCellRenderer<FileTransfer> {
 
	JLabel l1 = new JLabel();
	JLabel l2 = new JLabel();
    public FileTransfertCellRenderer() 
    {
    	this.setLayout(new BorderLayout());
    	this.add(l1, BorderLayout.NORTH);
    	this.add(l2, BorderLayout.CENTER);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends FileTransfer> list, FileTransfer fileTransfer, int index,
            boolean isSelected, boolean cellHasFocus) 
    {
    	
    	this.setSize(this.getWidth(), 300);
    	l1.setText(fileTransfer.filename);
    	
    	// L2
    	l2.setText(fileTransfer.getStateName());
    	l2.setLocation(10, 30);
    	
    	if(fileTransfer.ended)
    		if(fileTransfer.accepted)
    			l2.setForeground(Color.GREEN);
    		else
    			l2.setForeground(Color.RED);
    	else
    	{
    		l2.setForeground(Color.GRAY);
    	}
    	
    	if(isSelected)
    	{
    		this.setBackground(new Color(200, 200, 200));
    		this.setForeground(list.getSelectionForeground());
    	}
    	else
    	{
    		this.setBackground(list.getBackground());
    		this.setForeground(list.getForeground());
    	}
    	
        return this;
    }
} 
