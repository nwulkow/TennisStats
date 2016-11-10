package analytics;

import java.io.IOException;
import java.util.ArrayList;

import analysisFormats.MatchStatList;
import counts.Match;
import counts.Point;
import counts.Shot;
import dataload.LoadValues;
import objects.Shottypes;
import player.Player;

public class Metrics {

	public static double[] f_Function(Point p, Player p1, Player p2) {

		double serverFH = 0;
		double serverBH = 0;
		double returnerFH = 0;
		double returnerBH = 0;
		int shotcounter = 0;
		for (Shot shot : p.getShots()) {
			String s = shot.getShottype();
			if (s.equals(Shottypes.forehand)) {
				if (Math.floorMod(shotcounter, 2) == 0) {
					returnerFH++;
				} else {
					serverFH++;
				}
			} else if (s.equals(Shottypes.backhand)) {
				if (Math.floorMod(shotcounter, 2) == 0) {
					returnerBH++;
				} else {
					serverBH++;
				}
			}
			shotcounter++;
		}

		double value1 = -10;
		double value2 = -10;
		if (serverFH + serverBH > 0 && returnerFH + returnerBH > 0) {

			if (p.getWinner().equals(p1) && p.getServer().equals(p1)) {
				value1 = serverFH / (serverFH + serverBH);
				value2 = returnerBH / (returnerFH + returnerBH);
			} else if (p.getWinner().equals(p1) && p.getServer().equals(p2)) {
				value1 = returnerFH / (returnerFH + returnerBH);
				value2 = serverBH / (serverFH + serverBH);
			} else if (p.getWinner().equals(p2) && p.getServer().equals(p1)) {
				value1 = serverBH / (serverFH + serverBH);
				value2 = returnerFH / (returnerFH + returnerBH);
			} else if (p.getWinner().equals(p2) && p.getServer().equals(p1)) {
				value1 = returnerBH / (returnerFH + returnerBH);
				value2 = serverFH / (serverFH + serverBH);
			}
		}
		double[] ret = new double[2];
		ret[0] = value1;
		ret[1] = value2;
		return ret;
	}

	public static double[] g_Function(Point p, Player p1, Player p2, double p1_v2, double p2_v2) {

		double serverFH = 0;
		double serverBH = 0;
		double returnerFH = 0;
		double returnerBH = 0;
		int shotcounter = 0;
		for (Shot shot : p.getShots()) {
			String s = shot.getShottype();
			if (s.equals(Shottypes.forehand)) {
				if (Math.floorMod(shotcounter, 2) == 0) {
					returnerFH++;
				} else {
					serverFH++;
				}
			} else if (s.equals(Shottypes.backhand)) {
				if (Math.floorMod(shotcounter, 2) == 0) {
					returnerBH++;
				} else {
					serverBH++;
				}
			}
			shotcounter++;
		}
		double g1 = -10;
		double g2 = -10;
		if (serverFH + serverBH > 0 && returnerFH + returnerBH > 0) {

			double ser_rel = serverFH / (serverFH + serverBH);
			double ret_rel = returnerFH / (returnerFH + returnerBH);

			if (p.getWinner().equals(p1) && p.getServer().equals(p1)) {
				g1 = p2_v2 * (returnerFH / (returnerFH + returnerBH))
						+ (1 - p2_v2) * (returnerBH / (returnerFH + returnerBH));
				g2 = (1 - p1_v2) * (serverFH / (serverFH + serverBH)) + p1_v2 * (serverBH / (serverFH + serverBH));
			} else if (p.getWinner().equals(p1) && p.getServer().equals(p2)) {
				g2 = (1 - p1_v2) * (returnerFH / (returnerFH + returnerBH))
						+ (p1_v2) * (returnerBH / (returnerFH + returnerBH));
				g1 = (p2_v2) * (serverFH / (serverFH + serverBH)) + (1 - p2_v2) * (serverBH / (serverFH + serverBH));
			} else if (p.getWinner().equals(p2) && p.getServer().equals(p1)) {
				g1 = (1 - p2_v2) * (returnerFH / (returnerFH + returnerBH))
						+ (p2_v2) * (returnerBH / (returnerFH + returnerBH));
				g2 = (p1_v2) * (serverFH / (serverFH + serverBH)) + (1 - p1_v2) * (serverBH / (serverFH + serverBH));
			} else if (p.getWinner().equals(p2) && p.getServer().equals(p2)) {
				g2 = (p1_v2) * (returnerFH / (returnerFH + returnerBH))
						+ (1 - p1_v2) * (returnerBH / (returnerFH + returnerBH));
				g1 = (1 - p2_v2) * (serverFH / (serverFH + serverBH)) + (p2_v2) * (serverBH / (serverFH + serverBH));
			}
		}
		double[] ret = new double[2];
		ret[0] = g1;
		ret[1] = g2;
		return ret;
	}

