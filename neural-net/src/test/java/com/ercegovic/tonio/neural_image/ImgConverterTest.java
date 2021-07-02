package com.ercegovic.tonio.neural_image;

import static org.junit.Assert.*;


import org.junit.Test;

public class ImgConverterTest {

	@Test
	public void testAlone() {
		ImageToDoubleAdapter converter = new ImageToDoubleAdapter(32, 32);
		double[] pixArr = converter.transform("britni.jpg");
		assertEquals(pixArr.length, 1024);
		for(int i = 0; i < 1024; ++i) {
			assertTrue(pixArr[i] <= 1.0 && pixArr[i] >= 0.0);
		}
	}
	
	@Test
	public void testFolder() {
		ImageToDoubleAdapter converter = new ImageToDoubleAdapter(32, 32);
		converter.transformAll("slike");
	}

}
