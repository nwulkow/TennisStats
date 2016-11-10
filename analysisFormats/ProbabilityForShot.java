package analysisFormats;

public class ProbabilityForShot {

	// Eine Instanz dieser Klasse besteht aus: Der Wahrscheinlichkeit fuer den Uebergang von einem Platzbereich zu einem anderen
	// (double) und einem 5-dim. Vektor mit den Wahrscheinlichkeiten fuer Ball im Spiel, Winner, Fehler, Fehler des Gegners provoziert, Winner des Gegners provoziert.

	double probabilityForShot = 0;
	double[] outcomeProbabilities = new double[3];
	double[] outcomeOfNextShot = new double[3];
	
	public ProbabilityForShot(){
		this.probabilityForShot = 0;
	}
	
	public ProbabilityForShot(double probabilityForShot){
		this.probabilityForShot = probabilityForShot;
	}
	
	public ProbabilityForShot(double probabilityForShot, double probabilityForInPlay, double probabilityForWinner, double probabilityForError, double probabilityForNextShotInPlay, double probabilityForWinnerAllowed, double probabilityForErrorForced){
		this.probabilityForShot = probabilityForShot;
		this.outcomeProbabilities[0] = probabilityForInPlay;
		this.outcomeProbabilities[1] = probabilityForWinner;
		this.outcomeProbabilities[2] = probabilityForError;
		this.outcomeOfNextShot[0] = probabilityForNextShotInPlay;
		this.outcomeOfNextShot[1] = probabilityForWinnerAllowed;
		this.outcomeOfNextShot[2] = probabilityForErrorForced;
	}

	
	public void makeOutcomeStochastic(){
		double sum = 0;
		double sum_nextShot = 0;
		for(int i = 0; i < outcomeProbabilities.length; i++){
			sum += outcomeProbabilities[i];
			sum_nextShot += outcomeOfNextShot[i];
		}
		for(int j = 0; j < outcomeProbabilities.length; j++){
			outcomeProbabilities[j] = outcomeProbabilities[j] / sum;
			outcomeOfNextShot[j] = outcomeOfNextShot[j] / sum_nextShot;
		}
		if(sum == 0 || Double.isNaN(sum)){
			for(int j = 0; j < outcomeProbabilities.length; j++){
				outcomeProbabilities[j] = 1 / (double)outcomeProbabilities.length;
			}
		}
		if(sum_nextShot == 0 || Double.isNaN(sum_nextShot)){
			for(int j = 0; j < outcomeProbabilities.length; j++){
				outcomeOfNextShot[j] = 1 / (double)outcomeOfNextShot.length;
			}
		}
	}
	
	public double getProbabilityForShot() {
		return probabilityForShot;
	}

	public void setProbabilityForShot(double probabilityForShot) {
		this.probabilityForShot = probabilityForShot;
	}

	public double[] getOutcomeProbabilities() {
		return outcomeProbabilities;
	}

	public void setOutcomeProbabilities(double[] outcomeProbabilities) {
		this.outcomeProbabilities = outcomeProbabilities;
	}

	public double[] getOutcomeOfNextShot() {
		return outcomeOfNextShot;
	}

	public void setOutcomeOfNextShot(double[] outcomeOfNextShot) {
		this.outcomeOfNextShot = outcomeOfNextShot;
	}
	
}