	public static double[] fg_Function(Point p, Player p1, Player p2, double p1_v2, double p2_v2) {
		double[] newvalues = new double[2];
		double[] fs = f_Function(p, p1, p2);
		double[] gs = g_Function(p, p1, p2, p1_v2, p2_v2);
		newvalues[0] = fs[0] * gs[0];
		newvalues[1] = fs[1] * gs[1];
		return newvalues;
	}

	public static double[] V1(Match m, int maxpoints, boolean returnN) {

		double sum1 = 0;
		double sum2 = 0;
		int n = 0;

		for (Point p : m.getPoints()) {
			if (n < maxpoints) {
				double[] newv1 = f_Function(p, m.getPlayers()[0], m.getPlayers()[1]);
				if (newv1[0] != -10 && newv1[1] != -10) {
					sum1 += newv1[0];
					sum2 += newv1[1];
					n++;
					/*
					 * double serverFH = 0; double serverBH = 0; double
					 * returnerFH = 0; double returnerBH = 0; int shotcounter =
					 * 0; for(String s : p.getShots()){
					 * 
					 * if(s.equals("f") || s.equals("r") || s.equals("u") ||
					 * s.equals("l")){ if(Math.floorMod(shotcounter, 2) == 0){
					 * returnerFH++; } else{ serverFH++; } } else
					 * if(s.equals("b") || s.equals("s") || s.equals("y") ||
					 * s.equals("m")){ if(Math.floorMod(shotcounter, 2) == 0){
					 * returnerBH++; } else{ serverBH++; } } shotcounter++; }
					 * int winner = 1; if
					 * (p.getWinner().equals(m.getPlayers()[1])) winner = 2;
					 * //System.out.println("serverFH = " + serverFH +
					 * "  , serverBH = " + serverBH + "  , returnerFH = " +
					 * returnerFH + "  , returnerBH  " + returnerBH +
					 * "  winner = " + winner); if(serverFH + serverBH > 0 &&
					 * returnerFH + returnerBH > 0){
					 * if(p.getWinner().equals(m.getPlayers()[0]) &&
					 * p.getServer().equals(m.getPlayers()[0]) ){ sum1 +=
					 * serverFH / (serverFH + serverBH); sum2 += returnerBH /
					 * (returnerFH + returnerBH); }
					 * 
					 * else if(p.getWinner().equals(m.getPlayers()[0]) &&
					 * p.getServer().equals(m.getPlayers()[1])){ sum1 +=
					 * returnerFH / (returnerFH + returnerBH); sum2 += serverBH
					 * / (serverFH + serverBH); } else
					 * if(p.getWinner().equals(m.getPlayers()[1]) &&
					 * p.getServer().equals(m.getPlayers()[0])){ sum1 +=
					 * serverBH / (serverFH + serverBH); sum2 += returnerFH /
					 * (returnerFH + returnerBH); } else
					 * if(p.getWinner().equals(m.getPlayers()[1]) &&
					 * p.getServer().equals(m.getPlayers()[1])){ sum1 +=
					 * returnerBH / (returnerFH + returnerBH); sum2 += serverFH
					 * / (serverFH + serverBH); } n++;
					 * //System.out.println("sum1  = " + sum1 + " , sum2 = " +
					 * sum2); }
					 */
				}
			}
		}
		double v1_p1 = sum1 / n;
		double v1_p2 = sum2 / n;

		if (returnN) {
			double[] V1s = { v1_p1, v1_p2, n };
			return V1s;
		} else {
			double[] V1s = { v1_p1, v1_p2 };
			return V1s;
		}
	}

