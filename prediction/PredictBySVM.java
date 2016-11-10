package prediction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import analysisFormats.MatchStatList;
import analytics.StandardStats;
import automaticMethodExecution.AMETools;
import counts.Match;
import dataload.LoadValues;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.SolverType;
import player.Player;
import statsCategories.Category1Stats;
import statsCategories.Category2Stats;
import statsCategories.Category3Stats;
import statsCategories.Category4Stats;
import tools.ArrayTools;
import tools.MatchTools;

public class PredictBySVM {
	
	Player p1, p2;
	Model model;
	ArrayList<Method> method_list = new ArrayList<Method>();
	int category = 1;
	
	public PredictBySVM(){
		this.p1 = new Player();
		this.p2 = new Player();
		this.model = new Model();
	}
	
	public PredictBySVM(Player p1, Player p2){
		this.p1 = p1;
		this.p2 = p2;
		this.model = new Model();
	}
	
	
	public void assembleTrainingData(Player player, ArrayList<Match> matches) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		//ArrayList<String> commonOpponents = LoadValues.loadCommonOpponentNames(new ArrayList<String>(Arrays.asList(p1.getName(), p2.getName())));
		//ArrayList<Match> matches = LoadValues.loadMatchesAgainstSelectedOpponents(new ArrayList<String>(Arrays.asList(p1.getName())), commonOpponents);
		
		//ArrayList<Match> matches = LoadValues.loadMatchesAgainstSelectedOpponents(player.getName(), selectedOpponents);
		double[] labels = new double[matches.size()];
		
		// Alle Methoden in StandardStats holen
		Method[] methods = StandardStats.class.getDeclaredMethods();
		switch(category){
		case 1: methods = Category1Stats.class.getDeclaredMethods();
				break;
		case 2: methods = Category2Stats.class.getDeclaredMethods();
				break;
		case 3: methods = Category3Stats.class.getDeclaredMethods();
				break;
		case 4: methods = Category4Stats.class.getDeclaredMethods();
				break;
		default: methods = Category1Stats.class.getDeclaredMethods();
				break;
		}
		// Die rausfiltern und weiter benutzen, die "double[]" als Ausgabetyp haben
		for(Method method : methods){
			if(method.getGenericReturnType().getTypeName().equals("double[]") || method.getGenericReturnType().getTypeName().equals("analysisFormats.MatchStatList")){
				method_list.add(method);
			}
		}
		double[][] data = new double[matches.size()][method_list.size()];
		ArrayList<ArrayList<Double>> data_list = new ArrayList<ArrayList<Double>>();
		
		for(int i = 0; i < matches.size(); i++){
			data_list.add(new ArrayList<Double>());
			Match m = matches.get(i);
			int playerindex = Math.floorMod(MatchTools.getPlayerIndex(player, m)+1,2);
			int methodcounter = 0;
			for(int j = 0; j < method_list.size(); j++){
				Method method = method_list.get(j);
				// Ergebnis festhalten. Vorher bestimmen, ob der Spieler in diesem Match Spieler 1 oder 2 war
				//double[] output =  (double[])method.invoke(null, m);
				MatchStatList msl = AMETools.executeMethodToMSL(method, m);
				for(double[] output : msl.getStats()){
					//data[i][methodcounter] = output[playerindex];
					data_list.get(i).add(output[playerindex]);
					methodcounter++;
				}
			}
			labels[i] = 1;
			if(m.getWinner().getName().equals(player.getName())){
				labels[i] = -1;
			}
		}
		data = ArrayTools.ArrayList2DimToArray(data_list);
		//OutputTools.printMatrix(data);
		//data = new Matrix(data).transpose().getArray();
		player.setNodeSet(SVM.arrayToFeatureNodeSet(data));
		player.setLabels(labels);
	}
	
	public void assembleTrainingData(Player player) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		assembleTrainingData(player, LoadValues.loadAllMatchesOfPlayer(player.getName()));
	}
	
	
	public void assemblePredictionData(Player player, ArrayList<Match> matches) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		//ArrayList<String> commonOpponents = LoadValues.loadCommonOpponentNames(new ArrayList<String>(Arrays.asList(p1.getName(), p2.getName())));
		//ArrayList<Match> matches = LoadValues.loadMatchesAgainstSelectedOpponents(new ArrayList<String>(Arrays.asList(p1.getName())), commonOpponents);
		
		//ArrayList<Match> matches = LoadValues.loadMatchesAgainstSelectedOpponents(player.getName(), selectedOpponents);

		// Die rausfiltern und weiter benutzen, die "double[]" als Ausgabetyp haben
		Method[] methods = StandardStats.class.getDeclaredMethods();
		switch(category){
		case 1: methods = Category1Stats.class.getDeclaredMethods();
				break;
		case 2: methods = Category2Stats.class.getDeclaredMethods();
				break;
		case 3: methods = Category3Stats.class.getDeclaredMethods();
				break;
		case 4: methods = Category4Stats.class.getDeclaredMethods();
				break;
		default: methods = Category1Stats.class.getDeclaredMethods();
				break;
		}
		method_list = new ArrayList<Method>();
		for(Method method : methods){
			if(method.getGenericReturnType().getTypeName().equals("double[]")){
				method_list.add(method);
			}
		}
		//double[][] data = new double[matches.size()][method_list.size()];
		double[] sums = new double[method_list.size()];

		for(int i = 0; i < matches.size(); i++){
			Match m = matches.get(i);
			int playerindex = MatchTools.getPlayerIndex(player, m);
			for(int j = 0; j < method_list.size(); j++){
				Method method = method_list.get(j);
				double[] output =  (double[])method.invoke(null, m);
				// Ergebnis festhalten. Vorher bestimmen, ob der Spieler in diesem Match Spieler 1 oder 2 war
				//data[i][j] = output[playerindex];
				sums[j] += output[playerindex];
			}
		}
		sums = ArrayTools.multiplyArrayScalar(sums, 1 / (double)matches.size());
		player.setNodes(SVM.arrayToFeatureNodes(sums));
	}
	
	public void assemblePredictionData(Player player) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		assemblePredictionData(player, LoadValues.loadAllMatchesOfPlayer(player.getName()));
	}
	
	public void trainModel(Player player, SolverType solver, boolean fileOutput) throws IOException{
		this.model = SVM.trainModel(player.getNodeSet(), player.getLabels(), solver, fileOutput);
	}
	
	public double predictWinner(Player trainer, Player tester) throws IOException{
		return SVM.predictInstance(tester.getNodes(), this.model);
	}
	
	public double testTrainingDataSet(Player player) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		double correct = 0;
		if(player.getNodeSet() == null){
			assembleTrainingData(player);
		}
		for(int i = 0; i < player.getNodeSet().length; i++){
			FeatureNode[] nodes = player.getNodeSet()[i];
			//System.out.println(SVM.predictInstance(nodes, model));
			if(Math.signum(SVM.predictInstance(nodes, model)) == Math.signum(player.getLabels()[i])){
				correct++;
			}
		}
		double accuracy = (correct / (double)player.getNodeSet().length);
		System.out.println("Classified correctly: " + correct + " / " + player.getNodeSet().length + " ( = " + accuracy + " ) ");
		return accuracy;
	}
	


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

	public ArrayList<Method> getMethod_list() {
		return method_list;
	}

	public void setMethod_list(ArrayList<Method> method_list) {
		this.method_list = method_list;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

}
