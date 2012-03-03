package net.bramp.psyscript.action;

import java.util.List;

import net.bramp.psyscript.ConstExpression;
import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.Table;
import net.bramp.psyscript.parser.SemanticException;
import net.bramp.psyscript.variables.Variable;


public class RepeatUsingTableAction extends Action {

	private final Expression table;
	private final Expression limit;
	
	private final List<Variable> vs;
	private final List<Action> cs;

	public RepeatUsingTableAction(final Program program, final int line, Expression table, List<Variable> vs, List<Action> cs) {
		this( program, line, table, null, vs, cs);
	}

	public RepeatUsingTableAction(final Program program, final int line, Expression table, Expression limit, List<Variable> vs, List<Action> cs) {
		super(program, line);

		assert table != null;
		assert vs != null;
		assert cs != null;

		this.table = table;
		this.limit = limit;
		
		this.vs = vs;
		this.cs = cs;
	}

	@Override
	public void run() throws Exception {
		final String tableName = table.getString();
		final Table t = program.getTable(tableName);

		int limit;
		if ( this.limit != null )
			limit = this.limit.getInteger();
		else
			limit = t.getRows();

		while ( limit > 0 ) {
			if ( !program.isRunning() )
				return;
			
			limit--;
			t.fill( t.nextRow(), vs );
			runActions(cs);
		}
	}

	@Override
	public void check() throws SemanticException {
		if ( table instanceof ConstExpression ) {
			final String tableName = table.getString();
			final Table t = program.getTable(tableName);
			
			if ( t == null )
				throw new SemanticException(getLine(), "Table '" + tableName + "' does not exist");

			if ( t.getColumns() != vs.size() )
				throw new SemanticException(getLine(), "Table '" + tableName + "' has " + t.getColumns() + " columns but " + vs.size() + " variables used");
		}
		
		checkActions( cs );
	}
}
