package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import counts.Shot;

public class QuickSort {

	public static void sortiere(double x[], double[] y, int index, ArrayList<ArrayList<Shot>> patterns) {
		qSort(x, y, index, 0, x.length - 1, patterns);
	}

	public static void sortiere(double[] x) {
		ArrayList<ArrayList<Shot>> patterns = new ArrayList<ArrayList<Shot>>(x.length);
		for (int i = 0; i < x.length; i++) {
			patterns.add(new ArrayList<Shot>());
		}
		double[] y = new double[x.length];
		qSort(x, y, 0, 0, x.length - 1, patterns);
	}

	public static void sortiere(ArrayList<Double> x, ArrayList<ArrayList<Shot>> patterns) {
		double[] x_array = ArrayTools.ArrayListToArray(x);
		sortiere(x_array);
	}

	public static void qSort(double x[], double[] y, int index, int links, int rechts,
			ArrayList<ArrayList<Shot>> patterns) {
		if (links < rechts) {
			int i = partition(x, y, index, links, rechts, patterns);
			qSort(x, y, index, links, i - 1, patterns);
			qSort(x, y, index, i + 1, rechts, patterns);
		}
	}

	public static int partition(double x[], double[] y, int index, int links, int rechts,
			ArrayList<ArrayList<Shot>> patterns) {
		int i, j;
		double helpx, helpy, pivot = 0;
		if (index == 0) {
			pivot = x[rechts];
		} else if (index == 1) {
			pivot = y[rechts];
		}

		i = links;
		j = rechts - 1;
		while (i <= j) {
			boolean bigger = false;
			if (index == 0) {
				if (x[i] > pivot) {
					bigger = true;
				}
			}
			if (index == 1) {
				if (y[i] > pivot) {
					bigger = true;
				}
			}
			if (bigger) {
				// tausche x[i] und x[j]
				helpx = x[i];
				x[i] = x[j];
				x[j] = helpx;
				helpy = y[i];
				y[i] = y[j];
				y[j] = helpy;
				ArrayList<Shot> templist = patterns.get(i);
				patterns.set(i, patterns.get(j));
				patterns.set(j, templist);
				j--;
			} else
				i++;
		}
		// tausche x[i] und x[rechts]
		helpx = x[i];
		x[i] = x[rechts];
		x[rechts] = helpx;
		ArrayList<Shot> templist = patterns.get(i);
		patterns.set(i, patterns.get(rechts));
		patterns.set(rechts, templist);
		helpy = y[i];
		y[i] = y[rechts];
		y[rechts] = helpy;

		return i;
	}
	// -------------
	

	public static void sortiere_strings(double x[], double[] y, int index, ArrayList<String> names) {
		qSort_strings(x, y, index, 0, x.length - 1, names);
	}

	public static void sortiere_strings(double x[], int index, ArrayList<String> names) {
		double[] y = new double[x.length];
		qSort_strings(x, y, index, 0, x.length - 1, names);
	}

	public static void sortiere_strings(ArrayList<Double> x, ArrayList<String> names) {
		double[] x_array = ArrayTools.ArrayListToArray(x);
		sortiere(x_array);
	}

	public static void qSort_strings(double x[], double[] y, int index, int links, int rechts,
			ArrayList<String> names) {
		if (links < rechts) {
			int i = partition_strings(x, y, index, links, rechts, names);
			qSort_strings(x, y, index, links, i - 1, names);
			qSort_strings(x, y, index, i + 1, rechts, names);
		}
	}

	public static int partition_strings(double x[], double[] y, int index, int links, int rechts,
			ArrayList<String> names) {
		int i, j;
		double helpx, helpy, pivot = 0;
		if (index == 0) {
			pivot = x[rechts];
		} else if (index == 1) {
			pivot = y[rechts];
		}

		i = links;
		j = rechts - 1;
		while (i <= j) {
			boolean bigger = false;
			if (index == 0) {
				if (x[i] > pivot) {
					bigger = true;
				}
			}
			if (index == 1) {
				if (y[i] > pivot) {
					bigger = true;
				}
			}
			if (bigger) {
				// tausche x[i] und x[j]
				helpx = x[i];
				x[i] = x[j];
				x[j] = helpx;
				helpy = y[i];
				y[i] = y[j];
				y[j] = helpy;
				String tempstring = names.get(i);
				names.set(i, names.get(j));
				names.set(j, tempstring);
				j--;
			} else
				i++;
		}
		// tausche x[i] und x[rechts]
		helpx = x[i];
		x[i] = x[rechts];
		x[rechts] = helpx;
		String templist = names.get(i);
		names.set(i, names.get(rechts));
		names.set(rechts, templist);
		helpy = y[i];
		y[i] = y[rechts];
		y[rechts] = helpy;

		return i;
	}

	public static void main(String[] args) {
		double[] liste = { 0, 9, 4, 6, 2 };
		ArrayList<String> names = new ArrayList<String>(Arrays.asList("0", "neun", "vier", "sechs", "zwei"));
		sortiere_strings(liste, 0, names);
		for (int i = 0; i < liste.length; i++)
			System.out.println(liste[i] + " " + names.get(i));
	}
}