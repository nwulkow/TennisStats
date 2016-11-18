package archiveLoad;

import java.io.IOException;
import java.util.ArrayList;

import counts.Match;
import dataload.LoadValues;
import tools.OutputTools;
import tools.StatsTools;

public class LoadMatchResults {
	
	// Sind Spieler in mehrere Cluster eingeteilt, wird verglichen, wie die verschiedenen Cluster gegeneinander abschneiden.
	// Fuer jedes Cluster gegen ein anderes wird notiert, wie groﬂ der Anteil der Siege in gemeinsamen Duellen war
	public static double[][] allClustersAgainstEachOther(ArrayList<ArrayList<String>> playersInClusters) throws IOException{
		double[][] M = new double[playersInClusters.size()][playersInClusters.size()];
		for(int i = 0; i < playersInClusters.size(); i++){
			for(int j = i+1; j < playersInClusters.size(); j++){
				double[] clusterComparison = clustersAgainstEachOther(playersInClusters.get(i), playersInClusters.get(j));
				M[i][j] = clusterComparison[0];
				M[j][i] = clusterComparison[1];
			}
		}
		OutputTools.printMatrix(M);
		return M;
	}
	
	// Fuer Spieler, die in zwei Cluster eingeteilt wurden, werden alle Matches von Spielern aus beiden Clustern gegeneinander verglichen.
	// Output: Anteil der Siege von Cluster 1 an allen Matches und Anteil der Siege von Cluster 2
	public static double[] clustersAgainstEachOther(ArrayList<String> cluster1, ArrayList<String> cluster2) throws IOException{
		double[] clusterWinCounter = {0d,0d};
		for(String player1name : cluster1){
			for(String player2name : cluster2){
				ArrayList<Match> matchesAgainstEachOther = LoadValues.loadMatchesAgainstSelectedOpponents(player1name, player2name);
				for(Match m : matchesAgainstEachOther){
					if(m.getWinner().getName().equals(player1name)){
						clusterWinCounter[0]++;
					}
					else if(m.getWinner().getName().equals(player2name)){
						clusterWinCounter[1]++;
					}
				}
			}		
		}
		clusterWinCounter = StatsTools.absToRel(clusterWinCounter);
		return clusterWinCounter;
	}

}
