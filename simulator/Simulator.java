package simulator;

import java.util.Random;

import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.Dir;

import analysisFormats.ProbabilityForShot;
import analytics.StandardStats;
import counts.Match;
import counts.Point;
import counts.Score;
import counts.Serve;
import counts.Shot;
import objects.Shottypes;
import player.Player;
import tools.OutputTools;

public class Simulator {

	public static void main(String[] args) {

		Random r = new Random();
		Player p1 = new Player("Player 1");
		Player p2 = new Player("Player 2");
		int numIter = 10000;
		double p1_win_counts = 0;
		double p2_win_counts = 0;
		double p1_points_won_counts = 0;
		double p2_points_won_counts = 0;
		double points_played_total = 0;
		Point p = new Point();
		p.setServer(p1);
		p.setReturner(p2);

		for (int i = 0; i < numIter; i++) {
			Match m = new Match(p1, p2);
			m.setSetstowin(2);
			while (!m.isFinished()) {
				if (m.getPoints().size() > 0) {
					p = new Point(m.getLastPoint());
				}

				Score newscore = new Score();
				int pointwinner = determinePointWinnerSimple(p, p1, p2);
				newscore = m.computeNewScore(m.getPlayers()[pointwinner]);
				p.setCreatedscore(newscore);
				p.setWinner(m.getPlayers()[pointwinner]);

				m.getPoints().add(p);

			}
			if (m.getWinner().equals(p1)) {
				p1_win_counts++;
			} else if (m.getWinner().equals(p2)) {
				p2_win_counts++;
			}
			// m.printEndResult();
			double[] pointswon = StandardStats.pointsWonByPlayer(m);
			p1_points_won_counts += pointswon[0];
			p2_points_won_counts += pointswon[1];
			points_played_total += StandardStats.pointsPlayed(m);
		}
		System.out.println(p1_win_counts + " , " + p2_win_counts);
		System.out.println(
				p1_points_won_counts / points_played_total + " , " + p2_points_won_counts / points_played_total);
	}

