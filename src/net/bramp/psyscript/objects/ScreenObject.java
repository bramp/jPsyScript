package net.bramp.psyscript.objects;

import net.bramp.psyscript.Program;

public abstract class ScreenObject {

	final protected Program program;
	
	public ScreenObject(Program program) {
		assert program != null;

		this.program = program;
	}
	
	public Program getProgram() {
		return program;
	}

	/**
	 * Shows this object
	 */
	public abstract void show();

	/**
	 * Hides this object
	 */
	public abstract void hide();

	/**
	 * Move and size this object
	 * @param x
	 * @param y
	 */
	public abstract void setPosition(int x, int y, int width, int height);

	public abstract void setPosition(int x, int y);

}
