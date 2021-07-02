package com.ercegovic.tonio.neural_image.neural;

import org.apache.commons.math3.linear.RealVector;

public class SampleLabelPair {
	private RealVector sample;
	private RealVector label;
	
	public SampleLabelPair(RealVector sample, RealVector label) {
		super();
		this.sample = sample;
		this.label = label;
	}

	public RealVector getSample() {
		return sample;
	}

	public RealVector getLabel() {
		return label;
	}
}
