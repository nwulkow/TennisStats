package analysisFormats;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerStatList {
	
	String playername = "";
	ArrayList<String> statNames = new ArrayList<String>();
	ArrayList<Double> stats = new ArrayList<Double>();
	double instancesPlayed = 0d; //"Gewichtung"; gibt an, wie viele Punkte, Aufchlagspiele, Matches etc. zur Erhebung der Daten gespielt wurden
	
	public PlayerStatList(){
		
	}
	
	public PlayerStatList(String playername){
		this.playername = playername;
	}
	
	public PlayerStatList(String playername, ArrayList<Double> stats, ArrayList<String> statNames){
		this.playername = playername;
		this.statNames = statNames;
		this.stats = stats;
	}
	
	public PlayerStatList(String playername, ArrayList<Double> stats, ArrayList<String> statNames, double instancesPlayed){
		this.playername = playername;
		this.statNames = statNames;
		this.stats = stats;
		this.instancesPlayed = instancesPlayed;
	}
	
	public PlayerStatList(String playername, double stat, String statName, double instancesPlayed){
		this.playername = playername;
		this.statNames = new ArrayList<String>(Arrays.asList(statName));
		this.stats = new ArrayList<Double>(Arrays.asList(stat));
		this.instancesPlayed = instancesPlayed;
	}
	
	
	public void print(){
		String s = "";
		s += playername + " : ";
		for(int i = 0; i < Math.max(statNames.size(), stats.size()); i++){
			if(i < statNames.size()){
				s += statNames.get(i) + " = ";
			}
			else{
				s +=  i + " = ";
			}
			
			if(i < stats.size()){
				s += stats.get(i) + " , ";
			}
			else{
				s += " undef. , ";
			}
		}
		s += "\n";
		System.out.print(s);
	}
	

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public ArrayList<String> getStatNames() {
		return statNames;
	}

	public void setStatNames(ArrayList<String> statNames) {
		this.statNames = statNames;
	}

	public ArrayList<Double> getStats() {
		return stats;
	}

	public void setStats(ArrayList<Double> stats) {
		this.stats = stats;
	}

	public double getInstancesPlayed() {
		return instancesPlayed;
	}

	public void setInstancesPlayed(double instancesPlayed) {
		this.instancesPlayed = instancesPlayed;
	}

}
