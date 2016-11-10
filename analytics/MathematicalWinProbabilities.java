package analytics;

import java.io.IOException;

import dataload.LoadValues;

public class MathematicalWinProbabilities {

	
	// Mathematisch errechnete W'keiten auf Spielgewinn von allen Spielstaenden aus unter Annahme der Unabhaengigkeit
	// der Punktgewinn-W'keit vom Spielstand BEI EIGENEM AUFSCHLAG
	public static double[] mathematicalGameWinningPercentages(double p){
		double fromDeuce = Math.pow(p, 2) / (1-2*p*(1-p));
		
		double fromStart = Math.pow(p, 4) + 4*Math.pow(p, 4)*(1-p) + 10*Math.pow(p, 4)*Math.pow(1-p, 2) + 20 * Math.pow(p, 3)*Math.pow(1-p, 3) * fromDeuce;
		double fromFifteenLove = Math.pow(p, 3) + 3*Math.pow(p,3)*(1-p) + 6*Math.pow(p, 3)*Math.pow(1-p, 2) + 10*Math.pow(p, 2)*Math.pow(1-p, 3)*fromDeuce;
		double fromLoveFifteen = Math.pow(p, 4) + 4*Math.pow(p, 4)*(1-p) + 10*Math.pow(p, 3)*Math.pow(1-p, 2)*fromDeuce;
		double fromFifteenAll = Math.pow(p, 3) + 3*Math.pow(p, 3)*(1-p) + 6*Math.pow(p, 2)* Math.pow(1-p, 2)*fromDeuce;
		double fromThirtyLove = Math.pow(p, 2) + 2*Math.pow(p, 2)*(1-p) + 3*Math.pow(p, 2)*Math.pow(1-p, 2) + 4*p*Math.pow(1-p, 3)*fromDeuce;
		double fromLoveThirty = Math.pow(p, 4) + 4*Math.pow(p, 3)*(1-p)*fromDeuce;
		double fromThirtyFifteen = Math.pow(p, 2) + 2*Math.pow(p, 2)*(1-p) + 3*p*Math.pow(1-p, 2)*fromDeuce;
		double fromFifteenThirty = Math.pow(p, 3) + 3*Math.pow(p, 2)*(1-p)*fromDeuce;
		double fromFortyLove = p + p*(1-p) + p*Math.pow(1-p, 2) +  Math.pow(1-p, 3)*fromDeuce;
		double fromLoveForty = Math.pow(p, 3)*fromDeuce;
		double fromThirtyAll = fromDeuce;
		double fromFortyFifteen = p + p*(1-p) + Math.pow(1-p, 2)*fromDeuce;
		double fromFifteenForty = Math.pow(p, 2)*fromDeuce;
		double fromFortyThirty = p + (1-p)*fromDeuce;
		double fromThirtyForty = p*fromDeuce;
		double fromAdvantageServer = fromFortyThirty;
		double fromAdvantageReturner = fromThirtyForty;
		
		double[] probabilities = {fromStart, fromFifteenLove, fromLoveFifteen, fromFifteenAll, fromThirtyLove, fromLoveThirty, fromThirtyFifteen, fromFifteenThirty,
									fromFortyLove, fromLoveForty, fromThirtyAll, fromFortyFifteen, fromFifteenForty, fromFortyThirty, fromThirtyForty, fromDeuce, fromAdvantageServer, fromAdvantageReturner};
	
		return probabilities;
	}

	public static double[] mathematicalGameWinningPercentages(String playername) throws IOException{
		double p = PlayerStandardStats.servicePointsWon(playername, LoadValues.loadAllMatchesOfPlayer(playername));
		return mathematicalGameWinningPercentages(p);
	}
	
	
	// Errechnet die math. Spielgewinn-W'keit fuer den Aufschlaeger. p0 ist die Punktgewinn-W'keit des Aufschlaegers bei Aufschlag rechts, p1 von links
	public static double mathematicalGameWinPercentageWithSide(double p0, double p1) throws NumberFormatException{
		
		double p1_gameWinPercentage = 0;
		
		double fromDeuce = p0*p1 / (1-p0-p1+2*p0*p1);
		double toLove = Math.pow(p0, 2)*Math.pow(p1, 2);
		double to15 = Math.pow(p0, 3)*p1*2*(1-p1) + Math.pow(p0, 2)*Math.pow(p1, 2)*2*(1-p0);
		double to30 = Math.pow(p0, 3)*p1*Math.pow(1-p1, 2) + Math.pow(p1, 3)*p0*3*Math.pow(1-p0, 2) + Math.pow(p0, 2)*Math.pow(p1, 2)*2*(1-p0)*3*(1-p1);
		double overDeuce = ( Math.pow(p0, 3)*Math.pow(1-p1, 3) + Math.pow(p1, 3)*Math.pow(1-p0, 3) + Math.pow(p0,2)*p1*3*Math.pow(1-p1, 2)*3*(1-p0) + Math.pow(p1, 2)*p0*3*(1-p1)*3*Math.pow(1-p0, 2)) * fromDeuce;
		p1_gameWinPercentage = toLove + to15 + to30 + overDeuce;
		return p1_gameWinPercentage;
	}
	
	
	// Errechnet die math. Satzgewinn-W'keit. g0 ist die Spielgewinn-W'keit bei eigenem Aufschlag, g1 bei Return
	public static double mathematicalSetWinningPercentage(double g0, double g1){
		return 0;
	}
	
}
