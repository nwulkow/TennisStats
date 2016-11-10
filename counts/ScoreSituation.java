package counts;

import player.Player;

public class ScoreSituation extends Score{
	
	Player server;
	int serverIndex;
	boolean isBreakPoint = false;
	
	public ScoreSituation(){
		super();
		server = new Player();
	}
	
	public ScoreSituation(int p1_points, int p2_points){
		super(p1_points, p2_points);
		server = new Player();
		this.serverIndex = 0;
	}
	
	public ScoreSituation(Score score, Player server){
		this.p1_games = score.getP1_games();
		this.p2_games = score.getP2_games();
		this.p1_sets = score.getP1_sets();
		this.p2_sets = score.getP2_sets();
		this.p1_points = score.getP1_points();
		this.p2_points = score.getP2_points();
		this.server = server;
		this.serverIndex = 0;
	}
	
	public ScoreSituation(Score score, int serverIndex){
		this.p1_games = score.getP1_games();
		this.p2_games = score.getP2_games();
		this.p1_sets = score.getP1_sets();
		this.p2_sets = score.getP2_sets();
		this.p1_points = score.getP1_points();
		this.p2_points = score.getP2_points();
		this.server = new Player();
		this.serverIndex = serverIndex;
	}
	
	public ScoreSituation(Score score, Player server, Match m){
		this(score, figureServerIndex(server, m));
		this.checkBreakPoint();
	}
	
	
	public void printScoreSituation() {
		boolean tiebreak = false;
		if (this.getP1_games() == 6 && this.getP2_games() == 6 && this.getP1_sets() + this.getP2_sets() < 4) {
			tiebreak = true;
		}
		String p1points = convertToTennisScore(this.getP1_points(), this.getP2_points(), !tiebreak);
		String p2points = convertToTennisScore(this.getP2_points(), this.getP1_points(), !tiebreak);
		if(this.getServerIndex() == 0){
			System.out.print("* ");
		}
		System.out.print(this.getP1_games() + " : " + this.getP2_games() + " | " + p1points + " : " + p2points
				+ " ,  Sets: " + this.getP1_sets() + " : " + this.getP2_sets());
		if(this.getServerIndex() == 1){
			System.out.print(" *");
		}
		System.out.println("");
	}
	
	public void checkBreakPoint(){
		if(this.getP1_games() < 6 || this.getP2_games() < 6){
			if(serverIndex == 0){
				if(this.getP2_points() >= 3 && this.getP1_points() < this.getP2_points()){
					this.isBreakPoint = true;
				}
			}
			else{
				if(this.getP1_points() >= 3 && this.getP2_points() < this.getP1_points()){
					this.isBreakPoint = true;
				}
			}
		}
	}

	public Player getServer() {
		return server;
	}

	public void setServer(Player server) {
		this.server = server;
	}

	public int getServerIndex() {
		return serverIndex;
	}

	public void setServerIndex(int serverIndex) {
		this.serverIndex = serverIndex;
	}
	
	public void setServerIndex(Player server, Match m){
		if(server.equals(m.getPlayers()[0])){
			serverIndex = 0;
		}
		else{
			serverIndex = 1;
		}
	}
	
	public static int figureServerIndex(Player server, Match m){
		int index = 0;
		if(server.equals(m.getPlayers()[1])){
			index = 1;
		}
		return index;
	}

	public boolean isBreakPoint() {
		return isBreakPoint;
	}

	public void setBreakPoint(boolean isBreakPoint) {
		this.isBreakPoint = isBreakPoint;
	}

}
