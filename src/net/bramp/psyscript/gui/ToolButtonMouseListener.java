/**
 * 
 */
package net.bramp.psyscript.gui;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * Allows the toolbar's borders to change when hovered over etc
 * @author Andrew Brampton
 *
 */
class ToolButtonMouseListener implements MouseListener {

	final static Border marginBorder = BorderFactory.createEmptyBorder (5, 5, 5, 5);		
	final static Border upBorder = new CompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), marginBorder);
	final static Border downBorder = new CompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), marginBorder);
	final static Border blankBorder = createEmptyBorder(downBorder.getBorderInsets(null));
	
	final JButton btn;
	boolean isOver = false;

	/**
	 * Static method to make it easier to call BorderFactory.createEmptyBorder with a Insets
	 * @param size
	 * @return
	 */
	private static Border createEmptyBorder(final Insets size) {
		return BorderFactory.createEmptyBorder (
				size.top, 
				size.left, 
				size.bottom, 
				size.right
		);		
	}
	
	ToolButtonMouseListener( JButton btn ) {
		this.btn = btn;
		btn.setBorder( blankBorder );
	}
	
	public void mouseEntered(MouseEvent e) {
		if ( btn.isEnabled() )
			btn.setBorder( upBorder );
		isOver = true;
	}
	
	public void mouseExited(MouseEvent e) {
		if ( btn.isEnabled() )
			btn.setBorder( blankBorder );
		isOver = false;
	}
	
	public void mousePressed(MouseEvent e) {
		if ( btn.isEnabled() )
			btn.setBorder( downBorder );
	}

	public void mouseReleased(MouseEvent e) {
		if ( btn.isEnabled() ) {
			if ( isOver )
				btn.setBorder( upBorder );
			else
				btn.setBorder( blankBorder );
		}
	}

	public void mouseClicked(MouseEvent e) {}
}