package counts;

import player.Player;

public class Score {

	int p1_points, p2_points, p1_games, p2_games, p1_sets, p2_sets;
	Player[] players = new Player[2];
	//int serverindex = -1;
	
	public Score() {
		this.p1_games = 0;
		this.p2_games = 0;
		this.p1_points = 0;
		this.p2_points = 0;
		this.p1_sets = 0;
		this.p2_sets = 0;
	}

	public Score(int p1_points, int p2_points) {
		this.p1_games = 0;
		this.p2_games = 0;
		this.p1_sets = 0;
		this.p2_sets = 0;
		this.p1_points = p1_points;
		this.p2_points = p2_points;
	}

	public Score(int p1_points, int p2_points, int p1_games, int p2_games, int p1_sets, int p2_sets) {
		this.p1_games = p1_games;
		this.p2_games = p2_games;
		this.p1_sets = p1_sets;
		this.p2_sets = p2_sets;
		this.p1_points = p1_points;
		this.p2_points = p2_points;
	}

	public int getP1_points() {
		return p1_points;
	}

	public void setP1_points(int p1_points) {
		this.p1_points = p1_points;
	}

	public int getP2_points() {
		return p2_points;
	}

	public void setP2_points(int p2_points) {
		this.p2_points = p2_points;
	}

	public int getP1_games() {
		return p1_games;
	}

	public void setP1_games(int p1_games) {
		this.p1_games = p1_games;
	}

	public int getP2_games() {
		return p2_games;
	}

	public void setP2_games(int p2_games) {
		this.p2_games = p2_games;
	}

	public int getP1_sets() {
		return p1_sets;
	}

	public void setP1_sets(int p1_sets) {
		this.p1_sets = p1_sets;
	}

	public int getP2_sets() {
		return p2_sets;
	}

	public void setP2_sets(int p2_sets) {
		this.p2_sets = p2_sets;
	}

	// --------------
	public void printScore() {
		boolean tiebreak = false;
		if (this.getP1_games() == 6 && this.getP2_games() == 6 && this.getP1_sets() + this.getP2_sets() < 4) {
			tiebreak = true;
		}
		String p1points = convertToTennisScore(this.getP1_points(), this.getP2_points(), !tiebreak);
		String p2points = convertToTennisScore(this.getP2_points(), this.getP1_points(), !tiebreak);
		/*if(this.getServerindex() == 0){
			System.out.print("* ");
		}*/
		System.out.println(this.getP1_games() + " : " + this.getP2_games() + " | " + p1points + " : " + p2points
				+ " ,  Sets: " + this.getP1_sets() + " : " + this.getP2_sets());
		/*if(this.getServerindex() == 1){
			System.out.print(" *");
		}*/
	}

	public String convertToTennisScore(double points, double opponent_points, boolean doThis) {
		if (doThis) {
			if (points == 0) {
				return 0 + "";
			} else if (points == 1) {
				return 15 + "";
			} else if (points == 2) {
				return 30 + "";
			} else if (points >= 3) {
				if (points > 3 && opponent_points < points) {
					return "AD";
				} else {
					return 40 + "";
				}
			}
			return "";
		} else {
			return (int) points + "";
		}
	}
	
	// Spieler 1 bzw. 2 mit einem echten Spieler besetzen
	public void setPlayer(Player player, int index){
		this.players[index] = player;
	}
	
	public int getPointsOf(String playername){
		if(this.players[0].getName().equals(playername)){
			return this.p1_points;
		}
		if(this.players[1].getName().equals(playername)){
			return this.p2_points;
		}
		return -1;
	}
	
	public int getGamesOf(String playername){
		if(this.players[0].getName().equals(playername)){
			return this.p1_games;
		}
		if(this.players[1].getName().equals(playername)){
			return this.p2_games;
		}
		return -1;
	}
	
	public int getSetsOf(String playername){
		if(this.players[0].getName().equals(playername)){
			return this.p1_sets;
		}
		if(this.players[1].getName().equals(playername)){
			return this.p2_sets;
		}
		return -1;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	/*public int getServerindex() {
		return serverindex;
	}

	public void setServerindex(int serverindex) {
		this.serverindex = serverindex;
	}*/

}
