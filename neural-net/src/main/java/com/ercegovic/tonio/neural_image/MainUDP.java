package com.ercegovic.tonio.neural_image;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import com.ercegovic.tonio.neural_image.neural.SampleLabelPair;
import com.ercegovic.tonio.neural_image.neural.SigImageNeuralNet;

public class MainUDP {
	static private final int INPUT_LAYER = 28*28;
	static private final int OUTPUT_LAYER = 10;

	public static void main(String[] args) throws SocketException {
		AbstractImageAdapter converter = new DefaultImageAdapter(28,28);
		
		Scanner sc = new Scanner(System.in);
		System.out.print("h: ");
		String[] hs = sc.nextLine().split(",");
		int[] h = new int[hs.length+2];
		h[0] = INPUT_LAYER;
		h[h.length-1] = OUTPUT_LAYER;
		for(int i = 0; i < hs.length; ++i) {
			h[i+1] = Integer.parseInt(hs[i]);
		}
		SigImageNeuralNet net = new SigImageNeuralNet(h);
		
		System.out.println("Upiši broj epoha: ");
		long EPOCHS = sc.nextLong();
		
		System.out.println("Ciljana pogreška skupa za testiranje: ");
		double ERROR_LIMIT = sc.nextDouble();
		sc.close();
				
		List<SampleLabelPair> samplesAndLabels = new ArrayList<>();
		List<SampleLabelPair> checkSamplesAndLabels = new ArrayList<>();
		
		int imagesLoaded = 0;
		for(int i = 0; i <= 9; ++i) {
			List<double[]> vals = converter.transformAll("slike/"+i);
			List<double[]> checkVals = converter.transformAll("slike/check/"+i);
			imagesLoaded += vals.size();
			System.out.println("Test images loaded: " + imagesLoaded);
			
			RealVector corrOutputs = createLabel(i);
			for(int j = 0, l = vals.size(); j < l; ++j) {
				RealVector sample = MatrixUtils.createRealVector(vals.get(j));
				//printImg(sample);
				
				samplesAndLabels.add(new SampleLabelPair(sample, corrOutputs));
			}
			for(int j = 0, l = checkVals.size(); j < l; ++j) {
				RealVector checkSample = MatrixUtils.createRealVector(checkVals.get(j));
				
				checkSamplesAndLabels.add(new SampleLabelPair(checkSample, corrOutputs));
			}
		}
		//train
		net.train(samplesAndLabels, checkSamplesAndLabels, ERROR_LIMIT, EPOCHS);
		
		//UDP petlja za odgovaranje kljentima
		@SuppressWarnings("resource")
		DatagramSocket sock = new DatagramSocket(5555);
		ImageCenteringAdapter centeringAdapter = new ImageCenteringAdapter(28, 28);
		while(true) {
			try {
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				
				System.out.println("Čekam...");
				sock.receive(packet);
				
				InetAddress address = packet.getAddress();
	            int port = packet.getPort();
	            
	            String imgPath 
	              = new String(packet.getData(), 0, packet.getLength()).strip();
	            System.out.println("put slike: "+imgPath);
	            
	            //----- calculations ------
	            double[] imgArr = centeringAdapter.transform(imgPath);
	            centeringAdapter.print(imgArr);
				RealVector imgVec = MatrixUtils.createRealVector(imgArr);
				RealVector res = net.calculate(imgVec);
				//-------------------------
				
				String result = interpretateResult(res);
	            DatagramPacket outgoingPacket = new DatagramPacket(
	            		result.getBytes(), result.getBytes().length, address, port);
	            sock.send(outgoingPacket);
	            System.out.println("Odgovor poslan na adresu "+address+":"+port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String interpretateResult(RealVector res) {
		int index = 0;
		double max = res.getEntry(index);
		for(int i = 0, l = res.getDimension(); i < l; ++i) {
			if(res.getEntry(i) > max) {
				max = res.getEntry(i);
				index = i;
			}
		}
		return "Broj je "+index+" sa sigurnoscu "+max+".";
	}

	private static RealVector createLabel(int i) {
		double[] tmp = new double[10];
		tmp[i] = 1;
		return MatrixUtils.createRealVector(tmp);
	}
}
