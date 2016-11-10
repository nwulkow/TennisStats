package analytics;

import java.io.IOException;
import java.util.ArrayList;

import counts.Match;
import counts.Point;
import counts.Shot;
import dataload.LoadValues;
import graphics.LineChartDemo6;
import graphics.PlotTools;
import objects.Shottypes;
import player.Player;
import tools.ArrayTools;
import tools.OutputTools;
import tools.StatsTools;

public class ApproachStats {

	
	public static boolean[] isApproachPoint(Point p){
		boolean[] approach_point = new boolean[2]; // Erster Eintrag: Ja, wenn Punkt Approach-Punkt fuer den Returner war. Zweiter Eintrag fuer Aufschlaeger
		approach_point[0] = false;
		approach_point[1] = false;

		for(int i = 0; i < p.getShots().size(); i++){
			Shot s = p.getShots().get(i);
			if(s.isApproach() || s.getShottype().equals(Shottypes.volley) || s.getShottype().equals(Shottypes.smash) || s.getShottype().equals(Shottypes.halfvolley)){
				approach_point[Math.floorMod(i, 2)] = true;
			}
		}
		return approach_point;
	}
	
	public static void approachPointsWon(Match m){
		double[] approach_played_counter = new double[2];
		double[] approach_won_counter = new double[2];
		
		for(Point p : m.getPoints()){
			boolean[] approach_points = isApproachPoint(p);
			// Wenn fuer Spieler 1 Approach-Punkt, dann den Zaehler erhoehen
			if((approach_points[0] && p.getReturner().equals(m.getPlayers()[0])) || (approach_points[1] && p.getServer().equals(m.getPlayers()[0]))){
				approach_played_counter[0]++;
				// Wenn gewonnen, dann den Sieg-Zaehler erhoehen
				if(p.getWinner().equals(m.getPlayers()[0])){
					approach_won_counter[0]++;
				}
			}
			// Analog fuer Spieler 2
			if((approach_points[0] && p.getReturner().equals(m.getPlayers()[1])) || (approach_points[1] && p.getServer().equals(m.getPlayers()[1]))){
				approach_played_counter[1]++;
				p.printShots();
				System.out.println(p.getServer().getName() + " , " + approach_points[0] + " , " + approach_points[1]);
				if(p.getWinner().equals(m.getPlayers()[1])){
					approach_won_counter[1]++;
				}
			}
		}
		double[] approach_won_ratio = new double[2];
		approach_won_ratio[0] = approach_won_counter[0] / approach_played_counter[0];
		approach_won_ratio[1] = approach_won_counter[1] / approach_played_counter[1];

		System.out.println("Approach points player, Player 1: " + approach_played_counter[0] + " , Approach points won, Player 2: " + approach_played_counter[1]);
		System.out.println("Approach points won total, Player 1: " + approach_won_counter[0] + " , Approach points won total, Player 2: " + approach_won_counter[1]);
		System.out.println("Approach points won, Player 1: " + approach_won_ratio[0] + " , Approach points won, Player 2: " + approach_won_ratio[1]);	
	}
	
	
	
