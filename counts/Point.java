package counts;

import java.util.ArrayList;

import objects.Shottypes;
import player.Player;

public class Point {

	ArrayList<String> shots_strings;
	ArrayList<Shot> shots;
	Serve firstserve, secondserve, succesfullserve = new Serve();
	Player server, returner, winner;
	int noShotsIn = 0;
	Score score, createdscore;
	ScoreSituation scoreSituation;
	String side = "";

	public Point() {
		this.shots_strings = new ArrayList<String>();
		this.shots = new ArrayList<Shot>();
		this.firstserve = new Serve();
		this.secondserve = new Serve();
		this.score = new Score();
		this.createdscore = new Score();
		this.scoreSituation = new ScoreSituation();
	}

	public Point(Score score) {
		this.shots_strings = new ArrayList<String>();
		this.shots = new ArrayList<Shot>();
		this.firstserve = new Serve();
		this.secondserve = new Serve();
		this.score = score;
		this.createdscore = new Score();
		this.scoreSituation = new ScoreSituation(score, this.server);
	}

	public Point(Point previouspoint) {
		this.shots_strings = new ArrayList<String>();
		this.shots = new ArrayList<Shot>();
		this.firstserve = new Serve();
		this.secondserve = new Serve();
		this.score = previouspoint.getCreatedscore();
		this.createdscore = new Score();
		this.scoreSituation = new ScoreSituation();
		this.setServer(previouspoint.getServer());
		this.setReturner(previouspoint.getReturner());
		if (score.getP1_points() == 0 && score.getP2_points() == 0) {
			this.setServer(previouspoint.getReturner());
			this.setReturner(previouspoint.getServer());
		}
		if (score.getP1_games() == 6 && score.getP2_games() == 6 && score.getP1_sets() + score.getP2_sets() < 4) {
			if (Math.floorMod(score.getP1_points() + score.getP2_points(), 2) == 1) {
				this.setServer(previouspoint.getReturner());
				this.setReturner(previouspoint.getServer());
			}
		}
	}

	public void printShots(){
		System.out.print("First serve to " + this.firstserve.direction);
		if(this.firstserve.isAce()){
			System.out.print(":Ace. ");
		}
		else{
			if(!this.firstserve.isSuccess()){
				System.out.print(":Fault. ");
				System.out.print("Second serve->" + secondserve.direction);
				if(secondserve.isAce()){
					System.out.print(":Ace. ");
				}
				else{
					if(!secondserve.isSuccess()){
						System.out.print("Double fault. ");
					}
					else{
						for(Shot s : this.shots){
							System.out.print("->" + s.getShottype() + "," + s.getSpecifictype() + " to " + s.getDirection());
							if(!s.getOutcome().equals(Shottypes.in)){
								if(s.getOutcome().equals(Shottypes.winner)){
									System.out.print(" Winner. ");
								}
								else{
									System.out.print(" Error. ");
								}
							}
						}
					}
				}
			}
			else{
				for(Shot s : this.shots){
					System.out.print("->" + s.getShottype() + "," + s.getSpecifictype() + " to " + s.getDirection());
					if(!s.getOutcome().equals(Shottypes.in)){
						if(s.getOutcome().equals(Shottypes.winner)){
							System.out.print(" Winner. ");
						}
						else{
							System.out.print(" Error. ");
						}
					}
				}
			}
		}
		System.out.println(this.getWinner().getName() + " wins point.");
	}
	
	public Player getServer() {
		return server;
	}

	public void setServer(Player server) {
		this.server = server;
	}

	public Player getReturner() {
		return returner;
	}

	public void setReturner(Player returner) {
		this.returner = returner;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public ArrayList<String> getShots_strings() {
		return shots_strings;
	}

	public void setShots_strings(ArrayList<String> shots_strings) {
		this.shots_strings = shots_strings;
	}

	public void setShots(ArrayList<Shot> shots) {
		this.shots = shots;
	}

	public ArrayList<Shot> getShots() {
		return shots;
	}

	public Serve getFirstserve() {
		return firstserve;
	}

	public void setFirstserve(Serve firstserve) {
		this.firstserve = firstserve;
	}

	public Serve getSecondserve() {
		return secondserve;
	}

	public void setSecondserve(Serve secondserve) {
		this.secondserve = secondserve;
	}

	public void determineNoShotsIn(){
		if(this.shots.size() == 0){
			this.noShotsIn = 0;
			if(this.firstserve.isAce() || this.secondserve.isAce()){
				this.noShotsIn = 1;
			}
		}
		else{
			this.noShotsIn = this.shots.size() + 1;
			if(!this.getLastShot().getOutcome().equals(Shottypes.in)){
				this.noShotsIn -= 1;
			}
		}
	}
	
	public int getNoShotsIn() {
		return noShotsIn;
	}

	public void setNoShotsIn(int noShotsIn) {
		this.noShotsIn = noShotsIn;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public Score getCreatedscore() {
		return createdscore;
	}

	public void setCreatedscore(Score createdscore) {
		this.createdscore = createdscore;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public Serve getSuccesfullserve() {
		return succesfullserve;
	}

	public void setSuccesfullserve(Serve succesfullserve) {
		this.succesfullserve = succesfullserve;
	}

	public Shot getLastShot() {
		if (this.getShots().size() > 0) {
			return this.getShots().get(this.getShots().size() - 1);
		} else
			return null;
	}

	public ScoreSituation getScoreSituation() {
		return scoreSituation;
	}

	public void setScoreSituation(ScoreSituation scoreSituation) {
		this.scoreSituation = scoreSituation;
	}

}
