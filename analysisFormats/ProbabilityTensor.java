package analysisFormats;

import java.util.ArrayList;

import tools.StatsTools;

public class ProbabilityTensor {

	// Ein ProbabilityTensor enthält eine 3x3-Matrix mit Uebergangswahrscheinlichkeiten fuer die Platzbereiche 1,2 und 3. Darueber
	// hinaus enthaelt jeder dieser Eintraege einen 3-dim. Vektor it Wahrscheinlichkeiten fuer Fehler, Winner oder Ball im Spiel
	// aus dieser Position
	
	ProbabilityMatrixFromDirection tensor = new ProbabilityMatrixFromDirection();
	ProbabilityForShot[][] firstserves = new ProbabilityForShot[2][3];
	ProbabilityForShot[][] secondserves = new ProbabilityForShot[2][3];
	ProbabilityMatrixFromDirection serveplus1 = new ProbabilityMatrixFromDirection();
	ProbabilityMatrixFromDirection returns = new ProbabilityMatrixFromDirection();

	
	public ProbabilityTensor(){
		for(int l = 0; l < 3; l++){
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					tensor.setProbForShotsAtOneIndex(l,i,j, new ProbabilityForShot());
					serveplus1.setProbForShotsAtOneIndex(l,i,j, new ProbabilityForShot());
					returns.setProbForShotsAtOneIndex(l,i,j, new ProbabilityForShot());
				}
			}
		}
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 3; j++){
				firstserves[i][j] = new ProbabilityForShot();
				secondserves[i][j] = new ProbabilityForShot();
			}
		}
	}
	
	public ProbabilityTensor(ArrayList<ProbabilityTensor> tensor_list){
		for(int l = 0; l < 3; l++){
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					// Summen ueber alle Tensors fuer Rally...
					double sum = 0;
					double[] sumOutcomes = {0d,0,0};
					double[] sumNextOutcomes = {0d,0,0};
					
					// ... ServePlus1
					double sumSP1 = 0;
					double[] sumOutcomesSP1 = {0d,0,0};
					double[] sumNextOutcomesSP1 = {0d,0,0};
					
					// ... und Return
					double sumR = 0;
					double[] sumOutcomesR = {0d,0,0};
					double[] sumNextOutcomesR = {0d,0,0};
					
					for(int k = 0; k < tensor_list.size(); k++){
						// Summe ueber alle Tensors fuer die Richtung des jeweiligen Schlages
						sum += tensor_list.get(k).getTensor().getProbForShotsAtOneIndex(l, i, j).getProbabilityForShot();
						sumSP1 += tensor_list.get(k).getServeplus1().getProbForShotsAtOneIndex(l, i, j).getProbabilityForShot();
						sumR += tensor_list.get(k).getReturns().getProbForShotsAtOneIndex(l, i, j).getProbabilityForShot();
						// Fuer die verschiedenen Outcomes
						for(int u = 0; u < 3; u++){
							sumOutcomes[u] += tensor_list.get(k).getTensor().getProbForShotsAtOneIndex(l, i, j).getOutcomeProbabilities()[u];
							sumNextOutcomes[u] = tensor_list.get(k).getTensor().getProbForShotsAtOneIndex(l, i, j).getOutcomeOfNextShot()[u];
							sumOutcomesSP1[u] += tensor_list.get(k).getServeplus1().getProbForShotsAtOneIndex(l, i, j).getOutcomeProbabilities()[u];
							sumNextOutcomesSP1[u] = tensor_list.get(k).getServeplus1().getProbForShotsAtOneIndex(l, i, j).getOutcomeOfNextShot()[u];
							sumOutcomesR[u] += tensor_list.get(k).getReturns().getProbForShotsAtOneIndex(l, i, j).getOutcomeProbabilities()[u];
							sumNextOutcomesR[u] = tensor_list.get(k).getReturns().getProbForShotsAtOneIndex(l, i, j).getOutcomeOfNextShot()[u];
						}
					}
					tensor.setProbForShotsAtOneIndex(l, i, j, new ProbabilityForShot(sum, sumOutcomes[0], sumOutcomes[1], sumOutcomes[2], sumNextOutcomes[0], sumNextOutcomes[1], sumNextOutcomes[2]));
					serveplus1.setProbForShotsAtOneIndex(l,i,j, new ProbabilityForShot(sumSP1, sumOutcomesSP1[0], sumOutcomesSP1[1], sumOutcomesSP1[2], sumNextOutcomesSP1[0], sumNextOutcomesSP1[1], sumNextOutcomesSP1[2]));
					returns.setProbForShotsAtOneIndex(l,i,j, new ProbabilityForShot(sumR, sumOutcomesR[0], sumOutcomesR[1], sumOutcomesR[2], sumNextOutcomesR[0], sumNextOutcomesR[1], sumNextOutcomesR[2]));
				}
			}
		}
		
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 3; j++){
				double sumFirstServe = 0;
				double sumSecondServe = 0;
				double[] sumFirstServeOutcomes = new double[3];
				double[] sumSecondServeOutcomes = new double[3];
				double[] sumFirstServeNextOutcomes = new double[3];
				double[] sumSecondServeNextOutcomes = new double[3];
				for(int z = 0; z < tensor_list.size(); z++){
					// Summe ueber alle Tensors fuer die Richtung des jeweiligen Aufschlages
					sumFirstServe += tensor_list.get(z).getFirstserves()[i][j].getProbabilityForShot();
					sumSecondServe += tensor_list.get(z).getSecondserves()[i][j].getProbabilityForShot();
					for(int t = 0; t < 3; t++){
						// Und die Outcomes
						sumFirstServeOutcomes[t] += tensor_list.get(z).getFirstserves()[i][j].getOutcomeProbabilities()[t];
						sumSecondServeOutcomes[t] += tensor_list.get(z).getSecondserves()[i][j].getOutcomeProbabilities()[t];
						sumFirstServeNextOutcomes[t] += tensor_list.get(z).getFirstserves()[i][j].getOutcomeOfNextShot()[t];
						sumSecondServeNextOutcomes[t] += tensor_list.get(z).getSecondserves()[i][j].getOutcomeOfNextShot()[t];
					}
					
				}
				firstserves[i][j] = new ProbabilityForShot(sumFirstServe, sumFirstServeOutcomes[0], sumFirstServeOutcomes[1], sumFirstServeOutcomes[2], sumFirstServeNextOutcomes[0], sumFirstServeNextOutcomes[1], sumFirstServeNextOutcomes[2]);
				secondserves[i][j] = new ProbabilityForShot(sumSecondServe, sumSecondServeOutcomes[0], sumSecondServeOutcomes[1], sumSecondServeOutcomes[2], sumSecondServeNextOutcomes[0], sumSecondServeNextOutcomes[1], sumSecondServeNextOutcomes[2]);
			}
		}
		makeStochastic();
	}
	
	// Matrix stochastisch machen. Außerdem: Outcome Vektor stochastisch machen
	public void makeStochastic(){
		for(int h = 0; h < 3; h++){
			for(int i = 0; i < 3; i++){
				double sum = 0;
				double sum_sp1 = 0;
				double sum_return = 0;
				for(int j = 0; j < 3; j++){
					sum += this.tensor.getProbForShotsAtOneIndex(h,i,j).getProbabilityForShot();
					sum_sp1 += this.serveplus1.getProbForShotsAtOneIndex(h,i,j).getProbabilityForShot();
					sum_return += this.returns.getProbForShotsAtOneIndex(h,i,j).getProbabilityForShot();
				}

				for(int k = 0; k < 3; k++){
					if(sum != 0){
						this.tensor.getProbForShotsAtOneIndex(h, i, k).setProbabilityForShot(this.tensor.getProbForShotsAtOneIndex(h, i, k).getProbabilityForShot() / sum); 
					}
					else{
						this.tensor.getProbForShotsAtOneIndex(h, i, k).setProbabilityForShot(1.0 / (double)this.tensor.getProbForShots()[h][i].length);
					}
					if(sum_sp1 != 0){
						this.serveplus1.getProbForShotsAtOneIndex(h, i, k).setProbabilityForShot(this.serveplus1.getProbForShotsAtOneIndex(h, i, k).getProbabilityForShot() / sum_sp1);
					}
					else{
						this.serveplus1.getProbForShotsAtOneIndex(h, i, k).setProbabilityForShot(1.0 / (double)this.serveplus1.getProbForShots()[h][i].length);
					}
					if(sum_return != 0){
						this.returns.getProbForShotsAtOneIndex(h, i, k).setProbabilityForShot(this.returns.getProbForShotsAtOneIndex(h, i, k).getProbabilityForShot() / sum_return);
					}
					else{
						this.returns.getProbForShotsAtOneIndex(h, i, k).setProbabilityForShot(1 / (double)this.returns.getProbForShots()[h][i].length);
						}
					
					this.tensor.getProbForShotsAtOneIndex(h, i, k).makeOutcomeStochastic();
					this.serveplus1.getProbForShotsAtOneIndex(h, i, k).makeOutcomeStochastic();
					this.returns.getProbForShotsAtOneIndex(h, i, k).makeOutcomeStochastic();
				}
			}
		}

		// Aufschlaege
		// Erster Aufschlag
		for(int i = 0; i < 2; i++){
			double sum = 0;
			for(int j = 0; j < 3; j++){
				sum += this.firstserves[i][j].getProbabilityForShot();
			}
			for(int k = 0; k < 3; k++){
				this.firstserves[i][k].setProbabilityForShot(this.firstserves[i][k].getProbabilityForShot() / sum);
				this.firstserves[i][k].makeOutcomeStochastic();
			}
		}
		// Zweiter Aufschlag
		for(int i = 0; i < 2; i++){
			double sum = 0;
			for(int j = 0; j < 3; j++){
				sum += this.secondserves[i][j].getProbabilityForShot();
			}
			for(int k = 0; k < 3; k++){
				this.secondserves[i][k].setProbabilityForShot(this.secondserves[i][k].getProbabilityForShot() / sum);
				this.secondserves[i][k].makeOutcomeStochastic();
			}
		}
	}
	
	
	// Berechnet die durchschnittliche Standardabweichung der Zeilen der direction-Matrix
	public double standardDeviationOfDirection(){
		double sum = 0;
		for(int i = 0; i < 3; i++){
			double[] probs = new double[3];
			for(int j = 0; j < 3; j++){
				probs[j] = this.getTensor().getProbForShots()[0][i][j].getProbabilityForShot();
			}
			sum += StatsTools.standarddeviation(probs);
		}
		double result = sum / 3;
		return result;
	}
	

	public ProbabilityMatrixFromDirection getTensor() {
		return tensor;
	}

	public void setTensor(ProbabilityMatrixFromDirection tensor) {
		this.tensor = tensor;
	}
	


	public ProbabilityForShot[][] getFirstserves() {
		return firstserves;
	}

	public void setFirstserves(ProbabilityForShot[][] firstserves) {
		this.firstserves = firstserves;
	}

	public ProbabilityForShot[][] getSecondserves() {
		return secondserves;
	}

	public void setSecondserves(ProbabilityForShot[][] secondserves) {
		this.secondserves = secondserves;
	}

	public ProbabilityMatrixFromDirection getServeplus1() {
		return serveplus1;
	}

	public void setServeplus1(ProbabilityMatrixFromDirection serveplus1) {
		this.serveplus1 = serveplus1;
	}

	public ProbabilityMatrixFromDirection getReturns() {
		return returns;
	}

	public void setReturns(ProbabilityMatrixFromDirection returns) {
		this.returns = returns;
	}
}
