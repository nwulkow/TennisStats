package analytics;

import java.io.IOException;

import Jama.Matrix;
import player.Player;
import tools.Tools;

public class WinProbabilityByTensor {

	
	
	// Berechnet die mathematische Punktgewinn-W'keit nach Tensor. Gibt die W'keit zurueck, dass der Aufschlaeger den Punkt gewinnt
	public static double computePointWinProbabilitiesByTensor(Player p1, Player p2, int side, boolean withDirection) throws NumberFormatException, IOException{
		Player server = p1;
		Player returner = p2;
		//p1.loadCurrentTensor(withDirection);
		//p2.loadCurrentTensor(withDirection);
		double[] rallyStartArray = new double[3];
		double[] winner_prob = new double[2];
		double[] error_prob = new double[2];
		// Erst werden alle Moeglichkeiten fuer die ersten drei Schlaege errechnet. Dann werden diese mit den Rally-Gewinn-W'keiten
		// multipliziert
		for(int i = 0; i < 3; i++){
			double prob_shot1 = server.getTensor().getFirstserves()[side][i].getProbabilityForShot();
			double prob_shot1_inPlay = server.getTensor().getFirstserves()[side][i].getOutcomeProbabilities()[0] * prob_shot1;
			winner_prob[0] += server.getTensor().getFirstserves()[side][i].getOutcomeProbabilities()[1] * prob_shot1;
			// Zweiter Aufschlag:
			double prob_secondServe = prob_shot1 * server.getTensor().getFirstserves()[side][i].getOutcomeProbabilities()[2];
			for(int l = 0; l < 3; l++){
				rallyStartArray[l] +=  prob_secondServe * server.getTensor().getSecondserves()[side][l].getProbabilityForShot() * server.getTensor().getSecondserves()[side][l].getOutcomeProbabilities()[0];
				winner_prob[0] += prob_secondServe * server.getTensor().getSecondserves()[side][l].getProbabilityForShot() * server.getTensor().getSecondserves()[side][l].getOutcomeProbabilities()[1];
				error_prob[0] += prob_secondServe * server.getTensor().getSecondserves()[side][l].getProbabilityForShot() * server.getTensor().getSecondserves()[side][l].getOutcomeProbabilities()[2];
			}
			for(int j = 0; j < 3; j++){
				int hitFrom = side * 2;
				if(!withDirection){
					hitFrom = 0;
				}
				int position = i;
				if(side == 1){
					position = Math.min(i + 1,2);
				}
				else if(side == 0){
					position = Math.max(i - 1,0);
				}
				
				double prob_shot2 = prob_shot1_inPlay * returner.getTensor().getReturns().getProbForShots()[hitFrom][position][j].getProbabilityForShot();
				double prob_shot2_inPlay = returner.getTensor().getReturns().getProbForShots()[hitFrom][position][j].getOutcomeProbabilities()[0] * prob_shot2;
				winner_prob[1] += returner.getTensor().getReturns().getProbForShots()[hitFrom][position][j].getOutcomeProbabilities()[1] * prob_shot2;
				error_prob[1] += returner.getTensor().getReturns().getProbForShots()[hitFrom][position][j].getOutcomeProbabilities()[2] * prob_shot2;
				for(int k = 0; k < 3; k++){
					if(!withDirection){
						position = 0;
					}
					double prob_shot3 = prob_shot2_inPlay * server.getTensor().getServeplus1().getProbForShots()[position][j][k].getProbabilityForShot();
					double prob_shot3_inPlay = server.getTensor().getServeplus1().getProbForShots()[position][j][k].getOutcomeProbabilities()[0] * prob_shot3;
					winner_prob[0] += server.getTensor().getServeplus1().getProbForShots()[position][j][k].getOutcomeProbabilities()[1] * prob_shot3;
					error_prob[0] += server.getTensor().getServeplus1().getProbForShots()[position][j][k].getOutcomeProbabilities()[2] * prob_shot3;
					rallyStartArray[k] += prob_shot3_inPlay;
				}
			}
		}
		// Die W'keiten fuer beide Spieler, den Punkt innerhalb der ersten drei Schlaege zu gewinnen
		double[] pointWin_prob = {winner_prob[0] + error_prob[1], winner_prob[1] + error_prob[0]};
		//Tools.printArray(pointWin_prob);
		//Tools.printArray(rallyStartArray);
		Matrix rallyOutcomePercentages = computeWinProbabilityInRallyByTensor(server, returner);
		double returnerWinsRallyProbability = rallyStartArray[0]*rallyOutcomePercentages.get(0, 0) + rallyStartArray[1]*rallyOutcomePercentages.get(1, 0) + rallyStartArray[2]*rallyOutcomePercentages.get(2, 0);
		double serverWinsRallyProbability = rallyStartArray[0]*(1-rallyOutcomePercentages.get(0, 0)) + rallyStartArray[1]*(1-rallyOutcomePercentages.get(1, 0)) + rallyStartArray[2]*(1-rallyOutcomePercentages.get(2, 0));
		// Die Sieg-W'keiten in einer Rally werden zu den Punktgewinn-W'keiten addiert
		pointWin_prob[0] += serverWinsRallyProbability;
		pointWin_prob[1] += returnerWinsRallyProbability;
		//Tools.printArray(pointWin_prob);
		return pointWin_prob[0];
	}
	
	
	// Berechnet anhand des Rally-Tensors die W'keiten fuer Punktgewinn bei: [P2 schleagt zu Position 0, --- zu 1, ... zu 2, P2 schlaegt von Position 0, ... von 1, ... von 2]
	public static Matrix computeWinProbabilityInRallyByTensor(Player p1, Player p2) throws NumberFormatException, IOException{
		
		p1.loadTensor(false);
		p2.loadTensor(false);
		
		double[] b_array = rightHandSide(p1, p2);
		double[][] b_asMatrix = {b_array};
		Matrix b = new Matrix(b_asMatrix);
		
		double[][] M_array = coefficientMatrix(p1, p2);
		Matrix M = new Matrix(M_array);
		
		Matrix x = M.inverse().times(b.transpose());
		
		//Tools.printMatrix(x.getArray());
		
		return x;
	}
	
