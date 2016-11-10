package counts;

import java.util.ArrayList;

import player.Player;

public class Game {

	ArrayList<Point> points;
	Player server, returner, winner;

	public Game() {

		this.points = new ArrayList<Point>();
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
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

}
