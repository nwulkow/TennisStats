package tools;

import java.util.ArrayList;

public class ArrayTools {

	public static double[] addArray(double[] array1, double[] array2){
		
		if(array1.length != array2.length){
			return null;
		}
		double[] result = new double[array1.length];
		for(int i = 0; i < array1.length; i++){
			result[i] = array1[i] + array2[i];
		}
		return result;
	}
	
	public static double[] subtractArray(double[] array1, double[] array2){
		return ArrayTools.addArray(array1, ArrayTools.multiplyArrayScalar(array2, -1));
	}
	
	public static double norm1(double[] array){
		double sum = 0;
		for(double d : array){
			sum += Math.abs(d);
		}
		return sum;
	}
	
	public static double norm2(double[] array){
		double sum = 0;
		for(double d : array){
			sum += Math.pow(d,2);
		}
		sum = Math.pow(sum, 0.5);
		return sum;
	}
	
	public static double norm(double[] array, double p){
		double sum = 0;
		for(double d : array){
			sum += Math.pow(d,p);
		}
		sum = Math.pow(sum, 1/p);
		return sum;
	}
	
	public static double[] multiplyArrayScalar(double[] array, double value){
		double[] result = new double[array.length];
		for(int i = 0; i < array.length; i++){
			result[i] = array[i] * value;
		}
		return result;
	}

	public static double[] ArrayListToArray(ArrayList<Double> al) {
		double[] ar = new double[al.size()];
		for (int i = 0; i < al.size(); i++) {
			ar[i] = al.get(i);
		}
		return ar;
	}

	public static ArrayList<Double> ArrayToArrayList(double[] ar) {
		ArrayList<Double> al = new ArrayList<Double>();
		for (int i = 0; i < ar.length; i++) {
			al.add(ar[i]);
		}
		return al;
	}
	
	public static double[][] ArrayList2DimToArray(ArrayList<ArrayList<Double>> list){
		double[][] array = new double[list.size()][list.get(0).size()];
		for(int i = 0; i < list.size(); i++){
			for(int j = 0; j < list.get(0).size(); j++){
				array[i][j] = list.get(i).get(j);
			}
		}
		return array;
	}

	public static double[] getLineOfArrayList(ArrayList<double[]> al, int index) {
		double[] row = new double[al.size()];
		for (int i = 0; i < al.size(); i++) {
			row[i] = al.get(i)[index];
		}
		return row;
	}

	public static double[] sliceArrayIndices(double[] array, int start, int stop) {
		stop = Math.min(stop, array.length - 1);
		start = Math.max(start, 0);
		double[] sliced = new double[stop - start];
		for (int i = start; i < stop; i++) {
			sliced[i - start] = array[i];
		}
		return sliced;
	}

	public static double[] sliceArrayBounds(double[] array, double lower, double upper) {
		ArrayList<Double> sliced = new ArrayList<Double>();
		for (int i = 0; i < array.length; i++) {
			if (array[i] <= upper && array[i] >= lower) {
				sliced.add(array[i]);
			}
		}
		return ArrayListToArray(sliced);
	}

	public static double[] arrayWithConstant(int length, double value){
		double[] array = new double[length];
		for(int i = 0; i < length; i++){
			array[i] = value;
		}
		return array;
	}
	
	public static double[] arrayFrom1ToN(int length, int start){
		double[] array = new double[length];
		for(int i = 0; i < length; i++){
			array[i] = i + start;
		}
		return array;
	}
	
	public static double[] equidistantArray(double start, double end, int length){
		double[] array = new double[length];
		for(int i = 0; i < length; i++){
			array[i] = start + i*(end-start)/(length-1);
		}
		return array;
	}

	public static boolean compareDoubleArray(double[] array1, double[] array2){
		if(array1.length != array2.length){
			return false;
		}
		for(int i = 0; i < array1.length; i++){
			if(array1[i] != array2[i]){
				return false;
			}
		}
		return true;
	}
	
	public static double maxValueOfArray(double[] array){
		double max = array[0];
		for(double d : array){
			if(d > max){
				max = d;
			}
		}
		return max;
	}
	
	public static double minValueOfArray(double[] array){
		double min = array[0];
		for(double d : array){
			if(d < min){
				min = d;
			}
		}
		return min;
	}

	public static int entryOfMax(double[] array){
		int maxIndex = 0;
		double max = array[0];
		for(int i = 1; i < array.length; i++){
			if(array[i] > max){
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	public static int entryOfMin(double[] array){
		int minIndex = 0;
		double min = array[0];
		for(int i = 1; i < array.length; i++){
			if(array[i] < min){
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public static String[] arrayToStringarray(double[] array){
		String[] s = new String[array.length];
		for(int i = 0; i < array.length; i++){
			s[i] = array[i] + "";
		}
		return s;
	}
	
	public static ArrayList<String> arrayToStringArrayList(double[] array){
		ArrayList<String> s = new ArrayList<String>();
		for(int i = 0; i < array.length; i++){
			s.add(array[i] + "");
		}
		return s;
	}
	
	
}