	public static double[] rightHandSide(Player p1, Player p2){
		double[] b = new double[6];
		for(int i = 0; i < 6; i++){
			double s = 0;
			for(int j = 0; j < 3; j++){
				if(i < 3){
					s += p1.getTensor().getTensor().getProbForShots()[0][i][j].getProbabilityForShot() * p1.getTensor().getTensor().getProbForShots()[0][i][j].getOutcomeProbabilities()[2];
				}
				else{
					s += p2.getTensor().getTensor().getProbForShots()[0][i-3][j].getProbabilityForShot() * p2.getTensor().getTensor().getProbForShots()[0][i-3][j].getOutcomeProbabilities()[1];
				}
			}
			b[i] = s;
		}
		return b;
	}
	
	public static double[][] coefficientMatrix(Player p1, Player p2){
		double[][] M = new double[6][6];
		
		for(int i = 0; i < 6; i++){
			M[i][i] = 1;
			for(int j = 0; j < 3; j++){
				if(i < 3){
					M[i][j+3] = -p1.getTensor().getTensor().getProbForShots()[0][i][j].getProbabilityForShot() * p1.getTensor().getTensor().getProbForShots()[0][i][j].getOutcomeProbabilities()[0];
				}
				else{
					M[i][j] = -p2.getTensor().getTensor().getProbForShots()[0][i-3][j].getProbabilityForShot() * p2.getTensor().getTensor().getProbForShots()[0][i-3][j].getOutcomeProbabilities()[0];
				}
			}
		}
		return M;
	}
	
	

	
	public static double mathematicalGameWinPercentageByTensor(Player player1, Player player2, boolean withDirection) throws NumberFormatException, IOException{
		
		double p0 = computePointWinProbabilitiesByTensor(player1, player2,0,withDirection); // Punktgewin-W'keit des Aufchlaegers vom Deuce-Court 
		double p1 = computePointWinProbabilitiesByTensor(player1, player2,1,withDirection); // Punktgewin-W'keit des Aufchlaegers vom Ad-Court 
		return MathematicalWinProbabilities.mathematicalGameWinPercentageWithSide(p0, p1);
	}
	
	
}
