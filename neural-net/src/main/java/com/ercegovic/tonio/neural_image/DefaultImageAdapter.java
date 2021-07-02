package com.ercegovic.tonio.neural_image;

public class DefaultImageAdapter extends AbstractImageAdapter{

	public DefaultImageAdapter(int x, int y) {
		super(x, y);
	}

	@Override
	protected double[] pool(double[] pixels) {
		return pixels;
	}

}
