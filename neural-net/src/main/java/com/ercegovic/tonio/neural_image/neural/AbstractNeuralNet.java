package com.ercegovic.tonio.neural_image.neural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public abstract class AbstractNeuralNet {
	private static final double ETA = 0.05;
	protected List<RealMatrix> weights;
	protected List<RealVector> biases;
	
	public AbstractNeuralNet(int... layers) {
		biases = new ArrayList<>(layers.length-1);
		weights = new ArrayList<>(layers.length-1);
		
		for(int i = 1; i < layers.length; ++i) {
			int n = layers[i];
			int nLast = layers[i-1];
			//n=rows, nLast=columns
			double[][] matrixData = new double[n][nLast];
			RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
			double[] vectorData = new double[n];
			RealVector v = MatrixUtils.createRealVector(vectorData);
			
			weights.add(m);
			biases.add(v);
		}
	}
	
	protected abstract RealVector f(RealVector m);
	protected abstract void setRandomWeights();
	
	public RealVector calculate(RealVector sample) {
		RealVector y = sample;
		for(int i = 0, l = weights.size(); i < l; ++i) {
			y = weights.get(i).operate(y);
			y = y.add(biases.get(i));
			y = f(y);
		}
		return y;
	}
	
	public void train(List<SampleLabelPair> samples, List<SampleLabelPair> checks, double minErr, long limit) {
		setRandomWeights();
		
		double currErr = Double.MAX_VALUE;
		double minErrEver = currErr;
		int k = 0;
		while(minErr < currErr && k <= limit) {
			Collections.shuffle(samples);
			System.out.println("--->  Epoch: "+k);
			for(int i = 0, l = samples.size(); i < l; ++i) {
				SampleLabelPair sampleAndLabel = samples.get(i);
				RealVector sample = sampleAndLabel.getSample();
				RealVector corrOutput = sampleAndLabel.getLabel();
				
				List<RealVector> outputs = calculateAll(sample);
				
				RealVector oError = getOutputError(
						outputs.get(outputs.size()-1), corrOutput);

				List<RealVector> otherErrors = getOtherErrors(outputs, 
						oError);				
				
				mutateWeights(otherErrors, outputs);
			}
			double tmp = calcError(checks);
			System.out.println("Epoch error: " + tmp);
			if(tmp > currErr) {
				System.out.println("Overtraining!");
				//break;
			}
			currErr = tmp;
			if(minErrEver > currErr) minErrEver = currErr;
			System.out.println("Lowest error: "+minErrEver);
			++k;
		}
		System.out.println("Training done!");
	}

	private double calcError(List<SampleLabelPair> samples) {
		int sum = 0;
		for(SampleLabelPair p: samples) {
			RealVector o = calculate(p.getSample());
			if(isCorrect(o, p.getLabel())) {
				sum++;
			}
		}
		return 1-sum/(double)samples.size();
	}
	private boolean isCorrect(RealVector o, RealVector label) {
		int maxI = 0, maxJ = 0;
		for(int i = 0, l = o.getDimension(); i < l; ++i) {
			if(o.getEntry(i) > o.getEntry(maxI)) maxI = i;
			if(label.getEntry(i) > label.getEntry(maxJ)) maxJ = i;
		}
		return maxI == maxJ;
	}
	

	private void mutateWeights(List<RealVector> otherErrors,
			List<RealVector> outputs) {
		for(int k = 0, l = weights.size(); k < l; ++k) {
			RealMatrix layerWeights = weights.get(k);
			RealVector layerBias = biases.get(k);
			RealVector output = outputs.get(k);
			RealVector nextErrors = otherErrors.get(k+1);

			//korekcija svih tezina
			for(int i = 0, ll = layerWeights.getRowDimension(); i < ll; ++i) {
				for(int j = 0, lll = layerWeights.getColumnDimension()
						; j < lll; ++j) {
					double errorJ = nextErrors.getEntry(i);
					double yi = output.getEntry(j);
					layerWeights.setEntry(i, j, layerWeights.getEntry(
							i, j)+ETA*yi*errorJ);
				}
				//korekcija pragova
				layerBias.setEntry(i, layerBias.getEntry(i)
						+ETA*nextErrors.getEntry(i));
			}	
			weights.set(k, layerWeights);
			biases.set(k, layerBias);
		}
	}
	private List<RealVector> getOtherErrors(List<RealVector> outputs,
			RealVector oError) {
		List<RealVector> errors = new ArrayList<>(outputs.size());
		while(errors.size() < outputs.size()) errors.add(null);
		errors.set(errors.size()-1, oError);
		//sloj petlja (k)
		for(int k = errors.size() - 2; k >= 0; --k) {
			//po neuronima (i)
			RealVector neurons = outputs.get(k);
			RealVector nextErrors = errors.get(k+1);
			RealMatrix layerWeights = weights.get(k);
			double[] errorsOnLayer = new double[neurons.getDimension()];
			for(int i = 0, l = neurons.getDimension(); i < l; ++i) {
				double yi = neurons.getEntry(i);
				double error = yi*(1-yi);
				double sum = 0;
				//po greskama sloja nakon
				for(int j = 0, ll = nextErrors.getDimension(); j < ll; ++j) {
					sum += nextErrors.getEntry(j)*
							layerWeights.getEntry(j, i);

				}
				error *= sum;
				errorsOnLayer[i] = error;
			}
			errors.set(k, MatrixUtils.createRealVector(errorsOnLayer));
		}
		return errors;
	}
	private List<RealVector> calculateAll(RealVector sample) {
		List<RealVector> toReturn = new ArrayList<>();
		RealVector y = sample;
		toReturn.add(y);
		for(int i = 0, l = weights.size(); i < l; ++i) {
			y = weights.get(i).operate(y);
			y = y.add(biases.get(i));
			y = f(y);
			toReturn.add(y);
		}
		return toReturn;
	}
	private RealVector getOutputError(RealVector outputs, RealVector corrO) {
		double[] data = new double[outputs.getDimension()];
		for(int i = 0, l = outputs.getDimension(); i < l; ++i) {
			double o = outputs.getEntry(i);
			double t = corrO.getEntry(i);
			data[i] = o*(1-o)*(t-o);
		}
		return MatrixUtils.createRealVector(data);
	}
}
