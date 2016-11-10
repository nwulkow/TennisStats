package counts;

public class Serve extends Shot {

	String side;
	boolean ace, success, unreturned;

	public Serve() {
		super();
		this.side = "";
		this.ace = false;
		this.success = true;
		this.unreturned = false;
	}

	public Serve(String side, boolean success, boolean ace) {
		super();
		this.side = side;
		this.success = success;
		this.ace = ace;
	}

	public Serve(String side, boolean success) {
		super();
		this.side = side;
		this.success = success;
		this.ace = false;
	}

	public Serve(String side) {
		super();
		this.side = side;
		this.success = true;
		this.ace = false;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public boolean isAce() {
		return ace;
	}

	public void setAce(boolean ace) {
		this.ace = ace;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isUnreturned() {
		return unreturned;
	}

	public void setUnreturned(boolean unreturned) {
		this.unreturned = unreturned;
	}
}
