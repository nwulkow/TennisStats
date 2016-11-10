package prediction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import analytics.PlayerStandardStats;
import analytics.PlayerStats;
import analytics.StandardStats;
import analytics.Testing;
import counts.Match;
import dataload.LoadValues;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import player.Player;
import simulator.Simulator;
import tools.ArrayTools;
import tools.MatchTools;
import tools.OutputTools;


// Klasse, welche Methoden enthaelt zur Match-Simulation und Sieger-Voraussage. Prognose moeglich mit Punktgewinn-%-Methode, Aufschlag-/Return-%-Methode,
// Shot-by-Shot mit und ohne Direction und SVM
public class PredictResult {
	
	Player p1, p2;
	Model model;
	//ArrayList<Method> method_list = new ArrayList<Method>();
	ArrayList<Match> p1Matches, p2Matches;
	int setstowin;
	int numIter = 10;
	
	public PredictResult(){
		this.p1 = new Player();
		this.p2 = new Player();
		this.model = new Model();
	}
	
	public PredictResult(Player p1, Player p2){
		this.p1 = p1;
		this.p2 = p2;
		this.model = new Model();
	}
	
	
	// Bekommt ein "leeres" Match und simuliert dieses folgendermaßen: Wer gegen die commonOpponents eine hoehere Siegquote hatte, ist der Sieger
	public Match simulateMatchWinRatioAgainstCO() throws IOException{
		
		// Match-Win-% ueber alle ausgewaehlten Matches laden
		double winRatio1 = PlayerStats.matchWinPercentage(p1, p1Matches);
		double winRatio2 = PlayerStats.matchWinPercentage(p2, p2Matches);
		Match m_simulated = new Match(p1,p2);
		// Wer den hoehere Match-Gewinn-Prozentsatz in den jeweils ausgewaehlten Matches hatte, gewinnt das simulierte Match
		if(winRatio1 >= winRatio2){
			m_simulated.setWinner(p1);
		}
		else{
			m_simulated.setWinner(p2);
		}
		return m_simulated;
	}
	
	// Bekommt ein "leeres" Match und simuliert dieses nach der simplen Punkt-Gewinn-Methode
	public Match simulateMatchSimple() throws IOException{
		// Point-Win-% ueber alle ausgewaehlten Matches laden
		p1.setWinvalue(PlayerStandardStats.pointsWon(p1.getName(), p1Matches));
		p2.setWinvalue(PlayerStandardStats.pointsWon(p2.getName(), p2Matches));
		
		Match m_simulated = new Match(p1,p2);
		double[] simul_wincounter = {0d,0};
		for (int i = 0; i < numIter; i++){
			m_simulated = Simulator.simulateMatch(p1, p2, setstowin, 1); // Match simulieren
			simul_wincounter[MatchTools.getPlayerIndex(m_simulated.getWinner(), m_simulated)]++;
		}
		m_simulated.setWinner(m_simulated.getPlayers()[ArrayTools.entryOfMax(simul_wincounter)]);
		return m_simulated;
	}
	
