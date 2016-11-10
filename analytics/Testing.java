package analytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;

import analysisFormats.MatchStatList;
import analysisFormats.PlayerStatList;
import analysisFormats.PlayerStatListCollection;
import analysisFormats.ProbabilityForShot;
import analysisFormats.ProbabilityTensor;

//import org.jfree.chart.plot.Plot;

import counts.Game;
import counts.Match;
import counts.MatchInfo;
import counts.Point;
import counts.Shot;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import graphics.BarChart;
import graphics.LineChartDemo6;
import graphics.PlotTools;
import mathTools.Histogram;
import objects.OutputFinals;
import objects.Shottypes;
import player.Player;
import tools.ArrayTools;
import tools.MatchTools;
import tools.OutputTools;
import tools.QuickSort;
import tools.StatsTools;
import tools.Tools;

public class Testing {

	public static void v1Convergence(String filepath) throws IOException {

		MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
		String filename = filepath.split("/")[filepath.split("/").length - 1];
		String[] names = filename.replace(".csv", "").split("_");
		String player1name = names[0] + " " + names[1];
		String player2name = names[names.length - 2] + " " + names[names.length - 1];

		Match m = new Match(im, new Player(player1name), new Player(player2name));
		ArrayList<double[]> v1s = new ArrayList<double[]>();
		// double[] current_v1 = Metrics.f_Function(m.getPoints().get(0),
		// m.getPlayers()[0], m.getPlayers()[1]);
		// v1s.add(current_v1);
		int counter = 0;
		double cur_v1_1 = 0;
		double cur_v1_2 = 0;
		for (Point p : m.getPoints()) {
			// System.out.println(p.getWinner().getName());
			double[] current_v1 = Metrics.f_Function(p, m.getPlayers()[0], m.getPlayers()[1]);
			double[] new_v1 = new double[2];
			if (current_v1[0] > -10 && current_v1[1] > -10) {
				if (v1s.size() > 0) {
					cur_v1_1 = v1s.get(counter - 1)[0];
					cur_v1_2 = v1s.get(counter - 1)[1];
				}
				new_v1[0] = (cur_v1_1 * counter + current_v1[0]) / (counter + 1);
				new_v1[1] = (cur_v1_2 * counter + current_v1[1]) / (counter + 1);
				v1s.add(new_v1);
				counter++;
			}
		}

		for (int j = 0; j < v1s.size(); j++) {
			System.out.print(v1s.get(j)[0] + " , ");
		}
		System.out.println("");
		for (int j = 0; j < v1s.size(); j++) {
			System.out.print(v1s.get(j)[1] + " , ");
		}
	}

	public static void v1Convergence(ArrayList<Point> points) throws IOException {

		ArrayList<double[]> v1s = new ArrayList<double[]>();
		int counter = 0;
		double cur_v1_1 = 0;
		double cur_v1_2 = 0;
		String firstname = points.get(0).getServer().getName();
		String secondname = points.get(0).getReturner().getName();
		boolean firstnamebool = true;
		boolean secondnamebool = true;
		for (Point p : points) {
			if (!p.getServer().getName().equals(firstname) && !p.getReturner().getName().equals(firstname)) {
				firstnamebool = false;
			}
			if (!p.getServer().getName().equals(secondname) && !p.getReturner().getName().equals(secondname)) {
				secondnamebool = false;
			}
		}

		Player pl1 = new Player();
		if (firstnamebool)
			pl1 = points.get(0).getServer();
		if (secondnamebool)
			pl1 = points.get(0).getReturner();
		for (Point p : points) {
			Player myp1 = p.getServer();
			Player myp2 = p.getReturner();
			if (p.getReturner().getName().equals(pl1.getName())) {
				myp1 = pl1;
				myp2 = p.getServer();
			}
			double[] current_v1 = Metrics.f_Function(p, myp1, myp2);
			double[] new_v1 = new double[2];
			if (current_v1[0] > -10 && current_v1[1] > -10) {
				if (v1s.size() > 0) {
					cur_v1_1 = v1s.get(counter - 1)[0];
					cur_v1_2 = v1s.get(counter - 1)[1];
				}
				new_v1[0] = (cur_v1_1 * counter + current_v1[0]) / (counter + 1);
				new_v1[1] = (cur_v1_2 * counter + current_v1[1]) / (counter + 1);
				v1s.add(new_v1);
				counter++;
			}
		}

		for (int j = 0; j < v1s.size(); j++) {
			System.out.print(v1s.get(j)[0] + " , ");
		}
		System.out.println("");
		for (int j = 0; j < v1s.size(); j++) {
			System.out.print(v1s.get(j)[1] + " , ");
		}
		PlotTools.plot(ArrayTools.getLineOfArrayList(v1s, 0), "V1 Convergence");
	}

