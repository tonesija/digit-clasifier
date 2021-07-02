package com.ercegovic.tonio.neural_image;

public class ImageCenteringAdapter extends AbstractImageAdapter {	
	public ImageCenteringAdapter(int x, int y) {
		super(x, y);
	}

	@Override
	protected double[] pool(double[] pixels) {
		//make frame
		for(int i = 0; i < x; ++i){
			pixels[i] = 0;
			pixels[(x-1)*(y)+i] = 0;
			pixels[x*i] = 0;
			pixels[x*(i+1)-1] = 0;
		}
		
		int l,r,t,b;
		//top
		l = findLeft(pixels);
		r = findRight(pixels);
		t = findTop(pixels);
		b = findBottom(pixels);
		
		int h = (l - (x-r-1))/2;
		int v = (t - (y-b-1))/2;
		
		if(h < 0) {
			moveRight(pixels, h);
		}else if (h > 0){
			moveLeft(pixels, h);
		}
		
		if(v < 0) {
			moveDown(pixels, v);
		}else if(v > 0){
			moveUp(pixels, v);
		}
		
		System.out.println("Centrirani");
		print(pixels);
		return pixels;
	}
	
	/**
	 * For testing.
	 */
	public static void main(String[] args) {
		ImageCenteringAdapter adapter = new ImageCenteringAdapter(7,7);
		
		double[] data = 
			{0,0,0,0,0,0,0,
			 0,1,0,0,0,0,0,
			 0,0,0,0,0,0,0,
			 0,0,0,1,0,0,0,
			 0,0,0,1,1,1,0,
			 0,0,0,0,1,1,0,
			 0,0,0,0,0,0,0};
		
		double[] centeredData = adapter.pool(data);
		adapter.print(centeredData);
	}
	private int moveUp(double[] pixels, int n) {
		for(int k = 0; k < n; ++k) {
			for(int i = 0; i < y-1; ++i) {
				for(int j = 0; j < x; ++j) {
					pixels[x*i+j] = pixels[x*(i+1)+j];
				}
			}
		}
		return -1;
	}
	private int moveDown(double[] pixels, int n) {
		for(int k = 0; k < -n; ++k) {
			for(int i = y-1; i >= 1; --i) {
				for(int j = 0; j < x; ++j) {
					pixels[x*i+j] = pixels[x*(i-1)+j];
				}
			}	
		}
		return -1;
	}
	private int moveLeft(double[] pixels, int n) {
		for(int k = 0; k < n; ++k) {
			for(int j = 0; j < y-1; ++j) {
				for(int i = 0; i < x; ++i) {
					pixels[x*i+j] = pixels[x*i+j+1];
				}
			}
		}
		return -1;
	}
	private int moveRight(double[] pixels, int n) {
		for(int k = 0; k < -n; ++k) {
			for(int j = x-1; j >= 1; --j) {
				for(int i = 0; i < x; ++i) {
					pixels[x*i+j] = pixels[x*i+j-1];
				}
			}
		}
		return -1;
	}
	private int findTop(double[] pixels) {
		for(int i = 0; i < y; ++i) {
			for(int j = 0; j < x; ++j) {
				if(pixels[x*i+j] > 0.05) return i;
			}
		}
		return -1;
	}
	private int findBottom(double[] pixels) {
		for(int i = y-1; i >= 0; --i) {
			for(int j = 0; j < x; ++j) {
				if(pixels[x*i+j] > 0.05) return i;
			}
		}
		return -1;
	}
	private int findLeft(double[] pixels) {
		for(int j = 0; j < y; ++j) {
			for(int i = 0; i < x; ++i) {
				if(pixels[x*i+j] > 0.05) return j;
			}
		}
		return -1;
	}
	private int findRight(double[] pixels) {
		for(int j = x-1; j >= 0; --j) {
			for(int i = 0; i < x; ++i) {
				if(pixels[x*i+j] > 0.05) return j;
			}
		}
		return -1;
	}
}
