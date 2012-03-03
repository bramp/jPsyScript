package net.bramp.psyscript.objects;

import net.bramp.psyscript.NotImplementedException;
import net.bramp.psyscript.Program;

public class CursorObject extends ScreenObject {

	public CursorObject(Program program) {
		super(program);
	}

	@Override
	public String toString() {
		return "cursor";
	}

	@Override
	public void hide() {
		throw new NotImplementedException("hide() is not implemented on Cells");
	}

	@Override
	public void show() {
		throw new NotImplementedException("show() is not implemented on Cells");
	}

	@Override
	public void setPosition(int x, int y, int width, int height) {
		throw new RuntimeException("You can not set the size of position of the cursor");
	}

	@Override
	public void setPosition(int x, int y) {
		throw new RuntimeException("You can not set the size of position of the cursor");
	}
}
