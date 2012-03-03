package net.bramp.psyscript.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;

import net.bramp.psyscript.ClickEvent;
import net.bramp.psyscript.Event;
import net.bramp.psyscript.Program;

/**
 * The GUI to show the running script
 * 
 * @author Andrew
 *
 */
public class ProgramGUI extends JComponent implements ComponentListener, MouseListener, KeyListener {

	final MainGUI main;
	final Program program;

	/**
	 * Used to cache all the cell's positions, for easy click tracking and quick repaint
	 * Order in the Z order
	 */
	final List<CellPanel> zOrder = new ArrayList< CellPanel >();

	final Map<String, CellPanel> cells = new TreeMap<String, CellPanel>();

	final TextPanel text;

	/**
	 * Object we used for waiting synchronisation
	 */
	private Object waitObject = new Object();
	
	/**
	 * Event used for filtering input
	 */
	Event event;
	
	public ProgramGUI(MainGUI main, Program program) {
		this.main = main;
		this.program = program;

        text = new TextPanel(program); 
        add(text);

        setLayout(null);

        // Register for events so we know when we are resized
        addComponentListener(this);
        addKeyListener(this);
		addMouseListener(this);

		setDoubleBuffered( true );
	}

	public void run() throws Exception {
		reset();

		requestFocus();
        program.run(this);
	}

	public synchronized CellPanel getCell( String name ) {

		CellPanel cell = cells.get(name);

		if ( cell == null ) {

			// Setup a default cell
			cell = new CellPanel(this, name);

			cell.setVisible(false);
			zOrder.add( cell );

			cells.put(name, cell);
		}

		return cell;
	}
	
	public TextPanel getTextPanel() {
		return text;
	}
	
	/**
	 * Turns a user X,Y coordinate in the screen's coordinate
	 * @param x
	 * @return
	 */
	public Rectangle userToScreen(Rectangle user) {
		int x = user.x + ( getWidth() - user.width ) / 2;
		int y = user.y + ( getHeight() - user.height ) / 2;

		return new Rectangle(x, y, user.width, user.height);
	}

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {}

	public void componentShown(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		doLayout();
		validate();
	}

	public synchronized void doLayout() {
		super.doLayout();

		// When we resize all the components need to change position
		for (CellPanel c : zOrder ) {
			c.screenPos = userToScreen( c.getPos() );
		}

		// Always make the text full screen
		text.setBounds( getBounds() );
	}

	private void _waitFor(long milliseconds, Event event) {
		assert milliseconds >= 0;
		
		synchronized ( waitObject ) {
			try {
				if ( event != null ) {
					this.event = event;
					event.start();
				}

				waitObject.wait(milliseconds);
			} catch (InterruptedException e) {}

			if ( event != null ) {
				this.event = null;
				event.expire();
			}
		}
	}
	
	/**
	 * For for milliseconds, or until the event occurs
	 * whichever occurs first
	 * @param milliseconds
	 * @param event
	 */
	public void waitFor(long milliseconds, Event event) {
		if ( event == null )
			throw new IllegalArgumentException("Event can not be null");
		
		if ( milliseconds <= 0 )
			milliseconds = 1;
		
		_waitFor( milliseconds, event );
	}

	/**
	 * Wait for a specific number of milliseconds to elapse
	 * @param milliseconds
	 */
	public  void waitFor(long milliseconds) {
		if ( milliseconds <= 0 )
			milliseconds = 1;

		_waitFor( milliseconds, null );
	}
	
	/**
	 * Wait for event
	 * @param event
	 */
	public void waitFor(Event event) {
		if ( event == null )
			throw new IllegalArgumentException("Event can not be null");

		_waitFor( 0, event );
	}

	
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);

		text.setBackground(c);
	}

	public void keyPressed(KeyEvent e) {
		
		synchronized ( waitObject ) {
			if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
				waitObject.notifyAll();
				program.stop();
				return;
			}

			if (event == null)
				return;

			// Check if this mouse event is in the correct cell
			String name = "" + e.getKeyChar();

			if ( event instanceof net.bramp.psyscript.KeyEvent && event.filter(name) ) {

				// Update the cell that was last clicked
				event.stop(name);
				
				waitObject.notify();
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		synchronized ( waitObject ) {
			if (event == null)
				return;

			CellPanel found = null;

			// Figure out which cell this was in
			for (CellPanel c : zOrder ) {
				if ( c.isVisible() && c.screenPos.contains(e.getPoint()) ) {
					found = c;
					break;
				}
			}

			if ( found != null ) {
				String name = found.getName();

				// Check if this mouse event is in the correct cell
				if ( event instanceof ClickEvent && event.filter(name) ) {
				
					// Update the cell that was last clicked
					event.stop(name);
					
					waitObject.notify();
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

	public void paint(Graphics g) {

		//super.paint(g);

		try {
			// Paint the background
			g.setColor( getBackground() );
			g.fillRect(0, 0, getWidth(), getHeight());
	
			// Now paint all the cells
			for ( final CellPanel c : zOrder ) {
				if ( c.isVisible() ) {
					Image img = c.getImage();
	
					if ( c.screenPos == null ) {
						c.screenPos = userToScreen( c.getPos() );
					}
					final Rectangle screenPos = c.screenPos;
	
					g.drawImage(img, screenPos.x, screenPos.y, screenPos.width, screenPos.height, this);
				}
			}
	
			if ( text.isVisible() )
				text.paint(g);

		} catch (RuntimeException e) {
			program.stop();
			
			if ( main != null )
				main.displayException( e );			
			else
				throw e;
		}
	}
	
	public void reset() {

		zOrder.clear();
		cells.clear();

		setBackground( new Color(255, 255, 255) );
	}
}
