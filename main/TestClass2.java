package main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import analysisFormats.MatchStatList;
import analysisFormats.MatchStatListCollection;
import analysisFormats.PlayerStatList;
import analysisFormats.PlayerStatListCollection;
import analytics.PatternAnalysis;
import analytics.PlayerComparison;
import analytics.PlayerStandardStats;
import analytics.PlayerStats;
import analytics.StandardStats;
import analytics.Testing;
import analytics.TransitivityMetrics;
import automaticMethodExecution.AutomaticMethodExecution;
import automaticMethodExecution.ExecuteMatchMethodForMultipleMatches;
import automaticMethodExecution.ExecutePlayerMethodForMultiplePlayers;
import counts.Match;
import counts.Point;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import player.Player;
import simulator.TestingSimulator;
import tools.MatrixTools;
import tools.OutputTools;


public class TestClass2 {

	public static void main(String[] args) throws Exception {
		
		long t1 = System.currentTimeMillis();
		
		//double accuracy1 = TestingSimulator.simulateAllMatchesByCommonOpponents("simulateMatchServeReturn",2,21);
		
		//System.out.println(accuracy1);

		PlayerStatListCollection pslC = PatternAnalysis.tensorWinAndSTD();
		PatternAnalysis.regression(pslC);
		
		long t2 = System.currentTimeMillis();
		
		System.out.println("Zeit in Minjgkuten: " + (t2-t1) / (1000*60) );
		
		//PlayerStats.createPlayerTransitionProbabilities(true);
		//Player rf = new Player("Andy_Murray");
		//PlayerStats.createTensorForOnePlayer(rf.getName(), LoadValues.loadAllMatchesOfPlayer(rf.getName()), true);
		//rf.loadCurrentTensor(false);
		//OutputTools.printTensor(rf.getTensor());
		/*Player player = new Player("Ivo_Karlovic");
		ArrayList<Match> matches = LoadValues.loadAllMatchesOfPlayer(player.getName());
		System.out.println("Return points won: " + PlayerStandardStats.returnPointsWon(player.getName()) + ", Service points won: " + PlayerStandardStats.servicePointsWon(player.getName()));
		System.out.println("BP made: " + PlayerStandardStats.breakPointsMadePercentage(player, matches) + ", BP saved: " + PlayerStandardStats.breakPointsSavedPercentage(player, matches));
		*/
		ArrayList<String> playernames = new ArrayList<String>(Arrays.asList("Roger_Federer", "Novak_Djokovic", "Rafael_Nadal", "Andy_Murray",
				"Philipp_Kohlschreiber", "Richard_Gasquet", "Gael_Monfils", "John_Isner", "Ivo_Karlovic", "David_Ferrer", "Tomas_Berdych",
				"Gastao_Elias", "Jiri_Vesely", "Kei_Nishikori", "Lukas_Rosol", "Milos_Raonic", "Bjorn_Borg", "John_Mcenroe", "Boris_Becker",
				"Marin_Cilic", "Nick_Kyrgios"));
		//PlayerStatListCollection plsC = AutomaticMethodExecution.executeDoubleArrayMethodMultiplePlayers("rallyLengthsPercentages", playernames);
		//plsC.printCollection();
		//PlayerStatListCollection pslC = ExecutePlayerMethodForMultiplePlayers.executePlayerStatListMethodMultiplePlayers("breakPointsSavedPercentage", playernames);
		//PlayerStatList psl_multi = ExecutePlayerMethodForMultiplePlayers.averageResultForMultiplePlayers("breakPointsSavedPercentage", playernames);
		String[] playermethods = {"breakPointsSavedPercentage","rallyLengthsPercentages"};
		String[] matchmethods = {"pointsWonByPlayerPercentage","winnersPerPoint"};

		//PlayerStatListCollection pslC = ExecutePlayerMethodForMultiplePlayers.executePlayerStatListMethodMultiplePlayers(playermethods, playernames);
		//pslC.printCollection();
		Match m1 = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/John_Isner-Roger_Federer-20120802.csv"));
		Match m2 = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Albert_Ramos-Pablo_Andujar-20120415.csv"));
		ArrayList<Match> matches = LoadValues.loadAllMatches();
		MatchStatList msl = ExecuteMatchMethodForMultipleMatches.averageResultForMultipleMatches(matchmethods, matches);
		msl.print();
		//MatchStatListCollection mslC = ExecuteMatchMethodForMultipleMatches.executeMatchStatListMethodMultipleMatches(matchmethods, allMatches);
		//mslC.printCollection();
		//MatchStatList msl_multi = ExecuteMatchMethodForMultipleMatches.averageResultForMultipleMatches("pointsWonByPlayerPercentage", LoadValues.loadAllMatchesOfPlayer("Roger_Federer"));
		//msl_multi.print();
		//pslC.printCollection();
		//psl_multi.print();

		
		
	}



}


