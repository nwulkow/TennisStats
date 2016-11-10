package analytics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.Dir;

import analysisFormats.ProbabilityForShot;
import analysisFormats.ProbabilityMatrixFromDirection;
import analysisFormats.ProbabilityTensor;
import counts.Match;
import counts.Point;
import counts.Shot;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import objects.Shottypes;
import player.Player;
import tools.OutputTools;
import tools.Tools;

public class PlayerStats {

	
	public static double matchWinPercentage(Player player, ArrayList<Match> matches){
		double matchesWon = 0d;
		double matchesPlayed = 0d;
		for(Match m : matches){
			if(player.getName().equals(m.getWinner().getName())){
				matchesWon++;
			}
			matchesPlayed++;
		}
		double matchesWonRatio = matchesWon / matchesPlayed;
		return matchesWonRatio;
	}
	

	
	
	public static void createPlayerTransitionProbabilities(boolean withDirection) throws IOException{
		
		
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		//Spielerlist erstellen
		//----
		ArrayList<String> playernames = new ArrayList<String>();
		for (File file : folder.listFiles()) {
			String filename = file.getName();
			String p1name = filename.split("-")[0];
			String p2name = filename.split("-")[1];
			if(!playernames.contains(p1name)){
				playernames.add(p1name);
			}
			if(!playernames.contains(p2name)){
				playernames.add(p2name);
			}
		}
		//----
		
		for(String playername : playernames){
			createTensorForOnePlayer(playername, new ArrayList<Match>(), withDirection, false);
		}
	}
	
	
	public static void createTensorForOnePlayerFromStrings(String playername, ArrayList<String> matchesstrings, boolean withDirection, boolean current) throws IOException{
		ArrayList<Match> matches = LoadValues.loadMatchesFromPaths(matchesstrings);
		createTensorForOnePlayer(playername, matches, withDirection, current);
	}
	
	public static void createTensorForOnePlayer(String playername, ArrayList<Match> matches, boolean withDirection, boolean current) throws IOException{
		ProbabilityTensor tensor = createTensorForOnePlayer(playername, matches, withDirection);
		if(current){
			writePlayerTensor(tensor, "Current/" + playername, withDirection);
		}
		else{
			writePlayerTensor(tensor, "PlayerStats/" + playername, withDirection);
		}
	}
	
	
	public static ProbabilityTensor createTensorForOnePlayerAverageEachOpponent(String playername, ArrayList<Match> matches, boolean withDirection) throws IOException{
		ArrayList<String> opponents = LoadValues.loadNamesOfOpponents(playername, matches);
		ArrayList<ProbabilityTensor> tensor_list = new ArrayList<ProbabilityTensor>();
		for(String opponentName : opponents){
			ArrayList<Match> currentMatches = LoadValues.loadMatchesAgainstSelectedOpponents(playername, opponentName);
			ProbabilityTensor tensor = createTensorForOnePlayer(playername, currentMatches, withDirection);
			//System.out.println(opponentName);
			//OutputTools.printTensor(tensor);
			//System.out.println("--------");
			tensor_list.add(tensor);
		}
		ProbabilityTensor tensor = new ProbabilityTensor(tensor_list);
		return tensor;
	}
	
