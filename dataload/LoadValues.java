package dataload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import analysisFormats.ProbabilityTensor;
import analytics.StandardStats;
import counts.Match;
import counts.MatchInfo;
import counts.Point;
import counts.Score;
import counts.TennisAbstractPoint;
import objects.MatchDate;
import objects.OutputFinals;
import objects.Shottypes;
import player.Player;

public class LoadValues {

	public static double loadPlayerValue(String name, String category) throws NumberFormatException, IOException {
		FileReader csvFileToRead = new FileReader("C:/Users/Niklas/TennisStatsData/allPlayers.csv");
		BufferedReader br = new BufferedReader(csvFileToRead);

		String line = "";

		while ((line = br.readLine()) != null) {
			String[] row = line.split("  ");
			if (line.contains(name)) {
				if (category.equals(OutputFinals.v1name)) {
					br.close();
					return Double.parseDouble(row[1]);
				}
				if (category.equals(OutputFinals.v2name)) {
					br.close();
					return Double.parseDouble(row[2]);
				}
			}
		}
		br.close();
		return 0d;
	}
	
	public static ArrayList<Match> loadMatchesFromPaths(ArrayList<String> matchesstrings) throws IOException{
		
		boolean takeAll = true;
		if(matchesstrings.size() > 0){
			takeAll = false; // Wenn einzelne Matches ausgewaehlt sind, sollen nur die verwendet werden. Sonst alle
		}
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		ArrayList<Match> matches = new ArrayList<Match>();
		for (File file : folder.listFiles()) {
			String[] filename = file.getName().split("-");
			String player1name = filename[0];
			String player2name = filename[1];
			boolean doThisMatch = true;
			if(!takeAll && !matchesstrings.contains(file.getName()) || Shottypes.playersWhereErrorsHappen.contains(player1name) || Shottypes.playersWhereErrorsHappen.contains(player2name)){
				doThisMatch = false;// Wenn nicht alle Matches verwendet werden sollen und das aktuelle Match nicht in der Liste ist, dann boolean auf false
			}
			if (/*matchesstrings.contains(file.getName()) && */doThisMatch) {
				Match m = LoadMatchFromTennisAbstract.readCSVFile(file);
				matches.add(m);
			}
		}
		return matches;
	}
	
	public static ArrayList<Match> loadMatchesForPlayerlist(ArrayList<String> playernames) throws IOException{
		
		boolean takeAll = true;
		if(playernames.size() > 0){
			takeAll = false; // Wenn einzelne Matches ausgewaehlt sind, sollen nur die verwendet werden. Sonst alle
		}
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		ArrayList<Match> matches = new ArrayList<Match>();
		for (File file : folder.listFiles()) {
			String[] filename = file.getName().split("-");
			String player1name = filename[0];
			String player2name = filename[1];
			boolean doThisMatch = true;
			if(!takeAll && !playernames.contains(player1name) && !playernames.contains(player2name) || Shottypes.playersWhereErrorsHappen.contains(player1name) || Shottypes.playersWhereErrorsHappen.contains(player2name)){
				doThisMatch = false;// Wenn nicht alle Matches verwendet werden sollen und das aktuelle Match nicht in der Liste ist, dann boolean auf false
			}
			if (/*matchesstrings.contains(file.getName()) && */doThisMatch) {
				Match m = LoadMatchFromTennisAbstract.readCSVFile(file);
				matches.add(m);
			}
		}
		return matches;
	}
	
	public static Object[][] loadMatchDataForMatchSelectorFromStrings(String playername, ArrayList<String> matchesstrings) throws IOException{
		ArrayList<Match> matches = LoadValues.loadMatchesFromPaths(matchesstrings);
		return loadMatchDataForMatchSelector(playername, matches);
	}
	