	public static double[] V1(Match m, boolean returnN) {
		return V1(m, m.getPoints().size(), returnN);
	}
	
	public static MatchStatList V1_MSL(Match m, boolean returnN) {
		double[] v1 = V1(m, m.getPoints().size(), returnN);
		MatchStatList msl = new MatchStatList(m.getPlayers()[0].getName(), m.getPlayers()[1].getName(), v1, "V1");
		return msl;
	}

	public static double V1(String playername, int startyear, int endyear) throws IOException {
		ArrayList<Point> points = LoadValues.loadAllPointsOfPlayer(playername, startyear, endyear);
		double sum1 = 0;
		double sum2 = 0;
		double counter = 0;
		for (Point p : points) {
			Player ourPlayer = p.getServer();
			Player opponent = p.getReturner();
			if (p.getServer().getName().equals(opponent.getName())) {
				ourPlayer = p.getReturner();
				opponent = p.getServer();
			}
			double[] newv1 = f_Function(p, ourPlayer, opponent);

			if (newv1[0] != -10 && newv1[1] != -10) {
				sum1 += newv1[0];
				sum2 += newv1[1];
				counter++;
			}
		}
		double result = sum1 / counter;
		return result;
	}

	public static double V1(String playername) throws IOException {
		return V1(playername, 0, 100000);
	}

