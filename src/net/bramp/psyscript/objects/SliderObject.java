package net.bramp.psyscript.objects;

import net.bramp.psyscript.NotImplementedException;
import net.bramp.psyscript.Program;

public class SliderObject extends ScreenObject {
	
	public SliderObject(Program program) {
		super(program);
	}

	@Override
	public String toString() {
		return "slider";
	}
	

	@Override
	public void hide() {
		throw new NotImplementedException("hide() is not implemented on SliderObject");
	}

	@Override
	public void show() {
		throw new NotImplementedException("show() is not implemented on SliderObject");
	}

	public void loadImage(String filename) {
		throw new NotImplementedException("loadImage() is not implemented on SliderObject");
	}

	@Override
	public void setPosition(int x, int y, int width, int height) {
		throw new NotImplementedException("setPosition() is not implemented on SliderObject");
	}

	@Override
	public void setPosition(int x, int y) {
		throw new NotImplementedException("setPosition() is not implemented on SliderObject");
	}

}