	public static ProbabilityTensor createTensorForOnePlayer(String playername, ArrayList<Match> matches, boolean withDirection) throws IOException{
		ProbabilityTensor tensor = new ProbabilityTensor(); // Fuer Rally
		ProbabilityForShot[][] currentTensor = tensor.getTensor().getProbForShots()[0];
		boolean takeAll = true;
		if(matches.size() > 0){
			takeAll = false; // Wenn einzelne Matches ausgewaehlt sind, sollen nur die verwendet werden. Sonst alle
		}
		else{
			matches = LoadValues.loadAllMatchesOfPlayer(playername);
		}
		
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for (Match m : matches) {
				for(Point p : m.getPoints()){
					int counter = 1;
					int direction = 0;
					// Fuer Aufschlaege
					if(p.getServer().getName().equals(playername)){
						int side = 0;
						if(p.getSide().equals(Shottypes.adcourt)){
							side = 1;
						}
						// Erster Aufschlag
						direction = p.getFirstserve().getDirection()-1;
						if(direction >= 0 && direction <= 2){
							tensor.getFirstserves()[side][direction].setProbabilityForShot(tensor.getFirstserves()[side][direction].getProbabilityForShot()+1);
							int outcomeIndex = 0;
							int outcomeNextShotIndex = 0;
							if(p.getFirstserve().isAce() || p.getFirstserve().getOutcome().equals(Shottypes.serve_winner) || p.getFirstserve().getOutcome().equals(Shottypes.serve_ace)){
								outcomeIndex = 1;
							}
							if(!p.getFirstserve().isSuccess()){
								outcomeIndex = 2;
							}
							if(p.getShots().size() > 0){
								if(outcomeIndex == 0 && p.getShots().get(0).getOutcome().equals(Shottypes.winner)){
									outcomeNextShotIndex = 1;
								}
								if(outcomeNextShotIndex == 0 && (p.getShots().get(0).getOutcome().equals(Shottypes.UE) || p.getShots().get(0).getOutcome().equals(Shottypes.FE))){
									outcomeNextShotIndex = 2;
								}
							}
							tensor.getFirstserves()[side][direction].getOutcomeProbabilities()[outcomeIndex]++;
							tensor.getFirstserves()[side][direction].getOutcomeOfNextShot()[outcomeNextShotIndex]++;
						}
						// Zweiter Aufschlag
						if(!p.getFirstserve().isSuccess()){
							direction = p.getSecondserve().getDirection()-1;
							if(direction >= 0 && direction <= 2){
								tensor.getSecondserves()[side][direction].setProbabilityForShot(tensor.getSecondserves()[side][direction].getProbabilityForShot()+1);
								int outcomeIndex = 0;
								int outcomeNextShotIndex = 0;
								if(p.getSecondserve().isAce() || p.getSecondserve().getOutcome().equals(Shottypes.serve_winner) || p.getSecondserve().getOutcome().equals(Shottypes.serve_ace)){
									outcomeIndex = 1;
								}
								if(!p.getSecondserve().isSuccess()){
									outcomeIndex = 2;
								}
								if(p.getShots().size() > 0){
									if(outcomeIndex == 0 && p.getShots().get(0).getOutcome().equals(Shottypes.winner)){
										outcomeNextShotIndex = 1;
									}
									if(outcomeNextShotIndex == 0 && (p.getShots().get(0).getOutcome().equals(Shottypes.UE) || p.getShots().get(0).getOutcome().equals(Shottypes.FE))){
										outcomeNextShotIndex = 2;
									}
								}
								tensor.getSecondserves()[side][direction].getOutcomeProbabilities()[outcomeIndex]++;
								tensor.getSecondserves()[side][direction].getOutcomeOfNextShot()[outcomeNextShotIndex]++;
							}
						}
					}
					for(int i = 0; i < p.getShots().size(); i++){
						int hitFrom = 0;
						if(i == 0){
							if(p.getSuccesfullserve().getSide().equals(Shottypes.adcourt)){
								hitFrom = 2;
							}
						}
						if(i > 0){
							hitFrom = p.getShots().get(i-1).getPosition()-1;
						}
						if(!withDirection){
							hitFrom = 0;
						}
						Shot s = p.getShots().get(i);
						// Pruefen, ob der aktuelle Schlag vom richtigen Spieler ausgefuehrt wurde
						// ----
						boolean doNow = false;
						if(p.getServer().getName().equals(playername)){
							if(Math.floorMod(counter, 2) == 0){
								doNow = true;
							}
						}
						if(!p.getServer().getName().equals(playername)){
							if(Math.floorMod(counter, 2) == 1){
								doNow = true;
							}
						}

						if(hitFrom >= 0 && hitFrom <= 2){
						// ----
						// Pruefen, welcher Teil des Tensor bearbeitet werden soll, also ob Rally, 1st Serve + 1 oder 1st Serve return
						currentTensor = tensor.getTensor().getProbForShots()[hitFrom];
						if(i == 0 && p.getFirstserve().isSuccess()){
							currentTensor = tensor.getReturns().getProbForShots()[hitFrom];
						}
						if(i == 1 && p.getFirstserve().isSuccess()){
							currentTensor = tensor.getServeplus1().getProbForShots()[hitFrom];
						}

						if(doNow){
								int position = s.getPosition()-1;
								int directedAt = s.getDirection()-1;
								String outcome = s.getOutcome();
								if(position >= 0 && position <= 2 && directedAt >= 0 && directedAt <= 2){
									currentTensor[position][directedAt].setProbabilityForShot(currentTensor[position][directedAt].getProbabilityForShot()+1);	
									int outcomeIndex = 0;
									int outcomeNextShotIndex = 0;
									if(outcome.equals(Shottypes.winner)){
										outcomeIndex = 1;
									}
									if(outcome.equals(Shottypes.UE) || outcome.equals(Shottypes.FE)){
										outcomeIndex = 2;
									}
									if(i < p.getShots().size() - 1){
										if(p.getShots().get(i+1).getOutcome().equals(Shottypes.winner)){
											outcomeNextShotIndex = 1;
										}
										if(p.getShots().get(i+1).getOutcome().equals(Shottypes.UE) || p.getShots().get(i+1).getOutcome().equals(Shottypes.FE)){
											outcomeNextShotIndex = 2;
										}
									}
									currentTensor[position][directedAt].getOutcomeProbabilities()[outcomeIndex] += 1;
									currentTensor[position][directedAt].getOutcomeOfNextShot()[outcomeNextShotIndex] += 1;
								}
							}
							if(i == 0 && p.getFirstserve().equals(p.getSuccesfullserve())){
								tensor.getReturns().getProbForShots()[hitFrom] = currentTensor;
							}
							else if(i == 1 && p.getFirstserve().equals(p.getSuccesfullserve())){
								tensor.getServeplus1().getProbForShots()[hitFrom] = currentTensor;
							}
							else{
								tensor.getTensor().getProbForShots()[hitFrom] = currentTensor;
							}
						}
					}
						counter++; // Schlaganzahl erhoehen
				}
				//}
			}
		tensor.makeStochastic();
		return tensor;
	}
	
