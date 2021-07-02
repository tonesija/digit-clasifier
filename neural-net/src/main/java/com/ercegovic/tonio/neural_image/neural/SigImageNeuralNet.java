package com.ercegovic.tonio.neural_image.neural;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class SigImageNeuralNet extends AbstractNeuralNet {
	
	public SigImageNeuralNet(int... h) {
		super(h);
	}


	@Override
	protected RealVector f(RealVector m) {
		return m.map((v)->{
			return sigmoid(v);
		});
	}

	@Override
	protected void setRandomWeights() {
		//Random r = new Random();
		int lb = -1;
		int ub = 1;
		
		for(RealMatrix m: weights) {
			int nRow = m.getColumnDimension();
			for(int i = 0; i < nRow; ++i) {
				int nCol = m.getRowDimension();
				double[] data = new double[nCol];
				for(int j = 0, l = data.length; j < l; ++j) {
					//data[j] = r.nextGaussian();
					data[j] = Math.random()*(ub-lb)+lb;
				}
				m.setColumn(i, data);
			}
		}
		for(RealVector b: biases) {
			for(int j = 0, l = b.getDimension(); j < l; ++j) {
				//b.setEntry(j, r.nextGaussian());
				//b.setEntry(j, Math.random()*(ub-lb)+lb);
				b.setEntry(j, 0);
			}
		}
	}
	
	private static double sigmoid(double x){
	    return 1 / (1 + Math.exp(-x));
	}
}
