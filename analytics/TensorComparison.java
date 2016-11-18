package analytics;

import java.io.IOException;
import java.util.ArrayList;

import analysisFormats.ProbabilityForShot;
import analysisFormats.ProbabilityTensor;
import dataload.LoadValues;
import player.Player;
import tools.OutputTools;

public class TensorComparison {
	
	
	// Berechnet die Norm der Uebergangsmatrizen der Tensors zweier Spieler
	public static double compareTensors(ProbabilityTensor tensor1, ProbabilityTensor tensor2){
		
		ProbabilityForShot[][][] t1 = tensor1.getTensor().getProbForShots();
		ProbabilityForShot[][][] t2 = tensor2.getTensor().getProbForShots();
		double sum = 0;
		for(int i = 0; i < t1.length; i++){
			for(int j = 0; j < t1[0].length; j++){
				for(int h = 0; h < t1[0][0].length; h++){
					sum += Math.abs(t1[i][j][h].getProbabilityForShot() - t2[i][j][h].getProbabilityForShot()); // Fuer jeden Schlag der Unterschied in den W'keiten
					for(int k = 0; k < 3; k++){
						//sum += Math.pow(t1[i][j][h].getOutcomeProbabilities()[k]-t2[i][j][h].getOutcomeProbabilities()[k],2); // Fuer jeden Schlag der Unterschied in den W'keiten fuer die verschiedenene Outcomes
						//sum += Math.pow(t1[i][j][h].getOutcomeOfNextShot()[k]-t2[i][j][h].getOutcomeOfNextShot()[k],2);
					}
				}
			}
		}
		return sum;
	}
	
	
	public static double compareTensors(Player p1, Player p2) throws NumberFormatException, IOException{
		p1.loadTensor(false);
		p2.loadTensor(false);
		return compareTensors(p1.getTensor(), p2.getTensor());
	}
	
	public static double[][] createNetworkOfTensorDifferences(ArrayList<String> playernames) throws NumberFormatException, IOException{
		double[][] diffMatrix = new double[playernames.size()][playernames.size()];
		Player p1 = null;
		Player p2 = null;
		
		for(int i = 0; i < playernames.size(); i++){
			
			p1 = new Player(playernames.get(i));
			p1.loadTensor(false);
			System.out.println(playernames.get(i));
			
			for(int j = i+1; j < playernames.size(); j++){
				
				p2 = new Player(playernames.get(j));
				p2.loadTensor(false);
				double currentDifference = compareTensors(p1.getTensor(), p2.getTensor());
				//currentDifference = 1/currentDifference;
				//currentDifference = Math.pow(currentDifference,2);
				//currentDifference = 3 - currentDifference;
				currentDifference = Math.exp(-currentDifference);
				diffMatrix[i][j] = currentDifference;
				diffMatrix[j][i] = currentDifference;
			}
		}
		OutputTools.writeGraphForGellyAPI(diffMatrix, "diffMatrix", false);
		return diffMatrix;
	}
	
	
	
}
