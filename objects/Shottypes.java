package objects;

import java.util.ArrayList;
import java.util.Arrays;

public class Shottypes {

	public static String righthanded = "righthanded";
	public static String lefthanded = "lefthanded";
	
	public static String forehand = "forehand";
	public static String backhand = "backhand";
	public static String other = "other";

	public static String groundstroke = "groundstroke";
	public static String volley = "volley";
	public static String halfvolley = "halfvolley";
	public static String dropshot = "dropshot";
	public static String lob = "lob";
	public static String slice = "slice";
	public static String smash = "smash";
	public static String swingvolley = "swingvolley";
	public static String unknown = "unknown";
	public static String trick = "trick";

	public static String deucecourt = "deucecourt";
	public static String adcourt = "adcourt";
	public static String UE = "UE";
	public static String FE = "FE";
	public static String winner = "winner";
	public static String in = "in";
	public static String serve_net = "serve_net";
	public static String serve_deep = "serve_deep";
	public static String serve_wide = "serve_wide";
	public static String serve_both = "serve_both";
	public static String serve_footfault = "serve_fooutfault";
	public static String serve_in = "serve_in";
	public static String serve_winner = "serve_winner";
	public static String serve_ace = "serve_ace";

	public static ArrayList<String> forehandshottypes = new ArrayList<String>(
			Arrays.asList("f", "v", "u", "r", "l", "h", "o", "j"));
	public static ArrayList<String> backhandshottypes = new ArrayList<String>(
			Arrays.asList("b", "z", "s", "y", "m", "i", "p", "k"));
	public static ArrayList<String> othershottypes = new ArrayList<String>(Arrays.asList("q", "t"));
	public static ArrayList<String> outcometypes = new ArrayList<String>(Arrays.asList("@", "*", "#"));
	public static ArrayList<String> serveerrortypes = new ArrayList<String>(Arrays.asList("w", "d", "n", "x", "g"));
	public static ArrayList<Integer> positions = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
	public static ArrayList<Integer> directions = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
	public static ArrayList<String> groundstroketypes = new ArrayList<String>(Arrays.asList(forehand, backhand));

	public static ArrayList<String> playersWhereErrorsHappen = new ArrayList<String>(
			Arrays.asList(/*"Andre_Agassi", "Guillermo_Coria", "Juan_Martin_Del_Potro", "Nicolas_Almagro"*/));
	
	
	// Results:
	static double[] sixLove = {6d,0d};
	static double[] sixOne = {6d,1d};
	static double[] sixTwo = {6d,2d};
	static double[] sixThree = {6d,3d};
	static double[] sixFour = {6d,4d};
	static double[] sevenFive = {7d,5d};
	static double[] sevenSix = {7d,6d};
	static double[] loveSix = {0d,6d};
	static double[] oneSix = {1d,6d};
	static double[] twoSix = {2d,6d};
	static double[] threeSix = {3d,6d};
	static double[] fourSix = {4d,6d};
	static double[] fiveSeven = {5d,7d};
	static double[] sixSeven = {6d,7d};
	public static ArrayList<double[]> possibleResults = new ArrayList<double[]>(Arrays.asList(sixLove, sixOne, sixTwo, sixThree,
			sixFour, sevenFive, sevenSix, sixSeven, fiveSeven, fourSix, threeSix, twoSix, oneSix, loveSix));
	public static ArrayList<String> resultNames = new ArrayList<String>(Arrays.asList("6:0", "6:1", "6:2", "6:3", "6:4",
			"7:5", "7:6", "6:7", "5:7", "4:6", "3:6", "2:6", "1:6", "0:6"));
	public static ArrayList<String> scoreNames = new ArrayList<String>(Arrays.asList("0:0","15:0","0:15", "15:15", "30:0","0:30","30:15","15:30",
			"40:0","0:40","30:30", "40:15","15:40","40:30","30:40","Deuce","AD_40","40:AD"));

	
}
