package counts;

import java.util.ArrayList;
import java.util.Date;

import objects.MatchDate;
import objects.Shottypes;
import player.Player;
import tools.Tools;

public class Match {

	ArrayList<Game> games;
	Player[] players;
	Player winner;
	ArrayList<Point> points;
	int setstowin = 3;
	double[][] games_in_sets = new double[2][setstowin * 2 - 1];
	boolean finished = false;
	MatchDate date;
	String location = "", round = "", datestring = "";

	public Match() {
		this.games = new ArrayList<Game>();
		this.players = new Player[2];
		this.players[0] = new Player();
		this.players[1] = new Player();
		this.points = new ArrayList<Point>();
		this.date = new MatchDate();
	}

	public Match(Player p1, Player p2) {
		this.games = new ArrayList<Game>();
		this.players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		this.points = new ArrayList<Point>();
		this.date = new MatchDate();
	}

	public Match(MatchInfo mi, Player p1, Player p2) {
		this.date = Tools.stringToDate(mi.getDate());
		this.datestring = mi.getDate();
		this.location = mi.getLocation();
		this.round = mi.getRound();
		this.games = new ArrayList<Game>();
		this.players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		this.points = new ArrayList<Point>();
		// System.out.println(p1.getName() + " , " + p2.getName());
		ArrayList<TennisAbstractPoint> taps = mi.getTaps();
		ArrayList<Integer> serverlist = mi.getServerlist();
		ArrayList<Integer> winnerlist = mi.getWinnerlist();

		String previousstring = "";
		Game game = new Game();
		game.setServer(this.players[serverlist.get(0) - 1]);
		for (int i = 0; i < taps.size(); i++) {
			if (i > 0) {
				if (!game.getServer().equals(this.players[serverlist.get(i) - 1])) {
					game.setWinner(points.get(points.size() - 1).getWinner());
					this.games.add(game);
					game = new Game();
				}
			}

			TennisAbstractPoint tap = taps.get(i);
			Point p = new Point();
			p.setServer(this.players[serverlist.get(i) - 1]);
			p.setReturner(this.players[Math.floorMod(serverlist.get(i), 2)]);
			/*if(p.getServer().equals(this.getPlayers()[0])){
				p.getScore().setServerindex(0);
			}
			else{
				p.getScore().setServerindex(1);
			}*/
			if(winnerlist.get(i) == 1 || winnerlist.get(i) == 2){
				p.setWinner(this.players[winnerlist.get(i) - 1]);
			}
			else{
				p.setWinner(this.players[1]);
			}
			
			if (Math.floorMod(game.getPoints().size(), 2) == 0) {
				p.setSide(Shottypes.deucecourt);
			} else {
				p.setSide(Shottypes.adcourt);
			}
			Serve firstserve = new Serve();
			Serve secondserve = new Serve();
			if (Math.floorMod(game.getPoints().size(), 2) == 0) {
				firstserve.setSide(Shottypes.deucecourt);
			} else {
				firstserve.setSide(Shottypes.adcourt);
			}
			for (String s1 : tap.getFirstservestring()) {
				if (Tools.isInteger(s1)) {
					firstserve.setDirection(Integer.parseInt(s1) - 3);
				} else {
					if (s1.equals("*")) {
						firstserve.setAce(true);
						p.setNoShotsIn(1);
						firstserve.setUnreturned(true);
						firstserve.setOutcome(Shottypes.serve_ace);
					}
					if (s1.equals("#")) {
						p.setNoShotsIn(1);
						firstserve.setUnreturned(true);
						firstserve.setOutcome(Shottypes.serve_winner);
					} else if (Shottypes.serveerrortypes.contains(s1)) {
						firstserve.setSuccess(false);
						if (s1.equals("w"))
							firstserve.setOutcome(Shottypes.serve_wide);
						if (s1.equals("d"))
							firstserve.setOutcome(Shottypes.serve_deep);
						if (s1.equals("n"))
							firstserve.setOutcome(Shottypes.serve_net);
						if (s1.equals("x"))
							firstserve.setOutcome(Shottypes.serve_both);
						if (s1.equals("g"))
							firstserve.setOutcome(Shottypes.serve_footfault);

					}
				}
				if (s1.equals("+")) {
					firstserve.setApproach(true);
				}
			}
			firstserve.setPlayer(p.getServer());
			p.setFirstserve(firstserve);
			p.setSuccesfullserve(firstserve);
			if (Math.floorMod(game.getPoints().size(), 2) == 0) {
				secondserve.setSide(Shottypes.deucecourt);
			} else {
				secondserve.setSide(Shottypes.adcourt);
			}
			for (String s2 : tap.getSecondservestring()) {
				if (Tools.isInteger(s2)) {
					secondserve.setDirection(Integer.parseInt(s2) - 3);
				} else {
					if (s2.equals("*")) {
						secondserve.setAce(true);
						secondserve.setUnreturned(true);
						p.setNoShotsIn(1);
						firstserve.setOutcome(Shottypes.serve_ace);
					}
					if (s2.equals("#")) {
						p.setNoShotsIn(1);
						secondserve.setUnreturned(true);
						firstserve.setOutcome(Shottypes.serve_winner);
					} else if (!s2.equals("") && Shottypes.serveerrortypes.contains(s2)) {
						secondserve.setSuccess(false);
						if (s2.equals("w"))
							secondserve.setOutcome(Shottypes.serve_wide);
						if (s2.equals("d"))
							secondserve.setOutcome(Shottypes.serve_deep);
						if (s2.equals("n"))
							secondserve.setOutcome(Shottypes.serve_net);
						if (s2.equals("x"))
							secondserve.setOutcome(Shottypes.serve_both);
						if (s2.equals("g"))
							secondserve.setOutcome(Shottypes.serve_footfault);

					}
				}
				if (s2.equals("+")) {
					secondserve.setApproach(true);
				}
			}
			p.setSecondserve(secondserve);
			if (p.getFirstserve().isSuccess() == false && p.getSecondserve().isSuccess()) {
				secondserve.setPlayer(p.getServer());
				p.setSuccesfullserve(secondserve);
			}
			
			// Position des Returns:
			Shot shot = new Shot();
			int position = 1;
			if (p.getSide().equals(Shottypes.adcourt)) {
				position = 3;
			}
			if ((p.getSide().equals(Shottypes.adcourt) && p.getSuccesfullserve().getDirection() == 3) || (p.getSide().equals(Shottypes.deucecourt) && p.getSuccesfullserve().getDirection() == 3)){
				position = 2;
			}
			
			// Ab hier erst: Rally
			// System.out.println(tap.getStrings());
			for (String s : tap.getStrings()) {

				if (Shottypes.forehandshottypes.contains(s) || Shottypes.backhandshottypes.contains(s)
						|| Shottypes.othershottypes.contains(s)) {
					shot.setPosition(position);
					// System.out.println(shot.getDirection());
					// System.out.println(shot.getPosition());
					if (Shottypes.unknown.contains(s)) {
						shot.setShottype(Shottypes.other);
						if (s.equals("q"))
							shot.setShottype(Shottypes.unknown);
						if (s.equals("t"))
							shot.setShottype(Shottypes.trick);
					}
					if (Shottypes.forehandshottypes.contains(s)) {
						shot.setShottype(Shottypes.forehand);
						if (s.equals("f"))
							shot.setSpecifictype(Shottypes.groundstroke);
						if (s.equals("r"))
							shot.setSpecifictype(Shottypes.slice);
						if (s.equals("u"))
							shot.setSpecifictype(Shottypes.dropshot);
						if (s.equals("v"))
							shot.setSpecifictype(Shottypes.volley);
						if (s.equals("o"))
							shot.setSpecifictype(Shottypes.smash);
						if (s.equals("h"))
							shot.setSpecifictype(Shottypes.halfvolley);
						if (s.equals("l"))
							shot.setSpecifictype(Shottypes.lob);
						if (s.equals("j"))
							shot.setSpecifictype(Shottypes.swingvolley);
					}
					if (Shottypes.backhandshottypes.contains(s)) {
						shot.setShottype(Shottypes.backhand);
						if (s.equals("b"))
							shot.setSpecifictype(Shottypes.groundstroke);
						if (s.equals("s"))
							shot.setSpecifictype(Shottypes.slice);
						if (s.equals("z"))
							shot.setSpecifictype(Shottypes.volley);
						if (s.equals("y"))
							shot.setSpecifictype(Shottypes.dropshot);
						if (s.equals("p"))
							shot.setSpecifictype(Shottypes.smash);
						if (s.equals("i"))
							shot.setSpecifictype(Shottypes.halfvolley);
						if (s.equals("m"))
							shot.setSpecifictype(Shottypes.lob);
						if (s.equals("k"))
							shot.setSpecifictype(Shottypes.swingvolley);
					}
					// Welcher Spieler hat diesen Schlag gemacht?
					if(Math.floorMod(p.getShots().size(), 2) == 0){
						shot.setPlayer(p.getReturner());
					}
					else{
						shot.setPlayer(p.getServer());
					}
					p.getShots_strings().add(s);
					p.getShots().add(shot);
					shot = new Shot();
				}

				if (Tools.isInteger(s)) {
					if (Tools.isInteger(previousstring)) {
						// p.getShots().get(p.getShots().size()-1).setDepth(Integer.parseInt(s));
					} else {
						p.getShots().get(p.getShots().size() - 1).setDirection(Integer.parseInt(s));
						// shot.setPosition(Integer.parseInt(s));
						position = Integer.parseInt(s);
					}
				}
				if (Shottypes.outcometypes.contains(s)) {
					if (s.equals("*") && p.getShots().size() > 0) {
						// System.out.println(tap.getStrings());
						p.getShots().get(p.getShots().size() - 1).setOutcome(Shottypes.winner);
					} else if (s.equals("@")) {
						p.getShots().get(p.getShots().size() - 1).setOutcome(Shottypes.UE);
						p.getShots().get(p.getShots().size() - 1).setIn(false);
					}
					if (s.equals("#")) {
						p.getShots().get(p.getShots().size() - 1).setOutcome(Shottypes.FE);
						p.getShots().get(p.getShots().size() - 1).setIn(false);
					}
				}
				if (s.equals("+")) {
					p.getShots().get(p.getShots().size() - 1).setApproach(true);
				}
				previousstring = s;
			}
			if (p.getShots().size() > 0) {
				if (p.getShots().get(p.getShots().size() - 1).isIn()) {
					p.setNoShotsIn(p.getShots().size() + 1); // +1 wegen
																// Aufschlag
				} else {
					p.setNoShotsIn(p.getShots().size());
				}

			}
			Score newscore = computeNewScore(p.getWinner());
			newscore.setPlayer(this.getPlayers()[0], 0);
			newscore.setPlayer(this.getPlayers()[1], 1);
			p.setCreatedscore(newscore);
			if (this.getPoints().size() > 0) {
				p.setScore(this.getPoints().get(this.getPoints().size() - 1).getCreatedscore());
			}
			p.setScoreSituation(new ScoreSituation(p.getScore(), p.getServer(), this));
			//p.getScoreSituation().printScore();
			this.points.add(p);
			if (i == taps.size() - 1) {
				this.setWinner(p.getWinner());
				this.setstowin = Math.max(p.getCreatedscore().getP1_sets(), p.getCreatedscore().getP2_sets());
				// printEndResult();
			}
			game.getPoints().add(p);
			game.setServer(p.getServer());
			game.setReturner(p.getReturner());

		}

	}

