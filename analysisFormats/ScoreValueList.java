package analysisFormats;

import java.util.ArrayList;

import counts.ScoreSituation;

public class ScoreValueList {
	
	ArrayList<Double> values;
	ArrayList<ScoreSituation> situations;
	
	public ScoreValueList(){
		this.values = new ArrayList<Double>();
		this.situations = new ArrayList<ScoreSituation>();
	}
	
	public ScoreValueList(ArrayList<Double> values, ArrayList<ScoreSituation> situations){
		this.values = values;
		this.situations = situations;
	}

	public ArrayList<Double> getValues() {
		return values;
	}

	public void setValues(ArrayList<Double> values) {
		this.values = values;
	}

	public ArrayList<ScoreSituation> getSituations() {
		return situations;
	}

	public void setSituations(ArrayList<ScoreSituation> situations) {
		this.situations = situations;
	}
}
