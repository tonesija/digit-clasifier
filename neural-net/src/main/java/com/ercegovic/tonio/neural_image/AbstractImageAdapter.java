package com.ercegovic.tonio.neural_image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class AbstractImageAdapter {
	protected int x;
	protected int y;
	
	
	public AbstractImageAdapter(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double[] transform(String path) {
		BufferedImage img = null;
		double[] toReturn = null;
		try {
		    img = ImageIO.read(new File(path));
		    BufferedImage res = resize(img);
		    
		    BufferedImage noColor = toGrayscale(res);
		    		    		
            int[] imgIntArr = getImagePixels(noColor);
            
            toReturn = new double[imgIntArr.length];
            for(int i = 0; i < imgIntArr.length; ++i) {
            	toReturn[i] = imgIntArr[i]/(double)255;
            }
            
            
            for(int i = 0; i < x; ++i) {
            	for(int j = 0; j < y; ++j) {
            		if(toReturn[i*x+j] > 0.1) {
            			toReturn[i*x+j] = 1;
            		}else {
            			toReturn[i*x+j] = 0;
            		}
            	}
            }
            
            toReturn = pool(toReturn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
		
	}
	
	protected abstract double[] pool(double[] pixels);
	
	private BufferedImage resize(BufferedImage inputImage) {
		BufferedImage outputImage = new BufferedImage(x,
				y, inputImage.getType());

		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, x, y, null);
		g2d.dispose();

		return outputImage;
	}
	
	private BufferedImage toGrayscale(BufferedImage input) {
		BufferedImage image = new BufferedImage(x, y,  
			    BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = image.getGraphics();  
		g.drawImage(input, 0, 0, null);  
		g.dispose();  
		return image;
	}
	
	public int[] getImagePixels(BufferedImage img) {
	    int [] dummy = null;
	    int wid, hgt;

	    // compute size of the array
		wid = img.getWidth();
		hgt = img.getHeight();

		// start getting the pixels
	    Raster pixelData;
	    pixelData = img.getData();
	    return pixelData.getPixels(0, 0, wid, hgt, dummy);
	}


	public List<double[]> transformAll(String folderPath){
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		
		List<double[]> toReturn = new ArrayList<>();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			  if (listOfFiles[i].isFile()) {
			    String path = folderPath + "/" + listOfFiles[i].getName();
			    toReturn.add(transform(path));
			  } else if (listOfFiles[i].isDirectory()) {
			    System.out.println("Directory " + listOfFiles[i].getName());
			  }
		}
		return toReturn;
	}
	
	protected void print(double[] pixels) {
		NumberFormat formatter = new DecimalFormat("#0");  
		for(int i = 0; i < x; ++i) {
			System.out.println();
			for(int j = 0; j < y; ++j) {
				double e = pixels[i*x+j];

				System.out.print(formatter.format(e));
			}
		}
		System.out.println();
	}
}