	// Jetzt wird analysiert, ob die relative Haeufigkeit an Punktgewinn bei Netzangriff auf die Rueckhand sich aendert, wenn einige
	// Angriffe zuvor auch alle auf die Rueckhand gingen
	public static void approachToSameSideTest(ArrayList<String> playernames) throws IOException{

		//ArrayList<Match> matches = LoadValues.loadAllMatchesOfPlayer(playername);
		ArrayList<Match> matches = LoadValues.loadMatchesForPlayerlist(playernames);
		ArrayList<Double> forehandAttackedCounter = new ArrayList<Double>();
		ArrayList<Double> forehandWinCounter = new ArrayList<Double>();
		ArrayList<Double> backhandAttackedCounter = new ArrayList<Double>();
		ArrayList<Double> backhandWinCounter = new ArrayList<Double>();
		for(Match m : matches){
			int[] backhandcounter = {0,0};
			int[] forehandcounter = {0,0};
			for(Point p : m.getPoints()){
				for(Shot s : p.getShots()){
					
					boolean doPoint = false;
					Player hitter = s.getPlayer();
					if(playernames.size() == 0 || playernames.contains(hitter.getName())){
						doPoint = true;
					}
					
					if(doPoint){
						Player reciever = null;
						int playerindex = 0;
						if(hitter.getName().equals(m.getPlayers()[0].getName())){
							reciever = m.getPlayers()[1];
							playerindex = 0;
						}
						else{
							reciever = m.getPlayers()[0];
							playerindex = 1;
						}
						if(s.isApproach()){
							// Bei Angriff auf die Vorhand:
							if( (s.getDirection() == 1 && reciever.getHand().equals(Shottypes.righthanded)) || (s.getDirection() == 3 && reciever.getHand().equals(Shottypes.lefthanded))){
								if(forehandWinCounter.size()-1 < forehandcounter[playerindex]){
									forehandAttackedCounter.add(0d);
									forehandWinCounter.add(0d);
								}
								forehandAttackedCounter.set(forehandcounter[playerindex], forehandAttackedCounter.get(forehandcounter[playerindex])+1);
								if(p.getWinner().getName().equals(hitter.getName())){
									forehandWinCounter.set(forehandcounter[playerindex], forehandWinCounter.get(forehandcounter[playerindex])+1);
								}
								forehandcounter[playerindex]++;
								backhandcounter[playerindex] = 0;
								System.out.println(forehandcounter[playerindex] + " , " + backhandcounter[playerindex]);
							}
							// Bei Angriff auf die Rueckhand:
							if( (s.getDirection() == 3 && reciever.getHand().equals(Shottypes.righthanded)) || (s.getDirection() == 1 && reciever.getHand().equals(Shottypes.lefthanded))){
								if(backhandWinCounter.size()-1 < backhandcounter[playerindex]){
									backhandAttackedCounter.add(0d);
									backhandWinCounter.add(0d);
								}
								backhandAttackedCounter.set(backhandcounter[playerindex], backhandAttackedCounter.get(backhandcounter[playerindex])+1);
								if(p.getWinner().getName().equals(hitter.getName())){
									backhandWinCounter.set(backhandcounter[playerindex], backhandWinCounter.get(backhandcounter[playerindex])+1);
								}
								backhandcounter[playerindex]++;
								forehandcounter[playerindex] = 0;
								System.out.println(forehandcounter[playerindex] + " , " + backhandcounter[playerindex]);
							}
							break;
						}
					}
				}
			}
		}
		
		int chosenLength = Math.min( Math.min(20,backhandWinCounter.size()), forehandWinCounter.size()); // Laenge des Ergebnis-Arrays
		double[] forehandWinRatio = new double[chosenLength];
		double[] consecutiveToForehandRatio = new double[chosenLength];
		consecutiveToForehandRatio[0] = forehandAttackedCounter.get(0) / (backhandAttackedCounter.get(0) + forehandAttackedCounter.get(0));
		for(int i = 0; i < chosenLength; i++){
			forehandWinRatio[i] = forehandWinCounter.get(i) / forehandAttackedCounter.get(i);
			if(i < chosenLength - 1){
				consecutiveToForehandRatio[i+1] = forehandAttackedCounter.get(i+1) / forehandAttackedCounter.get(i);
			}
		}
		double[] backhandWinRatio = new double[chosenLength];
		double[] consecutiveToBackhandRatio = new double[chosenLength];
		consecutiveToBackhandRatio[0] = backhandAttackedCounter.get(0) / (backhandAttackedCounter.get(0) + forehandAttackedCounter.get(0));
		for(int i = 0; i < chosenLength ; i++){
			backhandWinRatio[i] = backhandWinCounter.get(i) / backhandAttackedCounter.get(i);
			if(i < chosenLength - 1){
				consecutiveToBackhandRatio[i+1] = backhandAttackedCounter.get(i+1) / backhandAttackedCounter.get(i);
			}
		}
		OutputTools.printArray(forehandWinRatio);
		OutputTools.printArray(backhandWinRatio);

		// forehandWinRatio: Der i-te Eintrag gibt die Punktsiegquote aller Netzangriffe auf die Vorhand an, wenn die letzten i Angriffe auch auf die Vorhand gingen
		
		LineChartDemo6 winlcd6 = new LineChartDemo6("Approaches Won After Consecutive Approaches to Same Side",forehandWinRatio, "Consecutive To Forehand Win %");
		winlcd6.addToDataset(backhandWinRatio, "Consecutive To Backhand Win %");
		
		
		// consecutiveToForehandRatio: Der i-te Eintrag gibt an, wie oft der naechste Angriff auch auf die Vorhand ging, wenn die letzten i Angriff auch auf die Vorhand gingen
		OutputTools.printArray(consecutiveToForehandRatio);
		OutputTools.printArray(consecutiveToBackhandRatio);
		LineChartDemo6 shotlcd6 = new LineChartDemo6("Approches Hit After Consecutive Approaches to Same side",consecutiveToForehandRatio, "Consecutive To Forehand %");
		shotlcd6.addToDataset(consecutiveToBackhandRatio, "Consecutive To Backhand %");
		System.out.println(forehandAttackedCounter);
		System.out.println(backhandAttackedCounter);
		
		OutputTools.printArray(ArrayTools.arrayFrom1ToN(consecutiveToBackhandRatio.length, 0));
		System.out.println(StatsTools.correlationcoefficient(ArrayTools.arrayFrom1ToN(consecutiveToBackhandRatio.length, 0), consecutiveToBackhandRatio));
		System.out.println(StatsTools.correlationcoefficient(ArrayTools.arrayFrom1ToN(consecutiveToForehandRatio.length, 0), consecutiveToForehandRatio));
		//PlotTools.plot(backhandWinRatio,"Approach to Backhand");
		double[] testdouble = {1,1,1.1,1,1};
		System.out.println(StatsTools.correlationcoefficient(ArrayTools.arrayFrom1ToN(5, 0), testdouble));
	}
	
	
	
