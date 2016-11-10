package analytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import counts.Match;
import counts.Point;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import objects.Shottypes;
import tools.StatsTools;

public class ServingStats {

	public static ArrayList<double[]> ServePlacement(String playername, int servenumber, boolean onlyMadeServes)
			throws IOException {

		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		double[] placements_deuce = new double[3]; // position 1: wide on deuce
													// court, position 3: t on
													// deuce court
		double[] placements_ad = new double[3]; // Serve placements. position 3:
												// T on ad court, position 1:
												// wide from ad court

		for (File file : folder.listFiles()) {
			if (file.getName().contains(playername)) {
				Match m = LoadMatchFromTennisAbstract.readCSVFile(file);
				for (Point p : m.getPoints()) {
					if (p.getServer().getName().equals(playername)) {
						if (servenumber == 0) {
							if (p.getSuccesfullserve().getDirection() >= 1
									&& p.getSuccesfullserve().getDirection() <= 3) {
								if (p.getSide().equals(Shottypes.deucecourt)) {
									placements_deuce[p.getSuccesfullserve().getDirection() - 1]++;
								} else {
									placements_ad[p.getSuccesfullserve().getDirection() - 1]++;
								}
							}
						}
						// Nur erster Aufschlag
						else if (servenumber == 1) {
							boolean countBool = true; // Wenn er nur Aufchlaege
														// zaehlen soll, die im
														// Feld sind,
														// ueberpruefe, ob der
														// aktuelle Aufschlag im
														// Feld war
							if (onlyMadeServes) {
								if (!p.getFirstserve().isSuccess()) {
									countBool = false;
								}
							}
							if (p.getFirstserve().getDirection() >= 1 && p.getFirstserve().getDirection() <= 3
									&& countBool) {
								if (p.getSide().equals(Shottypes.deucecourt)) {
									placements_deuce[p.getFirstserve().getDirection() - 1]++;
								} else {
									placements_ad[p.getFirstserve().getDirection() - 1]++;
								}
							}
						}
						// Nur zweiter Aufschlag
						else if (servenumber == 2) {
							boolean countBool = true;
							if (onlyMadeServes) {
								if (!p.getSecondserve().isSuccess()) {
									countBool = false;
								}
							}
							if (p.getSecondserve().getDirection() >= 1 && p.getSecondserve().getDirection() <= 3) {
								if (p.getSide().equals(Shottypes.deucecourt)) {
									placements_deuce[p.getSecondserve().getDirection() - 1]++;
								} else {
									placements_ad[p.getSecondserve().getDirection() - 1]++;
								}
							}
						}
					}
				}
			}
		}
		ArrayList<double[]> placements = new ArrayList<double[]>();
		placements.add(placements_deuce);
		placements.add(placements_ad);
		double[] placements_deuce_rel = StatsTools.absToRel(placements_deuce);
		double[] placements_ad_rel = StatsTools.absToRel(placements_ad);
		ArrayList<double[]> placements_rel = new ArrayList<double[]>();
		placements_rel.add(placements_deuce_rel);
		placements_rel.add(placements_ad_rel);

		for (int i = 0; i < placements.size(); i++) {
			for (int j = 0; j < placements_rel.get(i).length; j++) {
				System.out.print(placements_rel.get(i)[j] + " , ");
			}
			System.out.println("");
		}
		// System.out.println(placements);
		return placements;
	}

	public static void ServePlusOne(String playername) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		double FH_counter = 0;
		double BH_counter = 0;
		double FH_won = 0;
		double BH_won = 0;
		String[] names = playername.split("_");
		playername = names[0] + " " + names[1];
		for (Point p : points) {
			if (p.getServer().getName().equals(playername) && !p.getFirstserve().isSuccess()) {
				if (p.getShots().size() >= 2) {
					if (p.getShots().get(1).getShottype().equals(Shottypes.forehand)) {
						FH_counter++;
						if (p.getWinner().equals(p.getServer())) {
							FH_won++;
						}
					} else if (p.getShots().get(1).getShottype().equals(Shottypes.backhand)) {
						BH_counter++;
						if (p.getWinner().equals(p.getServer())) {
							BH_won++;
						}
					}
				}
			}
		}
		double FH_won_ratio = FH_won / FH_counter;
		double BH_won_ratio = BH_won / BH_counter;
		System.out.println("Forehands : " + FH_counter + " , FH won : " + FH_won + " , Ratio : " + FH_won_ratio
				+ " , Backhands: " + BH_counter + " , BH won: " + BH_won + " , Ratio : " + BH_won_ratio);
	}

}