	public static ProbabilityTensor createTensorForOnePlayerForOneMatch(String playername, Match match, boolean withDirection, boolean current) throws IOException{
		ProbabilityTensor tensor = new ProbabilityTensor(); // Fuer Rally
		ProbabilityForShot[][] currentTensor = tensor.getTensor().getProbForShots()[0];

		for(Point p : match.getPoints()){
			int counter = 1;
			int direction = 0;
			// Fuer Aufschlaege
			if(p.getServer().getName().equals(playername)){
				int side = 0;
				if(p.getSide().equals(Shottypes.adcourt)){
					side = 1;
				}
				// Erster Aufschlag
				direction = p.getFirstserve().getDirection()-1;
				if(direction >= 0 && direction <= 2){
					tensor.getFirstserves()[side][direction].setProbabilityForShot(tensor.getFirstserves()[side][direction].getProbabilityForShot()+1);
					int outcomeIndex = 0;
					int outcomeNextShotIndex = 0;
					if(p.getFirstserve().isAce() || p.getFirstserve().getOutcome().equals(Shottypes.serve_winner) || p.getFirstserve().getOutcome().equals(Shottypes.serve_ace)){
						outcomeIndex = 1;
					}
					if(!p.getFirstserve().isSuccess()){
						outcomeIndex = 2;
					}
					if(p.getShots().size() > 0){
						if(outcomeIndex == 0 && p.getShots().get(0).getOutcome().equals(Shottypes.winner)){
							outcomeNextShotIndex = 1;
						}
						if(outcomeNextShotIndex == 0 && (p.getShots().get(0).getOutcome().equals(Shottypes.UE) || p.getShots().get(0).getOutcome().equals(Shottypes.FE))){
							outcomeNextShotIndex = 2;
						}
					}
					tensor.getFirstserves()[side][direction].getOutcomeProbabilities()[outcomeIndex]++;
					tensor.getFirstserves()[side][direction].getOutcomeOfNextShot()[outcomeNextShotIndex]++;
				}
				// Zweiter Aufschlag
				if(!p.getFirstserve().isSuccess()){
					direction = p.getSecondserve().getDirection()-1;
					if(direction >= 0 && direction <= 2){
						tensor.getSecondserves()[side][direction].setProbabilityForShot(tensor.getSecondserves()[side][direction].getProbabilityForShot()+1);
						int outcomeIndex = 0;
						int outcomeNextShotIndex = 0;
						if(p.getSecondserve().isAce() || p.getSecondserve().getOutcome().equals(Shottypes.serve_winner) || p.getSecondserve().getOutcome().equals(Shottypes.serve_ace)){
							outcomeIndex = 1;
						}
						if(!p.getSecondserve().isSuccess()){
							outcomeIndex = 2;
						}
						if(p.getShots().size() > 0){
							if(outcomeIndex == 0 && p.getShots().get(0).getOutcome().equals(Shottypes.winner)){
								outcomeNextShotIndex = 1;
							}
							if(outcomeNextShotIndex == 0 && (p.getShots().get(0).getOutcome().equals(Shottypes.UE) || p.getShots().get(0).getOutcome().equals(Shottypes.FE))){
								outcomeNextShotIndex = 2;
							}
						}
						tensor.getSecondserves()[side][direction].getOutcomeProbabilities()[outcomeIndex]++;
						tensor.getSecondserves()[side][direction].getOutcomeOfNextShot()[outcomeNextShotIndex]++;
					}
				}
			}
			for(int i = 0; i < p.getShots().size(); i++){
				int hitFrom = 0;
				if(i == 0){
					if(p.getSuccesfullserve().getSide().equals(Shottypes.adcourt)){
						hitFrom = 2;
					}
				}
				if(i > 0){
					hitFrom = p.getShots().get(i-1).getPosition()-1;
				}
				if(!withDirection){
					hitFrom = 0;
				}
				Shot s = p.getShots().get(i);
				// Pruefen, ob der aktuelle Schlag vom richtigen Spieler ausgefuehrt wurde
				// ----
				boolean doNow = false;
				if(p.getServer().getName().equals(playername)){
					if(Math.floorMod(counter, 2) == 0){
						doNow = true;
					}
				}
				if(!p.getServer().getName().equals(playername)){
					if(Math.floorMod(counter, 2) == 1){
						doNow = true;
					}
				}

				if(hitFrom >= 0 && hitFrom <= 2){
				// ----
				// Pruefen, welcher Teil des Tensor bearbeitet werden soll, also ob Rally, 1st Serve + 1 oder 1st Serve return
				currentTensor = tensor.getTensor().getProbForShots()[hitFrom];
				if(i == 0 && p.getFirstserve().isSuccess()){
					currentTensor = tensor.getReturns().getProbForShots()[hitFrom];
				}
				if(i == 1 && p.getFirstserve().isSuccess()){
					currentTensor = tensor.getServeplus1().getProbForShots()[hitFrom];
				}

				if(doNow){
						int position = s.getPosition()-1;
						int directedAt = s.getDirection()-1;
						String outcome = s.getOutcome();
						if(position >= 0 && position <= 2 && directedAt >= 0 && directedAt <= 2){
							currentTensor[position][directedAt].setProbabilityForShot(currentTensor[position][directedAt].getProbabilityForShot()+1);	
							int outcomeIndex = 0;
							int outcomeNextShotIndex = 0;
							if(outcome.equals(Shottypes.winner)){
								outcomeIndex = 1;
							}
							if(outcome.equals(Shottypes.UE) || outcome.equals(Shottypes.FE)){
								outcomeIndex = 2;
							}
							if(i < p.getShots().size() - 1){
								if(p.getShots().get(i+1).getOutcome().equals(Shottypes.winner)){
									outcomeNextShotIndex = 1;
								}
								if(p.getShots().get(i+1).getOutcome().equals(Shottypes.UE) || p.getShots().get(i+1).getOutcome().equals(Shottypes.FE)){
									outcomeNextShotIndex = 2;
								}
							}
							currentTensor[position][directedAt].getOutcomeProbabilities()[outcomeIndex] += 1;
							currentTensor[position][directedAt].getOutcomeOfNextShot()[outcomeNextShotIndex] += 1;
						}
					}
					if(i == 0 && p.getFirstserve().equals(p.getSuccesfullserve())){
						tensor.getReturns().getProbForShots()[hitFrom] = currentTensor;
					}
					else if(i == 1 && p.getFirstserve().equals(p.getSuccesfullserve())){
						tensor.getServeplus1().getProbForShots()[hitFrom] = currentTensor;
					}
					else{
						tensor.getTensor().getProbForShots()[hitFrom] = currentTensor;
					}
				}
			}
				counter++; // Schlaganzahl erhoehen
		}
		//}
			
		tensor.makeStochastic();
		return tensor;
	}
	
	
	public static String figurePlayingHand(String playername) throws IOException{
		ArrayList<Match> matches = LoadValues.loadAllMatchesOfPlayer(playername);
		double forehandsInLeftCorner = 0;
		double forehandsInRightCorner = 0;
		double backhandsInLeftCorner = 0;
		double backhandsInRightCorner = 0;
		for(Match m : matches){
			for(Point p : m.getPoints()){
				for(Shot s : p.getShots()){
					if(s.getPlayer().getName().equals(playername)){
						// Aus der rechten Ecke:
						if(s.getPosition() == 1){
							if(s.getShottype().equals(Shottypes.forehand)){
								forehandsInRightCorner++;
							}
							if(s.getShottype().equals(Shottypes.backhand)){
								backhandsInRightCorner++;
							}
						}
						// Aus der linken Ecke
						if(s.getPosition() == 3){
							if(s.getShottype().equals(Shottypes.forehand)){
								forehandsInLeftCorner++;
							}
							if(s.getShottype().equals(Shottypes.backhand)){
								backhandsInLeftCorner++;
							}
						}
					}
				}
			}
		}
		double forehandInRightCornerRatio = forehandsInRightCorner / (forehandsInRightCorner + backhandsInRightCorner);
		double forehandInLeftCornerRatio = forehandsInLeftCorner / (forehandsInLeftCorner + backhandsInLeftCorner);
		if(forehandInRightCornerRatio > forehandInLeftCornerRatio){
			return Shottypes.righthanded;
		}
		else{
			return Shottypes.lefthanded;
		}
	}
	
