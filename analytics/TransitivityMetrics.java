package analytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.flink.graph.Vertex;

import dataload.LoadAllTAMatches;
import tools.MathTools;
import tools.OutputTools;
import tools.StatsTools;

public class TransitivityMetrics {

	// TransitivitaetsMetrik. Bisher nur vereinfacht mit Pfaden von ausschließlich der Laenge 3
	public static double transitivityMetric(double[][] M){
		if(M.length != M[0].length){
			System.out.println("Matrix not square");
			return 0;
		}
		double sum = 0;
		double valid_counter = 0; // Zaehlt, wie oft tatsaechlich Verbindindungen i->j, j->k und i->k vorliegen
		int n = M.length;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(j != i){
					for(int k = 0; k < n; k++){
						if(k !=i && k != j){
							if( (M[i][j] != 0 || M[j][i] != 0) && (M[j][k] != 0 || M[k][j] != 0) && (M[i][k] != 0 || M[k][i] != 0)){
								valid_counter++;
								sum += M[i][j]*M[j][k]*M[i][k];
							}
						}
					}
				}
			}
		}
		if(valid_counter == 0){
			return 0;
		}
		return sum/valid_counter;
	}
	
	
	public static double transitivityMetricFull(double[][] M){
		if(M.length != M[0].length){
			System.out.println("Matrix not square");
			return 0;
		}
		double sum = 0;
		double valid_counter = 0; // Zaehlt, wie oft tatsaechlich Verbindindungen i->j, j->k und i->k vorliegen
		int n = M.length;
		for(int pathlength = 3; pathlength <= 3 ; pathlength++){
			double counter = 0;
			while(counter < MathTools.factorialOverFactorial(n, n-pathlength)){
				ArrayList<Integer> availableEntries = new ArrayList<Integer>();
				for(int h = 0; h < n; h++){
					availableEntries.add(h);
				}
				ArrayList<Integer> entries = new ArrayList<Integer>();
				double[] indices = new double[pathlength];
				double tempCounter = counter;
				double product = MathTools.factorialOverFactorial(n, n-pathlength);
				for(int i = 0; i < pathlength; i++){
					product = product / availableEntries.size();
					indices[i] = (int)(tempCounter / product);
					//System.out.println("product = " + product + " , tempCounter = " + tempCounter + " , indices[i] = " + indices[i]);
					//System.out.println("avai: " + availableEntries);
					entries.add(availableEntries.get((int)indices[i]));
					availableEntries.remove((int)indices[i]);
					
					tempCounter = tempCounter - indices[i]*product;
				}			
				
				counter++;
				//System.out.println(entries);
				
				double pathproduct = 1;
				boolean valid = false;
				for(int f = 0; f < entries.size() - 1; f++){
					if(M[entries.get(f)][entries.get(f+1)] != 0 || M[entries.get(f+1)][entries.get(f)] != 0){ // Pruefen, ob das Match ueerhaupt stattfand
						valid = true;
					}
					pathproduct *= M[entries.get(f)][entries.get(f+1)];
				}
				pathproduct *= M[entries.get(entries.size()-1)][entries.get(0)];
				pathproduct = pathproduct / entries.size(); // Jeder Pfad kommt in der entries-Liste so oft vor, wie er lang ist (naemlich 1->2->3, 2->3->1 und 3->1->2)
				//pathproduct = Math.pow(pathproduct, 1/(double)entries.size());
				//System.out.println(pathproduct);
				sum += pathproduct;
				if(valid){
					valid_counter++;
				}	
			}	
		}
		
		if(valid_counter == 0){
			return 0;
		}
		return sum/valid_counter;
	}
	
	public static void multiplyTest() throws IOException{
		int N = 5;
		int[] a = {1,2,3,4,5}; // This is the input
				int[] products_below = new int[N];
				int p=1;
				for(int i=0;i<N;++i) {
				  products_below[i]=p;
				  p*=a[i];
				}

				int[] products_above = new int[N];
				p=1;
				for(int i=N-1;i>=0;--i) {
				  products_above[i]=p;
				  p*=a[i];
				}
				OutputTools.printArray(products_above);
				OutputTools.printArray(products_below);
				int[] products = new int[N]; // This is the result
				for(int i=0;i<N;++i) {
				  products[i]=products_below[i]*products_above[i];
				  System.out.print(products[i] + " , ");
				}
	}
	
	
	
	// Fuehrt pageRank Algorithmus auf M aus und gibt die Standardabweichung des resultierenden PageRanks-Vektors aus.
	// Je hoeher dieser ist, desto groeßer die Transitivitaet
	public static double pageRankSTD(double[][] M) throws Exception{
		//double[][] resultRatioMatrix = LoadAllTAMatches.printWinnersMatrix("C:/Users/Niklas/TennisStatsData/SackmannRawData/", false);
		//System.out.println(TransitivityMetrics.transitivityMetric(resultRatioMatrix));
		OutputTools.writeGraphForGellyAPI(M,"resultRatioMatrix", false);
		String edgesInputPath = "C:/Users/Niklas/TennisStatsData/resultRatioMatrix_Edges.csv";
		String vertexInputPath = "C:/Users/Niklas/TennisStatsData/resultRatioMatrix_vertices.csv";
		boolean fileOutput = false;
		GellyAPI gellyAPI = new GellyAPI(edgesInputPath, vertexInputPath);
		List<Vertex<Long,Double>> collectedVertices = gellyAPI.pageRank(edgesInputPath, vertexInputPath, fileOutput);
		double[] ranks = new double[(int) collectedVertices.size()];
		for(int i = 0; i < collectedVertices.size(); i++){
			ranks[i] = collectedVertices.get(i).getValue();
		}
		ranks = StatsTools.absToRel(ranks);
		double std = StatsTools.standarddeviation(ranks);
		//System.out.println("STD = " + std);
		return std;
	}
	
	
}
