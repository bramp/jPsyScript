package net.bramp.psyscript.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.bramp.psyscript.Helper;
import net.bramp.psyscript.LogListener;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.RuntimeExceptionOnLine;

public class MainGUI extends JFrame implements DocumentListener, LogListener {

	final MainToolbar toolBar;
	final MyJEditorPane editor;
	final MyJEditorPane log;
	final HelpPanel helpPanel;
	final JSplitPane splitPane;
	
	final Container content;

	File currentFile;
	boolean hasChanged;

	Program program;

	// A map of all the configurable options
	final Options options = createDefaultOptions();

	// Keeps trying to parse the editor window
	ParserThread parserThread;

	public MainGUI() {

		// Setup the icons for this app (this is a Java 6 feature)
		final List<Image> icons = new ArrayList<Image>();
		icons.add ( resourceLoadImage("icons/accessories-text-editor-16.png") );
		icons.add ( resourceLoadImage("icons/accessories-text-editor-22.png") );
		icons.add ( resourceLoadImage("icons/accessories-text-editor-32.png") );
		setIconImages( icons );

		// Set the default title
        setTitle();

        //Create and set up the window.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener( new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		exit();
        	}
        });

        setUpActions();

        content = getContentPane();

        final SpringLayout layout = new SpringLayout();
        content.setLayout( layout );

    	toolBar = new MainToolbar(this);
 
        // The main edit window
        editor = new MyJEditorPane();
        editor.addDocumentListener(this);

        log = new MyJEditorPane();
        log.setEditable( false );

        final JScrollPane editorScrollPane = new JScrollPane(editor,
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        final JScrollPane logScrollPane = new JScrollPane(log,
        		ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, logScrollPane);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(1.0);

        logScrollPane.setMinimumSize( new Dimension(0, 50) );
        logScrollPane.setPreferredSize( new Dimension(0, 150) );

        helpPanel = new HelpPanel(this);
       
        layout.putConstraint(SpringLayout.NORTH, toolBar, 0, SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.EAST,  toolBar, 0, SpringLayout.EAST, content);
        layout.putConstraint(SpringLayout.WEST,  toolBar, 0, SpringLayout.WEST, content);

        layout.putConstraint(SpringLayout.NORTH, helpPanel, 0, SpringLayout.SOUTH, toolBar);
        layout.putConstraint(SpringLayout.SOUTH, helpPanel, 0, SpringLayout.SOUTH, content);
        layout.putConstraint(SpringLayout.EAST,  helpPanel, 10, SpringLayout.EAST, content);

        layout.putConstraint(SpringLayout.NORTH, splitPane, 0, SpringLayout.SOUTH, toolBar);
        layout.putConstraint(SpringLayout.SOUTH, splitPane, 0, SpringLayout.SOUTH, content);
        layout.putConstraint(SpringLayout.WEST,  splitPane, 0, SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.EAST,  splitPane, -1, SpringLayout.WEST, helpPanel);

        content.add(toolBar);
        content.add(helpPanel);
        content.add(splitPane);

        // Start the help in the hidden state
        toggleHelp();

        //Display the window.
        setSize(800, 600);
        setVisible(true);
        
        // TODO Work out the min size
        // or atleast get content to figure out its min
        //setMinimumSize( toolBar.getMinimumSize() );
    }

	/**
	 * Called after the constructor
	 */
	public void init() {
		parserThread = new ParserThread(this);
		parserThread.start();

        newFile();
        setModified(false);

        // DEBUG LINE
        try {
			open(new File("E:\\My Projects\\My Java\\PsyScript v2\\PsyScript folder\\sample scripts\\timer test.psyScript"));
        	//open(new File("C:\\Projects\\PsyScript\\test.psyscript"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static Options createDefaultOptions() {
		Options options = new Options();

		options.put( "use_fullscreen", "true" );
		options.put( "look_and_feel", "" );

		return options;
	}

	private static Image resourceLoadImage(final String filename) {
		final URL url = ClassLoader.getSystemResource(filename);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();

		if ( url != null )
			return toolkit.getImage( url );;

		return toolkit.getImage( filename );
	}
	
	private static Icon resourceLoadIcon(final String filename) {
		final URL url = ClassLoader.getSystemResource(filename);

		if ( url != null )
			return new ImageIcon( url );

		return new ImageIcon( filename );
	}
	
	protected void setUpActions() {

		final String size = "-32"; // Quick way to change the size of the icons
		final InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		final ActionMap actionMap = getActionMap();
		final MainGUI gui = this;
		
		new MyAction("New", resourceLoadIcon("icons/document-new" + size + ".png"),	KeyStroke.getKeyStroke("control N")) {
		    public void actionPerformed(ActionEvent x) {
		    	newFile();
		    }
		}.mapIt(inputMap, actionMap);

		new MyAction("Open", resourceLoadIcon("icons/document-open" + size + ".png"),	KeyStroke.getKeyStroke("control O")) {
		    public void actionPerformed(ActionEvent x) {
		    	open();
		    }
		}.mapIt(inputMap, actionMap);

		new MyAction("Save", resourceLoadIcon("icons/document-save" + size + ".png"),	KeyStroke.getKeyStroke("control S")) {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		}.mapIt(inputMap, actionMap);

		new MyAction("Save As", resourceLoadIcon("icons/document-save-as" + size + ".png")) {
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		}.mapIt(inputMap, actionMap);

		new MyAction("Test", resourceLoadIcon("icons/media-playback-start" + size + ".png"), KeyStroke.getKeyStroke("control T")) {
			public void actionPerformed(ActionEvent e) {
				runNow();
			}
		}.mapIt(inputMap, actionMap).setEnabled(false);

		new MyAction("Run", resourceLoadIcon("icons/media-playback-start" + size + ".png"), KeyStroke.getKeyStroke("control R")) {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		}.mapIt(inputMap, actionMap).setEnabled(false);
		
		new MyAction("Options", resourceLoadIcon("icons/preferences-system" + size + ".png"), KeyStroke.getKeyStroke("control O")) {
			public void actionPerformed(ActionEvent e) {
				OptionsDialog d = new OptionsDialog(gui, options);
				d.setVisible(true);
			}
		}.mapIt(inputMap, actionMap);

		new MyAction("Help", resourceLoadIcon("icons/dialog-information" + size + ".png"), KeyStroke.getKeyStroke("F1")) {
			public void actionPerformed(ActionEvent e) {
				toggleHelp();
			}
		}.mapIt(inputMap, actionMap);
		
		new MyAction("Exit", resourceLoadIcon("icons/system-log-out" + size + ".png"), KeyStroke.getKeyStroke("control Q")) {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		}.mapIt(inputMap, actionMap);
	}

	protected void toggleHelp() {
		final SpringLayout layout = (SpringLayout) content.getLayout();
		if ( helpPanel.isVisible() ) {
			helpPanel.setVisible( false );
			layout.putConstraint(SpringLayout.EAST,  splitPane, 0, SpringLayout.EAST, content);
		} else {
			helpPanel.setVisible( true );
			layout.putConstraint(SpringLayout.EAST,  splitPane, 0, SpringLayout.WEST, helpPanel);			
		}
		validate();
	}

	/**
	 * Checks if the file has been changed
	 * TODO change to enum type
	 * @return 0 File has been saved
	 *         1 File has not been saved
	 *         2 Cancel the operation that lead to this
	 */
	protected int checkSaved(String title) {
		if ( hasChanged ) {
			int n = JOptionPane.showConfirmDialog(this, "You have not saved your changes! Would you like to save?", title, JOptionPane.YES_NO_CANCEL_OPTION);

			if ( n == 0 )
				save();
			
			return n;
		}
		
		return 0;
	}
	
    static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
	final static String defaultProgram = "-- Author      : Your name\n" +
										 "-- Date        : " + getDate() + "\n" +
										 "-- Description : The description of the experiment\n" +
										 "\n" +
										 "proc main\n\nend proc\n";
	
	protected void newFile() {
		setCurrentFile ( null );
		setProgram ( null );
		clearException();

		editor.setText(defaultProgram);
	}
	
	protected void exit() {
		final String title = "Quit";

		if ( hasChanged ) {
			if ( checkSaved(title) == 2 )
				return;

		} else {
			int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", title, JOptionPane.OK_CANCEL_OPTION);
			if ( n != 0 )
				return;
		}
		
		System.exit(0);
	}

	public void save() {
		if ( currentFile == null ) {
			saveAs();
		} else {
			try {
				save ( currentFile );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void saveAs() {
		final JFileChooser fc = new JFileChooser();
		fc.setName("Save As");
		fc.addChoosableFileFilter( new FileNameExtensionFilter("PsyScripts (*.psyscript)", "psyscript") );

		if ( currentFile != null )
			fc.setSelectedFile(currentFile);

		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File newFile = fc.getSelectedFile();
				
				// Make sure the file ends in ".psyscript"
				if ( !Helper.endsWithIgnoreCase( newFile.getName(), ".psyscript") ) {
					newFile = new File(newFile.getAbsolutePath() + ".psyscript");
				}
				
				save(newFile);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void open() {
		
		final String title = "Open";
		
		if ( checkSaved(title) == 2 )
			return;
		
		final JFileChooser fc = new JFileChooser();
		fc.setName( title );
		fc.addChoosableFileFilter( new FileNameExtensionFilter("PsyScripts (*.psyscript)", "psyscript") );
		
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				open(fc.getSelectedFile());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	

	public void open(File file) throws FileNotFoundException, IOException {
    	setProgram(null);
		editor.setContentType( "text/plain" );
    	editor.read(new FileReader(file) , null);
    	
    	setCurrentFile( file );
    	setModified(false);
    	
    	parse();
	}

	public void save(File file) throws IOException {
		final FileWriter f = new FileWriter(file);
    	editor.write(f);
    	setCurrentFile( file );
    	setModified(false);
    	f.close();
	}
	
	protected void setTitle() {
		if ( currentFile != null ) {
			setTitle("jPsyScript - " + currentFile.getAbsolutePath() + ( hasChanged ? "*" : "" ) );
		} else {
			setTitle("jPsyScript");
		}
	}
	
	/**
	 * Called to say the file in the text window has been modified
	 */
	protected void setModified(boolean changed) {
		hasChanged = changed;
		setTitle();
		
		getActionMap().get("Save").setEnabled( changed );
	}
	
	protected void setCurrentFile(File file) {
		currentFile = file;
		setTitle();
	}

	protected void setProgram(Program p) {

		program = p;
		
		final ActionMap actionMap = getActionMap();
		
		if ( program != null) {
			// Debug line to print out the problem
			//System.err.println(p);

			if ( currentFile != null )
				program.setFilename( currentFile.getAbsolutePath() );

	        program.setLogListener(this);
	        
	        actionMap.get("Run").setEnabled(true);
	        actionMap.get("Test").setEnabled(true);
		} else {
	        actionMap.get("Run").setEnabled(false);
	        actionMap.get("Test").setEnabled(false);
		}
	}

	/**
	 * Parse the editor
	 * Must be run from inside the AWT GUI thread
	 */
	public synchronized void parse() {

		clearException();

		final String text = editor.getText().trim();

		if (text.isEmpty()) {
    		setProgram ( null );
			return;
		}

    	try {
    		setProgram ( Program.load( new StringReader( text ) ) );

    		program.check();

    	} catch (Exception e) {
    		setProgram ( null );
    		displayException(e);
    	}
	}

	protected void goFullScreen(final Container c, boolean fullScreen) {

		setVisible(false);

		setContentPane( c );
		invalidate();

	    // Determine if full-screen mode is supported directly
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gs = ge.getDefaultScreenDevice();

		if ( fullScreen && "true".equals( options.get("use_fullscreen") ) ) {
			// Full Screen
			dispose();
			setUndecorated(true);
			setResizable(false);
			setAlwaysOnTop(true);
			gs.setFullScreenWindow(this);
		} else {
			// Windowed mode
			dispose();
			setUndecorated(false);
			setResizable(true);
			setAlwaysOnTop(false);
			gs.setFullScreenWindow(null);
		}

		setVisible(true);
	}

	/**
	 * Runs the problem now without any run dialog
	 * Useful for testing
	 */
	public void runNow() {
		if ( program != null ) {
			final Container oldContainer = getContentPane();

			// Setup the GUI to handle the programGUI
			final ProgramGUI programGUI = new ProgramGUI(this, program);
			goFullScreen( programGUI, true );

			// Run the program in a new thread
			new Thread("Program Thread") {
				@Override
				public void run() {
					try {
						programGUI.run();
					} catch (Exception e) {
						displayException( e );
					}

					goFullScreen ( oldContainer, false);
				}
			}.start();
		} else {
			// How did run get pressed when the program isn't ready?
			setProgram(null);
		}
	}

	protected class FileLogger implements LogListener {
		final FileWriter file;

		public FileLogger(String filename, boolean append) throws IOException {
			file = new FileWriter(filename, append);
		}

		public void log(String data) {
			try {
				file.write(data);
			} catch (IOException e) {
				displayException(e);
			}
		}

		public void close() {
			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				displayException(e);
			}
		}
	}

	/**
	 * Opens the run dialog, and then runs the program (if it is parsed correctly)
	 */
	public void run() {
		if ( program != null ) {
			RunDialog runDialog = new RunDialog(this);
	        runDialog.pack();

	        if ( currentFile != null ) {
	        	String shortName = currentFile.getName();

	        	if ( shortName.length() > 10 ) {
	        		int nameLen = shortName.length()-10;

	        		if ( shortName.substring(nameLen).equalsIgnoreCase(".PsyScript") ) {
	        			shortName = shortName.substring(0, nameLen);
	        		}
	        	}

	        	runDialog.setExperimentName(shortName);
	        }
	        runDialog.setLocationRelativeTo(this);
	        runDialog.setVisible(true);

	        if ( ! runDialog.getResult() )
	        	return;

	        final String filename = runDialog.getFilename();
	        final boolean append = runDialog.getAppend();
	        if ( filename != null && !filename.isEmpty() ) {
				try {
			        // Now decide the logging details, and setup some variables
					final FileLogger logger = new FileLogger(filename, append);
					program.setLogListener(logger);

					program.setVariable("$experiment", runDialog.getExperimentName());
					program.setVariable("$subject",    runDialog.getSubjectName());
					program.setVariable("$comments",   runDialog.getCommentsName());

					runNow();
				} catch (IOException e) {
					e.printStackTrace();
				}	        	
	        }

		} else {
			// How did run get pressed when the program isn't ready?
			setProgram(null);
		}
	}

	protected void clearException() {
		editor.clearHighlights();
		log.setText( "" );
	}

	protected void displayException(Exception e) {
		assert e != null;
		String message;

		if ( e instanceof RuntimeExceptionOnLine ) {
			RuntimeExceptionOnLine le = (RuntimeExceptionOnLine)e;
			int line = le.getLine();
			if ( line >= 0 ) {
				editor.clearHighlights();
				editor.highlightLine( line );
			}

			message = e.toString();

		} else {
			message = "Internal Error: " + e + "\n";
			for ( StackTraceElement s : e.getStackTrace() )
				message += "\t" + s + "\n";
		}

		log.setText( message );
		log.setCaretPosition(0);
	}

	private void documentChanged() {
    	assert parserThread != null;

		setModified(true);
		setProgram(null);
		parserThread.prepare();
    }

	public void changedUpdate(DocumentEvent e) {}

	public void insertUpdate(DocumentEvent e) {
		documentChanged();
	}

	public void removeUpdate(DocumentEvent e) {
		documentChanged();
	}

	public void log(String data) {
		log.setText( log.getText() + data ); 
	}
	public void close() {}

	public ActionMap getActionMap() {
		return getRootPane().getActionMap();
	}
	
	public InputMap getInputMap() {
		return getRootPane().getInputMap();
	}
	
	public InputMap getInputMap(int i) {
		return getRootPane().getInputMap(i);
	}

	/**
	 * Pastes some text into the main editor area at the current cursor pos
	 * @param command
	 */
	public void paste(String text) {
		editor.paste(text);
	}
	
    public static void main(String[] args) {

    	try {
    		 // Setup the System look and feel
    		 UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (Exception e) {}
    	
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainGUI gui = new MainGUI();
                gui.init();
            }
        });
    }

}
