package net.bramp.psyscript;

import net.bramp.psyscript.objects.Cell;
import net.bramp.psyscript.objects.Cells;
import net.bramp.psyscript.variables.Variable;

public class ClickEvent extends Event {
	
	Cells cells;
	
	public ClickEvent (Variable lastClick, Variable lastClickTime) {
		super(lastClick, lastClickTime);
		
	}

	public void setFilter(Cell c) {
		setFilter ( new Cells( c ) );
	}

	public void setFilter(Cells cells) {
		this.cells = cells;
	}

	@Override
	public boolean filter(String name) {
		if ( cells != null )
			return cells.contains(name);
		else
			return true;
	}

}