	public static int determinePointWinner(Point p, Player p1, Player p2) {
		Random r = new Random();
		double p1_servewinpercentage = p1.getServewinvalue() / (p1.getServewinvalue() + p2.getReturnwinvalue());
		double p2_servewinpercentage = p2.getServewinvalue() / (p2.getServewinvalue() + p1.getReturnwinvalue());
		if (p.getServer().equals(p1)) {
			if (r.nextDouble() < p1_servewinpercentage) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (r.nextDouble() < p2_servewinpercentage) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	public static Point determinePointWinnerShotByShot(Point p, Player p1, Player p2, boolean withDirection) {
		
		Random r = new Random();
			int side = 0;
			if(p.getSide().equals(Shottypes.adcourt)){
				side = 1;
			}
			double rand_serveDir = r.nextDouble();
			// Erster Aufschlag: Richtung
			int direction = 0;
			if(rand_serveDir > p.getServer().getTensor().getFirstserves()[side][0].getProbabilityForShot()){
				direction = 1;
			}
			if(rand_serveDir > p.getServer().getTensor().getFirstserves()[side][0].getProbabilityForShot() + p.getServer().getTensor().getFirstserves()[side][1].getProbabilityForShot()){
				direction = 2;
			}
			p.setFirstserve(new Serve(p.getSide()));
			p.getFirstserve().setDirection(direction);
			// Erster Aufschlag: Erfolg
			double rand_serveSuc = r.nextDouble();
			double in_prob = p.getServer().getTensor().getFirstserves()[side][direction].getOutcomeProbabilities()[0];
			double ace_prob = p.getServer().getTensor().getFirstserves()[side][direction].getOutcomeProbabilities()[1];
			double fault_prob = p.getServer().getTensor().getFirstserves()[side][direction].getOutcomeProbabilities()[2];
			if(rand_serveSuc <= in_prob + ace_prob){
				p.getFirstserve().setSuccess(true);
				if(rand_serveSuc > in_prob){
					p.getFirstserve().setAce(true);
					p.setSuccesfullserve(p.getFirstserve());
					p.setWinner(p.getServer());
					return p;
				}
				else{
					p.setSuccesfullserve(p.getFirstserve());
					// RALLY!! : 
					return simulateRallyShotByShot(p, p1, p2, withDirection);
				}
			}
			else if(rand_serveSuc > in_prob + ace_prob){
				p.getFirstserve().setSuccess(false);
				// Zweiter Aufschlag:
				// Richtung:
				double rand_2ndServeDir = r.nextDouble();
				direction = 0;
				if(rand_2ndServeDir > p.getServer().getTensor().getSecondserves()[side][0].getProbabilityForShot()){
					direction = 1;
				}
				if(rand_2ndServeDir > p.getServer().getTensor().getSecondserves()[side][0].getProbabilityForShot() + p.getServer().getTensor().getSecondserves()[side][1].getProbabilityForShot()){
					direction = 2;
				}
				// Erfolg:
				p.getSecondserve().setDirection(direction);
				double rand_2ndServeSuc = r.nextDouble();
				in_prob = p.getServer().getTensor().getSecondserves()[side][direction].getOutcomeProbabilities()[0];
				ace_prob = p.getServer().getTensor().getSecondserves()[side][direction].getOutcomeProbabilities()[1];
				fault_prob = p.getServer().getTensor().getSecondserves()[side][direction].getOutcomeProbabilities()[2];
				if(rand_2ndServeSuc <= in_prob + ace_prob){
					p.getSecondserve().setSuccess(true);
					if(rand_2ndServeSuc > in_prob){
						p.getSecondserve().setAce(true);
						p.setSuccesfullserve(p.getSecondserve());
						p.setWinner(p.getServer());
						return p;
					}
					else{
						p.setSuccesfullserve(p.getSecondserve());
						// RALLY!! : 
						return simulateRallyShotByShot(p, p1, p2, withDirection);
					}
				}
				else{
					// Doppelfehler:
					p.getSecondserve().setSuccess(false);
					p.setWinner(p.getReturner());
					return p;
				}
			}
		return p;	
	}
	
	public static Point simulateRallyShotByShot(Point p, Player p1, Player p2, boolean withDirection){
		Random r = new Random();
		boolean pointRunning = true;
		Player currentPlayer = p.getReturner(); // Der Spieler, der gerade schlaegt
		Player currentNotPlayer = p.getServer(); // Der Spieler, der gerade nicht schlaegt
		Shot s = new Shot();
		s.setPosition(1);
		int servingSide = 0;
		if(p.getSide().equals(Shottypes.adcourt)){
			s.setPosition(3);
			servingSide = 1;
		}
		int shotcounter = 0;
		// currentTensor ist der Tensors des gerade schlagenden Spielers. Dieser enthaelt die W'keiten fuer Schlagrichtung und Outcome des kommenden Schlages fuer diesen Spieler
		// otherTensor ist der Tensor des anderen Spielers, bzw. fuer den Schlag, den dieser gerade gemacht hat. Dieser enthaelt auch die W'keiten fuer den jeweils naechsten Schlag dessen Gegners
		ProbabilityForShot[][] currentTensor = currentPlayer.getTensor().getTensor().getProbForShots()[0];
		ProbabilityForShot[][] otherTensor = currentPlayer.getTensor().getTensor().getProbForShots()[0];
		int hitFrom = 0;
		int hitFrom_pre = 0;
		while(pointRunning){
			// Verschiedene Wahrscheinlichkeiten fuer Rally, serve+1 und Return
			if(shotcounter == 0){
				hitFrom = 0;
				if(p.getFirstserve().getSide().equals(Shottypes.adcourt)){
					hitFrom = 2;
				}
				if(!withDirection){
					hitFrom = 0;
				}
				if(p.getFirstserve().isSuccess()){
					currentTensor = currentPlayer.getTensor().getReturns().getProbForShots()[hitFrom];
					otherTensor = currentNotPlayer.getTensor().getFirstserves();
				}
				else{
					currentTensor = currentPlayer.getTensor().getTensor().getProbForShots()[hitFrom];
					otherTensor = currentNotPlayer.getTensor().getSecondserves();
				}
			}
			else{
				hitFrom = p.getLastShot().getPosition()-1;
				// hitFrom fuer den vorletzten Schlag
				hitFrom_pre = 0;
				if(shotcounter >= 2){
					hitFrom_pre = p.getShots().get(p.getShots().size()-2).getPosition()-1;
				}
				else if(shotcounter == 1){
					hitFrom_pre = servingSide * 2;
				}

				if(!withDirection){
					hitFrom = 0;
					hitFrom_pre = 0;
				}
				if(p.getFirstserve().isSuccess()){
					if(shotcounter == 1){
						currentTensor = currentPlayer.getTensor().getServeplus1().getProbForShots()[hitFrom];
						otherTensor = currentNotPlayer.getTensor().getReturns().getProbForShots()[hitFrom_pre];		
					}
					else if(shotcounter == 2){
						currentTensor = currentPlayer.getTensor().getTensor().getProbForShots()[hitFrom];
						otherTensor = currentNotPlayer.getTensor().getServeplus1().getProbForShots()[hitFrom_pre];
					}
					else{
						currentTensor = currentPlayer.getTensor().getTensor().getProbForShots()[hitFrom];
						otherTensor = currentNotPlayer.getTensor().getTensor().getProbForShots()[hitFrom_pre];
					}
				}
				else{
					currentTensor = currentPlayer.getTensor().getTensor().getProbForShots()[hitFrom];
					otherTensor = currentNotPlayer.getTensor().getTensor().getProbForShots()[hitFrom_pre];
				}		
			}
			// Richtung des naechsten Schlages bestimmen:
			int nextShotDirection = 1;
			double rand_shot = r.nextDouble();
			if(rand_shot > currentTensor[s.getPosition()-1][0].getProbabilityForShot()){
				if(rand_shot > currentTensor[s.getPosition()-1][0].getProbabilityForShot() + currentTensor[s.getPosition()-1][1].getProbabilityForShot()){
					nextShotDirection = 3;
				}
				else{
					nextShotDirection = 2;
				}
			}
			s.setDirection(nextShotDirection);
			// Outcome:
			double rand_shot_Suc = r.nextDouble(); // Zufallszahl aus U[0,1], mit welcher der Outcome des Schlages bestimmt werden soll
			double[] otherTensorOutcomes = new double[3];
			if(p.getShots().size() > 0){
				otherTensorOutcomes =  otherTensor[p.getLastShot().getPosition()-1][s.getPosition()-1].getOutcomeOfNextShot();
			}
			else{
				if(p.getFirstserve().isSuccess()){
					otherTensorOutcomes = currentNotPlayer.getTensor().getFirstserves()[servingSide][s.getPosition()-1].getOutcomeOfNextShot();
				}
				else{
					otherTensorOutcomes = currentNotPlayer.getTensor().getSecondserves()[servingSide][s.getPosition()-1].getOutcomeOfNextShot();
				}
			}
			double[] currentTensorOutcomes = currentTensor[s.getPosition()-1][s.getDirection()-1].getOutcomeProbabilities();
			double[] finalProbabilities = new double[3];
			finalProbabilities[0] = (currentTensorOutcomes[0] + otherTensorOutcomes[0]) / 2;
			finalProbabilities[1] = (currentTensorOutcomes[1] + otherTensorOutcomes[1]) / 2;
			finalProbabilities[2] = (currentTensorOutcomes[2] + otherTensorOutcomes[2]) / 2;

			// Ballwechsel zuende:
			if(rand_shot_Suc > finalProbabilities[0]){
				if(rand_shot_Suc > finalProbabilities[0] + finalProbabilities[1]){
					// Error:
					s.setOutcome(Shottypes.UE);
					p.getShots().add(s);
					p.setWinner(currentNotPlayer);
					return p;
				}
				else{
					// Winner:
					s.setOutcome(Shottypes.winner);
					p.getShots().add(s);
					p.setWinner(currentPlayer);
					return p;
				}
			}
			s.setOutcome(Shottypes.in);
			p.getShots().add(s);
			s = new Shot();
			s.setPosition(nextShotDirection);
			shotcounter++;
			// Fuer den naechsten Schlag Spieler wechseln
			Player temp = currentPlayer;
			currentPlayer = currentNotPlayer;
			currentNotPlayer = temp;
			
		}
		return p;
	}

	public static int determinePointWinnerSimple(Point p, Player p1, Player p2) {
		Random r = new Random();
		double p1_winpercentage = p1.getWinvalue() / (p1.getWinvalue() + p2.getWinvalue());
		if (r.nextDouble() < p1_winpercentage) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static Match simulateMatch(Player p1, Player p2, int setstowin, int mode){
		// mode: 1 = point win %, 2 = serve/return win %
		Match m = new Match(p1, p2);
		m.setSetstowin(setstowin);
		Point p = new Point();
		p.setServer(p1);
		p.setReturner(p2);

		while (!m.isFinished()) {
			if (m.getPoints().size() > 0) {
				p = new Point(m.getLastPoint());
			}

			Score newscore = new Score();
			int pointwinner = 0;
			if(mode == 2){
				pointwinner = determinePointWinner(p, p1, p2);
			}
			else{
				pointwinner = determinePointWinnerSimple(p, p1, p2);
			}
			newscore = m.computeNewScore(m.getPlayers()[pointwinner]);
			p.setCreatedscore(newscore);
			p.setWinner(m.getPlayers()[pointwinner]);

			m.getPoints().add(p);

		}
		return m;
	}
	
	
	public static Match simulateMatchShotByShot(Player p1, Player p2, int setstowin, boolean withDirection){
		Match m = new Match(p1,p2);
		m.setSetstowin(setstowin);
		Point p = new Point();
		p.setServer(p1);
		p.setReturner(p2);
		double rallylengths = 0;
		while (!m.isFinished()) {
			if (m.getPoints().size() > 0) {
				p = new Point(m.getLastPoint());
			}
			
			Score newscore = new Score();
			p = determinePointWinnerShotByShot(p, p1, p2, withDirection);
			p.determineNoShotsIn();
			//p.printShots();
			rallylengths += p.getNoShotsIn();
			newscore = m.computeNewScore(p.getWinner());
			p.setCreatedscore(newscore);
			//p.setWinner(p.getWinner());

			m.getPoints().add(p);

		}
		//System.out.println("Average point length : " +  rallylengths / m.getPoints().size());
		return m;
			
	}

}
