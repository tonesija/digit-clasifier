package com.ercegovic.tonio.neural_image.neural;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Jesu li 2 broja ista.
 * @author tonio
 */
public class SimpleNeuralNet extends AbstractNeuralNet {
	
	public SimpleNeuralNet() {
		super(2,2,1);
	}

	@Override
	protected RealVector f(RealVector m) {
		return m.map((v)->{
			return sigmoid(v);
		});
	}

	@Override
	protected void setRandomWeights() {
		for(RealMatrix m: weights) {
			int nRow = m.getColumnDimension();
			for(int i = 0; i < nRow; ++i) {
				int nCol = m.getRowDimension();
				double[] data = new double[nCol];
				for(int j = 0, l = data.length; j < l; ++j) {
					data[j] = 1;
				}
				m.setColumn(i, data);
			}
		}
		for(RealVector b: biases) {
			for(int j = 0, l = b.getDimension(); j < l; ++j) {
				b.setEntry(j, 1);
			}
		}
	}
	
	private static double sigmoid(double x){
	    return 1 / (1 + Math.exp(-x));
	}

}
