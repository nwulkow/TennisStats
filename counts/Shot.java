package counts;

import objects.Shottypes;
import player.Player;

public class Shot {

	int position, direction, depth;
	String shottype, outcome, specifictype;
	boolean in = true;
	boolean approach = false;
	Player player = new Player();

	public Shot() {
		this.position = 0;
		this.direction = 0;
		this.shottype = "";
		this.outcome = Shottypes.in;
		this.depth = 0;
		this.specifictype = "";
	}

	public Shot(String shottype, int position, int direction, int depth, String outcome, String specifictype) {
		this.shottype = shottype;
		this.position = position;
		this.direction = direction;
		this.outcome = outcome;
		this.depth = depth;
		this.specifictype = specifictype;
	}

	public Shot(String shottype, int position, int direction, int depth) {
		this(shottype, position, direction, depth, Shottypes.in, Shottypes.groundstroke);
	}

	public Shot(String shottype) {
		this(shottype, 0, 0, 0);
	}

	public Shot(String shottype, int direction) {
		this(shottype, 0, 0, direction);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getShottype() {
		return shottype;
	}

	public void setShottype(String shottype) {
		this.shottype = shottype;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getSpecifictype() {
		return specifictype;
	}

	public void setSpecifictype(String specifictype) {
		this.specifictype = specifictype;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public boolean isApproach() {
		return approach;
	}

	public void setApproach(boolean approach) {
		this.approach = approach;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
