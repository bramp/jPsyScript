package net.bramp.psyscript.action.gui;

import net.bramp.psyscript.Coordinate;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.objects.ScreenObject;
import net.bramp.psyscript.parser.SemanticException;


public class MoveAction extends Action {
	
	private final ScreenObject o;
	private final Coordinate pos;
	private final Coordinate size;
	
	public MoveAction(final Program program, final int line, ScreenObject o, Coordinate position, Coordinate size) {
		super(program, line);
		this.o = o;
		this.pos = position;
		this.size = size;
	}

	public MoveAction(final Program program, final int line, ScreenObject o, Coordinate position) {
		super(program, line);
		this.o = o;
		this.pos = position;
		this.size = null;
	}

	@Override
	public String toString() {
		return "move " + o + " to " + pos + ( size == null ? "" : " with size " + size );
	}

	@Override
	public void run() {
		if ( size != null ) {
			o.setPosition(pos.getX(), pos.getY(), size.getX(), size.getY());
		} else {
			o.setPosition(pos.getX(), pos.getY());
		}
	}

	@Override
	public void check() throws SemanticException {}
}
