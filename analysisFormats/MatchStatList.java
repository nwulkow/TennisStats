package analysisFormats;

import java.util.ArrayList;
import java.util.Arrays;

import counts.Match;

public class MatchStatList {
	
	String player1name, player2name;
	ArrayList<String> statNames = new ArrayList<String>();
	ArrayList<double[]> stats = new ArrayList<double[]>();
	double instancesPlayed = 0d; //"Gewichtung"; gibt an, wie viele Punkte, Aufchlagspiele etc. zur Erhebung der Daten gespielt wurden
	
	
	public MatchStatList(){
		
	}
	
	public MatchStatList(String player1name, String player2name, ArrayList<double[]> stats, ArrayList<String> statNames){
		this.player1name = player1name;
		this.player2name = player2name;
		this.stats = stats;
		this.statNames = statNames;
	}
	
	public MatchStatList(String player1name, String player2name, ArrayList<double[]> stats, ArrayList<String> statNames, double instancesPlayed){
		this.player1name = player1name;
		this.player2name = player2name;
		this.stats = stats;
		this.statNames = statNames;
		this.instancesPlayed = instancesPlayed;
	}
	
	public MatchStatList(String player1name, String player2name, double[] stat, String statName){
		this.player1name = player1name;
		this.player2name = player2name;
		this.stats = new ArrayList<double[]>(Arrays.asList(stat));
		this.statNames = new ArrayList<String>(Arrays.asList(statName));
	}
	
	public MatchStatList(String player1name, String player2name, double[] stat, String statName, double instancesPlayed){
		this.player1name = player1name;
		this.player2name = player2name;
		this.stats = new ArrayList<double[]>(Arrays.asList(stat));
		this.statNames = new ArrayList<String>(Arrays.asList(statName));
		this.instancesPlayed = instancesPlayed;
	}
	
	public MatchStatList(Match m, double[] stat, String statName){
		this.player1name = m.getPlayers()[0].getName();
		this.player2name = m.getPlayers()[1].getName();
		this.stats = new ArrayList<double[]>(Arrays.asList(stat));
		this.statNames = new ArrayList<String>(Arrays.asList(statName));
		this.instancesPlayed = m.getPoints().size();
	}
	
	public MatchStatList(Match m){
		this.player1name = m.getPlayers()[0].getName();
		this.player2name = m.getPlayers()[1].getName();
		this.instancesPlayed = m.getPoints().size();
	}

	public void print(){
		String[] playernames = {player1name, player2name};
		String s = "";
		for(int j = 0; j < playernames.length; j++){
			s += playernames[j] + " : ";
			for(int i = 0; i < Math.max(statNames.size(), stats.size()); i++){
				if(i < statNames.size()){
					s += statNames.get(i) + " = ";
				}
				else{
					s +=  i + " = ";
				}
				
				if(i < stats.size()){
					s += stats.get(i)[j] + " , ";
				}
				else{
					s += " undef. , ";
				}
			}
			s += "\n";
		}
		System.out.print(s);
	}

	public String getPlayer1name() {
		return player1name;
	}

	public void setPlayer1name(String player1name) {
		this.player1name = player1name;
	}

	public String getPlayer2name() {
		return player2name;
	}

	public void setPlayer2name(String player2name) {
		this.player2name = player2name;
	}

	public ArrayList<String> getStatNames() {
		return statNames;
	}

	public void setStatNames(ArrayList<String> statNames) {
		this.statNames = statNames;
	}

	public ArrayList<double[]> getStats() {
		return stats;
	}

	public void setStats(ArrayList<double[]> stats) {
		this.stats = stats;
	}

	public double getInstancesPlayed() {
		return instancesPlayed;
	}

	public void setInstancesPlayed(double instancesPlayed) {
		this.instancesPlayed = instancesPlayed;
	}
	
	
}
