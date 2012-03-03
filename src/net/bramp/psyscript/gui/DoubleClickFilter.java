package net.bramp.psyscript.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

	
	/**
	 * Class to handle Double Clicks and make sure 3 clicks don't get in
	 * @author Andrew Brampton
	 *
	 */
	public class DoubleClickFilter implements MouseListener, ActionListener {

		private final List<MouseListener> listeners = new ArrayList<MouseListener>();
		private final Timer timer;

		private MouseEvent lastEvent;

		public DoubleClickFilter() {
			lastEvent = null;
			timer = new Timer(500, this);
		}

		public DoubleClickFilter(MouseListener listener) {
			this();
			addMouseListener(listener);
		}

		public void addMouseListener(MouseListener listener) {
			listeners.add( listener );
		}
	 
		public void actionPerformed(ActionEvent argEvent) {
			timer.stop();
			lastEvent = null;
		}
	 
		public void mouseClicked(MouseEvent argEvent) {

			// Check if this is a double/triple click
			if (timer.isRunning() && lastEvent.getComponent() == argEvent.getComponent())
				return;

			for ( MouseListener l : listeners )
				l.mouseClicked(argEvent);
			
			// If we have clicked many times, stop future clicks
			if ( argEvent.getClickCount() >= 2 ) {
				timer.restart();
			}
			
			lastEvent = argEvent;
		}
	 
		public void mouseEntered(MouseEvent argEvent) {
			for ( MouseListener l : listeners )
				l.mouseEntered(argEvent);
		}
	 	public void mouseExited(MouseEvent argEvent) {
			for ( MouseListener l : listeners )
				l.mouseExited(argEvent);
	 	}
	 	public void mousePressed(MouseEvent argEvent) {
			for ( MouseListener l : listeners )
				l.mousePressed(argEvent);
	 	}
	 	public void mouseReleased(MouseEvent argEvent) {
			for ( MouseListener l : listeners )
				l.mouseReleased(argEvent);
	 	}
	}