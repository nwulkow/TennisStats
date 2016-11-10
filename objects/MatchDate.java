package objects;

import java.util.Date;

public class MatchDate extends Date {

	int dayinmonth = 0;

	public MatchDate() {
		super();
	}

	public MatchDate(int year, int month, int dayinmonth) {
		super(year, month, dayinmonth);
		this.dayinmonth = dayinmonth;
	}

	public int getDayinmonth() {
		return dayinmonth;
	}

	public void setDayinmonth(int dayinmonth) {
		this.dayinmonth = dayinmonth;
	}
}