	public static double[] V2(Match m, final double p1_v2, final double p2_v2, boolean returnN) {

		double sum1 = 0;
		double sum2 = 0;
		double g1sum = 0;
		double g2sum = 0;

		for (Point p : m.getPoints()) {
			double[] Fs = f_Function(p, m.getPlayers()[0], m.getPlayers()[1]);
			double[] Gs = g_Function(p, m.getPlayers()[0], m.getPlayers()[1], p1_v2, p2_v2);
			if (Fs[0] > -10 && Fs[1] > -10 && Gs[0] > -10 && Gs[1] > -10) {
				sum1 += Fs[0] * Gs[0];
				sum2 += Fs[1] * Gs[1];
				g1sum += Gs[0];
				g2sum += Gs[1];
			}
			/*
			 * double serverFH = 0; double serverBH = 0; double returnerFH = 0;
			 * double returnerBH = 0; int shotcounter = 0; for(String s :
			 * p.getShots()){
			 * 
			 * if(s.equals("f") || s.equals("r") || s.equals("u") ||
			 * s.equals("l")){ if(Math.floorMod(shotcounter, 2) == 0){
			 * returnerFH++; } else{ serverFH++; } } else if(s.equals("b") ||
			 * s.equals("s") || s.equals("y") || s.equals("m")){
			 * if(Math.floorMod(shotcounter, 2) == 0){ returnerBH++; } else{
			 * serverBH++; } } shotcounter++; } //int winner = 1; //if
			 * (p.getWinner().equals(m.getPlayers()[1])) winner = 2;
			 * //System.out.println("serverFH = " + serverFH + "  , serverBH = "
			 * + serverBH + "  , returnerFH = " + returnerFH +
			 * "  , returnerBH  " + returnerBH + "  winner = " + winner);
			 * if(serverFH + serverBH > 0 && returnerFH + returnerBH > 0){
			 * double g1 = 0; double g2 = 0; double ser_rel = serverFH /
			 * (serverFH + serverBH); double ret_rel = returnerFH / (returnerFH
			 * + returnerBH);
			 * 
			 * if(p.getWinner().equals(m.getPlayers()[0]) &&
			 * p.getServer().equals(m.getPlayers()[0]) ){ g1 = p2_v2*(returnerFH
			 * / (returnerFH + returnerBH)) + (1-p2_v2)*(returnerBH /
			 * (returnerFH + returnerBH)); g2 = (1-p1_v2)*(serverFH / (serverFH
			 * + serverBH)) + p1_v2 * (serverBH / (serverFH + serverBH)); sum1
			 * += g1*( serverFH / (serverFH + serverBH)); sum2 += g2* returnerBH
			 * / (returnerFH + returnerBH); } else
			 * if(p.getWinner().equals(m.getPlayers()[0]) &&
			 * p.getServer().equals(m.getPlayers()[1])){ g2 =
			 * (1-p1_v2)*(returnerFH / (returnerFH + returnerBH)) +
			 * (p1_v2)*(returnerBH / (returnerFH + returnerBH)); g1 =
			 * (p2_v2)*(serverFH / (serverFH + serverBH)) + (1-p2_v2) *
			 * (serverBH / (serverFH + serverBH)); sum1 += g1*returnerFH /
			 * (returnerFH + returnerBH); sum2 += g2*serverBH / (serverFH +
			 * serverBH); } else if(p.getWinner().equals(m.getPlayers()[1]) &&
			 * p.getServer().equals(m.getPlayers()[0])){ g1 =
			 * (1-p2_v2)*(returnerFH / (returnerFH + returnerBH)) +
			 * (p2_v2)*(returnerBH / (returnerFH + returnerBH)); g2 =
			 * (p1_v2)*(serverFH / (serverFH + serverBH)) + (1-p1_v2) *
			 * (serverBH / (serverFH + serverBH)); sum1 += g1*serverBH /
			 * (serverFH + serverBH); sum2 += g2*returnerFH / (returnerFH +
			 * returnerBH); } else if(p.getWinner().equals(m.getPlayers()[1]) &&
			 * p.getServer().equals(m.getPlayers()[1])){ g2 =
			 * (p1_v2)*(returnerFH / (returnerFH + returnerBH)) +
			 * (1-p1_v2)*(returnerBH / (returnerFH + returnerBH)); g1 =
			 * (1-p2_v2)*(serverFH / (serverFH + serverBH)) + (p2_v2) *
			 * (serverBH / (serverFH + serverBH)); sum1 += g1*returnerBH /
			 * (returnerFH + returnerBH); sum2 += g2*serverFH / (serverFH +
			 * serverBH); } //n++; g1sum += g1; g2sum += g2; if((g1 < 0 || g2 <
			 * 0) && ( p1_v2 < 1 || p2_v2 < 1)) {
			 * //System.out.println("p1_v2 = " + p1_v2 + "  p2_v2 = " + p2_v2);
			 * //System.out.println("gs : " + g1 + "   " + g2); } }
			 * //System.out.println("sum1  = " + sum1 + " , sum2 = " + sum2);
			 */}
		double newp1_v2 = sum1 / g1sum;
		double newp2_v2 = sum2 / g2sum;

		if (returnN) {
			double[] V2s = { newp1_v2, newp2_v2, g1sum, g2sum };
			return V2s;
		} else {
			double[] V2s = { newp1_v2, newp2_v2 };
			return V2s;
		}
	}

	public static double[] V2(Match m, boolean returnN) throws IOException {
		return V2(m, Metrics.V1(m.getPlayers()[0].getName()), Metrics.V1(m.getPlayers()[1].getName()), returnN);
	}

	public static double V1(double[][] M) {
		double sum = 0;
		for (double[] row : M) {
			if (row[0] == 1) {
				sum += row[1] / (row[1] + row[2]);
			} else {
				sum += row[2] / (row[1] + row[2]);
			}
		}
		return sum / M.length;
	}

}
