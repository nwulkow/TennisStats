package analytics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import analysisFormats.PlayerStatList;
import counts.Match;
import counts.Point;
import dataload.LoadMatchFromTennisAbstract;
import dataload.LoadValues;
import player.Player;

public class PlayerStandardStats {
	
	
	/*public static double pointsWon(String playername) throws IOException{
		return pointsWon(playername, LoadValues.loadMatchesFromPaths(LoadValues.loadAllMatchFilesOfPlayer(playername)));
	}*/
	
	public static double pointsWon(String playername, ArrayList<Match> matches) throws IOException{
		double pointsPlayed = 0;
		double pointsWon = 0;

		for (Match m : matches) {
			for(Point p : m.getPoints()){
				pointsPlayed++;
				if(p.getWinner().getName().equals(playername)){
					pointsWon++;
				}
			}
		}
		return pointsWon / pointsPlayed;		
	}
	
	/*public static double servicePointsWon(String playername) throws IOException{
		return servicePointsWon(playername, LoadValues.loadMatchesFromPaths(LoadValues.loadAllMatchFilesOfPlayer(playername)));
	}*/
	
	public static double servicePointsWon(String playername, ArrayList<Match> matches){
		double servicePointsPlayed = 0;
		double servicePointsWon = 0;
		for(Match m : matches){
			for(Point p : m.getPoints()){
				if(p.getServer().getName().equals(playername)){
					servicePointsPlayed++;
					if(p.getWinner().getName().equals(playername)){
						servicePointsWon++;
					}
				}
			}
		}
		return servicePointsWon / servicePointsPlayed;		
	}
	
	/*public static double returnPointsWon(String playername) throws IOException{
		return returnPointsWon(playername, LoadValues.loadMatchesFromPaths(LoadValues.loadAllMatchFilesOfPlayer(playername)));
	}*/
	
	public static double returnPointsWon(String playername, ArrayList<Match> matches) throws IOException{
		double servicePointsPlayed = 0;
		double servicePointsWon = 0;
		for(Match m : matches){
			for(Point p : m.getPoints()){
				if(!p.getServer().getName().equals(playername)){
					servicePointsPlayed++;
					if(p.getWinner().getName().equals(playername)){
						servicePointsWon++;
					}
				}
			}
		}

		return servicePointsWon / servicePointsPlayed;		
	}
	
	public static PlayerStatList breakPointsSavedPercentage(String playername, ArrayList<Match> matches){
		double[] breakPoints = new double[2];
		for(Match m : matches){
			double[] currentBreakPoints = StandardStats.breakPointsSaved(m);
			if(playername.equals(m.getPlayers()[0].getName())){
				breakPoints[0] += currentBreakPoints[0];
				breakPoints[1] += currentBreakPoints[1];
			}
			else if(playername.equals(m.getPlayers()[1].getName())){
				breakPoints[0] += currentBreakPoints[2];
				breakPoints[1] += currentBreakPoints[3];
			}
		}
		double bpSavedRatio = breakPoints[0] / breakPoints[1];
		PlayerStatList psl = new PlayerStatList(playername, new ArrayList<Double>(Arrays.asList(bpSavedRatio)), new ArrayList<String>(Arrays.asList("Break Points saved")), breakPoints[1]);
		return psl;
	}
	
	public static PlayerStatList breakPointsMadePercentage(String playername, ArrayList<Match> matches){
		double[] breakPoints = new double[2];
		for(Match m : matches){
			double[] currentBreakPoints = StandardStats.breakPointsMade(m);
			if(playername.equals(m.getPlayers()[0].getName())){
				breakPoints[0] += currentBreakPoints[0];
				breakPoints[1] += currentBreakPoints[1];
			}
			else if(playername.equals(m.getPlayers()[1].getName())){
				breakPoints[0] += currentBreakPoints[2];
				breakPoints[1] += currentBreakPoints[3];
			}
		}
		double bpMadeRatio = breakPoints[0] / breakPoints[1];
		PlayerStatList psl = new PlayerStatList(playername, new ArrayList<Double>(Arrays.asList(bpMadeRatio)), new ArrayList<String>(Arrays.asList("Break Points made")), breakPoints[1]);
		return psl;
	}

}
