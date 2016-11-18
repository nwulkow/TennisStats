package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.flink.graph.Vertex;
//import org.apache.flink.ml.classification.SVM;

import Jama.Matrix;
import analytics.ApproachStats;
import analytics.BaselineStats;
import analytics.GellyAPI;
import analytics.MathematicalWinProbabilities;
import analytics.Metrics;
import analytics.PatternGraph;
import analytics.PlayerComparison;
import analytics.PlayerStandardStats;
import analytics.PlayerStats;
import analytics.ScoreStats;
import analytics.ServingStats;
import analytics.StandardStats;
import analytics.TensorComparison;
import analytics.Testing;
import analytics.TransitivityMetrics;
import analytics.WinProbabilityByTensor;
import counts.Match;
import counts.MatchInfo;
import counts.Score;
import counts.ScoreSituation;
import dataload.LoadAllTAMatches;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import graphics.MatrixImage;
import graphics.PlotTools;
import objects.OutputFinals;
import objects.Shottypes;
import player.Player;
import prediction.PredictBySVM;
import prediction.PredictResult;
import prediction.SVM;
import simulator.TestingSimulator;
import tools.ArrayTools;
import tools.MatrixTools;
import tools.OutputTools;
import tools.StatsTools;
import tools.Tools;

public class Main {

	public static void main(String[] args) throws Exception {		
		
		System.out.println("START");
		//Match sampleMatch = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/John_Isner-Roger_Federer-20120802.csv"));

		//TestingSimulator.crossTableSimulation();
		//PlayerComparison.testConvergenceOfPlayerRanking();
		//PlayerComparison.bundesligaTable();

		
		
		//ScoreStats.pointWinMatchWinPercentages(playernames);
		//ScoreStats.measureClutchnessOfPlayersInMatch("C:/Users/Niklas/TennisStatsData/SackmannRawData/Rafael_Nadal-Richard_Gasquet-20151031.csv");
		
		//PlayerStats.createTensorForOnePlayer("Rafael_Nadal", new ArrayList<Match>());
		//PlayerStats.createPlayerTransitionProbabilities(true);
		//Testing.shotDirectionAndOutcomeCorrelations();
		
		
		/*PredictBySVM pr =  new PredictBySVM(new Player("Andy_Murray"), new Player("Novak_Djokovic"));
		pr.setCategory(4);
		pr.assembleTrainingData(pr.getP1());
		//pr.assembleTrainingData(pr.getP2());
		pr.assemblePredictionData(pr.getP2());
		pr.assemblePredictionData(pr.getP1());
		pr.trainModel(pr.getP1(), SolverType.L1R_L2LOSS_SVC, true);
		OutputTools.printArray(pr.getP1().getLabels());
		System.out.println(pr.predictWinner(pr.getP1(), pr.getP2()));
		System.out.println(pr.predictWinner(pr.getP2(), pr.getP1()));
		System.out.println(Tools.namesOfMethods(pr.getMethod_list()));
		OutputTools.printArray(pr.getModel().getFeatureWeights());
		pr.testTrainingDataSet(pr.getP2());*/
		
		double difference = TensorComparison.compareTensors(new Player("Roger_Federer"), new Player("Rafael_Nadal"));
		System.out.println(difference);
		ArrayList<String> playernames = LoadValues.loadAllPlayernames();
		//playernames = new ArrayList<String>(playernames.subList(0, 80));
		playernames = Shottypes.interestingPlayers;
		double[][] diffMatrix = TensorComparison.createNetworkOfTensorDifferences(playernames);
		String edgesInputPath = "C:/Users/Niklas/TennisStatsData/diffMatrix_edges.csv";
		String vertexInputPath = "C:/Users/Niklas/TennisStatsData/diffMatrix_vertices.csv";
		
		GellyAPI gellyAPI = new GellyAPI(edgesInputPath, vertexInputPath);
		
		gellyAPI.setMaxIterations(2);
		boolean fileOutput = true;
		gellyAPI.communityDetection(edgesInputPath, vertexInputPath, playernames, fileOutput);
		ArrayList<ArrayList<Double>> weights = gellyAPI.findCenterVertices(playernames);
		//OutputTools.writeGraphInGDF(diffMatrix, playernames,  collectedVertices, "CDGephiFile");
		OutputTools.writeGraphInGDF(diffMatrix, playernames,  gellyAPI.getCollectedVertices(), "CDGephiFile" , "name VARCHAR,cluster DOUBLE,weightECM DOUBLE,weightBCM DOUBLE", weights );
		
		//WinProbabilityByTensor.computeWinProbabilityInRallyByTensor(new Player("Roger_Federer"), new Player("Nicolas_Almagro"));
		//WinProbabilityByTensor.computePointWinProbabilitiesByTensor(new Player("Roger_Federer"), new Player("Nicolas_Almagro"),0, false);
		//System.out.println(WinProbabilityByTensor.gameWinPercentageByTensor(new Player("Roger_Federer"), new Player("Novak_Djokovic"), false));
		//System.out.println(MathematicalWinProbabilities.mathematicalGameWinPercentageWithSide(0.72, 0.70));
		//PlayerStats.writeGeneralPlayerInformationAllPlayers();
		//PlayerStats.createPlayerTransitionProbabilities();
		
		//TransitivityMetrics.multiplyTest();
		//Tools.writeGraphInGDF(resultRatioMatrix, collectedVertices, "gelly");


	}
}
