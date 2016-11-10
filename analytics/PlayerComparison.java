package analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;
import counts.Match;
import counts.MatchInfo;
import dataload.LoadMatchFromTennisAbstract;
import graphics.PlotTools;
import objects.Shottypes;
import tools.ArrayTools;
import tools.MatrixTools;
import tools.OutputTools;
import tools.StatsTools;
import tools.Tools;

public class PlayerComparison {

	
	public static double[][] createResultMatrix(boolean fileOutput) throws IOException{
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData");
		ArrayList<String> playernames = new ArrayList<String>();
		
		for (File file : folder.listFiles()) {

			String[] names = file.getName().replace(".csv", "").split("-");
			String player1name = names[0];
			String player2name = names[1];
			if (!playernames.contains(player1name) && !Shottypes.playersWhereErrorsHappen.contains(player1name))
				playernames.add(player1name);
			if (!playernames.contains(player2name) && !Shottypes.playersWhereErrorsHappen.contains(player2name))
				playernames.add(player2name);
		}
		
		ArrayList<String> shorterPlayernames = Tools.randomEntries(playernames, 20);
		return createResultMatrix(playernames, fileOutput);
	}
	
	public static double[][] createResultMatrix(ArrayList<String> playernames, boolean fileOutput) throws IOException {
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData");

		int n = playernames.size();
		double[][] M = new double[n][n];

		for (File file : folder.listFiles()) {
			String filepath = file.toString();
			String[] names = file.getName().replace(".csv", "").split("-");
			String player1name = names[0];
			String player2name = names[1];
			if(playernames.contains(player1name) && playernames.contains(player2name)){
				MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
				Match m = new Match(im);

				if (m.getWinner().equals(m.getPlayers()[0])) {
					M[playernames.indexOf(player1name)][playernames.indexOf(player2name)]++;
				}
				
				if (m.getWinner().equals(m.getPlayers()[1])) {
					M[playernames.indexOf(player2name)][playernames.indexOf(player1name)]++;
				}
			}
		}
		double[][] resultRatioMatrix = new double[M.length][M[0].length];
		for(int i = 0; i < M.length; i++){
			for(int j = i; j < M[0].length; j++){
				if(M[i][j] + M[j][i] == 0){
					resultRatioMatrix[i][j] = 0;
					resultRatioMatrix[j][i] = 0;
				}
				else{
					resultRatioMatrix[i][j] = M[i][j] / (M[j][i] + M[i][j]);
					resultRatioMatrix[j][i] = M[j][i] / (M[j][i] + M[i][j]);
				}
			}
		}
		if(fileOutput){
			OutputTools.writeMatrixToFile(resultRatioMatrix, "Matrix");
		}
		return resultRatioMatrix;
	}
	
	
	public static double[] rankPlayersByMatrixMultiplication(ArrayList<String> playernames, double power) throws IOException{
		double[][] M = createResultMatrix(playernames, false);
		return rankPlayersByMatrixMultiplication(M, power);
	}
	
	
	// Input: resultRatioMatrix M, Zeilennorm von M^2 ist Output
	public static double[] rankPlayersByMatrixMultiplication(double[][] M, double power) throws IOException{
		int n = M.length;
		double[][] hasMatchBeenPlayedMatrix = new double[n][n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(M[i][j] != 0 || M[j][i] != 0){
					hasMatchBeenPlayedMatrix[i][j] = 1;
					hasMatchBeenPlayedMatrix[j][i] = 1;
				}
			}
		}
		Matrix mat = new Matrix(M);
		Matrix mat_2 = mat;//  mat.times(mat); // M^2
		//int power = 3; // Potenz der Matrix, Sehr wichtig!
		for(int i = 1; i <= power-1; i++){
			mat_2 = mat_2.times(mat);
		}
		//Matrix mat_3 = mat_2.minus(mat); // M^2-M
		double[] oneVector = ArrayTools.arrayWithConstant(M[0].length, 1);
		double[][] oneVectorAsMatrix = {oneVector};
		Matrix sums = mat_2.times(new Matrix(oneVectorAsMatrix).transpose());
		Matrix numberOfOpponents = new Matrix(hasMatchBeenPlayedMatrix).times(new Matrix(oneVectorAsMatrix).transpose());
		//Tools.printMatrix(hasMatchBeenPlayedMatrix);
		for(int i = 0; i < n; i++){
			sums.set(i, 0, sums.get(i,0) / numberOfOpponents.get(i, 0));
		}
		//Tools.printMatrix(M);
		//Tools.printArray(sums.transpose().getArray()[0]);
		return sums.transpose().getArray()[0];
	}
	
	public static void testConvergenceOfPlayerRanking() throws IOException{
		ArrayList<String> playernames = new ArrayList<String>(Arrays.asList("Rafael_Nadal","Roger_Federer","Novak_Djokovic", "Andy_Murray", "Fabio_Fognini", "Ernests_Gulbis", "Gael_Monfils"));
		double[][] M = PlayerComparison.createResultMatrix(playernames, false);
		double power = 3;
		double[] ultimateValues = PlayerComparison.rankPlayersByMatrixMultiplication(M, power);
		Matrix M_Matrix = new Matrix(M);
		double[] ultimateRanks = StatsTools.ranksOfArrayEntries(ultimateValues);
		OutputTools.printArray(ultimateRanks);
		System.out.println("-------");
		
		double[] averageRanks = new double[playernames.size()];
		double numIter = 100;
		double[] ratios = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
		double[] norms = new double[ratios.length];
		for(int j = 0; j < ratios.length; j++){
			for(int i = 0; i < numIter; i++){
				Matrix oneZeroMatrix = MatrixTools.oneZeroMatrixSymmetric(M.length, M[0].length, ratios[j]);
				Matrix M_temp = M_Matrix.arrayTimes(oneZeroMatrix);
				double[] currentValues = PlayerComparison.rankPlayersByMatrixMultiplication(M_temp.getArray(), power);
				double[] currentRanks = StatsTools.ranksOfArrayEntries(currentValues);
				//Tools.printArray(currentRanks);
				averageRanks = ArrayTools.addArray(averageRanks, currentRanks);
			}
			//System.out.println("-------");
			averageRanks = ArrayTools.multiplyArrayScalar(averageRanks, 1/numIter);
			OutputTools.printArray(StatsTools.ranksOfArrayEntriesBackwards(averageRanks));
			double[] residual = ArrayTools.subtractArray(ultimateRanks, averageRanks);
			norms[j] = ArrayTools.norm2(residual) / residual.length;
		}
		System.out.println("-------");
		OutputTools.printArray(norms);
		PlotTools.plot(ratios, norms);
	}
	
	public static void bundesligaTable() throws IOException{
		
		ArrayList<String> teams = new ArrayList<String>();
		double[][] M = new double[18][18];
		File file = new File("C:/Users/Niklas/TennisStatsData/BL1516Hinrunde.txt");
		FileReader csvFileToRead = new FileReader(file);
		BufferedReader br = new BufferedReader(csvFileToRead);
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] row = line.split("-");
			String[] goals1 = row[0].split("  ");
			String goal1 = goals1[goals1.length-1].replaceAll(" ", "");
			
			String team1 = goals1[0];
			String[] goals2 = row[1].split("  ");
			String goal2 = goals2[0];
			//String[] goal2_mod = goal2.split(" ");
			//goal2 = goal2_mod[0];
			String team2 = goals2[1];
			System.out.println(team1 + " " + goal1 + " - " + goal2 + "  " + team2);
			if(!teams.contains(team1)){
				teams.add(team1);
			}
			if(!teams.contains(team2)){
				teams.add(team2);
			}
			int indexHomeTeam = teams.indexOf(team1);
			int indexAwayTeam = teams.indexOf(team2);
			if(Integer.parseInt(goal1) > Integer.parseInt(goal2)){
				M[indexHomeTeam][indexAwayTeam] = 1;
			}
			if(Integer.parseInt(goal2) > Integer.parseInt(goal1)){
				M[indexAwayTeam][indexHomeTeam] = 1;
			}
			if(Integer.parseInt(goal1) == Integer.parseInt(goal2)){
				M[indexHomeTeam][indexAwayTeam] = 0.5;
				M[indexAwayTeam][indexHomeTeam] = 0.5;
			}
		}
		//System.out.println(teams);
		//Tools.printMatrix(M);
		double power = 1;
		double[] ultimateValues = PlayerComparison.rankPlayersByMatrixMultiplication(M, power);
		double[] ultimateRanks = StatsTools.ranksOfArrayEntries(ultimateValues);
		OutputTools.printArray(ultimateRanks);
		Matrix M_Matrix = new Matrix(M);
		double[] averageRanks = new double[M.length];
		double numIter = 100;
		for(int i = 0; i < numIter; i++){
			Matrix oneZeroMatrix = MatrixTools.oneZeroMatrixSymmetric(M.length, M[0].length, 0.5);
			Matrix M_temp = M_Matrix.arrayTimes(oneZeroMatrix);
			double[] currentValues = PlayerComparison.rankPlayersByMatrixMultiplication(M_temp.getArray(), power);
			double[] currentRanks = StatsTools.ranksOfArrayEntries(currentValues);
			//Tools.printArray(currentRanks);
			averageRanks = ArrayTools.addArray(averageRanks, currentRanks);
		}
		averageRanks = ArrayTools.multiplyArrayScalar(averageRanks, 1/numIter);
		//Tools.printArray(StatsTools.ranksOfArrayEntriesBackwards(averageRanks));
		OutputTools.printArray(averageRanks);
		double[] residual = ArrayTools.subtractArray(ultimateRanks, averageRanks);
		System.out.println(StatsTools.ranksDeviationsMeasure(ultimateRanks, averageRanks));
		System.out.println(StatsTools.correlationcoefficient(ultimateRanks, averageRanks));
	}
	
	
	
}
