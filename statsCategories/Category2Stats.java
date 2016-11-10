package statsCategories;

import analytics.StandardStats;
import analytics.Testing;
import counts.Match;

public class Category2Stats {

	public static double[] servePercentage(Match m){
		return StandardStats.servePercentage(m);
	}
	public static double[] baselineWinPercentage(Match m){
		return Testing.baseLineWinPercentageMatch(m);
	}
	
}
