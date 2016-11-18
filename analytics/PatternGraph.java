package analytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import counts.Match;
import counts.Shot;
import dataload.LoadMatchFromTennisAbstract;
import objects.Shottypes;
import tools.ArrayTools;
import tools.OutputTools;
import tools.StatsTools;

public class PatternGraph {

	public static ArrayList<Shot> buildShotList() {
		ArrayList<Shot> shots = new ArrayList<Shot>();
		for (int i = 0; i < Shottypes.groundstroketypes.size(); i++) {
			for (int j = 0; j < Shottypes.positions.size(); j++) {
				Shot newshot = new Shot();
				newshot.setShottype(Shottypes.groundstroketypes.get(i));
				newshot.setPosition(Shottypes.positions.get(j));
			}
		}
		return shots;
	}

	public static void createStatisticsGraph(String playername) throws IOException {

		ArrayList<ArrayList<Double>> lists = new ArrayList<ArrayList<Double>>();
		int categories = 5;
		for (int k = 0; k < categories; k++) {
			lists.add(new ArrayList<Double>());
		}
		// 0: doublefaults, 1: firstservepercentage, 2: firstservewinpercentage,
		// 3: winners per point, 4: unforced errors per point

		// ArrayList<Double> doublefaults = new ArrayList<Double>();
		// ArrayList<Double> firstservepercentage = new ArrayList<Double>();
		// ArrayList<Double> firstservewinpercentage = new ArrayList<Double>();
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for (File file : folder.listFiles()) {
			String name = file.getName();
			String[] names_split = name.replace(".csv", "").split("-");
			String firstname = names_split[0];
			String secondname = names_split[1];
			if (file.getName().contains(playername) && !Shottypes.playersWhereErrorsHappen.contains(firstname)
					&& !Shottypes.playersWhereErrorsHappen.contains(secondname)) {
				Match m = LoadMatchFromTennisAbstract.readCSVFile(file);
				double[] doublefaults_current = StandardStats.numberOfDoubleFaults(m);
				double[] firstservepercentage_current = StandardStats.servePercentage(m);
				double[] firstservewinpercentage_current = StandardStats.firstServeWinPercentage(m);
				double[] winners_current = StandardStats.numberOfWinners(m);
				double[] unforcederros_current = StandardStats.numberOfUE(m);
				if (m.getPlayers()[0].getName().equals(playername)) {
					lists.get(0).add(doublefaults_current[0]);
					lists.get(1).add(firstservepercentage_current[0]);
					lists.get(2).add(firstservewinpercentage_current[0]);
					lists.get(3).add(winners_current[0] / m.getPoints().size());
					lists.get(4).add(unforcederros_current[0] / m.getPoints().size());
				} else {
					lists.get(0).add(doublefaults_current[1]);
					lists.get(1).add(firstservepercentage_current[1]);
					lists.get(2).add(firstservewinpercentage_current[1]);
					lists.get(3).add(winners_current[1] / m.getPoints().size());
					lists.get(4).add(unforcederros_current[1] / m.getPoints().size());
				}
			}
		}

		double[][] correlations = new double[lists.size()][lists.size()];

		for (int i = 0; i < lists.size(); i++) {
			for (int j = 0; j < lists.size(); j++) {
				correlations[i][j] = StatsTools.correlationcoefficient(ArrayTools.ArrayListToArray(lists.get(i)),
						ArrayTools.ArrayListToArray(lists.get(j)));
				correlations[j][i] = StatsTools.correlationcoefficient(ArrayTools.ArrayListToArray(lists.get(j)),
						ArrayTools.ArrayListToArray(lists.get(i)));
			}
		}
		OutputTools.printMatrix(correlations);
		OutputTools.writeGraphForGellyAPI(correlations, "correlations", false);

	}

	
	
	
}
