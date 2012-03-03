package net.bramp.psyscript.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Highlighter;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.undo.UndoManager;


/**
 * Subclass of JEditorPane to override some of the EditorKit settings, and provide other useful features
 * @author Andrew Brampton
 *
 */
public class MyJEditorPane extends JEditorPane implements DocumentListener, MouseListener, UndoableEditListener {

	private final List<DocumentListener> docListeners = new ArrayList<DocumentListener>();
	private final UndoManager undoManager = new UndoManager();

	static final HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 0, 0));

	final JPopupMenu popup;
	
	public MyJEditorPane() {
       
        final ActionMap actionMap = getActionMap();
        final InputMap inputMap = getInputMap();

		new MyAction("Undo", KeyStroke.getKeyStroke("control Z")) {
			public void actionPerformed(ActionEvent e) {
				undoManager.undo();
				checkUndoStats();
			}
		}.mapIt(inputMap, actionMap);

		new MyAction("Redo", KeyStroke.getKeyStroke("control Y")) {
			public void actionPerformed(ActionEvent e) {
				undoManager.redo();
				checkUndoStats();
			}
		}.mapIt(inputMap, actionMap);
		
		new MyAction("Select All", KeyStroke.getKeyStroke("control A")) {
			public void actionPerformed(ActionEvent e) {
				setSelectionStart( 0 );
				setSelectionEnd( getDocument().getLength() );
			}
		}.mapIt(inputMap, actionMap);
		
		new MyAction("Copy", KeyStroke.getKeyStroke("control C")) {
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		}.mapIt(inputMap, actionMap);
		
		new MyAction("Cut", KeyStroke.getKeyStroke("control X")) {
			public void actionPerformed(ActionEvent e) {
				cut();
			}
		}.mapIt(inputMap, actionMap);
		
		new MyAction("Paste", KeyStroke.getKeyStroke("control V")) {
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		}.mapIt(inputMap, actionMap);

		addMouseListener(this);

        popup = new JPopupMenu();
        createPopupMenu(popup);
        
        setHighlighter( new DefaultHighlighter() );
	}

	public EditorKit getEditorKitForContentType(String type){
		if ("text/plain".equalsIgnoreCase(type)) {
			return new StyledEditorKit();
		} else {
			return super.getEditorKitForContentType(type);
		}
	}

	@Override
	public void setDocument(Document doc) {
		super.setDocument(doc);
		if (doc != null) {
			doc.addDocumentListener(this);

			// We catch undos so we know when to enable/disable buttons
			doc.addUndoableEditListener(this);

			if (undoManager != null ) {
				undoManager.discardAllEdits();
				checkUndoStats();
			}
		}
	}

	public void addDocumentListener(DocumentListener d) {
		docListeners.add(d);
	}

	public void removeDocumentListener(DocumentListener d) {
		docListeners.remove(d);
	}

	public void clearHighlights() {
		final Highlighter high = getHighlighter();
		high.removeAllHighlights();
		repaint();
	}
	
	public void highlightLine(final int lineNum) {
		final Highlighter high = getHighlighter();

		final Document d = getDocument();

		int line = 0;
		int posStart = 0;
		int pos = 0;
		int lineTermLength = 0;

		try {

			while ( line < lineNum ) {
				posStart = pos;
				lineTermLength = 0;
				
				// Find the end of a line (or end of string)
				while ( pos < d.getLength() ) {
	    			char c = d.getText(pos, 1).charAt(0);
	    			pos++;
	
	    			if ( c == '\r' ) {
	    				lineTermLength = 1;
	    				
	    				if ( pos < d.getLength() && d.getText(pos, 1).charAt(0) == '\n' ) {
	    					pos++;
	    					lineTermLength = 2;
	    				}
	
	    				break;
	    			} else if ( c == '\n') {
	    				lineTermLength = 1;
	    				break;
	    			}
				}
				line++;
			}
			
			pos -= lineTermLength;
	
			assert posStart >= 0;
			assert pos >= posStart;
			assert pos <= d.getLength();

			high.addHighlight(posStart, pos, painter);
		
			repaint();

		} catch (BadLocationException e1) {}

	}

	public void changedUpdate(DocumentEvent e) {
		for ( DocumentListener d : docListeners )
			d.changedUpdate(e);
	}

	public void insertUpdate(DocumentEvent e) {
		for ( DocumentListener d : docListeners )
			d.insertUpdate(e);
	}

	public void removeUpdate(DocumentEvent e) {
		for ( DocumentListener d : docListeners )
			d.removeUpdate(e);
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	/**
	 * Creates a simple copy and paste popup menu
	 * @param e
	 */
	protected void popupMenu( MouseEvent e ) {
		if ( !e.isPopupTrigger() )
			return;
		
		popup.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private void createPopupMenu(JPopupMenu popup) {
		final ActionMap actionMap = getActionMap();
		
		popup.add( new JMenuItem( actionMap.get("Undo") ) );
		popup.add( new JMenuItem( actionMap.get("Redo") ) );
		popup.addSeparator();
		popup.add( new JMenuItem( actionMap.get("Cut") ) );
		popup.add( new JMenuItem( actionMap.get("Copy") ) );
		popup.add( new JMenuItem( actionMap.get("Paste") ) );
		popup.addSeparator();
		popup.add( new JMenuItem( actionMap.get("Select All") ) );
		

	}
	
	public void mousePressed(MouseEvent e) {
		popupMenu( e );

	}

	public void mouseReleased(MouseEvent e) {
		popupMenu( e );
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		undoManager.undoableEditHappened( e );
		checkUndoStats();
	}
	
	/**
	 * Enables or disables undo buttons based on if they can be used
	 */
	private void checkUndoStats() {
		final ActionMap actionMap = getActionMap();

		actionMap.get("Undo").setEnabled( undoManager.canUndo() );
		actionMap.get("Redo").setEnabled( undoManager.canRedo() );
		
	}

	public void paste(String text) {
		
		try {
			final Document d = getDocument();
			d.insertString(getCaretPosition(), text, null);
		} catch (BadLocationException e) {
			assert false;
		}
	}
}