	public static void writePlayerTensor(ProbabilityTensor tensor, String playerpath, boolean withDirection) throws IOException{
		
		String[] s = playerpath.split("/");
		File playerFolder = new File("C:/Users/Niklas/TennisStatsData/" + s[0]);
		//File playerFolder = new File("C:/Users/Niklas/TennisStatsData/" + playerpath);
		if(!playerFolder.isDirectory()){
			playerFolder.mkdir();
		}
		playerFolder = new File("C:/Users/Niklas/TennisStatsData/" + playerpath);
		playerFolder.mkdir();
		
		File f = new File(playerFolder.getAbsolutePath() + "/transitionProbabilities.csv");
		if(withDirection){
			f = new File(playerFolder.getAbsolutePath() + "/transitionProbabilitiesWithDirection.csv");
		}
		//System.out.println(f.getAbsolutePath());
		FileWriter fw = new FileWriter(f);
		int outcomeLength = tensor.getServeplus1().getProbForShots()[0][0][0].getOutcomeProbabilities().length;// + tensor.getServeplus1().getProbForShots()[0][0][0].getOutcomeOfNextShot().length;
		
		// Shot probabilites schreiben
		int numberRows = 1;
		if(withDirection){
			numberRows = 3;
		}
		for(int dir = 0; dir < numberRows; dir++){
			fw.write("From Position " + dir + "\n");
			fw.write("Transition probability matrix " + "\n");
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					fw.write(tensor.getTensor().getProbForShots()[dir][i][j].getProbabilityForShot() + ",");
				}
				fw.write("\n");
			}
		
