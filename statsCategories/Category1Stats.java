package statsCategories;

import analytics.StandardStats;
import counts.Match;

public class Category1Stats {

	public static double[] firstReturnWinPercentage(Match m){
		return StandardStats.firstReturnWinPercentage(m);
	}
	public static double[] firstServeWinPercentage(Match m){
		return StandardStats.firstServeWinPercentage(m);
	}
	public static double[] secondReturnWinPercentage(Match m){
		return StandardStats.secondReturnWinPercentage(m);
	}
	public static double[] secondServeWinPercentage(Match m){
		return StandardStats.secondServeWinPercentage(m);
	}
	public static double[] totalReturnWinPercentage(Match m){
		return StandardStats.totalReturnWinPercentage(m);
	}
	public static double[] totalServeWinPercentage(Match m){
		return StandardStats.totalServeWinPercentage(m);
	}
	public static double[] acesPerServicePoint(Match m){
		return StandardStats.acesPerServingPoint(m);
	}
	public static double[] doubleFaultsPerServingPoint(Match m){
		return StandardStats.doubleFaultsPerServingPoint(m);
	}
	public static double[] winnersPerPoint(Match m){
		return StandardStats.winnersPerPoint(m);
	}
	public static double[] uePerPoint(Match m){
		return StandardStats.uePerPoint(m);
	}
	public static double[] pointsWonByPlayerPercentage(Match m){
		return StandardStats.pointsWonByPlayerPercentage(m);
	}
	
}
