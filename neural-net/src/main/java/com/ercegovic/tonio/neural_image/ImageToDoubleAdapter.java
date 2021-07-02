package com.ercegovic.tonio.neural_image;

public class ImageToDoubleAdapter extends AbstractImageAdapter {	
	public ImageToDoubleAdapter(int x, int y) {
		super(2*x, 2*y);
	}

	@Override
	protected double[] pool(double[] pixels) {
		//--- pooling ---
        double[] toReturn = new double[pixels.length/(2*2)];
        int k = 0;
        for(int i = 0; i < x; i+=2) {
        	for(int j = 0; j < y; j+=2) {
        		//double val = toReturn[i*x+j] + toReturn[(i+1)*x+j]+
        				//toReturn[i*x+j+1] + toReturn[(i+1)*x+j+1];
        		double max = Math.max(Math.max(Math.max(pixels[i*x+j]
        				, pixels[(i+1)*x+j]), pixels[i*x+j+1])
        				, pixels[(i+1)*x+j+1]);
        		toReturn[k++] = max;
        	}
        }
        return toReturn;
	}
}
