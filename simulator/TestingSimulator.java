package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import analytics.PlayerComparison;
import analytics.PlayerStandardStats;
import analytics.PlayerStats;
import analytics.StandardStats;
import analytics.TransitivityMetrics;
import counts.Match;
import dataload.LoadValues;
import de.bwaldvogel.liblinear.SolverType;
import graphics.BarChart;
import graphics.PlotTools;
import objects.Shottypes;
import player.Player;
import prediction.PredictResult;
import statsCategories.Category1Stats;
import statsCategories.Category2Stats;
import statsCategories.Category3Stats;
import statsCategories.Category4Stats;
import tools.ArrayTools;
import tools.MatchTools;
import tools.MatrixTools;
import tools.OutputTools;
import tools.StatsTools;

public class TestingSimulator {

	
	public static void main(String[] args) throws IOException{
		
		ArrayList<double[]> possibleResults = Shottypes.possibleResults;
		ArrayList<Double> resultCounter = new ArrayList<Double>();
		for(int k = 0; k < possibleResults.size(); k++){
			resultCounter.add(0d);
		}
		int numIter = 100;
		String player1name = "Roger_Federer";
		String player2name = "Gael_Monfils";
		//LoadValues.LoadTensor(player1name);
		ArrayList<Match> matchesP1 = LoadValues.loadAllMatchesOfPlayer(player1name);
		ArrayList<Match> matchesP2 = LoadValues.loadAllMatchesOfPlayer(player2name);
		Player p1 = new Player(player1name, PlayerStandardStats.servicePointsWon(player1name,matchesP1),PlayerStandardStats.returnPointsWon(player1name,matchesP1));
		Player p2 = new Player(player2name, PlayerStandardStats.servicePointsWon(player2name,matchesP2),PlayerStandardStats.returnPointsWon(player2name,matchesP2));
		
		boolean withDirection = false;
		p1.loadCurrentTensor(withDirection);
		p2.loadCurrentTensor(withDirection);

		for (int i = 0; i < numIter; i++){
			Match m = Simulator.simulateMatchShotByShot(p1, p2, 2, withDirection); // Match simulieren
			//Match m = Simulator.simulateMatch(p1, p2, 2);
			StandardStats.printAllStandardStats(m);
			
			double[] result = {m.getGames_in_sets()[0][0], m.getGames_in_sets()[1][0]}; // Ergebnis des ersten Satzes notieren
			for(int j = 0; j < possibleResults.size(); j++){
				if(ArrayTools.compareDoubleArray(result,  possibleResults.get(j))){
					resultCounter.set(j, resultCounter.get(j)+1);
				}
			}
		}
		double[] resultCounter_relative = StatsTools.absToRel(ArrayTools.ArrayListToArray(resultCounter));
		double[] cummulated_relative = StatsTools.cummulatedArrayValues(resultCounter_relative);
		for(double d : resultCounter){
			//System.out.println(d);
		}
		double p1_winpercentage = StatsTools.sumOfArrayBoundaries(resultCounter_relative, 0, 7);
		System.out.println(player1name + " " + p1_winpercentage + " : " + (1-p1_winpercentage) + " " + player2name);
		PlotTools.plotBar(resultCounter_relative,Shottypes.resultNames, "Result distribution");
		PlotTools.plotBar(cummulated_relative, Shottypes.resultNames, "Cummulated Result Distribution");
	}
	
	
	// Wenn eine List aus Pfaden zu Matches gegeben ist, soll erst eine ArrayList<Match> erstellt werden und mit der dann
	// weitergearbeitet werden
	public static double winPercentageOfSimulationFromStrings(String player1name, ArrayList<String> matchesstrings1, String player2name, ArrayList<String> matchesstrings2, int setstowin, int numIter, int mode, boolean currentP1, boolean currentP2) throws IOException{
		ArrayList<Match> matches1 = new ArrayList<Match>();
		if(matchesstrings1.size() == 0){ // Wenn die Liste leer ist, dann alle Matches dieses Spielers verwenden
			matches1 = LoadValues.loadAllMatchesOfPlayer(player1name);
		}
		else{
			matches1 = LoadValues.loadMatchesFromPaths(matchesstrings1);
		}
		ArrayList<Match> matches2 = new ArrayList<Match>();
		if(matchesstrings2.size() == 0){
			matches2 = LoadValues.loadAllMatchesOfPlayer(player2name);
		}
		else{
			matches2 = LoadValues.loadMatchesFromPaths(matchesstrings2);
		}
		
		return winPercentageOfSimulation(player1name, matches1, player2name, matches2, setstowin, numIter, mode, currentP1, currentP2);
	}
	
	
	public static double winPercentageOfSimulation(String player1name, ArrayList<Match> matches1, String player2name, ArrayList<Match> matches2, int setstowin, int numIter, int mode, boolean currentP1, boolean currentP2) throws IOException{

		Player p1 = new Player(player1name, PlayerStandardStats.servicePointsWon(player1name, matches1),PlayerStandardStats.returnPointsWon(player1name, matches1));
		Player p2 = new Player(player2name, PlayerStandardStats.servicePointsWon(player2name, matches2),PlayerStandardStats.returnPointsWon(player2name, matches2));
		boolean withDirection = false;
		if(mode == 4){
			withDirection = true;
		}
		if(matches1.size() > 0){
			if(currentP1){
				PlayerStats.createTensorForOnePlayer(player1name, matches1, withDirection, currentP1);	
				p1.loadCurrentTensor(withDirection);
			}
			else{
				p1.loadTensor(withDirection);
			}
			
		}
		else{
			p1.loadTensor(true);
		}
		if(matches2.size() > 0){
			if(currentP2){
				PlayerStats.createTensorForOnePlayer(player2name, matches2, withDirection, currentP2);
				p2.loadCurrentTensor(withDirection);
			}
			else{
				p2.loadTensor(withDirection);
			}
		}
		else{
			p2.loadTensor(true);
		}

		p1.setWinvalue(PlayerStandardStats.pointsWon(player1name, matches1));
		p2.setWinvalue(PlayerStandardStats.pointsWon(player2name, matches2));
		
		// mode: 1 = Point win %, 2 = Serve/Return win %, 3 = Shot by Shot
		ArrayList<double[]> possibleResults = Shottypes.possibleResults;
		ArrayList<Double> resultCounter = new ArrayList<Double>();
		for(int k = 0; k < possibleResults.size(); k++){
			resultCounter.add(0d);
		}

		for (int i = 0; i < numIter; i++){
			Match m = new Match();
			if(mode != 3){
				m = Simulator.simulateMatch(p1, p2, setstowin, mode);
			}
			if(mode == 3){
				m = Simulator.simulateMatchShotByShot(p1, p2, setstowin, false); // Match simulieren
			}
			if(mode == 4){
				m = Simulator.simulateMatchShotByShot(p1, p2, setstowin, true); // Match simulieren
			}
			//
			//StandardStats.printAllStandardStats(m);
			
			double[] result = {m.getGames_in_sets()[0][0], m.getGames_in_sets()[1][0]}; // Ergebnis des ersten Satzes notieren
			for(int j = 0; j < possibleResults.size(); j++){
				if(ArrayTools.compareDoubleArray(result,  possibleResults.get(j))){
					resultCounter.set(j, resultCounter.get(j)+1);
				}
			}
		}
		double[] resultCounter_relative = StatsTools.absToRel(ArrayTools.ArrayListToArray(resultCounter));
		double[] cummulated_relative = StatsTools.cummulatedArrayValues(resultCounter_relative);
		for(double d : resultCounter){
			//System.out.println(d);
		}
		double p1_winpercentage = StatsTools.sumOfArrayBoundaries(resultCounter_relative, 0, 7);
		return p1_winpercentage;
	}
	