	// Bekommt ein "leeres" Match und simuliert dieses nach der Aufschlag-/Return-Gewinn-Methode
	public Match simulateMatchServeReturn() throws IOException{

		// Serve- und Return-Win-% ueber alle ausgewaehlten Matches laden
		p1.setServewinvalue(PlayerStandardStats.servicePointsWon(p1.getName(), p1Matches));
		p2.setServewinvalue(PlayerStandardStats.servicePointsWon(p2.getName(), p2Matches));
		
		Match m_simulated = new Match(p1,p2);
		double[] simul_wincounter = {0d,0};
		for (int i = 0; i < numIter; i++){
			m_simulated = Simulator.simulateMatch(p1, p2, setstowin, 2); // Match simulieren
			simul_wincounter[MatchTools.getPlayerIndex(m_simulated.getWinner(), m_simulated)]++;
		}
		m_simulated.setWinner(m_simulated.getPlayers()[ArrayTools.entryOfMax(simul_wincounter)]);
		return m_simulated;
	}
	
	
	// Bekommt ein "leeres" Match und simuliert dieses nach der Shot-by-Shot-Methode ohne Direction
		public Match simulateMatchShotByShot() throws IOException{
			
			boolean withDirection = false;
			// Tensors fuer beide Spieler zu ausgewaehlten Matches erstellen
			//p1.setTensor(PlayerStats.createTensorForOnePlayer(p1.getName(), p1Matches, withDirection));
			//p2.setTensor(PlayerStats.createTensorForOnePlayer(p2.getName(), p2Matches, withDirection));
			p1.setTensor(PlayerStats.createTensorForOnePlayerAverageEachOpponent(p1.getName(), p1Matches, withDirection));
			p2.setTensor(PlayerStats.createTensorForOnePlayerAverageEachOpponent(p2.getName(), p2Matches, withDirection));
			//p1.setTensor(PlayerStats.createTensorForOnePlayer(p1.getName(),LoadValues.loadAllMatchesOfPlayer(p1.getName()), withDirection));
			//p2.setTensor(PlayerStats.createTensorForOnePlayer(p2.getName(),LoadValues.loadAllMatchesOfPlayer(p2.getName()), withDirection));
			double power = 2;
			for(int j = 0; j < 3; j++){
				for(int k = 0; k < 3; k++){
					for(int l = 0; l < 3; l++){
						p1.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p1.getTensor().getTensor().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p2.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p2.getTensor().getTensor().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p1.getTensor().getServeplus1().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p1.getTensor().getServeplus1().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p2.getTensor().getServeplus1().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p2.getTensor().getServeplus1().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p1.getTensor().getReturns().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p1.getTensor().getReturns().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p2.getTensor().getReturns().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p2.getTensor().getReturns().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						
						for(int p = 0; p < 3; p ++){
							p1.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p] = Math.pow(p1.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p], power);
							p2.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p] = Math.pow(p2.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p], power);
						}
					}
				}
			}
			p1.getTensor().makeStochastic();
			p2.getTensor().makeStochastic();
			//OutputTools.printTensor(p1.getTensor());
			
			Match m_simulated = new Match(p1,p2);
			double[] simul_wincounter = {0d,0};
			for (int i = 0; i < numIter; i++){
				m_simulated = Simulator.simulateMatchShotByShot(p1, p2, setstowin, withDirection); // Match simulieren
				simul_wincounter[MatchTools.getPlayerIndex(m_simulated.getWinner(), m_simulated)]++;
			}
			m_simulated.setWinner(m_simulated.getPlayers()[ArrayTools.entryOfMax(simul_wincounter)]);
			return m_simulated;
		}
		
		// Bekommt ein "leeres" Match und simuliert dieses nach der Shot-by-Shot-Methode mit Direction
		public Match simulateMatchShotByShotWithDirection() throws IOException{
			
			boolean withDirection = true;
			// Tensors fuer beide Spieler zu ausgewaehlten Matches erstellen
			p1.setTensor(PlayerStats.createTensorForOnePlayer(p1.getName(), p1Matches, withDirection));
			p2.setTensor(PlayerStats.createTensorForOnePlayer(p2.getName(), p2Matches, withDirection));
			//p1.setTensor(PlayerStats.createTensorForOnePlayer(p1.getName(),LoadValues.loadAllMatchesOfPlayer(p1.getName()), withDirection));
			//p2.setTensor(PlayerStats.createTensorForOnePlayer(p2.getName(),LoadValues.loadAllMatchesOfPlayer(p2.getName()), withDirection));
			double power = 2;
			for(int j = 0; j < 3; j++){
				for(int k = 0; k < 3; k++){
					for(int l = 0; l < 3; l++){
						p1.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p1.getTensor().getTensor().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p2.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p2.getTensor().getTensor().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p1.getTensor().getServeplus1().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p1.getTensor().getServeplus1().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p2.getTensor().getServeplus1().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p2.getTensor().getServeplus1().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p1.getTensor().getReturns().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p1.getTensor().getReturns().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						p2.getTensor().getReturns().getProbForShotsAtOneIndex(j, k, l).setProbabilityForShot(Math.pow(p2.getTensor().getReturns().getProbForShotsAtOneIndex(j,k,l).getProbabilityForShot(),power));
						
						for(int p = 0; p < 3; p ++){
							p1.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p] = Math.pow(p1.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p], power);
							p2.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p] = Math.pow(p2.getTensor().getTensor().getProbForShotsAtOneIndex(j, k, l).getOutcomeProbabilities()[p], power);
						}
					}
				}
			}
			p1.getTensor().makeStochastic();
			p2.getTensor().makeStochastic();
			Match m_simulated = new Match(p1,p2);
			double[] simul_wincounter = {0d,0};
			for (int i = 0; i < numIter; i++){
				m_simulated = Simulator.simulateMatchShotByShot(p1, p2, setstowin, withDirection); // Match simulieren
				simul_wincounter[MatchTools.getPlayerIndex(m_simulated.getWinner(), m_simulated)]++;
			}
			m_simulated.setWinner(m_simulated.getPlayers()[ArrayTools.entryOfMax(simul_wincounter)]);
			return m_simulated;
		}
		
	
	// Simuliert einen Matchausgang durch SVM auf Stats-Kategorie 1
	public Match simulateMatchSVM_Category1() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		PredictBySVM pr = new PredictBySVM(p1, p2);
		
		// pr soll Daten erstellen...
		pr.setCategory(1);
		pr.assembleTrainingData(pr.getP1(), p1Matches);
		pr.assemblePredictionData(pr.getP2(), p2Matches);
		// und trainieren
		pr.trainModel(p1, SolverType.L1R_L2LOSS_SVC, false);
		
		// Matchausgang simulieren
		Match m_simulated = new Match(p1,p2);
		double didP2WinTheMatch = pr.predictWinner(p1, p2);
		if(didP2WinTheMatch == 1){
			m_simulated.setWinner(p2);
		}
		else{
			m_simulated.setWinner(p1);
		}
		return m_simulated;
	}
	
	// Simuliert einen Matchausgang durch SVM auf Stats-Kategorie 4
	public Match simulateMatchSVM_Category4() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		PredictBySVM pr = new PredictBySVM(p1, p2);
		
		// pr soll Daten erstellen...
		pr.setCategory(4);
		pr.assembleTrainingData(pr.getP1(), p1Matches);
		pr.assemblePredictionData(pr.getP2(), p2Matches);
		// und trainieren
		pr.trainModel(p1, SolverType.L1R_L2LOSS_SVC, false);
		
		// Matchausgang simulieren
		Match m_simulated = new Match(p1,p2);
		double didP2WinTheMatch = pr.predictWinner(p1, p2);
		if(didP2WinTheMatch == 1){
			m_simulated.setWinner(p2);
		}
		else{
			m_simulated.setWinner(p1);
		}
		return m_simulated;
	}
	
	
	// ------------------------- Getters & Setters --------------------------------

	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public Player getP2() {
		return p2;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ArrayList<Match> getP1Matches() {
		return p1Matches;
	}

	public void setP1Matches(ArrayList<Match> p1Matches) {
		this.p1Matches = p1Matches;
	}

	public ArrayList<Match> getP2Matches() {
		return p2Matches;
	}

	public void setP2Matches(ArrayList<Match> p2Matches) {
		this.p2Matches = p2Matches;
	}

	public int getSetstowin() {
		return setstowin;
	}

	public void setSetstowin(int setstowin) {
		this.setstowin = setstowin;
	}

	public int getNumIter() {
		return numIter;
	}

	public void setNumIter(int numIter) {
		this.numIter = numIter;
	}

}
