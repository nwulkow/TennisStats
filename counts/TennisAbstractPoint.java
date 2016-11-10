package counts;

import java.util.ArrayList;
import java.util.Arrays;

public class TennisAbstractPoint {

	ArrayList<String> strings;
	ArrayList<String> firstservestring, secondservestring;

	public TennisAbstractPoint() {
		this.strings = new ArrayList<String>();
		this.firstservestring = new ArrayList<String>();
		this.secondservestring = new ArrayList<String>();
	}

	public TennisAbstractPoint(String s, String firstserve, String secondserve) {
		this.strings = new ArrayList(Arrays.asList(s.split("(?!^)")));
		this.firstservestring = new ArrayList(Arrays.asList(firstserve.split("(?!^)")));
		this.secondservestring = new ArrayList(Arrays.asList(secondserve.split("(?!^)")));

	}

	public TennisAbstractPoint(String s) {
		this.strings = new ArrayList(Arrays.asList(s.split("(?!^)")));
		this.firstservestring = new ArrayList<String>();
		this.secondservestring = new ArrayList<String>();
	}

	public ArrayList<String> getStrings() {
		return strings;
	}

	public void setStrings(ArrayList<String> strings) {
		this.strings = strings;
	}

	public ArrayList<String> getFirstservestring() {
		return firstservestring;
	}

	public void setFirstservestring(ArrayList<String> firstservestring) {
		this.firstservestring = firstservestring;
	}

	public ArrayList<String> getSecondservestring() {
		return secondservestring;
	}

	public void setSecondservestring(ArrayList<String> secondservestring) {
		this.secondservestring = secondservestring;
	}

}
