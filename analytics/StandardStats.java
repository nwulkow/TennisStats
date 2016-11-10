package analytics;

import java.io.File;
import java.io.IOException;

import counts.Match;
import counts.Point;
import dataload.LoadMatchFromTennisAbstract;
import objects.Shottypes;
import tools.StatsTools;

public class StandardStats {

	public static double[] pointsWonByPlayer(Match m) {
		double[] counter = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getWinner().equals(m.getPlayers()[0])) {
				counter[0]++;
			} else if (p.getWinner().equals(m.getPlayers()[1])) {
				counter[1]++;
			}
		}
		return counter;
	}
	
	public static double[] pointsWonByPlayerPercentage(Match m){
		double[] pointswon = pointsWonByPlayer(m);
		double[] pointswon_rel = StatsTools.absToRel(pointswon);
		return pointswon_rel;
	}

	public static double pointsPlayed(Match m) {
		return m.getPoints().size();
	}

	public static double[] servePercentage(Match m) {
		double[] serveIn_abs = new double[2];
		double[] serveAttemp_abs = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getServer().equals(m.getPlayers()[0])) {
				serveAttemp_abs[0]++;
				if (p.getSuccesfullserve().equals(p.getFirstserve())) {
					//System.out.println("jetzt");
					serveIn_abs[0]++;
				}
			} else if (p.getServer().equals(m.getPlayers()[1])) {
				serveAttemp_abs[1]++;
				if (p.getSuccesfullserve().equals(p.getFirstserve())) {
					serveIn_abs[1]++;
				}
			}
		}
		double[] result = new double[2];
		result[0] = serveIn_abs[0] / serveAttemp_abs[0];
		result[1] = serveIn_abs[1] / serveAttemp_abs[1];
		return result;
	}

	public static double[] firstServeWinPercentage(Match m) {
		double[] serveplayed_abs = new double[2];
		double[] servewon_abs = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getServer().equals(m.getPlayers()[0])) {
				if (p.getSuccesfullserve().equals(p.getFirstserve())) {
					serveplayed_abs[0]++;
					if (p.getWinner().equals(p.getServer())) {
						servewon_abs[0]++;
					}
				}
			} else if (p.getServer().equals(m.getPlayers()[1])) {
				if (p.getSuccesfullserve().equals(p.getFirstserve())) {
					serveplayed_abs[1]++;
					if (p.getWinner().equals(p.getServer())) {
						servewon_abs[1]++;
					}
				}
			}
		}
		double[] result = new double[2];
		// System.out.println(servewon_abs[0] + " / " + serveplayed_abs[0]);
		// System.out.println(servewon_abs[1] + " / " + serveplayed_abs[1]);
		result[0] = servewon_abs[0] / serveplayed_abs[0];
		result[1] = servewon_abs[1] / serveplayed_abs[1];
		return result;
	}

	public static double[] secondServeWinPercentage(Match m) {
		double[] serveplayed_abs = new double[2];
		double[] servewon_abs = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getServer().equals(m.getPlayers()[0])) {
				if (!p.getSuccesfullserve().equals(p.getFirstserve()) && p.getSuccesfullserve().equals(p.getSecondserve())) {
					serveplayed_abs[0]++;
					if (p.getWinner().equals(p.getServer())) {
						servewon_abs[0]++;
					}
				}
			} else if (p.getServer().equals(m.getPlayers()[1])) {
				if (!p.getSuccesfullserve().equals(p.getFirstserve())) {
					serveplayed_abs[1]++;
					if (p.getWinner().equals(p.getServer())) {
						servewon_abs[1]++;
					}
				}
			}
		}
		double[] result = new double[2];
		// System.out.println(servewon_abs[0] + " / " + serveplayed_abs[0]);
		// System.out.println(servewon_abs[1] + " / " + serveplayed_abs[1]);
		result[0] = servewon_abs[0] / serveplayed_abs[0];
		result[1] = servewon_abs[1] / serveplayed_abs[1];
		return result;
	}

	public static double[] totalServeWinPercentage(Match m) {
		double[] serveplayed_abs = new double[2];
		double[] servewon_abs = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getServer().equals(m.getPlayers()[0])) {
				serveplayed_abs[0]++;
				if (p.getWinner().equals(p.getServer())) {
					servewon_abs[0]++;
				}
			} else if (p.getServer().equals(m.getPlayers()[1])) {
				serveplayed_abs[1]++;
				if (p.getWinner().equals(p.getServer())) {
					servewon_abs[1]++;
				}
			}
		}
		double[] result = new double[2];
		// System.out.println(servewon_abs[0] + " / " + serveplayed_abs[0]);
		// System.out.println(servewon_abs[1] + " / " + serveplayed_abs[1]);
		result[0] = servewon_abs[0] / serveplayed_abs[0];
		result[1] = servewon_abs[1] / serveplayed_abs[1];
		return result;
	}

	public static double[] firstReturnWinPercentage(Match m) {
		double[] firstservewon = firstServeWinPercentage(m);
		double[] result = new double[2];
		result[0] = 1 - firstservewon[1];
		result[1] = 1 - firstservewon[0];
		return result;
	}

	public static double[] secondReturnWinPercentage(Match m) {
		double[] secondservewon = secondServeWinPercentage(m);
		double[] result = new double[2];
		result[0] = 1 - secondservewon[1];
		result[1] = 1 - secondservewon[0];
		return result;
	}

	public static double[] totalReturnWinPercentage(Match m) {
		double[] totalservewon = totalServeWinPercentage(m);
		double[] result = new double[2];
		result[0] = 1 - totalservewon[1];
		result[1] = 1 - totalservewon[0];
		return result;
	}

	public static double[] numberOfWinners(Match m) {
		double[] winners = new double[2];
		for (Point p : m.getPoints()) {
			// Winner im Ballwechsel
			if (p.getShots().size() > 0) {
				if (p.getLastShot().getOutcome().equals(Shottypes.winner)) {
					if (p.getWinner().equals(m.getPlayers()[0])) {
						winners[0]++;
					} else if (p.getWinner().equals(m.getPlayers()[1])) {
						winners[1]++;
					}
				}
			}
		}
		double[] aces = numberOfAces(m);
		double[] serve_winners = numberOfServiceWinners(m);
		winners[0] += aces[0] + serve_winners[0];
		winners[1] += aces[1] + serve_winners[1];

		return winners;
	}
	
	public static double[] winnersPerPoint(Match m) {
		double[] winners = numberOfUE(m);
		double points = pointsPlayed(m);
		double[] winnersPerPoint = {winners[0] / points , winners[1] / points };
		return winnersPerPoint;
	}

	public static double[] numberOfUE(Match m) {
		double[] unforcederrors = new double[2];
		for (Point p : m.getPoints()) {
			// Unforced Errors im Ballwechsel
			if (p.getShots().size() > 0) {
				if (p.getLastShot().getOutcome().equals(Shottypes.UE)) {
					if (p.getWinner().equals(m.getPlayers()[0])) {
						unforcederrors[0]++;
					} else if (p.getWinner().equals(m.getPlayers()[1])) {
						unforcederrors[1]++;
					}
				}
			}
		}
		double[] doublefaults = numberOfDoubleFaults(m);
		unforcederrors[0] += doublefaults[0];
		unforcederrors[1] += doublefaults[1];

		return unforcederrors;
	}
	
	public static double[] uePerPoint(Match m) {
		double[] unforcederrors = numberOfUE(m);
		double points = pointsPlayed(m);
		double[] uePerPoint = {unforcederrors[0] / points , unforcederrors[1] / points };
		return uePerPoint;
	}

	public static double[] numberOfAces(Match m) {
		double[] aces = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getSuccesfullserve().getOutcome().equals(Shottypes.serve_ace) || p.getSuccesfullserve().isAce()) {
				if (p.getServer().equals(m.getPlayers()[0])) {
					aces[0]++;
				} else if (p.getServer().equals(m.getPlayers()[1])) {
					aces[1]++;
				}
			}
		}
		return aces;
	}
	
	public static double[] acesPerServingPoint(Match m) {
		double[] aces = numberOfAces(m);
		double[] servicePoints = numberOfServicePoints(m);
		double[] acesPerPoints = {aces[0] / servicePoints[0] , aces[1] / servicePoints[1]};
		return acesPerPoints;
	}
	
	public static double[] doubleFaultsPerServingPoint(Match m) {
		double[] daublefaults = numberOfAces(m);
		double[] servicePoints = numberOfServicePoints(m);
		double[] dfsPerPoints = {daublefaults[0] / servicePoints[0] , daublefaults[1] / servicePoints[1]};
		return dfsPerPoints;
	}
	

	public static double[] numberOfServicePoints(Match m) {
		double[] serve_points = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getServer().equals(m.getPlayers()[0])) {
				serve_points[0]++;
			} else if (p.getServer().equals(m.getPlayers()[1])) {
				serve_points[1]++;
			}
		}
		return serve_points;
	}
	
	public static double[] numberOfServiceWinners(Match m) {
		double[] serve_winners = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getSuccesfullserve().getOutcome().equals(Shottypes.serve_winner)) {
				if (p.getServer().equals(m.getPlayers()[0])) {
					serve_winners[0]++;
				} else if (p.getServer().equals(m.getPlayers()[1])) {
					serve_winners[1]++;
				}
			}
		}
		return serve_winners;
	}

	public static double[] numberOfDoubleFaults(Match m) {
		double[] doublefaults = new double[2];
		for (Point p : m.getPoints()) {
			if (p.getSuccesfullserve().isSuccess() == false || p.getSecondserve().isSuccess() == false) {
				if (p.getServer().equals(m.getPlayers()[0])) {
					doublefaults[0]++;
				} else if (p.getServer().equals(m.getPlayers()[1])) {
					doublefaults[1]++;
				}
			}
		}
		return doublefaults;
	}
	
	public static double[] breakPointsSaved(Match m){
		double[] breakpoints = new double[4];
		for(Point p : m.getPoints()){
			if(p.getScoreSituation().isBreakPoint()){
				if(p.getServer().equals(m.getPlayers()[0])){
					breakpoints[1]++;
					if(p.getWinner().equals(p.getServer())){
						breakpoints[0]++;
					}
				}
				else{
					breakpoints[3]++;
					if(p.getWinner().equals(p.getServer())){
						breakpoints[2]++;
					}
				}
			}
		}
		return breakpoints;
	}
	
	public static double[] breakPointsMade(Match m){
		double[] breakpoints = new double[4];
		for(Point p : m.getPoints()){
			if(p.getScoreSituation().isBreakPoint()){
				if(p.getServer().equals(m.getPlayers()[1])){
					breakpoints[1]++;
					if(p.getWinner().equals(p.getReturner())){
						breakpoints[0]++;
					}
				}
				else{
					breakpoints[3]++;
					if(p.getWinner().equals(p.getReturner())){
						breakpoints[2]++;
					}
				}
			}
		}
		return breakpoints;
	}

	// Output

	public static void printAllStandardStats(Match m) {
		double[] firstserveIn = servePercentage(m);
		double[] aces = numberOfAces(m);
		double[] doublefaults = numberOfDoubleFaults(m);
		double[] firstserveWin = firstServeWinPercentage(m);
		double[] secondserveWin = secondServeWinPercentage(m);
		double[] UE = numberOfUE(m);
		double[] winners = numberOfWinners(m);
		double[] totalpointswon = pointsWonByPlayer(m);
		m.printEndResult();
		System.out.println(m.getPlayers()[0].getName() + " , " + m.getPlayers()[1].getName());
		System.out.println("First serve in: " + firstserveIn[0] + " , " + firstserveIn[1]);
		System.out.println("Aces: " + aces[0] + " , " + aces[1]);
		System.out.println("Double Faults: " + doublefaults[0] + " , " + doublefaults[1]);
		System.out.println("First serve won: " + firstserveWin[0] + " , " + firstserveWin[1]);
		System.out.println("Second serve won: " + secondserveWin[0] + " , " + secondserveWin[1]);
		System.out.println("Unforced Errors: " + UE[0] + " , " + UE[1]);
		System.out.println("Winners: " + winners[0] + " , " + winners[1]);
		System.out.println("Total points won: " + totalpointswon[0] + " , " + totalpointswon[1]);
	}

	public static void printAllStandardStats(String filename) throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(new File(filename));
		printAllStandardStats(m);
	}

}