	public static void approachSideDistribution() throws IOException{
		ArrayList<Match> matches = LoadValues.loadMatchesFromPaths(new ArrayList<String>());
		double[] sides = new double[3];
		for(Match m : matches){
			for(Point p : m.getPoints()){
				for(Shot s : p.getShots()){
					if(s.isApproach()){
						if(s.getDirection() > 0 && s.getDirection() < 4){
							sides[s.getDirection()-1]++;
						}
					}
				}
			}	
		}
		double[] sides_rel = StatsTools.absToRel(sides);
		OutputTools.printArray(sides_rel);
	}
	
	
	// Zaehlt, wie oft Netzangriffe auf die Rueckhand gemacht wurden, wenn vorher x % aller Netzangriffe auf die Rueckhand gingen
	public static void approachToSameSideRatioTest(ArrayList<String> playernames) throws IOException{

		//ArrayList<Match> matches = LoadValues.loadAllMatchesOfPlayer(playername);
		ArrayList<Match> matches = LoadValues.loadMatchesForPlayerlist(playernames);
		int numBoxes = 6;
		ArrayList<Double> forehandAttackedCounter = new ArrayList<Double>(numBoxes);
		ArrayList<Double> forehandWinCounter = new ArrayList<Double>(numBoxes);
		ArrayList<Double> backhandAttackedCounter = new ArrayList<Double>(numBoxes);
		ArrayList<Double> backhandWinCounter = new ArrayList<Double>(numBoxes);
		ArrayList<Double> approachesMadeCounter = new ArrayList<Double>(numBoxes);
		for(int i = 0; i < numBoxes; i++){
			forehandAttackedCounter.add(0d);
			forehandWinCounter.add(0d);
			backhandAttackedCounter.add(0d);
			backhandWinCounter.add(0d);
		}
		for(Match m : matches){
			int[] backhandcounter = {0,0};
			int[] forehandcounter = {0,0};
			for(Point p : m.getPoints()){
				for(Shot s : p.getShots()){
					
					boolean doPoint = false;
					Player hitter = s.getPlayer();
					if(playernames.size() == 0 || playernames.contains(hitter.getName())){
						doPoint = true;
					}
					if(doPoint){
						Player reciever = null;
						int playerindex = 0;
						if(hitter.getName().equals(m.getPlayers()[0].getName())){
							reciever = m.getPlayers()[1];
							playerindex = 0;
						}
						else{
							reciever = m.getPlayers()[0];
							playerindex = 1;
						}
						if(s.isApproach() && s.getDirection() != 2){
							// Anteil der Angriffe auf die Rueckhand:
							int box = 0;
							if(forehandcounter[playerindex] + backhandcounter[playerindex] >= 5){
								box = (numBoxes-1) * backhandcounter[playerindex] / (forehandcounter[playerindex] + backhandcounter[playerindex]);
							}
							//approachesMadeCounter.set(box, approachesMadeCounter.get(box)+1);
							// Bei Angriff auf die Vorhand:
							if( (s.getDirection() == 1 && reciever.getHand().equals(Shottypes.righthanded)) || (s.getDirection() == 3 && reciever.getHand().equals(Shottypes.lefthanded))){
								forehandAttackedCounter.set(box, forehandAttackedCounter.get(box)+1);
								if(p.getWinner().getName().equals(hitter.getName())){
									forehandWinCounter.set(box, forehandWinCounter.get(box)+1);
								}
								forehandcounter[playerindex]++;
							}
							// Bei Angriff auf die Rueckhand:
							if( (s.getDirection() == 3 && reciever.getHand().equals(Shottypes.righthanded)) || (s.getDirection() == 1 && reciever.getHand().equals(Shottypes.lefthanded))){
								backhandAttackedCounter.set(box, backhandAttackedCounter.get(box)+1);
								if(p.getWinner().getName().equals(hitter.getName())){
									backhandWinCounter.set(box, backhandWinCounter.get(box)+1);
								}
								backhandcounter[playerindex]++;
							}
							break;
						}
					}
				}
			}
		}
		
		double[] toForehandRatio = new double[numBoxes];
		double[] toForehandWinRatio = new double[numBoxes];
		double[] toBackhandRatio = new double[numBoxes];
		double[] toBackhandWinRatio = new double[numBoxes];
		for(int j = 0; j < numBoxes; j++){
			toForehandRatio[j] = forehandAttackedCounter.get(j) / (backhandAttackedCounter.get(j) + forehandAttackedCounter.get(j));
			toForehandWinRatio[j] = forehandWinCounter.get(j) / forehandAttackedCounter.get(j);
			toBackhandRatio[j] = backhandAttackedCounter.get(j) / (backhandAttackedCounter.get(j) + forehandAttackedCounter.get(j));
			toBackhandWinRatio[j] = backhandWinCounter.get(j) / backhandAttackedCounter.get(j);
		}
		
		double[] boxesArray = ArrayTools.arrayFrom1ToN(numBoxes, 0);
		boxesArray = ArrayTools.multiplyArrayScalar(boxesArray, 1/(double)(numBoxes-1));
		//Tools.printArray(boxesArray);
		//System.out.println(backhandAttackedCounter);
		
		//Tools.printArray(toBackhandRatio);
		//Tools.printArray(toBackhandWinRatio);
		
		// forehandWinRatio: Der i-te Eintrag gibt die Punktsiegquote aller Netzangriffe auf die Vorhand an, wenn i / numBoxes * 100% aller Angriffe auf die Rueckhand gingen
		
		LineChartDemo6 winlcd6 = new LineChartDemo6("Approaches Won After x % of all Approaches Went to the Backhand", boxesArray,toBackhandWinRatio, "Approach to Backhand Won");
		winlcd6.addToDataset(boxesArray,toForehandWinRatio,"Approach to Forehand Won");
		
		LineChartDemo6 hitlcd6 = new LineChartDemo6("Approaches Hit When x % of All Approaches Went to the Backhand", boxesArray ,toBackhandRatio, "Approach to Backhand");
		hitlcd6.addToDataset(boxesArray,toForehandRatio, "Approach to Forehand");
		
	/*	toForehandWinRatio = ArrayTools.sliceArrayIndices(toForehandWinRatio, 3, numBoxes-1);
		toBackhandWinRatio = ArrayTools.sliceArrayIndices(toBackhandWinRatio, 3, numBoxes-1);
		toForehandRatio = ArrayTools.sliceArrayIndices(toForehandRatio, 3, numBoxes-1);
		toBackhandRatio = ArrayTools.sliceArrayIndices(toBackhandRatio, 3, numBoxes-1);
		boxesArray = ArrayTools.sliceArrayIndices(boxesArray, 3, numBoxes-1);*/
		OutputTools.printArray(toBackhandWinRatio);
		OutputTools.printArray(boxesArray);
		
		System.out.println("Forehand win correlation = " + StatsTools.correlationcoefficient(boxesArray, toForehandWinRatio));
		System.out.println("Backhand win correlation = " + StatsTools.correlationcoefficient(boxesArray, toBackhandWinRatio));
		System.out.println("Forehand correlation = " + StatsTools.correlationcoefficient(boxesArray, toForehandRatio));
		System.out.println("Backhand correlation = " + StatsTools.correlationcoefficient(boxesArray, toBackhandRatio));
		
	}
}
