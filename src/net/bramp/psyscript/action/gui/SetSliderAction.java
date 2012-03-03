package net.bramp.psyscript.action.gui;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.NotImplementedException;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.objects.SliderObject;
import net.bramp.psyscript.parser.SemanticException;

public class SetSliderAction extends Action {

	final SliderObject slider;
	final Expression v;
	final Expression min;
	final Expression max;

	public SetSliderAction(final Program program, final int line, SliderObject slider, Expression v, Expression min, Expression max) {
		super(program, line);

		assert slider != null;
		assert v != null;
		
		this.slider = slider;
		this.v = v;
		this.min = min;
		this.max = max;
	}

	public SetSliderAction(Program program, final int line, SliderObject slider, Expression v) {
		this(program, line, slider, v, null, null);
	}

	@Override
	public void run() {
		throw new NotImplementedException("SetSliderAction not implemented");
	}

	@Override
	public void check() throws SemanticException {}

}
