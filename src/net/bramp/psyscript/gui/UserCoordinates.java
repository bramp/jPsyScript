package net.bramp.psyscript.gui;

import java.awt.Component;
import java.awt.Rectangle;

/**
 * 
 * @author Andrew Brampton
 *
 */
public class UserCoordinates {

	final ProgramGUI gui;
	final Component c;

	final Rectangle pos = new Rectangle(0,0,0,0);

	public UserCoordinates(ProgramGUI gui, Component c) {
		this.gui = gui;
		this.c = c;
	}
	
	/**
	 * Set the size of this object
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y, int width, int height) {
		pos.x = x;
		pos.y = y;
		
		pos.width = width;
		pos.height = height;

		c.setBounds( gui.userToScreen( pos ) );
	}
	
	public void setPosition(int x, int y) {
		pos.x = x;
		pos.y = y;

		c.setBounds( gui.userToScreen( pos ) );
	}

	public int getUserX() {
		return pos.x;
	}

	public int getUserY() {
		return pos.y;
	}
	
	public int getUserWidth() {
		return pos.width;
	}
	
	public int getUserHeight() {
		return pos.height;
	}

	public Rectangle getUserPosition() {
		return pos;
	}
}
