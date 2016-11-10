package analytics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math.stat.descriptive.rank.Min;

import Jama.Matrix;
import analysisFormats.MatchStatList;
import analysisFormats.MatchStatListCollection;
import analysisFormats.PlayerStatList;
import analysisFormats.PlayerStatListCollection;
import analysisFormats.ProbabilityForShot;
import analysisFormats.ProbabilityTensor;
import counts.Match;
import counts.Point;
import counts.Shot;
import dataload.LoadValues;
import graphics.LineChartDemo6;
import mathTools.Histogram;
import player.Player;
import tools.ArrayTools;
import tools.MatchTools;
import tools.OutputTools;
import tools.StatsTools;

public class PatternAnalysis {
	

	public static ProbabilityTensor[] shotDirectionMatrix(Match m) throws IOException {
		ProbabilityTensor[] tensors = new ProbabilityTensor[2];
		for(int i = 0; i < m.getPlayers().length; i++){
			tensors[i] = PlayerStats.createTensorForOnePlayerForOneMatch(m.getPlayers()[i].getName(), m, false, false);
		}
		return tensors;
	}

	
	public static double[] patternWinFrequency(Match m, ArrayList<Shot> pattern){
		
		boolean[] pattern_bool = {false, false};
		boolean[] pattern_won_bool = {false, false};
		double[] pattern_counter = {0d,0};
		double[] pattern_won_counter = {0d,0};
		for(Point p : m.getPoints()){
			pattern_bool[0] = false;
			pattern_bool[1] = false;
			pattern_won_bool[0] = false;
			pattern_won_bool[1] = false;
			for (int i = 2; i < p.getShots().size(); i++) {
				boolean shotsequencebool = MatchTools.compareShotSequence(pattern, p.getShots(), i);
				if (shotsequencebool) {
					int playerindex = MatchTools.getPlayerIndex(p.getShots().get(i).getPlayer(), m);
					pattern_bool[playerindex] = true;
					if (p.getWinner().equals(m.getPlayers()[playerindex])) {
						pattern_won_bool[playerindex] = true;
					}
				}
			}
			// Wenn das Pattern von Spieler 1 ausgefuehrt wurde, counter erhoehen und bei Punktgewinn den won_counter erhoehen
			if(pattern_bool[0]){
				pattern_counter[0]++;
				if(pattern_won_bool[0]){
					pattern_won_counter[0]++;
				}
			}
			// Wenn das Pattern von Spieler 2 ausgefuehrt wurde, counter erhoehen und bei Punktgewinn den won_counter erhoehen
			if(pattern_bool[1]){
				pattern_counter[1]++;
				if(pattern_won_bool[1]){
					pattern_won_counter[1]++;
				}
			}
		}
		double[] result = { pattern_won_counter[0] / pattern_counter[0], pattern_won_counter[1] / pattern_counter[1] };
		return result;
	}
	
