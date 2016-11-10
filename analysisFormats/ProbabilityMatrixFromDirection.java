package analysisFormats;

public class ProbabilityMatrixFromDirection {

	ProbabilityForShot[][][] probForShots = new ProbabilityForShot[3][3][3];
	
	public ProbabilityMatrixFromDirection(){
		
	}
	
	public ProbabilityMatrixFromDirection(ProbabilityForShot[][][] probForShots){
		this.probForShots = probForShots;
	}


	public ProbabilityForShot[][][] getProbForShots() {
		return probForShots;
	}

	public void setProbForShots(ProbabilityForShot[][][] probForShots) {
		this.probForShots = probForShots;
	}
	
	public ProbabilityForShot getProbForShotsAtOneIndex(int fromDirection, int position, int directedAt){
		return this.probForShots[fromDirection][position][directedAt];
	}
	
	public void setProbForShotsAtOneIndex(int fromDirection, int position, int directedAt, ProbabilityForShot prob){
		this.probForShots[fromDirection][position][directedAt] = prob;
	}
	
}