	public static Object[][] loadMatchDataForMatchSelector(String playername, ArrayList<Match> matches) throws IOException{
		Object[][] data = new Object[matches.size()][4];
		for(int i = 0 ; i < matches.size(); i++){
			data[i] = loadMatchDataForMatchSelector(playername, matches.get(i));
		}
		return data;
	}
	
	
	public static Object[] loadMatchDataForMatchSelector(String playername, String matchname) throws IOException{
		Match m = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchname));
		return loadMatchDataForMatchSelector(playername, m);
	}
	
	public static Object[] loadMatchDataForMatchSelector(String playername, Match m) throws IOException{
		Player opponent = m.getPlayers()[1];
		int playerindex = 0;
		if(m.getPlayers()[1].getName().equals(playername)){
			opponent = m.getPlayers()[0];
			playerindex = 1;
		}
		String datestring = m.getDatestring();
		
		Player winner = m.getWinner();
		
		double pointswonpercentage[] = StandardStats.pointsWonByPlayerPercentage(m);
		double pointswon = pointswonpercentage[playerindex];
		
		Object[] result = new Object[4];
		
		result[0] = opponent.getName();
		result[1] = datestring;
		result[2] = "L";
		if(winner.getName().equals(playername)){
			result[2] = "W";
		}
		//result[2] = winner.getName();
		result[3] = pointswon;
		
		return result;
	}
	
	// Method um die Daten fuer den PlayerSelector im Simulator-Teil der GUI zu laden
	public static Object[][] loadPlayerDataForPlayerSelector() throws IOException{
		
		ArrayList<String> playernames = new ArrayList<String>();
		ArrayList<Double> matchesWon = new ArrayList<Double>();
		ArrayList<Double> matchesLost = new ArrayList<Double>();
		
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
			for(File matchFile : folder.listFiles()){
				String[] filename = matchFile.getName().split("-");
				String player1name = filename[0];
				String player2name = filename[1];
			if(!Shottypes.playersWhereErrorsHappen.contains(player1name) && !Shottypes.playersWhereErrorsHappen.contains(player2name)){
				if(!playernames.contains(player1name)){
					playernames.add(player1name);
					matchesWon.add(0d);
					matchesLost.add(0d);
				}
				if(!playernames.contains(player2name)){
					playernames.add(player2name);
					matchesWon.add(0d);
					matchesLost.add(0d);
				}
				
				Match m = LoadMatchFromTennisAbstract.readCSVFile(matchFile);
				if(m.getWinner().getName().equals(player1name)){
					int p1Index = playernames.indexOf(player1name);
					int p2Index = playernames.indexOf(player2name);
					matchesWon.set(p1Index, matchesWon.get(p1Index)+1);
					matchesLost.set(p2Index, matchesLost.get(p2Index)+1);
				}
				if(m.getWinner().getName().equals(player2name)){
					int p1Index = playernames.indexOf(player1name);
					int p2Index = playernames.indexOf(player2name);
					matchesLost.set(p1Index, matchesLost.get(p1Index)+1);
					matchesWon.set(p2Index, matchesWon.get(p2Index)+1);
				}
			}
		}
			
			Object[][] output = new Object[playernames.size()][4];
		for(int i = 0; i < playernames.size(); i++){
			Object[] result = new Object[4];
			result[0] = playernames.get(i);
			result[1] = matchesWon.get(i);
			result[2] = matchesLost.get(i);
			result[3] = 0; // Noch nicht fertig
			output[i] = result;
		}
		
		
		return output;
	}
	
	
	public static ProbabilityTensor loadTensor(String name, boolean withDirection) throws NumberFormatException, IOException {
		String filepath = "C:/Users/Niklas/TennisStatsData/" + name + "/transitionProbabilities.csv";
		if(withDirection){
			filepath = "C:/Users/Niklas/TennisStatsData/" + name + "/transitionProbabilitiesWithDirection.csv";
		}
		FileReader csvFileToRead = new FileReader(filepath);
		BufferedReader br = new BufferedReader(csvFileToRead);

		ProbabilityTensor tensor = new ProbabilityTensor();
		ProbabilityTensor serveplus1_tensor = new ProbabilityTensor();
		ProbabilityTensor return_tensor = new ProbabilityTensor();
		String line = "";
		int rowcounter = 0;
		if(withDirection){
			rowcounter = -1;
		}
		int hitFrom = -1;
		while ((line = br.readLine()) != null) {
			if(line.contains("From Position")){
				rowcounter = -1;
				hitFrom++;
			}
			if(rowcounter >= 1 && rowcounter <= 3){
				String row[] = line.split(",");
				tensor.getTensor().getProbForShots()[hitFrom][rowcounter-1][0].setProbabilityForShot(Double.parseDouble(row[0]));
				tensor.getTensor().getProbForShots()[hitFrom][rowcounter-1][1].setProbabilityForShot(Double.parseDouble(row[1]));
				tensor.getTensor().getProbForShots()[hitFrom][rowcounter-1][2].setProbabilityForShot(Double.parseDouble(row[2]));
			}
			if(rowcounter >= 5 && rowcounter <= 13){
				String row[] = line.split(",");
				int lineindex = (int) (rowcounter-5) / 3;
				tensor.getTensor().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-5, 3)].getOutcomeProbabilities()[0] = Double.parseDouble(row[0]);
				tensor.getTensor().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-5, 3)].getOutcomeProbabilities()[1] = Double.parseDouble(row[1]);
				tensor.getTensor().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-5, 3)].getOutcomeProbabilities()[2] = Double.parseDouble(row[2]);		
				tensor.getTensor().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-5, 3)].getOutcomeOfNextShot()[0] = Double.parseDouble(row[3]);
				tensor.getTensor().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-5, 3)].getOutcomeOfNextShot()[1] = Double.parseDouble(row[4]);
				tensor.getTensor().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-5, 3)].getOutcomeOfNextShot()[2] = Double.parseDouble(row[5]);		
			}
			//Serve+1
			if(rowcounter >= 15 && rowcounter <= 17){
				String row[] = line.split(",");
				tensor.getServeplus1().getProbForShots()[hitFrom][rowcounter-15][0].setProbabilityForShot(Double.parseDouble(row[0]));
				tensor.getServeplus1().getProbForShots()[hitFrom][rowcounter-15][1].setProbabilityForShot(Double.parseDouble(row[1]));
				tensor.getServeplus1().getProbForShots()[hitFrom][rowcounter-15][2].setProbabilityForShot(Double.parseDouble(row[2]));
			}
			if(rowcounter >= 19 && rowcounter <= 27){
				String row[] = line.split(",");
				int lineindex = (int) (rowcounter-19) / 3;
				tensor.getServeplus1().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-19, 3)].getOutcomeProbabilities()[0] = Double.parseDouble(row[0]);
				tensor.getServeplus1().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-19, 3)].getOutcomeProbabilities()[1] = Double.parseDouble(row[1]);
				tensor.getServeplus1().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-19, 3)].getOutcomeProbabilities()[2] = Double.parseDouble(row[2]);
				tensor.getServeplus1().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-19, 3)].getOutcomeOfNextShot()[0] = Double.parseDouble(row[3]);
				tensor.getServeplus1().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-19, 3)].getOutcomeOfNextShot()[1] = Double.parseDouble(row[4]);
				tensor.getServeplus1().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-19, 3)].getOutcomeOfNextShot()[2] = Double.parseDouble(row[5]);
			}
			// Return
			if(rowcounter >= 29 && rowcounter <= 31){
				String row[] = line.split(",");
				tensor.getReturns().getProbForShots()[hitFrom][rowcounter-29][0].setProbabilityForShot(Double.parseDouble(row[0]));
				tensor.getReturns().getProbForShots()[hitFrom][rowcounter-29][1].setProbabilityForShot(Double.parseDouble(row[1]));
				tensor.getReturns().getProbForShots()[hitFrom][rowcounter-29][2].setProbabilityForShot(Double.parseDouble(row[2]));
			}
			if(rowcounter >= 33 && rowcounter <= 41){
				String row[] = line.split(",");
				int lineindex = (int) (rowcounter-33) / 3;
				tensor.getReturns().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-33, 3)].getOutcomeProbabilities()[0] = Double.parseDouble(row[0]);
				tensor.getReturns().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-33, 3)].getOutcomeProbabilities()[1] = Double.parseDouble(row[1]);
				tensor.getReturns().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-33, 3)].getOutcomeProbabilities()[2] = Double.parseDouble(row[2]);
				tensor.getReturns().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-33, 3)].getOutcomeOfNextShot()[0] = Double.parseDouble(row[3]);
				tensor.getReturns().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-33, 3)].getOutcomeOfNextShot()[1] = Double.parseDouble(row[4]);
				tensor.getReturns().getProbForShots()[hitFrom][lineindex][Math.floorMod(rowcounter-33, 3)].getOutcomeOfNextShot()[2] = Double.parseDouble(row[5]);
			}
			// Erster Aufschlag
			if(rowcounter >= 43 && rowcounter <= 44){
				String row[] = line.split(",");
				tensor.getFirstserves()[rowcounter-43][0].setProbabilityForShot(Double.parseDouble(row[0]));
				tensor.getFirstserves()[rowcounter-43][1].setProbabilityForShot(Double.parseDouble(row[1]));
				tensor.getFirstserves()[rowcounter-43][2].setProbabilityForShot(Double.parseDouble(row[2]));
			}
			if(rowcounter >= 46 && rowcounter <= 51){
				String row[] = line.split(",");
				int lineindex = (int) (rowcounter-46) / 3;
				tensor.getFirstserves()[lineindex][Math.floorMod(rowcounter-46, 3)].getOutcomeProbabilities()[0] = Double.parseDouble(row[0]);
				tensor.getFirstserves()[lineindex][Math.floorMod(rowcounter-46, 3)].getOutcomeProbabilities()[1] = Double.parseDouble(row[1]);
				tensor.getFirstserves()[lineindex][Math.floorMod(rowcounter-46, 3)].getOutcomeProbabilities()[2] = Double.parseDouble(row[2]);
				tensor.getFirstserves()[lineindex][Math.floorMod(rowcounter-46, 3)].getOutcomeOfNextShot()[0] = Double.parseDouble(row[3]);
				tensor.getFirstserves()[lineindex][Math.floorMod(rowcounter-46, 3)].getOutcomeOfNextShot()[1] = Double.parseDouble(row[4]);
				tensor.getFirstserves()[lineindex][Math.floorMod(rowcounter-46, 3)].getOutcomeOfNextShot()[2] = Double.parseDouble(row[5]);
			}
			// Zweiter Aufschlag
			if(rowcounter >= 53 && rowcounter <= 54){
				String row[] = line.split(",");
				tensor.getSecondserves()[rowcounter-53][0].setProbabilityForShot(Double.parseDouble(row[0]));
				tensor.getSecondserves()[rowcounter-53][1].setProbabilityForShot(Double.parseDouble(row[1]));
				tensor.getSecondserves()[rowcounter-53][2].setProbabilityForShot(Double.parseDouble(row[2]));
			}
			if(rowcounter >= 56 && rowcounter <= 61){
				String row[] = line.split(",");
				int lineindex = (int) (rowcounter-56) / 3;
				tensor.getSecondserves()[lineindex][Math.floorMod(rowcounter-56, 3)].getOutcomeProbabilities()[0] = Double.parseDouble(row[0]);
				tensor.getSecondserves()[lineindex][Math.floorMod(rowcounter-56, 3)].getOutcomeProbabilities()[1] = Double.parseDouble(row[1]);
				tensor.getSecondserves()[lineindex][Math.floorMod(rowcounter-56, 3)].getOutcomeProbabilities()[2] = Double.parseDouble(row[2]);	
				tensor.getSecondserves()[lineindex][Math.floorMod(rowcounter-56, 3)].getOutcomeOfNextShot()[0] = Double.parseDouble(row[3]);
				tensor.getSecondserves()[lineindex][Math.floorMod(rowcounter-56, 3)].getOutcomeOfNextShot()[1] = Double.parseDouble(row[4]);
				tensor.getSecondserves()[lineindex][Math.floorMod(rowcounter-56, 3)].getOutcomeOfNextShot()[2] = Double.parseDouble(row[5]);
			}
			
			rowcounter++;
		}
		br.close();
		return tensor;

	}

	public static ArrayList<Point> loadAllPointsOfPlayer(String playername, int startyear, int endyear)
			throws IOException {
		// ArrayList<Map<Point, Player>> points = new ArrayList<Map<Point,
		// Player>>();
		ArrayList<Point> points = new ArrayList<Point>();
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for (File file : folder.listFiles()) {
			boolean loadbool = false;
			if (file.getName().contains(playername) || playername.equals("")) {
				loadbool = true;
			}
			if (loadbool) {
				String[] names = file.getName().replace(".csv", "").split("-");
				String player1name = names[0];
				String player2name = names[1];
				MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(file.getAbsolutePath());
				Match m = new Match(im, new Player(player1name), new Player(player2name));
				if (m.getDate().getYear() >= startyear && m.getDate().getYear() <= endyear) {
					/*
					 * for(Point p : m.getPoints()){
					 * if(p.getServer().getName().equals(playername)){
					 * points.add(p); } }
					 */
					points.addAll(m.getPoints());
				}
			}
		}
		return points;
	}

	public static ArrayList<Point> loadAllPointsOfPlayer(String playername) throws IOException {
		return loadAllPointsOfPlayer(playername, 0, 10000000);
	}

	
	public static ArrayList<String> loadAllPlayernames(){
		
		ArrayList<String> playernames = new ArrayList<String>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/PlayerStats/");
		for(File file : playerstats_folder.listFiles()){
			playernames.add(file.getName());
		}
		return playernames;
	}
	
	public static ArrayList<String> loadAllMatchFilesOfPlayer(String playername){
		
		ArrayList<String> matches = new ArrayList<String>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for(File file : playerstats_folder.listFiles()){
			//System.out.println(file.getName());
			if(file.getName().contains(playername)){
				matches.add(file.getName());
			}
		}
		return matches;
	}
	
	public static ArrayList<Match> loadAllMatchesOfPlayer(String playername) throws IOException{
		
		ArrayList<Match> matches = new ArrayList<Match>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for(File file : playerstats_folder.listFiles()){
			if(file.getName().contains(playername)){
				matches.add(LoadMatchFromTennisAbstract.readCSVFile(file));
			}
		}
		return matches;
	}
	
	public static ArrayList<Match> loadAllMatches() throws IOException{
		
		ArrayList<Match> matches = new ArrayList<Match>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for(File file : playerstats_folder.listFiles()){
			matches.add(LoadMatchFromTennisAbstract.readCSVFile(file));
		}
		return matches;
	}
	
	
	// Findet alle Spieler, die schon gegen jeden Spieler aus einer Input-Liste gespielt haben
	public static ArrayList<String> loadCommonOpponentNames(ArrayList<String> playernames){
		
		ArrayList<ArrayList<String>> opponents = new ArrayList<ArrayList<String>>();
		for(int k = 0; k < playernames.size(); k++){
			opponents.add(new ArrayList<String>());
		}
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for(File file : folder.listFiles()){
			String title = file.getName();
			String player1name = title.split("-")[0];
			String player2name = title.split("-")[1];
			for(int i = 0; i < playernames.size(); i++){
				if(player1name.equals(playernames.get(i)) && !opponents.get(i).contains(player2name)){
					opponents.get(i).add(player2name);
				}	
				if(player2name.equals(playernames.get(i)) && !opponents.get(i).contains(player1name)){
					opponents.get(i).add(player1name);
				}	
			}
		}
		ArrayList<String> commonOpponents = opponents.get(0);
		for(int j = 1; j < playernames.size(); j++){
			commonOpponents.retainAll(opponents.get(j));
		}
		return commonOpponents;		
	}
	
	public static ArrayList<Match> loadMatchesAgainstCommonOpponents(ArrayList<String> playernames) throws IOException{
		ArrayList<String> commonOpponents = loadCommonOpponentNames(playernames);
		//ArrayList<Match> allMatches = loadAllMatches();
		ArrayList<Match> selectedMatches = new ArrayList<Match>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for(File file : playerstats_folder.listFiles()){
			String[] names_part = file.getName().replace(".csv", "").split("/");
			String[] names = names_part[names_part.length - 1].split("-");
			String player1name = names[0];
			String player2name = names[1];
			if((playernames.contains(player1name) && commonOpponents.contains(player2name)) || (playernames.contains(player1name) && commonOpponents.contains(player2name))){
				selectedMatches.add(LoadMatchFromTennisAbstract.readCSVFile(file));
			}
		}

		return selectedMatches;
	}
	
	public static ArrayList<Match> loadMatchesAgainstSelectedOpponents(ArrayList<String> playernames, ArrayList<String> opponents) throws IOException{
		//ArrayList<Match> allMatches = loadAllMatches();
		ArrayList<Match> selectedMatches = new ArrayList<Match>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for(File file : playerstats_folder.listFiles()){
			String[] names_part = file.getName().replace(".csv", "").split("/");
			String[] names = names_part[names_part.length - 1].split("-");
			String player1name = names[0];
			String player2name = names[1];
			if((playernames.contains(player1name) && opponents.contains(player2name)) || (playernames.contains(player2name) && opponents.contains(player1name))){
				selectedMatches.add(LoadMatchFromTennisAbstract.readCSVFile(file));
			}
		}

		return selectedMatches;
	}
	
	public static ArrayList<Match> loadMatchesAgainstSelectedOpponents(String playername, ArrayList<String> opponents) throws IOException{
		return loadMatchesAgainstSelectedOpponents(new ArrayList<String>(Arrays.asList(playername)), opponents);
	}
	public static ArrayList<Match> loadMatchesAgainstSelectedOpponents(String playername, String opponent) throws IOException{
		return loadMatchesAgainstSelectedOpponents(new ArrayList<String>(Arrays.asList(playername)), new ArrayList<String>(Arrays.asList(opponent)));
	}
	

	public static double loadNumberOfMatchesAgainstSelectedOpponents(ArrayList<String> playernames, ArrayList<String> opponents) throws IOException{
		//ArrayList<Match> allMatches = loadAllMatches();
		ArrayList<Match> selectedMatches = new ArrayList<Match>();
		File playerstats_folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		int counter = 0;
		for(File file : playerstats_folder.listFiles()){
			String[] names_part = file.getName().replace(".csv", "").split("/");
			String[] names = names_part[names_part.length - 1].split("-");
			String player1name = names[0];
			String player2name = names[1];
			if((playernames.contains(player1name) && opponents.contains(player2name)) || (playernames.contains(player2name) && opponents.contains(player1name))){
				counter++;
			}
		}

		return counter;
	}
	
	public static double loadNumberOFMatchesAgainstSelectedOpponents(String playername, ArrayList<String> opponents) throws IOException{
		return loadNumberOfMatchesAgainstSelectedOpponents(new ArrayList<String>(Arrays.asList(playername)), opponents);
	}
	public static double loadNumberOfMatchesAgainstSelectedOpponents(String playername, String opponent) throws IOException{
		return loadNumberOfMatchesAgainstSelectedOpponents(new ArrayList<String>(Arrays.asList(playername)), new ArrayList<String>(Arrays.asList(opponent)));
	}
	
	
	public static ArrayList<String> loadNamesOfOpponents(String playername, ArrayList<Match> matches){
		ArrayList<String> opponentNames = new ArrayList<String>();
		for(Match m : matches){
			for(Player player : m.getPlayers()){
				if(!player.getName().equals(playername) && !opponentNames.contains(player.getName())){
					opponentNames.add(player.getName());
				}
			}
		}
		return opponentNames;
	}
	
	public static String loadPlayingHand(String playername) throws IOException{
		FileReader csvFileToRead = new FileReader("C:/Users/Niklas/TennisStatsData/PlayerStats/" + playername + "/generalInfo.csv");
		BufferedReader br = new BufferedReader(csvFileToRead);

		String line = "";
		int rowcounter = 0;
		while ((line = br.readLine()) != null) {
			if(rowcounter == 1){
				String[] info = line.split(",");
				return info[1];
			}
			rowcounter++;
		}
		return "";
	}
}