	public static void v1ConvergencePlayer(String playername, int startyear, int endyear) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername, startyear, endyear);
		v1Convergence(points);
	}

	public static void v1ConvergencePlayer(String playername) throws IOException {
		v1ConvergencePlayer(playername, 0, 1000000);
	}

	public static void v1BlockConvergence(String filepath) throws IOException {

		MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
		String filename = filepath.split("/")[filepath.split("/").length - 1];
		String[] names = filename.replace(".csv", "").split("_");
		String player1name = names[0] + " " + names[1];
		String player2name = names[names.length - 2] + " " + names[names.length - 1];

		Match m = new Match(im, new Player(player1name), new Player(player2name));
		ArrayList<double[]> v1s = new ArrayList<double[]>();
		ArrayList<double[]> v1Blocks = new ArrayList<double[]>();
		int counter = 0;
		double cur_v1_1 = 0;
		double cur_v1_2 = 0;
		int noPoints = 25;
		for (Point p : m.getPoints()) {
			double[] current_v1 = Metrics.f_Function(p, m.getPlayers()[0], m.getPlayers()[1]);
			double[] new_v1 = new double[2];
			if (current_v1[0] > -10 && current_v1[1] > -10) {
				if (v1s.size() > 0) {
					cur_v1_1 = v1s.get(counter - 1)[0];
					cur_v1_2 = v1s.get(counter - 1)[1];
				}
				new_v1[0] = (cur_v1_1 * counter + current_v1[0]) / (counter + 1);
				new_v1[1] = (cur_v1_2 * counter + current_v1[1]) / (counter + 1);
				v1s.add(new_v1);
				counter++;
				if (Math.floorMod(counter, noPoints) == 0) {
					v1Blocks.add(new_v1);
					v1s.clear();
					counter = 0;
				}
			}
		}

		for (int j = 0; j < v1Blocks.size(); j++) {
			System.out.print(v1Blocks.get(j)[0] + " , ");
		}
		System.out.print("\n");
		for (int j = 0; j < v1Blocks.size(); j++) {
			System.out.print(v1Blocks.get(j)[1] + " , ");
		}
	}

	public static void v1BlockConvergence(ArrayList<Point> points) throws IOException {

		ArrayList<double[]> v1s = new ArrayList<double[]>();
		int counter = 0;
		double cur_v1_1 = 0;
		double cur_v1_2 = 0;
		String firstname = points.get(0).getServer().getName();
		String secondname = points.get(0).getReturner().getName();
		boolean firstnamebool = true;
		boolean secondnamebool = true;

		for (Point p : points) {
			if (!p.getServer().getName().equals(firstname) && !p.getReturner().getName().equals(firstname)) {
				firstnamebool = false;
			}
			if (!p.getServer().getName().equals(secondname) && !p.getReturner().getName().equals(secondname)) {
				secondnamebool = false;
			}
		}

		Player pl1 = new Player();
		if (firstnamebool)
			pl1 = points.get(0).getServer();
		if (secondnamebool)
			pl1 = points.get(0).getReturner();
		int noPoints = 500;
		int totalcounter = 0;
		ArrayList<double[]> v1Blocks = new ArrayList<double[]>();
		for (Point p : points) {
			// if(p.getShots().size() > 1) totalcounter++;
			Player myp1 = p.getServer();
			Player myp2 = p.getReturner();
			if (p.getReturner().getName().equals(pl1.getName())) {
				myp1 = pl1;
				myp2 = p.getServer();
			}
			double[] current_v1 = Metrics.f_Function(p, myp1, myp2);
			double[] new_v1 = new double[2];
			if (current_v1[0] > -10 && current_v1[1] > -10) {
				totalcounter++;
				if (v1s.size() > 0) {
					cur_v1_1 = v1s.get(counter - 1)[0];
					cur_v1_2 = v1s.get(counter - 1)[1];
				}
				new_v1[0] = (cur_v1_1 * counter + current_v1[0]) / (counter + 1);
				new_v1[1] = (cur_v1_2 * counter + current_v1[1]) / (counter + 1);
				v1s.add(new_v1);
				counter++;
				if (Math.floorMod(counter, noPoints) == 0) {
					v1Blocks.add(new_v1);
					v1s.clear();
					counter = 0;
				}
			}
		}
		System.out.println(totalcounter);
		PlotTools.plot(ArrayTools.getLineOfArrayList(v1Blocks, 0), "V1 Blocks");
	}

	public static void v2Convergence(String filepath) throws IOException {

		MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
		String filename = filepath.split("/")[filepath.split("/").length - 1];
		String[] names = filename.replace(".csv", "").split("_");
		String player1name = names[0] + " " + names[1];
		String player2name = names[names.length - 2] + " " + names[names.length - 1];

		Match m = new Match(im, new Player(player1name), new Player(player2name));
		ArrayList<double[]> v2s = new ArrayList<double[]>();
		double gsum1 = 0;
		double gsum2 = 0;
		/*
		 * double[] current_f = Metrics.f_Function(m.getPoints().get(0),
		 * m.getPlayers()[0], m.getPlayers()[1]); double[] current_g =
		 * Metrics.g_Function(m.getPoints().get(0),m.getPlayers()[0],
		 * m.getPlayers()[1], 0.5, 0.5); double[] newvalue = new double[2];
		 * newvalue[0] = current_f[0]*current_g[0]; newvalue[1] =
		 * current_f[1]*current_g[1]; if(current_f[0] > -10 && current_f[1] >
		 * -10 && current_g[0] > -10 && current_g[1] > -10){ v2s.add(newvalue);
		 * gsum1 += current_g[0]; gsum1 += current_g[1]; }
		 */
		int counter = -1;
		double curv2_1 = 0;
		double curv2_2 = 0;

		for (Point p : m.getPoints()) {
			double[] current_f = Metrics.f_Function(p, m.getPlayers()[0], m.getPlayers()[1]);
			double[] current_g = Metrics.g_Function(p, m.getPlayers()[0], m.getPlayers()[1], 0.5, 0.5);
			// double[] current_g = {1,1};
			double[] current_v2 = new double[2];
			current_v2[0] = current_f[0] * current_g[0];
			current_v2[1] = current_f[1] * current_g[1];
			double[] new_v2 = new double[2];

			if (current_f[0] > -10 && current_f[1] > -10 && current_g[0] > -10 && current_g[1] > -10) {
				// System.out.println(current_v2[0] + " , " + current_v2[1]);
				if (v2s.size() > 0) {
					curv2_1 = v2s.get(v2s.size() - 1)[0];
					curv2_2 = v2s.get(v2s.size() - 1)[1];
				}
				new_v2[0] = (curv2_1 * gsum1 + current_v2[0] * current_g[0]) / (gsum1 + current_g[0]);
				new_v2[1] = (curv2_2 * gsum2 + current_v2[1] * current_g[1]) / (gsum2 + current_g[1]);
				v2s.add(new_v2);
				counter++;
				gsum1 += current_g[0];
				gsum2 += current_g[1];
			}

		}

		double[] firstline = ArrayTools.getLineOfArrayList(v2s, 0);
		double[] secondline = ArrayTools.getLineOfArrayList(v2s, 1);
		PlotTools.plot(firstline, "First Line");
		PlotTools.plot(secondline, "second Line");

		for (int j = 0; j < v2s.size(); j++) {
			System.out.print(v2s.get(j)[0] + " , ");
		}
		System.out.println("");
		for (int j = 0; j < v2s.size(); j++) {
			System.out.print(v2s.get(j)[1] + " , ");
		}
	}

	public static void v1BlockConvergencePlayer(String playername) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		v1BlockConvergence(points);
	}

	public static void v2Convergence(ArrayList<Point> points) throws IOException {

		ArrayList<double[]> v2s = new ArrayList<double[]>();
		double gsum1 = 0;
		double gsum2 = 0;
		double curv2_1 = 0;
		double curv2_2 = 0;
		String firstname = points.get(0).getServer().getName();
		String secondname = points.get(0).getReturner().getName();
		boolean firstnamebool = true;
		boolean secondnamebool = true;
		for (Point p : points) {
			if (!p.getServer().getName().equals(firstname) && !p.getReturner().getName().equals(firstname)) {
				firstnamebool = false;
			}
			if (!p.getServer().getName().equals(secondname) && !p.getReturner().getName().equals(secondname)) {
				secondnamebool = false;
			}
		}

		Player pl1 = new Player();
		if (firstnamebool)
			pl1 = points.get(0).getServer();
		if (secondnamebool)
			pl1 = points.get(0).getReturner();

		for (Point p : points) {
			Player myp1 = p.getServer();
			Player myp2 = p.getReturner();
			if (p.getReturner().equals(pl1)) {
				myp1 = pl1;
				myp2 = p.getServer();
			}
			double p1_v2 = LoadValues.loadPlayerValue(myp1.getName(), OutputFinals.v2name);
			double p2_v2 = LoadValues.loadPlayerValue(myp2.getName(), OutputFinals.v2name);
			double[] current_f = Metrics.f_Function(p, myp1, myp2);
			double[] current_g = Metrics.g_Function(p, myp1, myp2, p1_v2, p2_v2);
			double[] current_v2 = new double[2];
			current_v2[0] = current_f[0] * current_g[0];
			current_v2[1] = current_f[1] * current_g[1];
			double[] new_v2 = new double[2];

			if (current_f[0] > -10 && current_f[1] > -10 && current_g[0] > -10 && current_g[1] > -10) {
				if (v2s.size() > 0) {
					curv2_1 = v2s.get(v2s.size() - 1)[0];
					curv2_2 = v2s.get(v2s.size() - 1)[1];
				}
				new_v2[0] = (curv2_1 * gsum1 + current_v2[0] * current_g[0]) / (gsum1 + current_g[0]);
				new_v2[1] = (curv2_2 * gsum2 + current_v2[1] * current_g[1]) / (gsum2 + current_g[1]);
				v2s.add(new_v2);
				gsum1 += current_g[0];
				gsum2 += current_g[1];
			}

		}
		System.out.println(v2s.size());
		double[] firstline = ArrayTools.getLineOfArrayList(v2s, 0);
		double[] secondline = ArrayTools.getLineOfArrayList(v2s, 1);
		PlotTools.plot(firstline, "First Line");
		PlotTools.plot(secondline, "Second Line");

		for (int j = 0; j < v2s.size(); j++) {
			System.out.print(v2s.get(j)[0] + " , ");
		}
		System.out.println("");
		for (int j = 0; j < v2s.size(); j++) {
			System.out.print(v2s.get(j)[1] + " , ");
		}
	}

	public static void v2ConvergencePlayer(String playername) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		v2Convergence(points);
	}

	public static void v2BlockConvergence(ArrayList<Point> points) throws IOException {

		ArrayList<double[]> v2s = new ArrayList<double[]>();
		int counter = 0;
		double cur_v2_1 = 0;
		double cur_v2_2 = 0;
		double gsum1 = 0;
		double gsum2 = 0;
		String firstname = points.get(0).getServer().getName();
		String secondname = points.get(0).getReturner().getName();
		boolean firstnamebool = true;
		boolean secondnamebool = true;

		for (Point p : points) {
			if (!p.getServer().getName().equals(firstname) && !p.getReturner().getName().equals(firstname)) {
				firstnamebool = false;
			}
			if (!p.getServer().getName().equals(secondname) && !p.getReturner().getName().equals(secondname)) {
				secondnamebool = false;
			}
		}

		Player pl1 = new Player();
		if (firstnamebool)
			pl1 = points.get(0).getServer();
		if (secondnamebool)
			pl1 = points.get(0).getReturner();
		int noPoints = 300;
		int totalcounter = 0;
		ArrayList<double[]> v2Blocks = new ArrayList<double[]>();
		double p1_v2 = LoadValues.loadPlayerValue(pl1.getName(), OutputFinals.v2name);
		double p2_v2 = 0.5;
		String current_playername = "";
		for (Point p : points) {

			Player myp1 = p.getServer();
			Player myp2 = p.getReturner();
			if (p.getReturner().getName().equals(pl1.getName())) {
				myp1 = pl1;
				myp2 = p.getServer();
			}
			if (!myp2.getName().equals(current_playername)) {
				p2_v2 = LoadValues.loadPlayerValue(myp2.getName(), OutputFinals.v2name);
				current_playername = myp2.getName();
			}
			double[] current_f = Metrics.f_Function(p, myp1, myp2);
			double[] current_g = Metrics.g_Function(p, myp1, myp2, p1_v2, p2_v2);
			double[] current_v2 = new double[2];
			current_v2[0] = current_f[0] * current_g[0];
			current_v2[1] = current_f[1] * current_g[1];
			double[] new_v2 = new double[2];
			if (current_f[0] > -10 && current_f[1] > -10 && current_g[0] > -10 && current_g[1] > -10) {
				totalcounter++;
				if (v2s.size() > 0) {
					cur_v2_1 = v2s.get(counter - 1)[0];
					cur_v2_2 = v2s.get(counter - 1)[1];
				}
				new_v2[0] = (cur_v2_1 * gsum1 + current_v2[0] * current_g[0]) / (gsum1 + current_g[0]);
				new_v2[1] = (cur_v2_2 * gsum2 + current_v2[1] * current_g[1]) / (gsum2 + current_g[1]);
				v2s.add(new_v2);
				System.out.println(new_v2[0] + " , " + new_v2[1]);
				counter++;
				gsum1 += current_g[0];
				gsum2 += current_g[1];
				if (Math.floorMod(counter, noPoints) == 0) {
					v2Blocks.add(new_v2);
					v2s.clear();
					counter = 0;
					gsum1 = 0;
					gsum2 = 0;
				}
			}
		}
		System.out.println(totalcounter);
		PlotTools.plot(ArrayTools.getLineOfArrayList(v2Blocks, 0), "V2 Blocks");
	}

	public static void v2BlockConvergencePlayer(String playername) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		v2BlockConvergence(points);
	}

	// Zaehlt Richtung und Asse erster Aufschlaege
	public static void acesDistribution(String filepath) throws IOException {

		MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
		String filename = filepath.split("/")[filepath.split("/").length - 1];
		String[] names = filename.replace(".csv", "").split("_");
		String player1name = names[0] + " " + names[1];
		String player2name = names[names.length - 2] + " " + names[names.length - 1];

		Match m = new Match(im, new Player(player1name), new Player(player2name));

		double wide_count = 0;
		double t_count = 0;
		double wace_count = 0;
		double tace_count = 0;

		for (Point p : m.getPoints()) {
			if (p.getServer().equals(m.getPlayers()[0])) {
				if (p.getFirstserve().getDirection() == 3) {
					wide_count++;
					if (p.getFirstserve().isAce()) {
						wace_count++;
					}
				} else if (p.getFirstserve().getDirection() == 1) {
					t_count++;
					if (p.getFirstserve().isAce()) {
						tace_count++;
					}
				}
			}
		}
		System.out.println("wide_count = " + wide_count + "  , wace_count = " + wace_count + "  , t_count = " + t_count
				+ "  , tace_count  = " + tace_count);
	}

	public static void countDoubleFaults(String filepath) throws IOException {

		MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
		String filename = filepath.split("/")[filepath.split("/").length - 1];
		String[] names = filename.replace(".csv", "").split("_");
		String player1name = names[0] + " " + names[1];
		String player2name = names[names.length - 2] + " " + names[names.length - 1];

		Match m = new Match(im, new Player(player1name), new Player(player2name));
		int DFcount1 = 0;
		int DFcount2 = 0;

		for (Point p : m.getPoints()) {
			if (!p.getSecondserve().isSuccess()) {
				if (p.getServer().equals(m.getPlayers()[0])) {
					DFcount1++;
				} else if (p.getServer().equals(m.getPlayers()[1])) {
					DFcount2++;
				}
			}
		}
		System.out.println("Double Faults " + player1name + " : " + DFcount1 + " , Double Faults " + player2name + " : "
				+ DFcount2);
	}
	
	
	// Berechnet die Korrelationskoeffizienten zwischen den Arrays Anteil der Schleage von fester Position i in Richtung j und dem Matchausang (0 fuer Nieder-
	// lage, 1 fuer Sieg). Ein hoher Wert fuer 0->2 wuerde bedeuten, dass eine hohe Korrelation besteht zwischen "wenn der Ball bei 0 hat der Spieler oft zu 2
	// geschlagen" und Matchgewinn.
	public static void shotDirectionAndOutcomeCorrelations() throws IOException{
		
		ArrayList<Match> matches = LoadValues.loadAllMatches();

		ArrayList<ProbabilityTensor> tensors = new ArrayList<ProbabilityTensor>();
		ArrayList<Double> matchWon = new ArrayList<Double>();
		
		// Tensors fuer alle Matches fuer jeden Spieler zusammenstellen
		for(Match m : matches){
			for(Player p : m.getPlayers()){
				tensors.add(PlayerStats.createTensorForOnePlayerForOneMatch(p.getName(), m, false, false));
				if(m.getWinner().equals(p)){
					matchWon.add(1d);
				}
				else{
					matchWon.add(0d);
				}
			}
		}
		//-----
		double[] matchWon_array = ArrayTools.ArrayListToArray(matchWon);
		
		ProbabilityTensor correlationsTensor = new ProbabilityTensor(); // Tensor, der die Corcoefs enthaelt
		ProbabilityTensor correlationsBetweenHistogramsTensor = new ProbabilityTensor(); // Es werden Histogramme erstellt mit den Anteilen der Schlaege in eine
		// bestimmte Richtung fuer jeweils die Sieger und die Verlierer der Matches. Diese Histogramm werden dann ebenfalls mit dem Corcoef verglichen 
		ProbabilityTensor averagesTensor = new ProbabilityTensor(); // Tensor, der die Durchschnittswerte ueber alle Matches enthaelt. Dabei werden
		// alle Matches unabhaenig von ihrer Anzahl an Punkten gleich gewichtet
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				double[] currentShotDirection = new double[tensors.size()];
				double[] currentShotInPlay = new double[tensors.size()];
				double[] currentShotWinner = new double[tensors.size()];
				double[] currentShotError = new double[tensors.size()];
				ArrayList<Double> valuesIfMatchWasLost = new ArrayList<Double>();
				ArrayList<Double> valuesIfMatchWasWon = new ArrayList<Double>();
				double currentShotSum = 0d;
				for(int k = 0; k < tensors.size(); k++){
					currentShotSum += tensors.get(k).getTensor().getProbForShots()[0][i][j].getProbabilityForShot();
					// Fuer jedes Match den Anteil der Schlaege von i nach j rausschreiben sowie Outcomes
					currentShotDirection[k] = tensors.get(k).getTensor().getProbForShots()[0][i][j].getProbabilityForShot();
					currentShotInPlay[k] = tensors.get(k).getTensor().getProbForShots()[0][i][j].getOutcomeProbabilities()[0];
					currentShotWinner[k] = tensors.get(k).getTensor().getProbForShots()[0][i][j].getOutcomeProbabilities()[1];
					currentShotError[k] = tensors.get(k).getTensor().getProbForShots()[0][i][j].getOutcomeProbabilities()[2];
					if(matchWon_array[k] == 0){
						valuesIfMatchWasLost.add(currentShotDirection[k]);
					}
					else{
						valuesIfMatchWasWon.add(currentShotDirection[k]);
					}
				}
				
				// Histogramme erstellen und anzeigen fuer die Haeufigkeit jedes Schlages.
				Histogram histogramMatchLost = new Histogram(ArrayTools.ArrayListToArray(valuesIfMatchWasLost), 10);
				histogramMatchLost.makeDataStochastic();
				Histogram histogramMatchWon = new Histogram(ArrayTools.ArrayListToArray(valuesIfMatchWasWon), histogramMatchLost.getBins());
				histogramMatchWon.makeDataStochastic();
				LineChartDemo6 lcd6 = new LineChartDemo6(i + " " + j,histogramMatchLost.getBins(), histogramMatchLost.getFrequencies(),"Match lost");
				lcd6.addToDataset(histogramMatchWon.getBins(),histogramMatchWon.getFrequencies(),"Match won");
				
				averagesTensor.getTensor().getProbForShots()[0][i][j].setProbabilityForShot(currentShotSum / tensors.size());
				
				// Korrelationen berechnen
				double directionCorcoef = StatsTools.correlationcoefficient(currentShotDirection, matchWon_array);
				double inPlayCorcoef = StatsTools.correlationcoefficient(currentShotInPlay, matchWon_array);
				double winnerCorcoef = StatsTools.correlationcoefficient(currentShotWinner, matchWon_array);
				double errorCorcoef = StatsTools.correlationcoefficient(currentShotError, matchWon_array);
				// Korrelationstensor fuellen
				correlationsTensor.getTensor().setProbForShotsAtOneIndex(0, i, j, new ProbabilityForShot(directionCorcoef,inPlayCorcoef, winnerCorcoef, errorCorcoef, 0, 0, 0));
				correlationsBetweenHistogramsTensor.getTensor().setProbForShotsAtOneIndex(0, i, j, new ProbabilityForShot(StatsTools.correlationcoefficient(histogramMatchLost.getFrequencies(), histogramMatchWon.getFrequencies()),inPlayCorcoef, winnerCorcoef, errorCorcoef,0,0,0));
				//PlotTools.plot(currentShotDirection, matchWon_array, i + " " + j);
			}
		}
		
		OutputTools.printTensor(correlationsTensor);
		
		OutputTools.printTensor(correlationsBetweenHistogramsTensor);
		
		//Tools.printTensor(averagesTensor);
		
	}

	public static void baseLineWinPercentageAllPlayers() throws IOException {

		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		double BL_won = 0;
		double BL_played = 0;
		for (File file : folder.listFiles()) {
			MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(file.getAbsolutePath());
			Match m = new Match(im);
			for (Point p : m.getPoints()) {
				boolean p1_bool = true;
				boolean p2_bool = true;
				int shotcounter = 0;
				for (Shot s : p.getShots()) {
					if (Math.floorMod(shotcounter, 2) == 0) {
						if (!(s.getSpecifictype().equals(Shottypes.groundstroke)
								|| s.getSpecifictype().equals(Shottypes.lob)
								|| s.getSpecifictype().equals(Shottypes.dropshot))) {
							p1_bool = false;
						}
					}
					if (Math.floorMod(shotcounter, 2) == 1) {
						if (!(s.getSpecifictype().equals(Shottypes.groundstroke)
								|| s.getSpecifictype().equals(Shottypes.lob)
								|| s.getSpecifictype().equals(Shottypes.dropshot))) {
							p2_bool = false;
						}
					}
					if (p1_bool) {
						BL_played++;
						if (p.getWinner().equals(p.getReturner())) {
							BL_won++;
						}
					}
					if (p2_bool) {
						BL_played++;
						if (p.getWinner().equals(p.getServer())) {
							BL_won++;
						}
					}
				}
			}
		}
		double BL_won_ratio = BL_won / BL_played;
		System.out.println(BL_won_ratio);
	}

	public static PlayerStatList baseLineWinPercentage(String playername, ArrayList<Match> matches) throws IOException {

		double BL_won = 0;
		double BL_played = 0;
		for (Match m : matches) {
			for (Point p : m.getPoints()) {
				if(p.getNoShotsIn() >= 2){
					boolean p1_bool = true;
					int shotcounter = 0;
					for(Shot s : p.getShots()) {
						if(s.getPlayer().getName().equals(playername) && s.isApproach()){
							p1_bool = false;
						}
					}
					if (p1_bool) {
						BL_played++;
						if (p.getWinner().getName().equals(playername)) {
							BL_won++;
						}
					}
				}
			}
		}
		double BL_won_ratio = BL_won / BL_played;
		PlayerStatList psl = new PlayerStatList(playername, new ArrayList<Double>(Arrays.asList(BL_won_ratio)), new ArrayList<String>(Arrays.asList("Baseline points won")), BL_played);
		return psl;
	}
	
	public static double[] baseLineWinPercentageMatch(Match m){

		double BL_won1 = 0;
		double BL_played1 = 0;
		double BL_won2 = 0;
		double BL_played2 = 0;
				for (Point p : m.getPoints()) {
					boolean p1_bool = true;
					boolean p2_bool = true;
					int shotcounter = 0;
					for (Shot s : p.getShots()) {
						if(s.isApproach()){
							if((p.getServer().equals(m.getPlayers()[0]) && Math.floorMod(shotcounter, 2) == 1) || (p.getServer().equals(m.getPlayers()[1]) && Math.floorMod(shotcounter, 2) == 0)){
								p1_bool = false;
							}
							else{
								p2_bool = false;
							}
						}
						if (p1_bool) {
							BL_played1++;
							if (p.getWinner().equals(m.getPlayers()[0])) {
								BL_won1++;
							}
						}
						if (p2_bool) {
							BL_played2++;
							if (p.getWinner().equals(m.getPlayers()[1])) {
								BL_won2++;
							}
						}
			}
		}
		double[] BL_won_ratio = { BL_won1 / BL_played1, BL_won2 / BL_played2 };
		return BL_won_ratio;
	}

	public static void rallylenghts() throws IOException {

		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");

		double[] lengths = { 3, 8, 1000 };
		ArrayList<Double> counts = new ArrayList<Double>();
		ArrayList<Double> counts_SW = new ArrayList<Double>();
		ArrayList<Double> counts_RW = new ArrayList<Double>();
		ArrayList<Double> server_won = new ArrayList<Double>();
		ArrayList<Double> returner_won = new ArrayList<Double>();
		ArrayList<Double> server_won_SW = new ArrayList<Double>();
		ArrayList<Double> returner_won_SW = new ArrayList<Double>();
		ArrayList<Double> server_won_RW = new ArrayList<Double>();
		ArrayList<Double> returner_won_RW = new ArrayList<Double>();
		ArrayList<Double> FS_server_won = new ArrayList<Double>();
		ArrayList<Double> FS_returner_won = new ArrayList<Double>();
		ArrayList<Double> SS_server_won = new ArrayList<Double>();
		ArrayList<Double> SS_returner_won = new ArrayList<Double>();
		ArrayList<Double> FS_server_won_bh = new ArrayList<Double>();
		ArrayList<Double> FS_returner_won_bh = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> counts_perMatch = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> counts_perMatch_ratio = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> breaksmadelist = new ArrayList<Double>();
		ArrayList<Double> pointsWonUnder4_list = new ArrayList<Double>();
		ArrayList<Double> played_ratio = new ArrayList<Double>();

		for (int i = 0; i < lengths.length; i++) {
			counts.add(0d);
			counts_SW.add(0d);
			counts_RW.add(0d);
			server_won.add(0d);
			returner_won.add(0d);
			server_won_SW.add(0d);
			returner_won_SW.add(0d);
			server_won_RW.add(0d);
			returner_won_RW.add(0d);
			FS_server_won.add(0d);
			FS_returner_won.add(0d);
			SS_server_won.add(0d);
			SS_returner_won.add(0d);
			FS_server_won_bh.add(0d);
			FS_returner_won_bh.add(0d);
			counts_perMatch.add(new ArrayList<Double>());
			counts_perMatch_ratio.add(new ArrayList<Double>());
			played_ratio.add(0d);
		}

		for (File file : folder.listFiles()) {
			Match m = LoadMatchFromTennisAbstract.readCSVFile(file);
			// Match m = new Match(im);
			double noGames = 0;
			double breaksmade = 0;
			for (int k = 0; k < lengths.length; k++) {
				counts_perMatch.get(k).add(0d);
			}
			pointsWonUnder4_list.add(0d);
			for (Game g : m.getGames()) {
				noGames++;
				if (!g.getWinner().equals(g.getServer())) {
					breaksmade++;
				}
				for (Point p : g.getPoints()) {

					// Wenn Punkt vom Returner unter 4 Schlaagen gewonnen wurde
					if (p.getShots().size() <= 3 && !p.getWinner().equals(p.getServer())) {
						pointsWonUnder4_list.set(pointsWonUnder4_list.size() - 1,
								pointsWonUnder4_list.get(pointsWonUnder4_list.size() - 1) + 1);
					}

					for (int j = 0; j < lengths.length; j++) {
						if (p.getShots().size() <= lengths[j]) {
							counts.set(j, counts.get(j) + 1);
							counts_perMatch.get(j).set(counts_perMatch.get(j).size() - 1,
									(double) (counts_perMatch.get(j).get(counts_perMatch.get(j).size() - 1) + 1));
							if (g.getWinner().equals(g.getServer())) {
								counts_SW.set(j, counts_SW.get(j) + 1);
								if (p.getWinner().equals(p.getServer())) {
									server_won_SW.set(j, server_won_SW.get(j) + 1);
								} else {
									returner_won_SW.set(j, returner_won_SW.get(j) + 1);
								}
							} else {
								counts_RW.set(j, counts_RW.get(j) + 1);
								if (p.getWinner().equals(p.getServer())) {
									server_won_RW.set(j, server_won_RW.get(j) + 1);
								} else {
									returner_won_RW.set(j, returner_won_RW.get(j) + 1);
								}
							}
							if (p.getWinner().equals(p.getServer())) {
								server_won.set(j, server_won.get(j) + 1);
								if (p.getFirstserve().isSuccess()) {
									FS_server_won.set(j, FS_server_won.get(j) + 1);
									if (p.getShots().size() >= 3) {
										if (p.getShots().get(2).getShottype().equals(Shottypes.backhand)) {
											FS_server_won_bh.set(j, FS_server_won_bh.get(j) + 1);
										}
									}
								} else if (!p.getFirstserve().isSuccess() && p.getSecondserve().isSuccess()) {
									SS_server_won.set(j, SS_server_won.get(j) + 1);
								}
							} else {
								returner_won.set(j, returner_won.get(j) + 1);
								if (p.getFirstserve().isSuccess()) {
									FS_returner_won.set(j, FS_returner_won.get(j) + 1);
									if (p.getShots().size() >= 3) {
										if (p.getShots().get(2).getShottype().equals(Shottypes.backhand)) {
											FS_returner_won_bh.set(j, FS_returner_won_bh.get(j) + 1);
										}
									}
								} else if (!p.getFirstserve().isSuccess() && p.getSecondserve().isSuccess()) {
									SS_returner_won.set(j, SS_returner_won.get(j) + 1);
								}
							}
							break;
						}
					}
				}
			}
			pointsWonUnder4_list.set(pointsWonUnder4_list.size() - 1,
					pointsWonUnder4_list.get(pointsWonUnder4_list.size() - 1) / m.getPoints().size());
			breaksmadelist.add(breaksmade / noGames);
		}
		System.out.println(StatsTools.sumOfArray(ArrayTools.ArrayListToArray(counts_perMatch.get(0))));
		for (int p = 0; p < counts_perMatch.get(0).size(); p++) {
			for (int l = 0; l < lengths.length; l++) {
				counts_perMatch_ratio.get(l).add(counts_perMatch.get(l).get(p) / (counts_perMatch.get(0).get(p)
						+ counts_perMatch.get(1).get(p) + counts_perMatch.get(2).get(p)));
			}
		}
		double corrcoef = StatsTools.correlationcoefficient(ArrayTools.ArrayListToArray(breaksmadelist),
				ArrayTools.ArrayListToArray(counts_perMatch_ratio.get(1)));
		System.out.println("corrcoef = " + corrcoef);
		double corrcoef_wonunder4 = StatsTools.correlationcoefficient(ArrayTools.ArrayListToArray(breaksmadelist),
				ArrayTools.ArrayListToArray(pointsWonUnder4_list));
		System.out.println("corrcoef wonunder4 = " + corrcoef_wonunder4);

		for (int k = 0; k < lengths.length; k++) {
			played_ratio.set(k, counts.get(k) / StatsTools.sumOfArray(ArrayTools.ArrayListToArray(counts)));
			if (k == 0) {
				System.out.println("0 to " + lengths[0] + " shots:");
			} else {
				System.out.println(lengths[k - 1] + " to " + lengths[k] + " shots:");
			}
			System.out.println("Played: " + counts.get(k) + " , " + server_won.get(k) + " , " + returner_won.get(k));
			System.out.println("First Serve: " + FS_server_won.get(k) + " , " + FS_returner_won.get(k));
			System.out.println("Second Serve: " + SS_server_won.get(k) + " , " + SS_returner_won.get(k));
			System.out.println(
					"First serve, Backhand 4th shot: " + FS_server_won_bh.get(k) + " , " + FS_returner_won_bh.get(k));
			System.out.println("Server won the game : " + counts_SW.get(k));
			System.out.println("Returner won the game : " + counts_RW.get(k));
			System.out.println("Server won those points when server won the game : " + server_won_SW.get(k));
			System.out.println("Server won those points when server lost the game : " + server_won_RW.get(k));
			System.out.println("Returner won those points when server won the game : " + returner_won_SW.get(k));
			System.out.println("Returner won those points when server lost the game : " + returner_won_RW.get(k));
			System.out.println("");
		}
		System.out.println("played ratios: " + played_ratio);

	}

	// Zaehlt, wie viele 5-9-Schleage Rallies die Spieler anteilsmaeßig gewinnen in Aufschlagspielen in denen sie breaken
	public static PlayerStatList breakWith5To9(String playername, ArrayList<Match> matches) throws IOException {

		double FtNWon = 0; // Punkte, die der Returner zwischen 5 und 9 Schlägen
							// gewinnt, wenn er breakt
		double FtNLost = 0;
		double breaksmade = 0;
		double noGames = 0;
		double breaksmadetotal = 0;
		double noGamestotal = 0;
		ArrayList<Double> breakpercentage_list = new ArrayList<Double>();
		ArrayList<Double> fiveToNine_list = new ArrayList<Double>();
		ArrayList<Double> breakpercentages_Ratio = new ArrayList<Double>();
		for (double minratio = 0; minratio <= 1; minratio += 0.1) {
			//noGamestotal = 0;
			breaksmadetotal = 0;
			for (Match m : matches) {
				breaksmade = 0;
				noGames = 0;
				FtNLost = 0;
				FtNWon = 0;
				for (Game g : m.getGames()) {
					if (g.getServer().equals(m.getPlayers()[0])) {
						noGames++;
						if (!g.getWinner().equals(g.getServer())) {
							breaksmade++;
						}
						for (Point p : g.getPoints()) {
							if (p.getNoShotsIn() >= 5 && p.getNoShotsIn() <= 9) {
								if (p.getWinner().equals(p.getServer())) {
									FtNLost++;
									// System.out.println("Server");
								} else if (p.getWinner().equals(p.getReturner())) {
									FtNWon++;
									// System.out.println("Returner");
								}
							}
						}
					}
				}
				double ratio = FtNWon / (FtNWon + FtNLost);
				if (ratio >= minratio && ratio < minratio + 0.1) {
					double breakpercentage = breaksmade / noGames;
					breakpercentage_list.add(breakpercentage);
					fiveToNine_list.add(ratio);
					noGamestotal += noGames;
					breaksmadetotal += breaksmade;
				}
			}
			double breakpercentagetotal = breaksmadetotal / noGamestotal;
			breakpercentages_Ratio.add(breakpercentagetotal);
		}

		double corcoef = StatsTools.correlationcoefficient(ArrayTools.ArrayListToArray(breakpercentage_list),
				ArrayTools.ArrayListToArray(fiveToNine_list));
		PlayerStatList psl = new PlayerStatList(playername, new ArrayList<Double>(Arrays.asList(corcoef)),
				new ArrayList<String>(Arrays.asList("Correlation between break percentage and 5 to 9 shot rallies won")), noGamestotal);
		return psl;
	}
	
	public static PlayerStatList breakWith5To9MultiplePlayers(ArrayList<String> playernames) throws IOException {
		
		PlayerStatList psl = new PlayerStatList("All players", new ArrayList<Double>(Arrays.asList(0d)),new ArrayList<String>(Arrays.asList("")));
		//ArrayList<String> playernames = LoadValues.loadAllPlayernames();
		for(String playername : playernames){
			PlayerStatList current_psl = breakWith5To9(playername, LoadValues.loadAllMatchesOfPlayer(playername));
			double totalInstancesPlayed = psl.getInstancesPlayed();
			double currentInstancesPlayed = current_psl.getInstancesPlayed();
			psl.getStats().set(0, (psl.getStats().get(0) * totalInstancesPlayed + current_psl.getStats().get(0) * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed));
			psl.getStatNames().set(0, current_psl.getStatNames().get(0));
		}
		return psl;
	}

	public static MatchStatList averagePointLength(Match m){
		ArrayList<Double> rallylengths = new ArrayList<Double>();
		for(Point p : m.getPoints()){
			rallylengths.add((double) p.getNoShotsIn());
		}
		double[] result = {StatsTools.mean(ArrayTools.ArrayListToArray(rallylengths)), 0d};
		MatchStatList msl = new MatchStatList(m.getPlayers()[0].getName(), m.getPlayers()[1].getName(), new ArrayList<double[]>(Arrays.asList(result)),new ArrayList<String>(Arrays.asList("Average Point length")));
		return msl;
	}

	public static PlayerStatList rallyLengthsPercentages(String playername, ArrayList<Match> matches) throws IOException {

		double[] lengths = { 4, 8, 1000 };
		ArrayList<Double> counts = new ArrayList<Double>();
		ArrayList<Double> won = new ArrayList<Double>();
		ArrayList<Double> lost = new ArrayList<Double>();
		ArrayList<Double> won_ratio = new ArrayList<Double>();
		ArrayList<Double> won_ratio_total = new ArrayList<Double>();
		ArrayList<Double> played_ratio = new ArrayList<Double>();
		for (int i = 0; i < lengths.length; i++) {
			counts.add(0d);
			won.add(0d);
			lost.add(0d);
			won_ratio.add(0d);
			won_ratio_total.add(0d);
			played_ratio.add(0d);
		}
		ArrayList<Double> rallylengths = new ArrayList<Double>();

		for (Match m : matches) {
			for (Point p : m.getPoints()) {
				rallylengths.add((double) p.getNoShotsIn());
				for (int j = 0; j < lengths.length; j++) {
					if(p.getNoShotsIn() <= lengths[j]) {
						counts.set(j, counts.get(j) + 1);
						if (p.getWinner().getName().equals(playername)) {
							won.set(j, won.get(j) + 1);
						}
						else{
							lost.set(j, won.get(j) + 1);
						}
						break;
					}
				}
			}
		}

		double averagelength = StatsTools.mean(ArrayTools.ArrayListToArray(rallylengths));
		double allpoints = StatsTools.sumOfArray(ArrayTools.ArrayListToArray(counts));
		double allwon = StatsTools.sumOfArray(ArrayTools.ArrayListToArray(won));
		
		double[] result = new double[lengths.length];
		PlayerStatList psl = new PlayerStatList(playername);
		
		for (int k = 0; k < lengths.length; k++) {
			result[k] = won.get(k) / counts.get(k);
			won_ratio.set(k, won.get(k) / counts.get(k));
			won_ratio_total.set(k, won.get(k) / allpoints);
			played_ratio.set(k, counts.get(k) / allpoints);
		}
		psl.setStats(ArrayTools.ArrayToArrayList(result));
		psl.setStatNames(ArrayTools.arrayToStringArrayList(lengths));
		boolean output = false;
		if (output) {
			System.out.println("points won ratios: " + won_ratio);
			System.out.println("points played ratios: " + played_ratio);
			System.out.println("points won out of all points played: " + won_ratio_total);
			System.out.println("points won overall ratio: " + (allwon / allpoints));
			System.out.println("average point length: " + averagelength);
		}
		return psl;
	}

	public static PlayerStatListCollection rallyLengthsPercentagesAllPlayers(ArrayList<String> playernames) throws IOException {

		PlayerStatListCollection plsC = new PlayerStatListCollection();
		for(String playername : playernames){
			PlayerStatList psl = rallyLengthsPercentages(playername, LoadValues.loadAllMatchesOfPlayer(playername));
			plsC.add(psl);
		}
		return plsC;
	}

	public static void netPointsWon() throws IOException {
		ArrayList<String> names = new ArrayList<String>();

		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		// Spielerliste erstellen
		for (File file : folder.listFiles()) {
			String name = file.getName();
			String[] names_split = name.replace(".csv", "").split("-");
			String firstname = names_split[0];
			String secondname = names_split[1];
			if (!names.contains(firstname) && !Shottypes.playersWhereErrorsHappen.contains(firstname)) {
				names.add(firstname);
			}
			if (!names.contains(secondname) && !Shottypes.playersWhereErrorsHappen.contains(secondname)) {
				names.add(secondname);
			}
		}
		// ---
		ArrayList<Double> approaches = new ArrayList<Double>();
		ArrayList<Double> approaches_won = new ArrayList<Double>();
		double[][] netResults = new double[3][names.size()];
		for (int i = 0; i < names.size(); i++) {
			// System.out.println(names.get(i));
			double[] current_netResults_abs = netPointsWon(names.get(i));
			netResults[0][i] = current_netResults_abs[1] / current_netResults_abs[0]; // Anteil
																						// gewonnene
																						// Punkte
																						// am
																						// Netz
																						// mit
																						// Netzangriffen
			netResults[1][i] = current_netResults_abs[0] / current_netResults_abs[2]; // Anteil
																						// Netzangriffe
																						// an
																						// allen
																						// Punkten
			netResults[2][i] = current_netResults_abs[2]; // Anzahl aller Punkte
			if (current_netResults_abs[0] >= 20) {
				approaches.add(current_netResults_abs[0] / current_netResults_abs[2]);
				approaches_won.add(current_netResults_abs[1] / current_netResults_abs[0]);
			}
		}

		double[] approaches_array = ArrayTools.ArrayListToArray(approaches);
		double[] approaches_won_array = ArrayTools.ArrayListToArray(approaches_won);
		// double[] boxes =
		// {0,0.03,0.04,0.05,0.06,0.07,0.08,0.09,0.1,0.11,0.12,0.13,0.14,0.15,0.16,0.17,0.18,0.19,0.2,0.3,1};
		double[] boxes = { 0, 0.03, 0.06, 0.09, 0.12, 0.15, 0.18, 0.21, 0.24, 0.27, 1 };
		double[] box_averages = new double[boxes.length];
		int index = -1;
		int previous_index = 0;
		for (int j = 0; j < boxes.length - 1; j++) {
			double[] slice_approaches = ArrayTools.sliceArrayBounds(approaches_array, boxes[j], boxes[j + 1]);
			previous_index = Math.max(index, 0);
			index += slice_approaches.length;
			double[] slice_approaches_won = ArrayTools.sliceArrayIndices(approaches_won_array, previous_index, index);
			box_averages[j] = StatsTools.mean(slice_approaches_won);
			System.out.println(boxes[j] + " : " + box_averages[j]);
		}

		PlotTools.plot(boxes, box_averages, "Boxes");
		PlotTools.plot(netResults[1], netResults[0], "Net Results (all)");
		PlotTools.plot(approaches_array, approaches_won_array, "Net results (over 20)");
		QuickSort.sortiere_strings(netResults[0], 0, names);
		for (int j = 0; j < netResults[0].length; j++) {
			System.out.println(names.get(j) + " : " + netResults[0][j]);
		}
		PlotTools.plotBar(netResults[0], names);
	}

	public static double[] netPointsWon(String playername) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		double[] result = new double[3];
		double approachCounter = 0;
		double approachWonCounter = 0;
		for (Point p : points) {
			boolean approach = false;
			// Bei Serve&Volley:
			if (p.getServer().getName().equals(playername) && p.getSuccesfullserve().isApproach()) {
				approach = true;
			}
			int mod_value = 0;
			if (p.getServer().getName().equals(playername)) {
				mod_value = 1; // Wenn Spieler aufschlaegt, dann schaue jeden
								// geraden Schlag in der Rally an
			}
			for (int i = 0; i < p.getShots().size(); i++) { // Ueberpruefen, ob
															// ans Netz gegangen
															// wurde
				if (Math.floorMod(i, 2) == mod_value) {
					if (p.getShots().get(i).isApproach()) {
						approach = true;
						break;
					}
				}
			}
			if (approach) {
				approachCounter++;
				if (p.getWinner().getName().equals(playername)) {
					approachWonCounter++;
				}
			}
		}
		result[0] = approachCounter; // Punkte mit Netzangriff
		result[1] = approachWonCounter; // Erfolgreichen Netzangriffe
		result[2] = points.size();
		return result;
	}

	// Zaehlt, wie viele Punkte eine Spieler gegen einen anderen durch Winner gewonnen hat geteilt durch die Anzahl der Punkte,
	// die er insgesamt gewonnen hat
	public static MatchStatList pointsWonWithWinners(Match m) {

		double[] pointswonbywinner = {0d,0};
		double[] pointswon = {0d,0};

		for (Point p : m.getPoints()) {
			if(p.getShots().size() > 0){
				int playerindex = MatchTools.getPlayerIndex(p.getWinner(), m);
				pointswon[playerindex]++;
				if (p.getLastShot().getOutcome().equals(Shottypes.winner)) {
					pointswonbywinner[playerindex]++;
				}
			}
		}

		double[] result = { pointswonbywinner[0] / pointswon[0], pointswonbywinner[1] / pointswon[1]};
		MatchStatList msl = new MatchStatList(m.getPlayers()[0].getName(), m.getPlayers()[1].getName(), result, "points won by winner ratio");
		return msl;
	}

	public static void longlineMatchWinPercentage() throws IOException {

		double LLshots_winner = 0; // Zaehlt, wie oft derjenige gewinnt, der
									// oefter longline gespielt hat
		double matchcounter = 0;
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		for (File file : folder.listFiles()) {
			String name = file.getName();
			String[] names_split = name.replace(".csv", "").split("-");
			String firstname = names_split[0];
			String secondname = names_split[1];
			if (!Shottypes.playersWhereErrorsHappen.contains(firstname)
					&& !Shottypes.playersWhereErrorsHappen.contains(secondname)) {
				Match m = LoadMatchFromTennisAbstract.readCSVFile(file);
				MatchStatList currentLLShots_msl = countLongLineShots(m);
				double[] currentLLShots = currentLLShots_msl.getStats().get(0);
				if (currentLLShots[0] != currentLLShots[1]) {
					matchcounter++;
					if ((currentLLShots[0] > currentLLShots[1] && m.getWinner().equals(m.getPlayers()[0]))
							|| (currentLLShots[1] > currentLLShots[0] && m.getWinner().equals(m.getPlayers()[1]))) {
						LLshots_winner++;
					}
				}
			}
		}
		System.out.println(LLshots_winner + " , matches: " + matchcounter);
		System.out.println(LLshots_winner / matchcounter);
	}

	// Zaehlt die relative Anzahl der Longline-Schlaege beider Spieler in einem Match
	public static MatchStatList countLongLineShots(Match m) {
		double[] longlineshots = new double[2];
		double[] shots_hit = new double[2];
		for (Point p : m.getPoints()) {
			for (int i = 1; i < p.getShots().size(); i++) {
				if (Math.floorMod(i, 2) == 0) {
					if (p.getServer().equals(m.getPlayers()[0])) {
						shots_hit[1]++;
					} else {
						shots_hit[0]++;
					}
				}
				if (Math.floorMod(i, 2) == 1) {
					if (p.getServer().equals(m.getPlayers()[0])) {
						shots_hit[0]++;
					} else {
						shots_hit[1]++;
					}
				}
				// System.out.println(p.getShots().get(i).getDirection() + " , "
				// + p.getShots().get(i).getPosition());
				if (Math.abs(p.getShots().get(i).getDirection() - p.getShots().get(i - 1).getDirection()) == 2
						&& p.getShots().get(i).getDirection() != 2) {
					if (Math.floorMod(i, 2) == 0) {
						if (p.getServer().equals(m.getPlayers()[0])) {
							longlineshots[1]++;
						} else {
							longlineshots[0]++;
						}
					}
					if (Math.floorMod(i, 2) == 1) {
						if (p.getServer().equals(m.getPlayers()[0])) {
							longlineshots[0]++;
						} else {
							longlineshots[1]++;
						}
					}
				}
			}
		}
		// System.out.println(longlineshots[0] + " , " + longlineshots[1]);
		longlineshots[0] = longlineshots[0] / shots_hit[0];
		longlineshots[1] = longlineshots[1] / shots_hit[1];
		MatchStatList msl = new MatchStatList(m.getPlayers()[0].getName(), m.getPlayers()[1].getName(), longlineshots, "Shots hit longline ratio");
		return msl;
	}

	// Zaehlt, wie oft ein Spieler ein bestimmtes Pattern anwendet und wie oft er damit den Punkt gewinnt
	public static void bFAggressiveness(String playername) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		// System.out.println("Total points: " + points.size());
		double pattern_counter = 0;
		double pattern_won_counter = 0;
		double pattern_total_counter = 0;
		double attack_counter = 0;
		double shotsuntil_sum = 0;
		playername = Tools.nameConversion(playername);
		// System.out.println(playername);
		boolean loadall_bool = false;
		if (playername.equals("")) {
			loadall_bool = true;
		}
		Shot shot0 = new Shot();
		shot0.setShottype(Shottypes.forehand);
		shot0.setPosition(3);

		Shot shot1 = new Shot();
		shot1.setShottype(Shottypes.backhand);
		shot1.setPosition(3);

		Shot shot2 = new Shot();
		shot2.setShottype(Shottypes.forehand);

		ArrayList<Shot> shots = new ArrayList<Shot>(Arrays.asList(shot2, shot1, shot0));
		for (Point p : points) {
			boolean pattern_bool1 = false;
			boolean pattern_won_bool1 = false;
			boolean pattern_bool2 = false;
			boolean pattern_won_bool2 = false;
			for (int i = 2; i < p.getShots().size(); i++) {
				boolean cur_pb1 = false;
				boolean cur_pb2 = false;

				boolean shotsequencebool = MatchTools.compareShotSequence(shots, p.getShots(), i);
				if (shotsequencebool) {

					if ((p.getReturner().getName().equals(playername) || loadall_bool) && Math.floorMod(i, 2) == 0) {
						pattern_bool1 = true;
						cur_pb1 = true;
						pattern_total_counter++;
						if (p.getWinner().equals(p.getReturner())) {
							pattern_won_bool1 = true;
						}
					} else if ((p.getServer().getName().equals(playername) || loadall_bool)
							&& Math.floorMod(i, 2) == 1) {
						pattern_bool2 = true;
						cur_pb2 = true;
						pattern_total_counter++;
						if (p.getWinner().equals(p.getServer())) {
							pattern_won_bool1 = true;
						}
					}
				}
				if (cur_pb1 || cur_pb2) {
					if (p.getShots().size() == i + 1) {
						// System.out.println("hier");
						attack_counter++;
					} else if (p.getShots().size() >= i + 3) {
						if (p.getShots().get(i + 2).getSpecifictype().equals(Shottypes.volley)) {
							// System.out.println("hier 3");
							attack_counter++;
						}
					}
				}
				cur_pb1 = false;
				cur_pb2 = false;
			}
			if (pattern_bool1)
				pattern_counter++;
			if (pattern_bool2)
				pattern_counter++;
			if (pattern_won_bool1)
				pattern_won_counter++;
		}
		double win_ratio = pattern_won_counter / pattern_counter;
		// System.out.println("attack counter = " + attack_counter);
		// System.out.println("pattern total counter = " +
		// pattern_total_counter);
		// System.out.println("Win ratio = " + win_ratio + " , pattern_counter =
		// " + pattern_counter);

		double attack_ratio = attack_counter / pattern_total_counter;
		// System.out.println("Attack ratio = " + attack_ratio + " ,
		// pattern_total_counter = " + pattern_total_counter);
	}

	// Zaehlt, wie oft ein Spieler nach Anwendung eines bestimmten Patterns ans Netz gegangen ist
	public static double[] patternAnalysisAggressiveness(ArrayList<Point> points, String playername,
			ArrayList<Shot> shots) throws IOException {
		// System.out.println("Total points: " + points.size());
		double pattern_counter = 0;
		double pattern_won_counter = 0;
		double pattern_total_counter = 0;
		double attack_counter = 0;
		double shotsuntil_sum = 0;
		playername = Tools.nameConversion(playername);
		// System.out.println(playername);
		boolean loadall_bool = false;
		if (playername.equals("")) {
			loadall_bool = true;
		}
		for (Point p : points) {
			boolean pattern_bool1 = false;
			boolean pattern_won_bool1 = false;
			boolean pattern_bool2 = false;
			boolean pattern_won_bool2 = false;
			for (int i = 2; i < p.getShots().size(); i++) {
				boolean cur_pb1 = false;
				boolean cur_pb2 = false;

				boolean shotsequencebool = MatchTools.compareShotSequence(shots, p.getShots(), i);
				if (shotsequencebool) {
					if ((p.getReturner().getName().equals(playername) || loadall_bool) && Math.floorMod(i, 2) == 0) {
						pattern_bool1 = true;
						cur_pb1 = true;
						pattern_total_counter++;
						if (p.getWinner().equals(p.getReturner())) {
							pattern_won_bool1 = true;
						}
					} else if ((p.getServer().getName().equals(playername) || loadall_bool)
							&& Math.floorMod(i, 2) == 1) {
						pattern_bool2 = true;
						cur_pb2 = true;
						pattern_total_counter++;
						if (p.getWinner().equals(p.getServer())) {
							pattern_won_bool1 = true;
						}
					}
				}
				if (cur_pb1 || cur_pb2) {
					if(p.getShots().get(i+2).isApproach()){
						attack_counter++;
					}
					/*
					if (p.getShots().size() == i + 1) {
						// System.out.println("hier");
						attack_counter++;
					} else if (p.getShots().size() >= i + 3) {
						if (p.getShots().get(i + 2).getSpecifictype().equals(Shottypes.volley)) {
							// System.out.println("hier 3");
							attack_counter++;
						}
					}
					*/
				}
				cur_pb1 = false;
				cur_pb2 = false;
			}
			if (pattern_bool1)
				pattern_counter++;
			if (pattern_bool2)
				pattern_counter++;
			if (pattern_won_bool1)
				pattern_won_counter++;
		}
		double win_ratio = pattern_won_counter / pattern_counter;
		// System.out.println("attack counter = " + attack_counter);
		// System.out.println("Win ratio = " + win_ratio + " , pattern_counter =
		// " + pattern_counter);

		double attack_ratio = attack_counter / pattern_total_counter;
		// System.out.println("Attack ratio = " + attack_ratio + " ,
		// pattern_total_counter = " + pattern_total_counter);

		double[] output = new double[2];
		output[0] = win_ratio;
		output[1] = pattern_counter;
		return output;
	}

	public static void patternAnalysisAggressiveness(String playername, ArrayList<Shot> shots) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
		patternAnalysisAggressiveness(points, playername, shots);
	}

	public static void allPatternsAnalysis(String playername) throws IOException {

		ArrayList<String> groundstroketypes = new ArrayList<String>(
				Arrays.asList(Shottypes.forehand, Shottypes.backhand));
		ArrayList<Integer> positions = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
		int counter = 0;
		int max_pattern_length = 5;
		int min_pattern_length = 2;
		// double[] results = new double[60000]; // 2000 muss noch weg
		ArrayList<Double> results = new ArrayList<Double>();
		ArrayList<Double> lengths = new ArrayList<Double>();
		ArrayList<ArrayList<Shot>> allPatternsFull = new ArrayList<ArrayList<Shot>>();

		for (int i = min_pattern_length; i <= max_pattern_length; i++) {
			ArrayList<ArrayList<Shot>> patterns = new ArrayList<ArrayList<Shot>>();
			for (int j = 0; j < Math.pow(groundstroketypes.size(), i); j++) {
				ArrayList<Shot> shots = new ArrayList<Shot>();
				for (int k = 0; k < i; k++) {
					Shot shot = new Shot();
					shot.setShottype(Shottypes.forehand);
					shots.add(shot);

				}
				patterns.add(shots);
			}

			for (int l = 0; l < i; l++) {
				for (int p = 0; p < Math.pow(groundstroketypes.size(), i); p++) {
					if (Math.floorMod(
							(int) (Math.round((p + 1) * Math.pow(groundstroketypes.size(), l - i) - 0.000001)),
							2) == 0) {
						patterns.get(p).get(l).setShottype(Shottypes.backhand);
					}
				}

			}
			// Liste mit groundstroketypes fertig

			// Nun: Position hinzufügen
			ArrayList<ArrayList<Shot>> allPatterns = new ArrayList<ArrayList<Shot>>();
			allPatterns = patterns;
			// allPatterns = extendPatternList(patterns, "position", i);

			/*
			 * ArrayList<ArrayList<ArrayList<Shot>>> patterns_wpos = new
			 * ArrayList<ArrayList<ArrayList<Shot>>>(); for(ArrayList<Shot> list
			 * : patterns){ ArrayList<ArrayList<Shot>> pos_list = new
			 * ArrayList<ArrayList<Shot>>(); for ( int d = 0; d <
			 * Math.pow(positions.size(),list.size()); d++){ ArrayList<Shot>
			 * current_sublist = new ArrayList<Shot>(); for(int f = 0; f <
			 * list.size(); f++){ Shot s = new Shot();
			 * s.setShottype(list.get(f).getShottype());
			 * s.setPosition(positions.get(
			 * Math.floorMod((int)(Math.round(positions.size()*(d+1) *
			 * Math.pow(positions.size(), f-i)-0.000001)),4)));
			 * current_sublist.add(s); } pos_list.add(current_sublist);
			 * allPatterns.add(current_sublist); } patterns_wpos.add(pos_list);
			 * }
			 */
			/*
			 * for(int s = 0; s < patterns_wpos.size(); s++){ for(int a = 0; a <
			 * i; a++){ for(int w = 0; w < Math.pow(positions.size(),
			 * i)/*patterns_wpos.get(s).get(a).size(); w++){
			 * //if(Math.floorMod((int)(Math.round((w+1) *
			 * Math.pow(groundstroketypes.size(), l-i)-0.000001)),4) == 1 ){
			 * //System.out.println(Math.floorMod((int)(Math.round(4*(w+1) *
			 * Math.pow(positions.size(), a-i)-0.000001)),4)); int newvalue =
			 * Math.floorMod((int)(Math.round(positions.size()*(w+1) *
			 * Math.pow(positions.size(), a-i)-0.000001)),4);
			 * patterns_wpos.get(s).get(w).get(a).setPosition(newvalue); //} } }
			 * allPatterns.addAll(patterns_wpos.get(s)); }
			 */

			// prints
			ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername);
			for (int h = 0; h < allPatterns.size(); h++) {
				double[] pattern_output = patternAnalysisAggressiveness(points, playername, allPatterns.get(h));
				double winratio = pattern_output[0];
				double patterncounter = pattern_output[1];
				if (!Double.isNaN(winratio) && pattern_output[1] >= 10) {
					// results[counter] = winratio;
					results.add(winratio);
					lengths.add(patterncounter);
					counter++;
					allPatternsFull.add(allPatterns.get(h));
				}
				/*
				 * for(int g = 0; g < allPatterns.get(h).size(); g++){
				 * System.out.print(allPatterns.get(h).get(g).getPosition() +
				 * " , "); } System.out.println("");
				 */

			}
			// allPatternsFull.addAll(allPatterns);
		}
		System.out.println(allPatternsFull.size() + " , " + results.size());
		double[] results_array = ArrayTools.ArrayListToArray(results);
		double[] lengths_array = ArrayTools.ArrayListToArray(lengths);
		System.out.println(lengths_array.length);
		PlotTools.plot(results_array, "All Patterns");
		QuickSort.sortiere(lengths_array, results_array, 0, allPatternsFull);
		PlotTools.plot(lengths_array, "Sorted By: Number of Occurences");
		QuickSort.sortiere(results_array, lengths_array, 0, allPatternsFull);
		PlotTools.plot(results_array, "Sorted By: Win ratio");

		System.out.println("Most successfull:");
		for (int e = results_array.length - 1; e >= results_array.length - 3; e--) {
			for (Shot shot : allPatternsFull.get(e)) {
				System.out.print(shot.getShottype() + " , " + shot.getPosition() + " | ");
			}
			System.out.println(" " + results_array[e]);
		}
		System.out.println("Least successfull:");
		for (int e = 0; e < 3; e++) {
			for (Shot shot : allPatternsFull.get(e)) {
				System.out.print(shot.getShottype() + " , " + shot.getPosition() + " | ");
			}
			System.out.println(" " + results_array[e]);
		}

	}

	public static ArrayList<ArrayList<Shot>> extendPatternList(ArrayList<ArrayList<Shot>> patterns, String type,
			int patternlength) {

		ArrayList<ArrayList<Shot>> allPatterns = new ArrayList<ArrayList<Shot>>();

		// ArrayList<Object> consideredList = new
		// ArrayList<Integer>(Arrays.asList(0,1,2,3));
		int listsize = 0;
		switch (type) {
		case "position":
			listsize = Shottypes.positions.size();
		case "direction":
			Shottypes.directions.size();
		case "groundstroke":
			Shottypes.groundstroketypes.size();
		default:
			break;
		}

		for (ArrayList<Shot> list : patterns) {
			for (int d = 0; d < Math.pow(listsize, list.size()); d++) {
				ArrayList<Shot> current_sublist = new ArrayList<Shot>();
				for (int f = 0; f < list.size(); f++) {
					Shot s = MatchTools.cloneShot(list.get(f));
					if (type.equals("position"))
						s.setPosition(
								Shottypes.positions
										.get(Math
												.floorMod(
														(int) (Math.round(Shottypes.positions.size() * (d + 1)
																* Math.pow(Shottypes.positions.size(),
																		f - patternlength)
																- 0.000001)),
														Shottypes.positions.size())));
					// else if(type.equals("direction"))
					// s.setDirection(Shottypes.directions.get(
					// Math.floorMod((int)(Math.round(Shottypes.directions.size()*(d+1)
					// * Math.pow(Shottypes.directions.size(),
					// f-patternlength)-0.000001)),Shottypes.directions.size())));
					// else if(type.equals("groundstroke"))
					// System.out.println("HALLLLLLLLLLLLLLLLLLLLLLLLLOOOOOOOOOOOOOOOOO");
					// s.setShottype(Shottypes.groundstroketypes.get(
					// Math.floorMod((int)(Math.round(Shottypes.groundstroketypes.size()*(d+1)
					// * Math.pow(Shottypes.groundstroketypes.size(),
					// f-patternlength)-0.000001)),Shottypes.groundstroketypes.size())));
					// System.out.print(s.getShottype() + " , " +
					// s.getPosition() + " | ");
					current_sublist.add(s);
				}
				// System.out.println("");
				allPatterns.add(current_sublist);
			}
		}
		return allPatterns;
	}

	public static double[] serviceGameHoldPercentages(String playername, ArrayList<Match> matches) throws IOException {
		File folder = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/");
		double[] games = new double[2];
		double[] fifteenLove = new double[2];
		double[] loveFifteen = new double[2];
		double[] fifteenAll = new double[2];
		double[] thirtyLove = new double[2];
		double[] loveThirty = new double[2];
		double[] thirtyFifteen = new double[2];
		double[] fifteenThirty = new double[2];
		double[] thirtyAll = new double[2];
		double[] fortyThirty = new double[2];
		double[] thirtyForty = new double[2];
		double[] fortyLove = new double[2];
		double[] loveForty = new double[2];
		double[] fortyFifteen = new double[2];
		double[] fifteenForty = new double[2];
		double[] deuce = new double[2];
		double[] advantageServer = new double[2];
		double[] advantageReturner = new double[2];
		double[] deucePlus = new double[2];

		for (Match m : matches) {
				String opponentName = m.getPlayers()[1].getName();
				if(opponentName.equals(playername)){
					opponentName = m.getPlayers()[0].getName();
				}
				for (Game g : m.getGames()) {
					if (g.getServer().getName().equals(playername)) {
						
						// Spiele insgesamt
						games[1]++;
						if (g.getWinner().getName().equals(playername)) {
							games[0]++;
						}
						
						// 15:0- und 0:15-Spiele
						if (g.getPoints().size() >= 2) {
							if (g.getPoints().get(1).getScore().getPointsOf(playername) == 1
									&& g.getPoints().get(1).getScore().getPointsOf(opponentName) == 0) {
								fifteenLove[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fifteenLove[0]++;
								}
							}
							else if(g.getPoints().get(1).getScore().getPointsOf(playername) == 0
									&& g.getPoints().get(1).getScore().getPointsOf(opponentName) == 1){
								loveFifteen[1]++;
								if (g.getWinner().getName().equals(playername)) {
									loveFifteen[0]++;
								}
							}
						}						
						// 15:15- und 30:0- und 0:30-Spiele
						if (g.getPoints().size() >= 3) {
							if (g.getPoints().get(2).getScore().getPointsOf(playername)== 1
									&& g.getPoints().get(2).getScore().getPointsOf(opponentName) == 1) {
								fifteenAll[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fifteenAll[0]++;
								}
							}
							else if (g.getPoints().get(2).getScore().getPointsOf(playername) == 2
									&& g.getPoints().get(2).getScore().getPointsOf(opponentName) == 0) {
								thirtyLove[1]++;
								if (g.getWinner().getName().equals(playername)) {
									thirtyLove[0]++;
								}
							}
							if (g.getPoints().get(2).getScore().getPointsOf(playername) == 0
									&& g.getPoints().get(2).getScore().getPointsOf(opponentName) == 2) {
								loveThirty[1]++;
								if (g.getWinner().getName().equals(playername)) {
									loveThirty[0]++;
								}
							}
						}
						
						// 30:15-, 15:30-, 40:0- und 0:40-Spiele
						if (g.getPoints().size() >= 4) {
							if (g.getPoints().get(3).getScore().getPointsOf(playername)== 2
									&& g.getPoints().get(3).getScore().getPointsOf(opponentName) == 1) {
								thirtyFifteen[1]++;
								if (g.getWinner().getName().equals(playername)) {
									thirtyFifteen[0]++;
								}
							}
							else if (g.getPoints().get(3).getScore().getPointsOf(playername) == 1
									&& g.getPoints().get(3).getScore().getPointsOf(opponentName) == 2) {
								fifteenThirty[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fifteenThirty[0]++;
								}
							}
							if (g.getPoints().get(3).getScore().getPointsOf(playername) == 3
									&& g.getPoints().get(3).getScore().getPointsOf(opponentName) == 0) {
								fortyLove[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fortyLove[0]++;
								}
							}
							if (g.getPoints().get(3).getScore().getPointsOf(playername) == 0
									&& g.getPoints().get(3).getScore().getPointsOf(opponentName) == 3) {
								loveForty[1]++;
								if (g.getWinner().getName().equals(playername)) {
									loveForty[0]++;
								}
							}
						}
						// 30:30-, 40:15- und 15:40-Spiele
						if (g.getPoints().size() >= 5) {
							if (g.getPoints().get(4).getScore().getPointsOf(playername) == 2
									&& g.getPoints().get(4).getScore().getPointsOf(opponentName) == 2) {
								thirtyAll[1]++;
								if (g.getWinner().getName().equals(playername)) {
									thirtyAll[0]++;
								}
							}
							if (g.getPoints().get(4).getScore().getPointsOf(playername) == 3
									&& g.getPoints().get(4).getScore().getPointsOf(opponentName) == 1) {
								fortyFifteen[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fortyFifteen[0]++;
								}
							}
							if (g.getPoints().get(4).getScore().getPointsOf(playername) == 1
									&& g.getPoints().get(4).getScore().getPointsOf(opponentName) == 3) {
								fifteenForty[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fifteenForty[0]++;
								}
							}
						}
						// 40:30- und 30:40-Spiele
						if (g.getPoints().size() >= 6) {
							if (g.getPoints().get(5).getScore().getPointsOf(playername) == 3
									&& g.getPoints().get(5).getScore().getPointsOf(opponentName) == 2) {
								fortyThirty[1]++;
								if (g.getWinner().getName().equals(playername)) {
									fortyThirty[0]++;
								}
							}
							if (g.getPoints().get(5).getScore().getPointsOf(playername) == 2
									&& g.getPoints().get(5).getScore().getPointsOf(opponentName) == 3) {
								thirtyForty[1]++;
								if (g.getWinner().getName().equals(playername)) {
									thirtyForty[0]++;
								}
							}
						}
						// Deuce-Spiele
						if (g.getPoints().size() >= 7) {
							if (g.getPoints().get(6).getScore().getPointsOf(playername) == 3
									&& g.getPoints().get(6).getScore().getPointsOf(opponentName) == 3) {
								deuce[1]++;
								if (g.getWinner().getName().equals(playername)) {
									deuce[0]++;
								}
							}
						}
						// AD:40- und 40:AD-Spiele
						if (g.getPoints().size() >= 8) {
							if (g.getPoints().get(7).getScore().getPointsOf(playername) == 4
									&& g.getPoints().get(7).getScore().getPointsOf(opponentName) == 3) {
								advantageServer[1]++;
								if (g.getWinner().getName().equals(playername)) {
									advantageServer[0]++;
								}
							}
							if (g.getPoints().get(7).getScore().getPointsOf(playername) == 3
									&& g.getPoints().get(7).getScore().getPointsOf(opponentName) == 4) {
								advantageReturner[1]++;
								if (g.getWinner().getName().equals(playername)) {
									advantageReturner[0]++;
								}
							}
						}
						// "Mehr als ein Deuce" Spiele
						if (g.getPoints().size() > 6) {
							if (!(g.getPoints().get(5).getScore().getPointsOf(playername) == 3)
									|| (!(g.getPoints().get(5).getScore().getPointsOf(opponentName) == 3))) {
							}
							deucePlus[1]++;
							if (g.getWinner().getName().equals(playername)) {
								deucePlus[0]++;
							}
						}
					}
				}
		}
		double games_rel = games[0] / games[1];
		double fifteenLove_rel = fifteenLove[0] / fifteenLove[1];
		double loveFifteen_rel = loveFifteen[0] / loveFifteen[1];
		double fifteenAll_rel = fifteenAll[0] / fifteenAll[1];
		double thirtyLove_rel = thirtyLove[0] / thirtyLove[1];
		double loveThirty_rel = loveThirty[0] / loveThirty[1];
		double thirtyFifteen_rel = thirtyFifteen[0] / thirtyFifteen[1];
		double fifteenThirty_rel = fifteenThirty[0] / fifteenThirty[1];
		double fortyLove_rel = fortyLove[0] / fortyLove[1];
		double loveForty_rel = loveForty[0] / loveForty[1];
		double thirtyAll_rel = thirtyAll[0] / thirtyAll[1];
		double fortyFifteen_rel = fortyFifteen[0] / fortyFifteen[1];
		double fifteenForty_rel = fifteenForty[0] / fifteenForty[1];
		double fortyThirty_rel = fortyThirty[0] / fortyThirty[1];
		double thirtyForty_rel = thirtyForty[0] / thirtyForty[1];
		double deuce_rel = deuce[0] / deuce[1];
		double advantageServer_rel = advantageServer[0] / advantageServer[1];
		double advantageReturner_rel = advantageReturner[0] / advantageReturner[1];
		/*System.out.println("0:0 " + games[0] / games[1]);
		System.out.println("15:0 " + fifteenLove[0] / fifteenLove[1]);
		System.out.println("0:15 " + loveFifteen[0] / loveFifteen[1]);
		System.out.println("15:15 " + fifteenAll[0] / fifteenAll[1]);
		System.out.println("30:0 " + thirtyLove[0] / thirtyLove[1]);
		System.out.println("0:30 " + loveThirty[0] / loveThirty[1]);
		System.out.println("40:0 " + fortyLove[0] / fortyLove[1]);
		System.out.println("0:40 " + loveForty[0] / loveForty[1]);
		System.out.println("15:15: " + fifteenAll[0] / fifteenAll[1]);
		System.out.println("30:15 " + thirtyFifteen[0] / thirtyFifteen[1]);
		System.out.println("15:30 " + fifteenThirty[0] / fifteenThirty[1]);
		System.out.println("40:15 " + fortyLove[0] / fortyLove[1]);
		System.out.println("15:40 " + fifteenForty[0] / fifteenForty[1]);
		System.out.println("30:30: " + thirtyAll[0] / thirtyAll[1]);
		System.out.println("40:30 " + fortyThirty[0] / fortyThirty[1]);
		System.out.println("30:40 " + thirtyForty[0] / thirtyForty[1]);
		System.out.println("Deuce: " + deuce[0] / deuce[1]);
		System.out.println("AD:40 " + advantageServer[0] / advantageServer[1]);
		System.out.println("40:AD " + advantageReturner[0] / advantageReturner[1]);
		System.out.println("Deuce plus: " + deucePlus[0] / deucePlus[1]);*/
		
		double[] frequencies = {games_rel, fifteenLove_rel, loveFifteen_rel, fifteenAll_rel, thirtyLove_rel, loveThirty_rel, thirtyFifteen_rel, fifteenThirty_rel,
				fortyLove_rel, loveForty_rel, thirtyAll_rel, fortyFifteen_rel, fifteenForty_rel, fortyThirty_rel, thirtyForty_rel, deuce_rel, advantageServer_rel, advantageReturner_rel};
		return frequencies;
	}
	
	
	// Ermittelt die BreakPoint Saved und Made Werte und vergleicht mit den allgemeinen Return und Serve Points Won Werten jedes Spielers
	// Ausgegeben wird eine Liste mit: BreakPointsMade% / ReturnPointsWon% - 1 sowie BreakPointsSaved% / ServicePointsWon% - 1
	public static void breakPointRatioEvaluation(ArrayList<String> playernames) throws IOException{
		ArrayList<double[]> BPratio_list = new ArrayList<double[]>();
		for(String playername : playernames){
			ArrayList<Match> matches = LoadValues.loadAllMatchesOfPlayer(playername);
			double returnWinRatio = PlayerStandardStats.returnPointsWon(playername, matches);
			double sericeWinRatio = PlayerStandardStats.servicePointsWon(playername, matches);
			double bpMade = PlayerStandardStats.breakPointsMadePercentage(playername, matches).getStats().get(0);
			double bpSaved = PlayerStandardStats.breakPointsSavedPercentage(playername, matches).getStats().get(0);
			double returnValue = bpMade / returnWinRatio - 1;
			double serviceValue = bpSaved / sericeWinRatio - 1;
			double[] currentValues = {returnValue, serviceValue};
			BPratio_list.add(currentValues);
			System.out.println(playername + ": " + returnValue + " , " + serviceValue);
		}
	}
	
	

	// Tests fuer StandardStats
	public static void pointsWonTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] counter = StandardStats.pointsWonByPlayer(m);
		System.out.println(counter[0] + " , " + counter[1]);
	}

	public static void servePercentageTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] serveIn_rel = StandardStats.servePercentage(m);
		System.out.println(serveIn_rel[0] + " , " + serveIn_rel[1]);
	}

	public static void numberOfWinnersTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] winners = StandardStats.numberOfWinners(m);
		System.out.println(winners[0] + " , " + winners[1]);
	}

	public static void numberOfAcesTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] aces = StandardStats.numberOfAces(m);
		System.out.println(aces[0] + " , " + aces[1]);
	}

	public static void numberOfUETest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] unforcederrors = StandardStats.numberOfUE(m);
		System.out.println(unforcederrors[0] + " , " + unforcederrors[1]);
	}

	public static void firstServeWinPercentageTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] firstservewon = StandardStats.firstServeWinPercentage(m);
		System.out.println(firstservewon[0] + " , " + firstservewon[1]);
	}

	public static void secondServeWinPercentageTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] secondservewon = StandardStats.secondServeWinPercentage(m);
		System.out.println(secondservewon[0] + " , " + secondservewon[1]);
	}

	public static void totalServeWinPercentageTest() throws IOException {
		Match m = LoadMatchFromTennisAbstract.readCSVFile(
				new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/Marin_Cilic-Roger_Federer-20140906.csv"));
		double[] totalservewon = StandardStats.totalServeWinPercentage(m);
		System.out.println(totalservewon[0] + " , " + totalservewon[1]);
	}

}
