package net.bramp.psyscript.action;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.objects.Cells;
import net.bramp.psyscript.parser.SemanticException;

/**
 * Sets the Cell's font
 * @author Andrew
 *
 */
public class SetCellFontAction extends Action {

	private final Cells c;
	private final Expression fontname;
	private final Expression fontsize;
	
	public SetCellFontAction(final Program program, final int line, Cells c, Expression fontname, Expression fontsize) {
		super(program, line);

		assert c != null;
		assert fontname != null;
		assert fontsize != null;

		this.c = c;
		this.fontname = fontname;
		this.fontsize = fontsize;
	}

	@Override
	public void run() {
		if ( fontsize == null)
			c.setFont(fontname.getString());
		else
			c.setFont(fontname.getString(), fontsize.getInteger());
	}

	@Override
	public void check() throws SemanticException {}

}