	// Aus einer Liste von Spielern spielt jeder gegen jeden. Die Siegquoten werden in einer Matrix
	// notiert welche dann geprintet wird
	public static void crossTableSimulation(boolean withDirection) throws NumberFormatException, IOException{
		ArrayList<String> playernames = new ArrayList<String>(Arrays.asList("Roger_Federer","Rafael_Nadal","Novak_Djokovic","Andy_Murray", "John_Isner","Tomas_Berdych", "Fabio_Fognini", "Dustin_Brown", "Gilles_Simon", "Philipp_Kohlschreiber"));
		double[][] resultmatrix = new double[playernames.size()][playernames.size()];
		double[][] resultmatrix_unweighted = new double[playernames.size()][playernames.size()];
		for(int i = 0; i < playernames.size(); i++){
			for(int j = i; j < playernames.size(); j++){
				double result = simulateMatchesBetweenPlayers(playernames.get(i), playernames.get(j), 500, withDirection);
				resultmatrix[i][j] = result;
				resultmatrix[j][i] = 1 - result;
				resultmatrix_unweighted[i][j] = Math.round(result);
				resultmatrix_unweighted[j][i] = Math.round(1-result);
			}
		}
		System.out.println(playernames);
		OutputTools.writeGraphForGellyAPI(resultmatrix, "resultmatrix",true);
		
	}
	
	
	// Simuliert einige (numIter) Matches zweier gewaehlter Spieler gegeneinander.
	// Return: Siegquote von Spieler 1
	public static double simulateMatchesBetweenPlayers(String player1name, String player2name, int numIter, boolean withDirection) throws NumberFormatException, IOException{
		
		ArrayList<double[]> possibleResults = Shottypes.possibleResults;
		ArrayList<Double> resultCounter = new ArrayList<Double>();
		for(int k = 0; k < possibleResults.size(); k++){
			resultCounter.add(0d);
		}
		ArrayList<Match> matchesP1 = LoadValues.loadAllMatchesOfPlayer(player1name);
		ArrayList<Match> matchesP2 = LoadValues.loadAllMatchesOfPlayer(player2name);
		Player p1 = new Player(player1name, PlayerStandardStats.servicePointsWon(player1name,matchesP1),PlayerStandardStats.returnPointsWon(player1name,matchesP1));
		Player p2 = new Player(player2name, PlayerStandardStats.servicePointsWon(player2name,matchesP2),PlayerStandardStats.returnPointsWon(player2name,matchesP2));
		
		p1.loadTensor(true);
		p2.loadTensor(true);

		for (int i = 0; i < numIter; i++){
			Match m = Simulator.simulateMatchShotByShot(p1, p2, 2, withDirection); // Match simulieren
			//StandardStats.printAllStandardStats(m);
			
			double[] result = {m.getGames_in_sets()[0][0], m.getGames_in_sets()[1][0]}; // Ergebnis des ersten Satzes notieren
			for(int j = 0; j < possibleResults.size(); j++){
				if(ArrayTools.compareDoubleArray(result,  possibleResults.get(j))){
					resultCounter.set(j, resultCounter.get(j)+1);
				}
			}
		}
		double[] resultCounter_relative = StatsTools.absToRel(ArrayTools.ArrayListToArray(resultCounter));
		double[] cummulated_relative = StatsTools.cummulatedArrayValues(resultCounter_relative);

		double p1_winpercentage = StatsTools.sumOfArrayBoundaries(resultCounter_relative, 0, 7);
		
		return p1_winpercentage;
	}
	
	
	public static double simulateAllMatchesByCommonOpponents(String methodname, int setstowin, int numIter) throws Exception{

		List<String> playernames = LoadValues.loadAllPlayernames();
		//playernames = playernames.subList(300, playernames.size()-1);

		PredictResult pr = new PredictResult();
		pr.setSetstowin(setstowin);
		pr.setNumIter(numIter);
		
		// Alle Prediction-Methods
		Method[] methods = PredictResult.class.getDeclaredMethods();
		
		// Nur die benutzen aus PredictResult, die tatsaechlich zur Prediction gedacht sind, also nicht Getters oder Setters
		ArrayList<Method> method_list = new ArrayList<Method>();
		ArrayList<String> methodnames_list = new ArrayList<String>();
		for(Method method : methods){
			if(method.getName().contains("simulate")){
				method_list.add(method);
				methodnames_list.add(method.getName());
			}
		}
		
		// Methodenindex in der method_list
		int methodNumber = methodnames_list.indexOf(methodname);
		if(methodNumber == -1){
			System.out.println("No method found");
			return 0;
		}
		Method method = method_list.get(methodNumber);
		//ArrayList<Match> usedMatches = new ArrayList<Match>();
		ArrayList<String> realWinnerNames = new ArrayList<String>();
		ArrayList<Double> sampleSTDs = new ArrayList<Double>();
		ArrayList<Double> realSTDs = new ArrayList<Double>();
		
		double correctResults = 0d; // Zaehlt, wie viele Ergebnisse richtig vorhergesagt wurden
		double attempts = 0d; // Zaehlt, wie viele Ergebnisse richtig oder falsch vorhergesagt wurden
		ArrayList<String> forbiddenPlayers = new ArrayList<String>(Arrays.asList(/*"Roger_Federer","Rafael_Nadal","Novak_Djokovic","Andy_Murray"*/));
		for(int p1index = 0; p1index < playernames.size(); p1index++){
			String player1name = playernames.get(p1index);
			for(int p2index = p1index; p2index < playernames.size(); p2index++){
				String player2name = playernames.get(p2index);
				boolean doThisMatchBool = true;
				if(forbiddenPlayers.contains(player1name) || forbiddenPlayers.contains(player2name)){
					doThisMatchBool = false;
				}
				ArrayList<String> commonOpponents = new ArrayList<String>();
				ArrayList<Match> matchesAgainstEachOther = new ArrayList<Match>();
				
				if(doThisMatchBool){
					double numberOFMatchesAgainstEachOther = LoadValues.loadNumberOfMatchesAgainstSelectedOpponents(player1name, player2name);
					if(numberOFMatchesAgainstEachOther == 0){
						doThisMatchBool = false;
					}
				}
				// Gemeinsame Gegner  laden
				if(doThisMatchBool){
					commonOpponents = LoadValues.loadCommonOpponentNames(new ArrayList<String>(Arrays.asList(player1name, player2name)));
					if(commonOpponents.size() <= 3){
							doThisMatchBool = false;
					}
				}
				
				if(doThisMatchBool){
					// Matches gegeneinander laden
				matchesAgainstEachOther = LoadValues.loadMatchesAgainstSelectedOpponents(player1name, player2name);		
					if(matchesAgainstEachOther.size() == 0){
						doThisMatchBool = false;
					}
				}

				double sampleSTD = 0;
				double realSTD = 0;
				if(doThisMatchBool){
					// Zufallsmatrix von der Groeße der resultRatioMatrix erstellen und pageRank laufen lassen (als Referenz fuer den Durchschnitt)
					// Wenn der PageRank-StD-Wert der resultRatioMatrix groeßer ist als 8 mal jener der sampleResultMatrix, weitermachen
					
					//double[][] sampleResultMatrix = MatrixTools.randomMatrix(commonOpponents.size(), commonOpponents.size(), 0, 1).getArray();
					//sampleSTD = TransitivityMetrics.pageRankSTD(sampleResultMatrix);
					//double[][] resultRatioMatrix = PlayerComparison.createResultMatrix(commonOpponents, false);
					//realSTD = TransitivityMetrics.pageRankSTD(resultRatioMatrix);
					//System.out.println(sampleSTD + " , " + realSTD);
					if(doThisMatchBool && realSTD <= sampleSTD*8){
						//doThisMatchBool = false;
					}
					
				}
				if(doThisMatchBool){
					realSTDs.add(realSTD);
					sampleSTDs.add(sampleSTD);
							
						Player p1 = new Player(player1name);
						Player p2 = new Player(player2name);
						
						ArrayList<Match> p1Matches = LoadValues.loadMatchesAgainstSelectedOpponents(player1name, commonOpponents);
						ArrayList<Match> p2Matches = LoadValues.loadMatchesAgainstSelectedOpponents(player2name, commonOpponents);
						//ArrayList<Match> p1Matches = LoadValues.loadAllMatchesOfPlayer(player1name);
						//ArrayList<Match> p2Matches = LoadValues.loadAllMatchesOfPlayer(player2name);
						double p1PointWinValue = PlayerStandardStats.pointsWon(player1name, p1Matches);
						double p2PointWinValue = PlayerStandardStats.pointsWon(player2name, p2Matches);
						if(Math.abs(p1PointWinValue - p2PointWinValue) < 0.005){
							//System.out.println(commonOpponents);
							Match m_simulated = new Match();
							double[] tensorMatchWinCounter = {0d,0};
						//	for(Match m1 : p1Matches){
							//	for(Match m2 : p2Matches){
								//	ArrayList<Match> p1Matches_slice = new ArrayList<Match>(Arrays.asList(m1));
									//ArrayList<Match> p2Matches_slice = new ArrayList<Match>(Arrays.asList(m2));

												
						
						pr.setP1(p1);
						pr.setP2(p2);
						pr.setP1Matches(p1Matches);
						pr.setP2Matches(p2Matches);
						
						
						
						// Ausfuehren der Prediction-Methode
						m_simulated = (Match) method.invoke(pr);
						tensorMatchWinCounter[MatchTools.getPlayerIndex(m_simulated.getWinner().getName(), m_simulated)]++;
						//usedMatches.add(m_simulated);
								//}
							//}
							//OutputTools.printArray(tensorMatchWinCounter);
							//m_simulated.setWinner(m_simulated.getPlayers()[ArrayTools.entryOfMax(tensorMatchWinCounter)]);
						// Echte Resultate suchen. Derjenige, der mehr Matches in echt gewonnen hat (gegeneinander) wird als Sieger gewertet
						if(matchesAgainstEachOther.size() > 0){
							double[] wincounter = {0d,0};
							for(Match m_current : matchesAgainstEachOther){
								
								// Wincounter des Siegers des aktuellen echten Matches wird um 1 erhoeht
								wincounter[MatchTools.getPlayerIndex(m_current.getWinner().getName(), m_simulated)]++;
								
							}
							// Output
							realWinnerNames.add(m_simulated.getPlayers()[ArrayTools.entryOfMax(wincounter)].getName());
							if(MatchTools.getPlayerIndex(m_simulated.getWinner(), m_simulated) == ArrayTools.entryOfMax(wincounter)){
								correctResults++;
							}
							attempts++;
							System.out.println("P1: " + m_simulated.getPlayers()[0].getName() + " , P2: " + m_simulated.getPlayers()[1].getName());
							System.out.println("Simulated winner: " + m_simulated.getWinner().getName() + " , Real winner: " + m_simulated.getPlayers()[ArrayTools.entryOfMax(wincounter)].getName());
							System.out.println(player1name + " , " + correctResults + " , " + attempts + " ( = " + (100*correctResults/attempts) + "% )");
							System.out.println("---");
						}
						}
					}
				}
		}

		double accuracy = correctResults / attempts;
		System.out.println("Correctly predicted : " + correctResults + " / " + attempts + " ( = " + accuracy + " ) ");
		outputSimulationOfAllMatchesByCO(accuracy, method);
		
	/*	for(int k = 0; k < usedMatches.size(); k++){
			System.out.println(usedMatches.get(k).getPlayers()[0].getName() + " : " + usedMatches.get(k).getPlayers()[1].getName() + " Simulated Winner " +
					usedMatches.get(k).getWinner().getName() + " , Real winner: " + realWinnerNames.get(k) + " sample STD = " + sampleSTDs.get(k)
					+ " , real STD = " + realSTDs.get(k));
		}*/
		
		return accuracy;
	}
	
	public static void outputSimulationOfAllMatchesByCO(double accuracy, Method method) throws IOException{
		File file = new File("C:/Users/Niklas/TennisStatsData/" + "simulationResults");
		String modetext = method.getName();
		FileUtils.writeStringToFile(file, modetext + "\t" + accuracy + "\n", true);		
	}
	
	
}