	public Match(MatchInfo mi) {
		this(mi, new Player(), new Player());
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public void setPlayers1(Player player) {
		this.players[0] = player;
	}

	public void setPlayers2(Player player) {
		this.players[1] = player;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public int getSetstowin() {
		return setstowin;
	}

	public void setSetstowin(int setstowin) {
		this.setstowin = setstowin;
		this.games_in_sets = new double[2][setstowin * 2 - 1];
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void score(Player player) {
		Point p = new Point();
		if (this.getPoints().size() > 0) {
			p = new Point(this.getLastPoint());
		}
		Score newscore = computeNewScore(player);
		/*
		 * if(newscore.getP1_points() == 0 && newscore.getP2_points() == 0){
		 * if(this.getPoints().get(this.points.size()-1).getServer().equals(this
		 * .getPlayers()[0])){ p.setServer(this.getPlayers()[1]); } else
		 * if(this.getPoints().get(this.points.size()-1).getServer().equals(this
		 * .getPlayers()[1])){ p.setServer(this.getPlayers()[0]); } } else{
		 * p.setServer(this.getPoints().get(this.points.size()-1).getServer());
		 * }
		 */
		p.setCreatedscore(newscore);
		this.getPoints().add(p);
	}

	public Score computeNewScore(Player player) {
		Point lastpoint;
		if (this.getPoints().size() > 0) {
			lastpoint = this.getPoints().get(this.getPoints().size() - 1);
		} else {
			lastpoint = new Point();
		}
		Score lastscore = lastpoint.getCreatedscore();
		Score newscore = new Score(lastscore.getP1_points(), lastscore.getP2_points(), lastscore.getP1_games(),
				lastscore.getP2_games(), lastscore.getP1_sets(), lastscore.getP2_sets());

		if (player.equals(this.getPlayers()[0])) {

			// Tiebreak:
			if (lastscore.getP1_games() == 6 && lastscore.getP2_games() == 6
					&& lastscore.getP1_sets() + lastscore.getP2_sets() < this.setstowin * 2 - 2) {
				newscore.setP1_points(lastscore.getP1_points() + 1);
				if (lastscore.getP1_points() >= 6 && lastscore.getP2_points() < lastscore.getP1_points()) {
					newscore.setP1_points(0);
					newscore.setP2_points(0);
					games_in_sets[0][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP1_games() + 1;
					games_in_sets[1][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP2_games();
					newscore.setP1_games(0);
					newscore.setP2_games(0);
					newscore.setP1_sets(lastscore.getP1_sets() + 1);
					if (lastscore.getP1_sets() == setstowin - 1) {
						this.setFinished(true);
						this.setWinner(this.getPlayers()[0]);
						// System.out.println("Match over! Player 1 wins");
					}
				}
			}
			// Kein Tiebreak:
			else {
				if (lastscore.getP1_points() < 3) {
					newscore.setP1_points(lastscore.getP1_points() + 1);
				} else if (lastscore.getP1_points() >= 3) {
					if (lastscore.getP2_points() >= lastscore.getP1_points()) {
						newscore.setP1_points(lastscore.getP1_points() + 1);
					} else {
						newscore.setP1_points(0);
						newscore.setP2_points(0);
						newscore.setP1_games(lastscore.getP1_games() + 1);
						if ((lastscore.getP1_games() == 5 && lastscore.getP2_games() < 5)
								|| (lastscore.getP1_games() >= 6
										&& lastscore.getP2_games() < lastscore.getP1_games())) {
							newscore.setP1_sets(lastscore.getP1_sets() + 1);
							games_in_sets[0][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP1_games()
									+ 1;
							games_in_sets[1][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP2_games();
							newscore.setP1_games(0);
							newscore.setP2_games(0);
							if (lastscore.getP1_sets() == setstowin - 1) {
								this.setFinished(true);
								this.setWinner(this.getPlayers()[0]);
								// System.out.println("Match over! Player 1
								// wins");
							}
						}
					}
				}
			}

		}

		if (player.equals(this.getPlayers()[1])) {
			// Tiebreak:
			if (lastscore.getP1_games() == 6 && lastscore.getP2_games() == 6
					&& lastscore.getP1_sets() + lastscore.getP2_sets() < this.setstowin * 2 - 2) {
				newscore.setP2_points(lastscore.getP2_points() + 1);
				if (lastscore.getP2_points() >= 6 && lastscore.getP1_points() < lastscore.getP2_points()) {
					newscore.setP1_points(0);
					newscore.setP2_points(0);
					games_in_sets[1][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP2_games() + 1;
					games_in_sets[0][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP1_games();
					newscore.setP1_games(0);
					newscore.setP2_games(0);
					newscore.setP2_sets(lastscore.getP2_sets() + 1);
					if (lastscore.getP2_sets() == setstowin - 1) {
						this.setFinished(true);
						this.setWinner(this.getPlayers()[1]);
						// System.out.println("Match over! Player 2 wins");
					}
				}
			}
			// Kein Tiebreak
			else {
				if (lastscore.getP2_points() < 3) {
					newscore.setP2_points(lastscore.getP2_points() + 1);
				} else if (lastscore.getP2_points() >= 3) {
					if (lastscore.getP1_points() >= lastscore.getP2_points()) {
						newscore.setP2_points(lastscore.getP2_points() + 1);
					} else {
						newscore.setP2_points(0);
						newscore.setP1_points(0);
						newscore.setP2_games(lastscore.getP2_games() + 1);
						if ((lastscore.getP2_games() == 5 && lastscore.getP1_games() < 5)
								|| (lastscore.getP2_games() >= 6
										&& lastscore.getP1_games() < lastscore.getP2_games())) {
							newscore.setP2_sets(lastscore.getP2_sets() + 1);
							/*
							 * if(lastscore.getP1_sets() +
							 * lastscore.getP2_sets() == 5){
							 * this.getPoints().get(this.getPoints().size()-1).
							 * getScore().printScore(); }
							 */
							games_in_sets[1][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP2_games()
									+ 1;
							games_in_sets[0][lastscore.getP1_sets() + lastscore.getP2_sets()] = lastscore.getP1_games();
							newscore.setP2_games(0);
							newscore.setP1_games(0);
							if (lastscore.getP2_sets() == setstowin - 1) {
								this.setFinished(true);
								this.setWinner(this.getPlayers()[1]);
								// System.out.println("Match over! Player 2
								// wins");
							}
						}
					}
				}
			}
		}
		return newscore;
	}

	@SuppressWarnings("deprecation")
	public void printEndResult() {
		System.out.println(this.getDate().getDayinmonth() + "/" + this.getDate().getMonth() + "/"
				+ this.getDate().getYear() + " , " + this.getLocation() + " , " + " Round: " + this.getRound());
		Score lastscore = this.getPoints().get(this.getPoints().size() - 1).getCreatedscore();
		int noSets = lastscore.getP1_sets() + lastscore.getP2_sets();
		for (int i = 0; i < 2; i++) {
			System.out.print(this.getPlayers()[i].getName() + " : ");
			for (int j = 0; j < noSets; j++) {
				System.out.print((int) games_in_sets[i][j] + " | ");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public MatchDate getDate() {
		return date;
	}

	public void setDate(MatchDate date) {
		this.date = date;
	}
	
	public String getDatestring() {
		return datestring;
	}

	public void setDatestring(String datestring) {
		this.datestring = datestring;
	}

	public double[][] getGames_in_sets() {
		return games_in_sets;
	}

	public void setGames_in_sets(double[][] games_in_sets) {
		this.games_in_sets = games_in_sets;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Point getLastPoint() {
		return this.getPoints().get(this.getPoints().size() - 1);
	}

}