	public static double[] patternFrequency(Match m, ArrayList<Shot> pattern){
		
		boolean[] pattern_bool = {false, false};
		double[] pattern_counter = {0d,0};
		for(Point p : m.getPoints()){
			pattern_bool[0] = false;
			pattern_bool[1] = false;
			for (int i = pattern.size()-1; i < p.getShots().size(); i++) {
				boolean shotsequencebool = MatchTools.compareShotSequence(pattern, p.getShots(), i);
				if (shotsequencebool) {
					int playerindex = MatchTools.getPlayerIndex(p.getShots().get(i).getPlayer(), m);
					pattern_bool[playerindex] = true;
				}
			}
			// Wenn das Pattern von Spieler 1 ausgefuehrt wurde, counter erhoehen und bei Punktgewinn den won_counter erhoehen
			if(pattern_bool[0]){
				pattern_counter[0]++;
			}
			// Wenn das Pattern von Spieler 2 ausgefuehrt wurde, counter erhoehen und bei Punktgewinn den won_counter erhoehen
			if(pattern_bool[1]){
				pattern_counter[1]++;
			}
		}
		double[] result = { pattern_counter[0] / m.getPoints().size(), pattern_counter[1] / m.getPoints().size() };
		return result;
	}
	
	
	// Berechnet fuer alle Matches die (nach Tensor) W'keiten beider Spieler, einen Punkt zu gewinnen sowie haelt den
	// tatsaechlichen Anteil an gewonnenen Punkten fest und berechnet dann den Korrelationskoeffizienten beider Werte
	public static PlayerStatListCollection tensorWinAndSTD() throws IOException{
		
		MatchStatListCollection mslC = new MatchStatListCollection();
		PlayerStatListCollection pslC = new PlayerStatListCollection();
		ArrayList<Match> matches = LoadValues.loadAllMatches();
		for (Match m : matches){
			Player p1 = m.getPlayers()[0];
			Player p2 = m.getPlayers()[1];
			p1.loadTensor(false);
			p2.loadTensor(false);
			
			double p1TensorSTD = p1.getTensor().standardDeviationOfDirection();
			double p2TensorSTD = p2.getTensor().standardDeviationOfDirection();
			double p1ServeWinProbability = WinProbabilityByTensor.computePointWinProbabilitiesByTensor(p1, p2, 0, false);
			double p2ServeWinProbability = WinProbabilityByTensor.computePointWinProbabilitiesByTensor(p2, p1, 0, false);
			double p1WinProbability = p1ServeWinProbability;//(p1ServeWinProbability + (1-p2ServeWinProbability)) / 2;
			double p2WinProbability = p2ServeWinProbability;//(p2ServeWinProbability + (1-p1ServeWinProbability)) / 2;
			double[] pointWinFrequency = StandardStats.totalServeWinPercentage(m);//StandardStats.pointsWonByPlayer(m);
			double p1WinFrequency = pointWinFrequency[0];
			double p2WinFrequency = pointWinFrequency[1];
			double[] serveWinProbability = {p1ServeWinProbability, p2ServeWinProbability};
			double[] tensorSTD = {p1TensorSTD, p2TensorSTD};
			
			MatchStatList msl = new MatchStatList(m, serveWinProbability , "Serve win math. probability");
			msl.getStats().add(pointWinFrequency);
			msl.getStatNames().add("Serve point win frequency");
			msl.getStats().add(tensorSTD);
			msl.getStatNames().add("Tensor STD");
			mslC.add(msl);
			
			PlayerStatList psl1 = new PlayerStatList("",p1ServeWinProbability,"Tensor STD",1);
			psl1.getStats().add(p1TensorSTD);
			psl1.getStats().add(p1WinFrequency);
			PlayerStatList psl2 = new PlayerStatList("",p2ServeWinProbability,"Tensor STD",1);
			psl2.getStats().add(p2TensorSTD);
			psl2.getStats().add(p2WinFrequency);
			
			pslC.add(psl1);
			pslC.add(psl2);
		}
	
		return pslC;
	}
	
	
	public static double[] regression(PlayerStatListCollection pslC){
		int noRows = Math.min(pslC.size(), 100);
		double[][] X = new double[noRows][pslC.get(0).getStats().size()-1];
		double[] Y = new double[noRows];
		Random r = new Random();
		for(int i = 0; i < noRows; i++){
			Y[i] = pslC.get(i).getStats().get(pslC.get(i).getStats().size()-1);
			for(int j = 0; j < pslC.get(i).getStats().size()-1; j++){
				X[i][j] = pslC.get(i).getStats().get(j);
			}
		}
		//pslC.printCollection();
		MultipleLinearRegression mlr = new MultipleLinearRegression(X, Y);
		
		// Testing data
		double[][] Ytesting = new double[1][pslC.size()-noRows];
		double[][] Xtesting = new double[pslC.size()-noRows][pslC.get(0).getStats().size()-1];
		for(int i = 0; i < pslC.size()-noRows; i++){
			Ytesting[0][i] = pslC.get(i).getStats().get(pslC.get(i).getStats().size()-1);
			for(int j = 0; j < pslC.get(i).getStats().size()-1; j++){
				Xtesting[i][j] = pslC.get(i).getStats().get(j);
			}
		}
		
		OutputTools.printArray(mlr.beta.transpose().getArray()[0]);
		Matrix prediction = new Matrix(Xtesting).times(mlr.beta);
		Matrix residual = prediction.minus(new Matrix(Ytesting).transpose());
		System.out.println(ArrayTools.norm1(residual.getArray()[0])/residual.getArray()[0].length);
		return mlr.beta.transpose().getArray()[0];
		
	}

}
