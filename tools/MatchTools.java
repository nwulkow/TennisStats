package tools;

import java.util.ArrayList;

import counts.Match;
import counts.Point;
import counts.ScoreSituation;
import counts.Serve;
import counts.Shot;
import objects.Shottypes;
import player.Player;

public class MatchTools {

	public static Shot cloneShot(Shot shot) {
		Shot s = new Shot();
		s.setDepth(shot.getDepth());
		s.setDirection(shot.getDirection());
		s.setOutcome(shot.getOutcome());
		s.setPosition(shot.getPosition());
		s.setShottype(shot.getShottype());
		s.setSpecifictype(shot.getSpecifictype());
		return s;
	}

	public static int getDirectionOfServe(Serve serve){
		
		if(serve.getSide().equals(Shottypes.deucecourt) && (serve.getDirection() == 1 || serve.getDirection() == 2)){
			return 0;
		}
		else if(serve.getSide().equals(Shottypes.adcourt) && (serve.getDirection() == 2 || serve.getDirection() == 3)){
			return 2;
		}
		else {
			return 1;
		}
	}

	public static int getPlayerIndex(String playername, Match m){
		if(m.getPlayers()[0].getName().equals(playername)){
			return 0;
		}
		else{
			return 1;
		}
	}

	public static int getPlayerIndex(Player player, Match m){
		return getPlayerIndex(player.getName(), m);
	}

	
	
	public static boolean compareShotSequence(ArrayList<Shot> shots, ArrayList<Shot> trueshots, int i) {
	
		// System.out.println("shots size = " + shots.size() + " , " +
		// trueshots.size() + ", i = " + i);
		if (i - shots.size() < 0) {
			return false;
		}
		for (int j = shots.size() - 1; j > -1; j--) {
			if (!shots.get(j).getShottype().equals("")
					&& !shots.get(j).getShottype().equals(trueshots.get(i + j - shots.size() + 1).getShottype())) {
				return false;
			}
			if (!shots.get(j).getSpecifictype().equals("") && !shots.get(j).getSpecifictype()
					.equals(trueshots.get(i + j - shots.size() + 1).getSpecifictype())) {
				return false;
			}
			if (!(shots.get(j).getPosition() == 0)
					&& !(shots.get(j).getPosition() == trueshots.get(i + j - shots.size() + 1).getPosition())) {
				return false;
			}
			if (!(shots.get(j).getDirection() == 0)
					&& !(shots.get(j).getDirection() == trueshots.get(i + j - shots.size() + 1).getDirection())) {
				return false;
			}
			if (!(shots.get(j).getDepth() == 0)
					&& !(shots.get(j).getDepth() == trueshots.get(i + j - shots.size() + 1).getDepth())) {
				return false;
			}
			if (!shots.get(j).getOutcome().equals("")
					&& !shots.get(j).getOutcome().equals(trueshots.get(i + j - shots.size() + 1).getOutcome())) {
				return false;
			}
		}
	
		return true;
	}

	public static boolean compareScoreSituations(ScoreSituation sit1, ScoreSituation sit2){
		if(sit1.getServerIndex() == sit2.getServerIndex()){
			if(sit1.getP1_games() == sit2.getP1_games() && sit1.getP1_sets() == sit2.getP1_sets() && 
					sit1.getP2_games() == sit2.getP2_games() && sit1.getP2_sets() == sit2.getP2_sets() ){
				if((sit1.getP1_points() == sit2.getP1_points() && sit1.getP2_points() == sit2.getP2_points()) || 
						(sit1.getP1_points() >= 3 && sit2.getP1_points() >= 3 && sit1.getP2_points() >= 3 && sit2.getP2_points() >= 3 
						&& sit1.getP1_points() - sit1.getP2_points() == sit2.getP1_points() - sit2.getP2_points())){
					return true;
				}
				else{
					return false;
				}		
			}
			else{
				return false;
			}
		}
		else{
			if(sit1.getP1_games() == sit2.getP2_games() && sit1.getP1_sets() == sit2.getP2_sets() && sit1.getP2_games() == sit2.getP1_games() && 
				sit1.getP2_sets() == sit2.getP1_sets() ){
				if((sit1.getP1_points() == sit2.getP2_points() && sit1.getP2_points() == sit2.getP1_points()) || 
						(sit1.getP1_points() >= 3 && sit2.getP1_points() >= 3 && sit1.getP2_points() >= 3 && sit2.getP2_points() >= 3 
						&& sit1.getP1_points() - sit1.getP1_points() == sit2.getP2_points() - sit2.getP2_points())){
					return true;
				}
				else{
					return false;
				}		
			}
			else{
				return false;
			}
		}
	}

	public static boolean comparePoints(Point p, Point truepoint){
		// Muss noch richtig geschrieben werden
		//if(p.getFirstserve().isSuccess() != null && !p.getFirstserve().isSuccess() == truepoint.getFirstserve().isSuccess()){
			return false;
	//	}
	}

}
