package tools;

import java.util.Arrays;
import java.util.List;

public class StatsTools {

	public static double sumOfArray(double[] array) {
		double sum = 0;
		for (double d : array) {
			sum += d;
		}
		return sum;
	}
	public static double sumOfArrayBoundaries(double[] array, int start, int end) {
		double sum = 0;
		for (int i = Math.max(0,start); i < Math.min(end,array.length); i++) {
			sum += array[i];
		}
		return sum;
	}

	public static double mean(double[] array) {

		double sum = sumOfArray(array);
		return (sum / array.length);
	}

	public static double variance(double[] array, double mean) {

		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += Math.pow(array[i] - mean, 2);
		}
		return (sum / array.length);
	}
	
	public static double variance(double[] array){
		double mean = mean(array);
		return variance(array, mean);
	}

	public static double standarddeviation(double[] array, double mean) {
		return Math.sqrt(variance(array,mean));
	}
	
	public static double standarddeviation(double[] array){
		return Math.sqrt(variance(array));
	}

	public static double correlationcoefficient(double[] array1, double[] array2) {

		double mean1 = mean(array1);
		double mean2 = mean(array2);
		double sum = 0;

		for (int i = 0; i < array1.length; i++) {
			sum += (array1[i] - mean1) * (array2[i] - mean2);
		}
		sum = sum / (standarddeviation(array1,mean1) * standarddeviation(array2,mean2) * array1.length);

		return sum;
	}

	
	// Korrelationskoeffizient, wobei nur Werte von array1, die mindestens den Durchschnitt von array1 sind, herangezogen werden
	public static double correlationOneSided(double[] array1, double[] array2) {

		double mean1 = mean(array1);
		double mean2 = mean(array2);
		double sum = 0;
		double n = 0;
		double std1 = 0;
		double std2 = 0;
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] > mean1) {
				sum += (array1[i] - mean1) * (array2[i] - mean2);
				n++;
				std1 += Math.pow(array1[i] - mean1, 2);
				std2 += Math.pow(array2[i] - mean2, 2);
			}
		}
		std1 = Math.sqrt(std1);
		std2 = Math.sqrt(std2);

		sum = sum / (std1 * std2);

		return sum;

	}

	// Werte im Array geteilt durch Summe aller Werte des Arrays
	public static double[] absToRel(double[] array) {
		double sum = sumOfArray(array);
		double[] relativeValues = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			relativeValues[i] = array[i] / sum;
		}
		return relativeValues;
	}
	
	// Summierte Werte im Array
	public static double[] cummulatedArrayValues(double[] array){
		double[] cummulated = new double[array.length];
		double sum = 0;
		for(int i = 0; i < array.length; i++){
			sum += array[i];
			cummulated[i] = sum;
		}
		return cummulated;
	}
	
	
	// Gibt an, der wievielt-groesste Eintrag jeder Eintrag in einem Array ist. (5,8,4)->(2,1,3)
	public static Integer[] ranksOfArrayEntries(Double[] array){
		List <Double> list = Arrays.asList(array);
		Integer[] outArr  = list.stream()
		        .sorted()
		        .map(e -> list.indexOf(e))
		        .toArray(Integer[]::new);
		return outArr;
	}
	
	public static double[] ranksOfArrayEntries(double[] array){
		double[] ranks = new double[array.length];
		for(int i = 0; i < array.length; i++){
			int rank = 0;
			double x = array[i];
			for(int j = 0; j < array.length; j++){
				double y = array[j];
				if(i != j && y >= x){
					rank++;
				}
			}
			ranks[i] = rank;
		}
		return ranks;
	}
	
	public static double[] ranksOfArrayEntriesBackwards(double[] array){
		double[] ranks = new double[array.length];
		for(int i = 0; i < array.length; i++){
			int rank = 0;
			double x = array[i];
			for(int j = 0; j < array.length; j++){
				double y = array[j];
				if(i != j && y <= x){
					rank++;
				}
			}
			ranks[i] = rank;
		}
		return ranks;
	}

	
	public static double ranksDeviationsMeasure(double[] ranks1, double[] ranks2){
		double sum = 0;
		for(int i = 0; i < ranks1.length; i++){
			sum += Math.abs(ranks1[i] - ranks2[i]) * (ranks1.length - ranks1[i]+1);
		}
		return sum;
	}
	
	

}
