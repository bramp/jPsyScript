package net.bramp.psyscript.objects;

import java.awt.Font;
import java.io.IOException;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.gui.CellPanel;


/**
 * A wrapper around a Cell object
 * @author Andrew Brampton
 */
public abstract class Cell extends ScreenObject {

	protected static void checkValidName(String name) throws IllegalArgumentException {
		// Check the name is a valid cell
		if (name.length() != 1)
			throw new IllegalArgumentException("Cell name must be A-Z, not '" + name + "'");

		name = name.toUpperCase();

		if (name.charAt(0) < 'A' || name.charAt(0) > 'Z')
			throw new IllegalArgumentException("Cell name must be A-Z, not '" + name + "'");
	}

	public Cell(Program p) {
		super(p);
	}
	
	public abstract String getName();
	
	@Override
	public String toString() {
		return "cell " + getName();
	}

	private CellPanel getCell() {
		return program.getGUI().getCell(getName());
	}

	@Override
	public void hide() {
		getCell().setVisible(false);
	}

	@Override
	public void show() {
		getCell().setVisible(true);
	}

	public void loadImage(String filename) throws IOException {
		getCell().setImage( program.getAbsolutePath(filename) );
	}


	@Override
	public void setPosition(int x, int y, int width, int height) {
		getCell().setBounds(x, y, width, height);
	}

	@Override
	public void setPosition(int x, int y) {
		getCell().setLocation(x, y);
	}

	public void setText(String string) {
		getCell().setText(string);
	}
	
	public Font getFont() {
		return getCell().getFont();
	}
	
	public void setFont(Font font) {
		getCell().setFont(font);
	}
}
