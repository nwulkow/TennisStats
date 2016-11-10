package counts;

import java.util.ArrayList;

import player.Player;

public class MatchInfo {

	ArrayList<TennisAbstractPoint> taps;
	ArrayList<Integer> serverlist, winnerlist;
	String date = "", location = "", round = "";
	// Player p1, p2;

	public MatchInfo(ArrayList<TennisAbstractPoint> taps, ArrayList<Integer> serverlist,
			ArrayList<Integer> winnerlist) {
		this.taps = taps;
		this.serverlist = serverlist;
		this.winnerlist = winnerlist;
	}

	public ArrayList<TennisAbstractPoint> getTaps() {
		return taps;
	}

	public void setTaps(ArrayList<TennisAbstractPoint> taps) {
		this.taps = taps;
	}

	public ArrayList<Integer> getServerlist() {
		return serverlist;
	}

	public void setServerlist(ArrayList<Integer> serverlist) {
		this.serverlist = serverlist;
	}

	public ArrayList<Integer> getWinnerlist() {
		return winnerlist;
	}

	public void setWinnerlist(ArrayList<Integer> winnerlist) {
		this.winnerlist = winnerlist;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

}
