package net.bramp.psyscript.action.gui;

import java.io.IOException;

import net.bramp.psyscript.ConstExpression;
import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.objects.Cells;
import net.bramp.psyscript.parser.SemanticException;


public class LoadImageAction extends Action {

	private final Expression filename;
	private final Cells c;

	public LoadImageAction(final Program program, final int line, Expression filename, Cells c) {
		super(program, line);
		this.filename = filename;
		this.c = c;
	}

	@Override
	public void run() throws IOException {
		c.loadImage( filename.getString() );
	}

	@Override
	public void check() throws SemanticException {
		if ( filename instanceof ConstExpression ) {
			try {
				program.checkFileExists( filename.getString() );
			} catch (IOException e) {
				throw new SemanticException(getLine(), e);
			}
		}
	}

}
