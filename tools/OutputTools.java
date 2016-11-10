package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.graph.Vertex;

import analysisFormats.ProbabilityTensor;
import counts.TennisAbstractPoint;

public class OutputTools {

	public static void writeMatrixToFile(double[][] M, String filepath) throws IOException {
	
		File f = new File("C:/Users/Niklas/TennisStatsData/" + filepath + ".csv");
		FileWriter fw = new FileWriter(f);
	
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M.length; j++) {
				fw.write(M[i][j] + ",");
			}
			fw.write("\n");
		}
		fw.close();
	}

	public static void printMatrix(double[][] M) {
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				System.out.print(M[i][j] + " , ");
			}
			System.out.print("\n");
		}
	}

	public static void printArray(double[] array){
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " , ");
		}
		System.out.print("\n");
	}

	public static void printArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " , ");
		}
		System.out.print("\n");
	}

	public static void printCSVFile(String filename) throws IOException {
		FileReader csvFileToRead = new FileReader(filename);
		BufferedReader br = new BufferedReader(csvFileToRead);
	
		ArrayList<TennisAbstractPoint> taps = new ArrayList<TennisAbstractPoint>();
		ArrayList<Integer> serverlist = new ArrayList<Integer>();
		ArrayList<Integer> winnerlist = new ArrayList<Integer>();
		String line = "";
	
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",");
			if (row.length >= 27) {
				System.out.println(row[27]);
			}
		}
	
		br.close();
	}

	public static void deleteFilesInFolder(String path){
		File folder = new File(path);
		for(File f : folder.listFiles()){
			if(f.isDirectory()){
				deleteFilesInFolder(f.getAbsolutePath());
			}
			f.delete();
		}
	}

	public static void writeGraphForGellyAPI(double[][] M, String filepath) throws IOException {
		File f_edges = new File("C:/Users/Niklas/TennisStatsData/" + filepath + "_edges" + ".csv");
		FileWriter fw_edges = new FileWriter(f_edges);
		File f_vertices = new File("C:/Users/Niklas/TennisStatsData/" + filepath + "_vertices" + ".csv");
		FileWriter fw_vertices = new FileWriter(f_vertices);
	
		for (int i = 0; i < M.length; i++) {
			fw_vertices.write(i + "," + (i*100) + "\n");
			for (int j = 0; j < M.length; j++) {
				if(M[i][j] != 0){
					fw_edges.write(i + "," + j + "," + Math.abs(M[i][j]));
					fw_edges.write("\n");
				}
			}
		}
		fw_edges.close();
		fw_vertices.close();
	}

	public static void writeGraphInGDF(double[][] M, List<Vertex<Long,Double>> vertices, String filepath) throws IOException {
		
		File f = new File("C:/Users/Niklas/TennisStatsData/" + filepath + ".gdf");
		FileWriter fw = new FileWriter(f);
		fw.write("nodedef>name VARCHAR,rank DOUBLE\n");
	
		for (int i = 0; i < M.length; i++) {
			fw.write("s" + i + "," + vertices.get(i).getValue() + "\n");
		}
		fw.write("edgedef>node1 VARCHAR,node2 VARCHAR,weight DOUBLE \n");
		for(int k = 0; k < M.length; k++){
			for (int j = 0; j < M[0].length; j++) {
				if(M[k][j] != 0){
					fw.write("s" + k + "," + "s" + j + "," + Math.abs(M[k][j]));
					fw.write("\n");
				}
			}
		}
		fw.close();
	}

	public static void writeGraphInGDF(double[][] M, String filepath) throws IOException {
		
		File f = new File("C:/Users/Niklas/TennisStatsData/" + filepath + ".gdf");
		FileWriter fw = new FileWriter(f);
		fw.write("nodedef>name VARCHAR,rank DOUBLE\n");
	
		for (int i = 0; i < M.length; i++) {
			fw.write("s" + i + "," + i + "\n");
		}
		fw.write("edgedef>node1 VARCHAR,node2 VARCHAR,weight DOUBLE \n");
		for(int k = 0; k < M.length; k++){
			for (int j = 0; j < M[0].length; j++) {
				if(M[k][j] != 0){
					fw.write("s" + k + "," + "s" + j + "," + Math.abs(M[k][j]));
					fw.write("\n");
				}
			}
		}
		fw.close();
	}

	public static void printTensor(ProbabilityTensor tensor){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				System.out.print(tensor.getTensor().getProbForShots()[0][i][j].getProbabilityForShot() + " , ");
			}
			System.out.println("");
		}
		System.out.println("");
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				for(int k = 0; k < 3; k++){
					System.out.print(tensor.getTensor().getProbForShots()[0][i][j].getOutcomeProbabilities()[k] + " , ");
				}
				for(int k = 0; k < 3; k++){
					System.out.print(tensor.getTensor().getProbForShots()[0][i][j].getOutcomeOfNextShot()[k] + " , ");
				}
				System.out.println("");
			}
		}
		System.out.println("---");
	}

}
