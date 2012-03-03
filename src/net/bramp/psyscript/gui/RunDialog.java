package net.bramp.psyscript.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;

class RunDialog extends JDialog implements PropertyChangeListener, DocumentListener, ActionListener {

	final JOptionPane optionPane;

	final JTextField experimentField;
	final JTextField subjectField;
	final JTextArea commentsField;
	
	final ButtonGroup group; 
    final JRadioButton perSubjectButton;
    final JRadioButton perExperimentButton;
    
	final FilenamePanel filenameField;

	enum LogType {
		PerSubject,
		PerExperiment,
	}
	LogType logType = LogType.PerSubject;
	boolean result = false;
	boolean append = true;

	public class FilenamePanel extends JPanel implements ActionListener, DocumentListener {
		final JTextField filenameField = new JTextField(20);
		final JButton filenameOpen = new JButton("Browse");

		boolean beenSet = false;

		public FilenamePanel() {
			filenameOpen.addActionListener(this);
			filenameField.getDocument().addDocumentListener(this);

			setLayout( new FlowLayout() );

			filenameOpen.setMaximumSize( filenameOpen.getMinimumSize() );

			setLayout(new BorderLayout());

			add(filenameField, BorderLayout.CENTER);
			add(filenameOpen, BorderLayout.EAST);
		}

		public void actionPerformed(ActionEvent e) {
			if ( e.getSource() == filenameOpen) {
				final JFileChooser fc = new JFileChooser();

				File f = new File(getFilename());

				fc.setSelectedFile(f);
				fc.addChoosableFileFilter( new FileNameExtensionFilter("Text Files (*.txt)", "txt") );

				int returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filenameField.setText( fc.getSelectedFile().toString() );
					beenSet = true;
				}
			}
		}

		public String getFilename() {
			return filenameField.getText();
		}

		public void setFilename(String filename) {
			filenameField.setText(filename);
		}

		List<DocumentListener> documentListeners = new ArrayList<DocumentListener>();

		public void addDocumentListener(DocumentListener l) {
			if ( !documentListeners.contains(l) )
				documentListeners.add( l );
		}

		public void changedUpdate(DocumentEvent e) {
			for(DocumentListener d : documentListeners)
				d.changedUpdate(e);
		}

		public void insertUpdate(DocumentEvent e) {
			for(DocumentListener d : documentListeners)
				d.insertUpdate(e);
		}

		public void removeUpdate(DocumentEvent e) {
			for(DocumentListener d : documentListeners)
				d.removeUpdate(e);
		}

		public Document getDocument() {
			return filenameField.getDocument();
		}

		public boolean hasBeenSet() {
			return beenSet;
		}
	}

	private void addTextField(Panel p, String name, Component text) {
		GridBagLayout gridbag = (GridBagLayout) p.getLayout();
		GridBagConstraints c = new GridBagConstraints();
		JLabel label = new JLabel( name );

		c.fill = GridBagConstraints.BOTH;
		gridbag.setConstraints(label, c);

		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridbag.setConstraints(text, c);

        p.add( label );
        p.add( text );
	}

    public RunDialog(Frame aFrame) {
        super(aFrame, true);

        setTitle("Run");

        //Create the radio buttons.
        perSubjectButton = new JRadioButton("One file per subject");
        perSubjectButton.setMnemonic(KeyEvent.VK_S);
        perSubjectButton.setSelected(true);

        perExperimentButton = new JRadioButton("One file per experiment");
        perExperimentButton.setMnemonic(KeyEvent.VK_E);
        perExperimentButton.setSelected(false);

        //Group the radio buttons.
        group = new ButtonGroup();
        group.add(perSubjectButton);
        group.add(perExperimentButton);
        
        if ( logType == LogType.PerSubject ) {
        	perSubjectButton.setSelected(true);
        } else if ( logType == LogType.PerExperiment ) {
        	perExperimentButton.setSelected(true);
        }

        perSubjectButton.addActionListener(this);
        perExperimentButton.addActionListener(this);

        Panel textPanel = new Panel();
        GridBagLayout grid = new GridBagLayout();
        textPanel.setLayout( grid );

        // Create a grid of text fields
        experimentField = new JTextField(20);
        subjectField    = new JTextField(20);
        commentsField   = new JTextArea(3, 20);

        // A couple of stylings to make the TextArea consistant
        commentsField.setBorder( experimentField.getBorder() );
        commentsField.setFont( experimentField.getFont() );
        commentsField.setLineWrap(true);
        commentsField.setWrapStyleWord(true);
        
        experimentField.getDocument().addDocumentListener(this);
        subjectField.getDocument().addDocumentListener(this);
        commentsField.getDocument().addDocumentListener(this);
        
        addTextField(textPanel, "Experiment Name:", experimentField);
        addTextField(textPanel, "Subject Name:",    subjectField);
        addTextField(textPanel, "Comments:",        commentsField);
        
        Panel filePanel = new Panel();
        grid = new GridBagLayout();
        filePanel.setLayout( grid );
        
        filenameField = new FilenamePanel();
        addTextField(filePanel, "Log File:",        filenameField);

        //Create an array of the text and components to be displayed.
        Object[] array = {
        		textPanel,
        		"Log the results:", 
        		perSubjectButton, perExperimentButton,
        		filePanel
        		};

        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {"Begin", "Cancel"};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
            	experimentField.requestFocusInWindow();
            }
        });

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }
    
	public String getExperimentName() {
		return experimentField.getText();
	}

	public void setExperimentName(String string) {
		experimentField.setText(string);
	}

	public String getSubjectName() {
		return subjectField.getText();
	}

	public String getCommentsName() {
		return commentsField.getText();
	}
	
	public boolean getResult() {
		return result;
	}
	
	public LogType getLogType() {
		return logType;
	}
	
	public void setLogType(LogType logType) {
		this.logType = logType;
	}

    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {

    	if ( !isVisible() )
    		return;
    	
    	String prop = e.getPropertyName();
    	
        if ( e.getSource() == optionPane
        	&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))
       	) {
        	Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            result = "Begin".equals(value);
            if ( result ) {
            	// Check the filename
            	File f = new File(getFilename());
            	if ( f.exists() ) {
            		append = true;
            		if ( logType == LogType.PerSubject ) {
            			String options[] = {"Append", "Overwrite", "Cancel"};
            			int n = JOptionPane.showOptionDialog(this, "The log file already exists! Would you like to append to or overwrite the file?", "Log File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            			if ( n == 0 )
            				append = true;
            			else if ( n == 1 )
            				append = false;
            			else
            				return;
            		}
            	}
            }

            this.setVisible(false);
        }
    }

    protected void textUpdate(DocumentEvent e) {
    	if ( !filenameField.hasBeenSet() ) {
    		// Lets create a filename based on some fields
    		String filename = getExperimentName() + "_" + getSubjectName() + ".txt";
    		setFilename(filename);
    	}
    }

	public void changedUpdate(DocumentEvent e) {
		textUpdate(e);
	}

	public void insertUpdate(DocumentEvent e) {
		textUpdate(e);
	}

	public void removeUpdate(DocumentEvent e) {
		textUpdate(e);
	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == perSubjectButton || e.getSource() == perExperimentButton ) {
			if ( perSubjectButton.isSelected() )
				logType = LogType.PerSubject;
			else if ( perExperimentButton.isSelected() )
				logType = LogType.PerExperiment;
			else
				assert false : "Invalid button is selected";
		}
	}

	public String getFilename() {
		return filenameField.getFilename();
	}

	public void setFilename(String filename) {
		filenameField.setFilename(filename);
	}
	
	public boolean getAppend() {
		return append;
	}
}