package analytics;

import counts.Match;
import counts.Point;
import counts.Shot;
import objects.Shottypes;

public class BaselineStats {

	public static boolean[] isBaselinePoint(Point p){
		boolean[] bp_point = new boolean[2]; // Erster Eintrag: Ja, wenn Punkt Baselne-Punkt fuer den Returner war. Zweiter Eintrag fuer Aufschlaeger
		bp_point[0] = true;
		bp_point[1] = true;
		if(p.getNoShotsIn() < 2){ // Wenn Return nicht im Feld, kein Baseline-Punkt
			bp_point[0] = false;
			bp_point[1] = false;
			return bp_point;
		}
		for(int i = 0; i < p.getShots().size(); i++){
			Shot s = p.getShots().get(i);
			if(s.isApproach() || s.getShottype().equals(Shottypes.volley) || s.getShottype().equals(Shottypes.smash) || s.getShottype().equals(Shottypes.halfvolley)){
				bp_point[Math.floorMod(i, 2)] = false;
			}
		}
		return bp_point;
	}
	
	
	public static void baselinePointsWon(Match m){
		double[] bp_played_counter = new double[2];
		double[] bp_won_counter = new double[2];
		
		for(Point p : m.getPoints()){
			boolean[] bp_points = isBaselinePoint(p);
			//p.printShots();
			//System.out.println(bp_points[0] + " , " + bp_points[1] + "\n");
			// Wenn fuer Spieler 1 Baseline-Punkt, dann den Zaehler erhoehen
			if((bp_points[0] && p.getReturner().equals(m.getPlayers()[0])) || (bp_points[1] && p.getServer().equals(m.getPlayers()[0]))){
				bp_played_counter[0]++;
				// Wenn gewonnen, dann den Sieg-Zaehler erhoehen
				if(p.getWinner().equals(m.getPlayers()[0])){
					bp_won_counter[0]++;
				}
			}
			// Analog fuer Spieler 2
			if((bp_points[0] && p.getReturner().equals(m.getPlayers()[1])) || (bp_points[1] && p.getServer().equals(m.getPlayers()[1]))){
				bp_played_counter[1]++;
				if(p.getWinner().equals(m.getPlayers()[1])){
					bp_won_counter[1]++;
				}
			}
		}
		double[] bp_won_ratio = new double[2];
		bp_won_ratio[0] = bp_won_counter[0] / bp_played_counter[0];
		bp_won_ratio[1] = bp_won_counter[1] / bp_played_counter[1];
		
		
		System.out.println("Baseline points won, Player 1: " + bp_won_ratio[0] + " , Baseline points won, Player 2: " + bp_won_ratio[1]);
		
	}
	
}
