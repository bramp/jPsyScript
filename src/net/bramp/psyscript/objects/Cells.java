package net.bramp.psyscript.objects;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.variables.ConstVariable;
import net.bramp.psyscript.variables.Variable;


/**
 * A wrapper around a Cells object
 * @author Andrew Brampton
 */
public class Cells extends ScreenObject implements Iterable<Cell> {

	private final Variable name;

	public Cells(Cell c) {
		this(c.getProgram(), c.getName());
	}
	
	public Cells(Program p, String name) {
		super(p);
		this.name = new ConstVariable( null, name );
	}

	public Cells(Program p, Variable var) {
		super(p);
		name = var;
	}

	/**
	 * Return a list of cells
	 * @return
	 */
	private List<Cell> split() {
		String cs = name.getString();

	  	List<Cell> cells = new ArrayList<Cell>( cs.length() );

	  	for ( char c : cs.toCharArray() )
	  		cells.add( new ConstCell( program, String.valueOf(c) ) );

	  	return cells;
	}
	
	@Override
	public String toString() {
		return "cells " + name.toString();
	}
	
	@Override
	public void hide() {
		for (Cell c : this)
			c.hide();
	}

	@Override
	public void show() {
		for (Cell c : this)
			c.show();
	}

	public void loadImage(String filename) throws IOException {
		for (Cell c : this)
			c.loadImage(filename);
	}

	@Override
	public void setPosition(int x, int y, int width, int height) {
		for (Cell c : this)
			c.setPosition(x, y, width, height);
	}

	public void setText(String text) {
		for (Cell c : this)
			c.setText(text);
	}

	@Override
	public void setPosition(int x, int y) {
		for (Cell c : this)
			c.setPosition(x, y);
	}

	public boolean contains(String name) {
		String cells = this.name.getString();
		return cells.contains(name);
	}

	public Iterator<Cell> iterator() {
		return split().iterator();
	}

	public void setFont(String fontname) {
		for (Cell c : this) {
			Font oldFont = c.getFont();
			Font newFont;

			if ( oldFont != null )
				newFont = new Font(fontname, oldFont.getStyle(), oldFont.getSize());
			else
				newFont = new Font(fontname, Font.PLAIN, 24);

			c.setFont(newFont);
		}
	}

	public void setFont(String fontname, int fontsize) {
		for (Cell c : this) {
			final Font oldFont = c.getFont();
			final Font newFont;

			if ( oldFont != null )
				newFont = new Font(fontname, oldFont.getStyle(), fontsize);
			else
				newFont = new Font(fontname, Font.PLAIN, fontsize);

			c.setFont(newFont);
		}
	}
	
	public void setFont(Font f) {
		for (Cell c : this) {
			c.setFont(f);
		}		
	}
}
