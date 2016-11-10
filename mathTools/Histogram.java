package mathTools;

import java.io.IOException;

import graphics.PlotTools;
import tools.ArrayTools;
import tools.StatsTools;
import tools.Tools;

public class Histogram {

	double[] bins, frequencies;
	
	public Histogram(){
		this.bins = new double[0];
		this.frequencies = new double[0];
	}
	
	public Histogram(double[] data, int noBins){
		this(data, ArrayTools.equidistantArray(ArrayTools.minValueOfArray(data), ArrayTools.maxValueOfArray(data), noBins));
		
	}
	
	public Histogram(double[] data, double[] bins){
		int noBins = bins.length;
		this.bins = bins;
		this.frequencies = new double[noBins];
		for(double d : data){
			for(int i = 0; i < this.bins.length-1; i++){
				if(d >= this.bins[i] && d < this.bins[i+1]){
					this.frequencies[i] += 1;
				}
			}
		}	
	}
	
	public void makeDataStochastic(){
		double sum = StatsTools.sumOfArray(this.frequencies);
		for(int i = 0; i < this.frequencies.length; i++){
			this.frequencies[i] = this.frequencies[i] / sum;
		}
	}
	
	public void plot(){
		PlotTools.plot(this.bins, this.frequencies);
	}

	public double[] getBins() {
		return bins;
	}

	public void setBins(double[] bins) {
		this.bins = bins;
	}

	public double[] getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(double[] frequencies) {
		this.frequencies = frequencies;
	}
	
}
