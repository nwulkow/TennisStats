package statsCategories;

import java.io.IOException;
import java.util.ArrayList;

import analysisFormats.MatchStatList;
import analysisFormats.ProbabilityTensor;
import analytics.Metrics;
import analytics.PatternAnalysis;
import analytics.Testing;
import counts.Match;
import counts.Shot;

public class Category4Stats {
	
	public static MatchStatList countLonglineShots(Match m){
		return Testing.countLongLineShots(m);
	}
	public static MatchStatList pointsWonWithWinners(Match m){
		return Testing.pointsWonWithWinners(m);
	}
	public static MatchStatList V1(Match m){
		return Metrics.V1_MSL(m, false);
	}
	/*public static double[] V2(Match m) throws IOException{
		return Metrics.V2(m, false);
	}*/
	/*public static double[] patternFrequency(Match m, ArrayList<Shot> pattern){
		return PatternAnalysis.patternFrequency(m, pattern);
	}*/
	
	public static MatchStatList allShotsFromTensor(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		MatchStatList msl = new MatchStatList(m);
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				double[] result = { tensors[0].getTensor().getProbForShots()[0][i][j].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][i][j].getProbabilityForShot() };
				msl.getStats().add(result);
			}
		}
		return msl;
	}
	
	/*
	public static double[] oneTo1(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][0][0].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][0][0].getProbabilityForShot() };
		return result;
	}
	public static double[] oneTo2(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][0][1].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][0][1].getProbabilityForShot() };
		return result;
	}
	public static double[] oneTo3(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][0][2].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][0][2].getProbabilityForShot() };
		return result;
	}
	public static double[] twoTo1(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][1][0].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][1][0].getProbabilityForShot() };
		return result;
	}
	public static double[] twoTo2(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][1][1].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][1][1].getProbabilityForShot() };
		return result;
	}
	public static double[] twoTo3(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][1][2].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[2][1][2].getProbabilityForShot() };
		return result;
	}
	public static double[] threeTo1(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][2][0].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][2][0].getProbabilityForShot() };
		return result;
	}
	public static double[] threeTo2(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][2][1].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][2][1].getProbabilityForShot() };
		return result;
	}
	public static double[] threeTo3(Match m) throws IOException{
		ProbabilityTensor[] tensors = PatternAnalysis.shotDirectionMatrix(m);
		double[] result = { tensors[0].getTensor().getProbForShots()[0][2][2].getProbabilityForShot(), tensors[1].getTensor().getProbForShots()[0][2][2].getProbabilityForShot() };
		return result;
	}
	 */
}
