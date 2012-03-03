package net.bramp.psyscript.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import net.bramp.psyscript.Program;

public class TextPanel extends JPanel implements ActionListener {
	
	final Program program;
	final MyJEditorPane text;
	final JButton okButton;

	protected Object waitObject = new Object();
	
	public TextPanel(Program program) {
		
		this.program = program;

		setVisible(false);

		SpringLayout layout = new SpringLayout();

		setLayout( layout );

		okButton = new JButton("OK");
		okButton.setVisible(true);
		okButton.addActionListener(this);
		add(okButton);

		text = new MyJEditorPane();
		text.setVisible(true);
        text.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(text, 
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        add(scrollPane);
 
        layout.putConstraint(SpringLayout.SOUTH, okButton, -5, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.EAST, okButton, -5, SpringLayout.EAST, this);

        layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST, scrollPane, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.SOUTH, scrollPane, -5, SpringLayout.NORTH, okButton);
	}

	public void setContentType(String mimeType) {
		text.setContentType(mimeType);
	}

	public void read(FileReader fileReader, Object desc) throws IOException {
		text.read(fileReader, desc);
	}

	public void actionPerformed(ActionEvent e) {
		// OK button was clicked, we can now exit
		synchronized ( waitObject ) {
			waitObject.notify();
		}
	}

	public void loadText(String filename, String mimeType) throws FileNotFoundException, IOException {
		filename = program.getAbsolutePath(filename);

		text.setContentType( mimeType );
		text.read(new FileReader(filename) , null);
	}

	public void loadText(String filename) throws IOException {
		loadText(filename, "text/plain");
	}

	public void loadRichText(String filename) throws IOException {
		loadText(filename, "text/rtf");
	}

	public void loadHTMLText(String filename) throws IOException {
		loadText(filename, "text/html");
	}

	public void setText(String string) {
		text.setContentType( "text/plain" );
		text.setText( string );
	}
	
	public void display() {
		
		try {
			synchronized ( waitObject ) {
				EventQueue.invokeLater( new Runnable() {
					public void run() {
						setVisible(true);
					}
				} );

				waitObject.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		EventQueue.invokeLater( new Runnable() {
			public void run() {
				setVisible(false);
			}
		} );
	}
}
