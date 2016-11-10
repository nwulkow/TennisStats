package analytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import analysisFormats.ScoreValueList;
import counts.Match;
import counts.Point;
import counts.Score;
import counts.ScoreSituation;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import player.Player;
import tools.ArrayTools;
import tools.MatchTools;

public class ScoreStats {

	
	public static ScoreValueList pointWinMatchWinPercentages(ArrayList<String> playernames) throws IOException{
		
		ArrayList<ScoreSituation> situations = new ArrayList<ScoreSituation>();
		ArrayList<Double> situationCounter = new ArrayList<Double>();
		ArrayList<Double> situationWinCounter_server = new ArrayList<Double>(); // Zaehlt, wie oft der Aufschlaeger eine Situation gewonnen hat
		ArrayList<Double> situationMatchWinCounter_server = new ArrayList<Double>(); // Zaehlt, wie oft der Aufschlaeger dann auch das Match gewonnen hat
		ArrayList<Double> situationWinCounter_returner = new ArrayList<Double>();
		ArrayList<Double> situationMatchWinCounter_returner = new ArrayList<Double>();
		Player samplePlayer1 = new Player();
		Player samplePlayer2 = new Player();
		ArrayList<Match> matches = LoadValues.loadMatchesForPlayerlist(playernames);
		for(Match m : matches){
			if(m.getSetstowin() == 2){
			for(Point p : m.getPoints()){
				if(p.getScore().getP1_sets() == 0 && p.getScore().getP2_sets() == 0){
				Score score = p.getScore();
				// Serverindex bestimmen
				int serverIndex = 0;
				if(p.getServer().equals(m.getPlayers()[1])){
					serverIndex = 1;
				}
				// -----
				ScoreSituation sit = new ScoreSituation(score, serverIndex);
				int index = indexOfScoreSituationInList(sit, situations);
				if(index == -1){ // Wenn ScoreSituation noch nicht in der Liste ist
					situations.add(sit);
					situationCounter.add(0d);
					situationWinCounter_server.add(0d);
					situationWinCounter_returner.add(0d);
					situationMatchWinCounter_server.add(0d);
					situationMatchWinCounter_returner.add(0d);
					index = situations.size()-1;
				}
				if(p.getWinner().getName().equals(p.getServer().getName())){
					situationWinCounter_server.set(index, situationWinCounter_server.get(index)+1);
					if(m.getWinner().getName().equals(p.getServer().getName())){
						situationMatchWinCounter_server.set(index, situationMatchWinCounter_server.get(index)+1);
					}
				}
				else/* if(p.getWinner().getName().equals(p.getReturner().getName()))*/{
					situationWinCounter_returner.set(index, situationWinCounter_returner.get(index)+1);
					if(m.getWinner().getName().equals(p.getServer().getName())){
						situationMatchWinCounter_returner.set(index, situationMatchWinCounter_returner.get(index)+1);
					}
				}
			}}
			}
		}	
		double[] situationWinRatio_server = new double[situations.size()];
		double[] situationWinRatio_returner = new double[situations.size()];
		double[] situationWinDifference = new double[situations.size()];
		// Fuer jede ScoreSituation erfassen, wie oft der Sieger des Punktes relativ das Match gewonnen hat
		for(int j = 0; j < situations.size(); j++){
			situationWinRatio_server[j] = situationMatchWinCounter_server.get(j) / situationWinCounter_server.get(j);
			situationWinRatio_returner[j] = situationMatchWinCounter_returner.get(j) / situationWinCounter_returner.get(j);
			situationWinDifference[j] = situationWinRatio_server[j] - situationWinRatio_returner[j];
			//System.out.print(situationWinRatio_server[j]+ " , " + situationWinRatio_returner[j] + "   ,   ");
			if(situationWinDifference[j] > 0.2){
				//System.out.print(situationWinDifference[j] + "  ,   ");
				//situations.get(j).printScoreSituation();
			}
		}
		// Fuer jede ScoreSituation die Differenz der Siegchance bei gewinnen und verlieren des Punktes erfassen
		for(int j = 0; j < situations.size(); j++){
		//	situationWinRatio[j] = situationWinCounter.get(j) / situationCounter.get(j);
		}
		ScoreValueList result = new ScoreValueList(ArrayTools.ArrayToArrayList(situationWinDifference), situations);
	
		// result beinhaltet die Wichtigkeitswerte der Scores und die Scores selber
		return result;
	}
	
	
	public static void measureClutchnessOfPlayer(String playername) throws IOException{
		ArrayList<Match> matches = LoadValues.loadAllMatchesOfPlayer(playername);
		ScoreValueList scoreValues = pointWinMatchWinPercentages(new ArrayList<String>());//(Arrays.asList(playername)));
		ArrayList<Double> values = scoreValues.getValues();
		ArrayList<ScoreSituation> situations = scoreValues.getSituations();
		double sum = 0;
		double pointCounter = 0;
		for(Match m : matches){
			for(Point p : m.getPoints()){
				if(p.getWinner().getName().equals(playername)){
					int serverIndex = 0;
					if(p.getServer().equals(m.getPlayers()[1])){
						serverIndex = 1;
					}
					ScoreSituation sit = new ScoreSituation(p.getScore(), serverIndex);
					int index = indexOfScoreSituationInList(sit, situations);
					if(index != -1){
						if(!values.get(index).isNaN()){
							sit.printScoreSituation();
							situations.get(index).printScoreSituation();
							System.out.println(" -- ");
							sum += values.get(index);
							pointCounter++;
						}
					}
				}
			}
		}
		double result = sum / pointCounter;
		System.out.println(result);
	}
	
	public static void measureClutchnessOfPlayersInMatch(String filename) throws IOException{
		Match m = LoadMatchFromTennisAbstract.readCSVFile(new File(filename));
		ScoreValueList scoreValues = pointWinMatchWinPercentages(new ArrayList<String>());//(Arrays.asList(playername)));
		ArrayList<Double> values = scoreValues.getValues();
		ArrayList<ScoreSituation> situations = scoreValues.getSituations();
		double[] sum = {0d,0d};
		double[] pointCounter = {0,0};
		for(Point p : m.getPoints()){
			int winnerIndex = 0;
			if(p.getWinner().equals(m.getPlayers()[1])){
				winnerIndex = 1;
			}
			int serverIndex = 0;
			if(p.getServer().equals(m.getPlayers()[1])){
				serverIndex = 1;
			}
			ScoreSituation sit = new ScoreSituation(p.getScore(), serverIndex);
			int index = indexOfScoreSituationInList(sit, situations);
			if(index != -1){
				if(!values.get(index).isNaN()){
					sum[winnerIndex] += values.get(index);
					pointCounter[winnerIndex]++;
				}
			}
		}

		double[] result = {sum[0] / pointCounter[0], sum[1] / pointCounter[1]};
		System.out.println(m.getPlayers()[0].getName() + ": " +  result[0] + " , " + m.getPlayers()[1].getName() + ": " + result[1]);
		m.printEndResult();
	}
	
	
	public static boolean checkIfScoreSituationInList(ScoreSituation sit, ArrayList<ScoreSituation> situations){
		for(ScoreSituation situation : situations){
			if(MatchTools.compareScoreSituations(sit,situation)){
				return true;
			}
		}
		return false;
	}
	
	public static int indexOfScoreSituationInList(ScoreSituation sit, ArrayList<ScoreSituation> situations){
		for(int i = 0; i < situations.size(); i++){
			if(MatchTools.compareScoreSituations(sit,situations.get(i))){
				return i;
			}
		}
		return -1;
	}
	
	
	
}