			// Outcome probabilites schreiben
			fw.write("Outcome probabilities, by line (position) then column (direction) \n");
			for(int k = 0; k < 3; k++){
				for(int l = 0; l < 3; l++){
					for(int h = 0; h < outcomeLength; h++){
						fw.write(tensor.getTensor().getProbForShots()[dir][k][l].getOutcomeProbabilities()[h] + ",");
					}
					for(int h = 0; h < outcomeLength; h++){
						fw.write(tensor.getTensor().getProbForShots()[dir][k][l].getOutcomeOfNextShot()[h] + ",");
					}
					fw.write("\n");
				}
			}
		
			// Serve+1 Shot probabilites schreiben
			fw.write("First serve+1 Transition probability matrix \n");
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					fw.write(tensor.getServeplus1().getProbForShots()[dir][i][j].getProbabilityForShot() + ",");
				}
				fw.write("\n");
			}
			// Serve+1 Outcome probabilites schreiben
			fw.write("First serve+1 Outcome probabilities, by line (position) then column (direction) \n");
			for(int k = 0; k < 3; k++){
				for(int l = 0; l < 3; l++){
					for(int h = 0; h < outcomeLength; h++){
						fw.write(tensor.getServeplus1().getProbForShots()[dir][k][l].getOutcomeProbabilities()[h] + ",");
					}
					for(int h = 0; h < outcomeLength; h++){
						fw.write(tensor.getServeplus1().getProbForShots()[dir][k][l].getOutcomeOfNextShot()[h] + ",");
					}
					fw.write("\n");
				}
			}
			
			// Return Shot probabilites schreiben
			fw.write("First serve Return Transition probability matrix \n");
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					fw.write(tensor.getReturns().getProbForShots()[dir][i][j].getProbabilityForShot() + ",");
				}
				fw.write("\n");
			}
			// Return Outcome probabilites schreiben
			fw.write("First serve Return Outcome probabilities, by line (position) then column (direction) \n");
			for(int k = 0; k < 3; k++){
				for(int l = 0; l < 3; l++){
					for(int h = 0; h < outcomeLength; h++){
						fw.write(tensor.getReturns().getProbForShots()[dir][k][l].getOutcomeProbabilities()[h] + ",");
					}
					for(int h = 0; h < outcomeLength; h++){
						fw.write(tensor.getReturns().getProbForShots()[dir][k][l].getOutcomeOfNextShot()[h] + ",");
					}
					fw.write("\n");
				}
			}
		}
		//Aufschlag probabilities schreiben
		fw.write("First serve probabilities \n");
		for(int g = 0; g < 2; g++){
			for(int d = 0; d < 3; d++){
				fw.write(tensor.getFirstserves()[g][d].getProbabilityForShot() + ",");
			}
			fw.write("\n");
		}
		// Erster Aufschlag outcome probabilites schreiben
		fw.write("First serve outcome probabilities, by line (side) then columns (direction) \n");
		for(int k = 0; k < 2; k++){
			for(int l = 0; l < 3; l++){
				for(int h = 0; h < outcomeLength; h++){
					fw.write(tensor.getFirstserves()[k][l].getOutcomeProbabilities()[h] + ",");
				}
				for(int h = 0; h < outcomeLength; h++){
					fw.write(tensor.getFirstserves()[k][l].getOutcomeOfNextShot()[h] + ",");
				}
				fw.write("\n");
			}
		}
		
		fw.write("Second serve probabilities \n");
		for(int g = 0; g < 2; g++){
			for(int d = 0; d < 3; d++){
				fw.write(tensor.getSecondserves()[g][d].getProbabilityForShot() + ",");
			}
			fw.write("\n");
		}
		// Zweiter Aufschlag outcome probabilites schreiben
		fw.write("Second serve outcome probabilities, by line (side) then columns (direction) \n");
		for(int k = 0; k < 2; k++){
			for(int l = 0; l < 3; l++){
				for(int h = 0; h < outcomeLength; h++){
					fw.write(tensor.getSecondserves()[k][l].getOutcomeProbabilities()[h] + ",");
				}
				for(int h = 0; h < outcomeLength; h++){
					fw.write(tensor.getSecondserves()[k][l].getOutcomeOfNextShot()[h] + ",");
				}
				fw.write("\n");
			}
		}
		
		fw.close();
	}
	
	public static void writeGeneralPlayerInformation(String playername) throws IOException{
		File playerFolder = new File("C:/Users/Niklas/TennisStatsData/PlayerStats/" + playername);
		
		String hand = figurePlayingHand(playername);
		
		File f = new File(playerFolder.getAbsolutePath() + "/generalInfo.csv");
		//System.out.println(f.getAbsolutePath());
		FileWriter fw = new FileWriter(f);
		fw.write("Name,Hand" + "\n");
		fw.write(playername + "," + hand);
		fw.close();
	}
	
	public static void writeGeneralPlayerInformationAllPlayers() throws IOException{
		ArrayList<String> playernames = LoadValues.loadAllPlayernames();
		for(String s : playernames){
			writeGeneralPlayerInformation(s);
		}
	}
	
}