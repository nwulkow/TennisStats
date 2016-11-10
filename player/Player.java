package player;

import java.io.IOException;

import org.apache.flink.graph.Triplet;

import analysisFormats.ProbabilityTensor;
import dataload.LoadValues;
import de.bwaldvogel.liblinear.FeatureNode;

public class Player {

	String name, firstname, lastname, hand;
	double winvalue,servewinvalue, returnwinvalue,v1, v2, g;
	int N; //Anzahl der Punkte, die er gespielt hat
	double[][][] TP_shots = new double[3][3][3];
	ProbabilityTensor tensor;
	FeatureNode[][] nodeSet;
	FeatureNode[] nodes;
	double[] labels;

	public Player() {

		this.name = "";
		this.firstname = "";
		this.lastname = "";
		this.servewinvalue = 0.5;
		this.returnwinvalue = 0.5;
		this.v1 = 0.5;
		this.v2 = 0.5;
		this.g = 0;
		this.N = 0;
		//this.tensor = new ProbabilityTensor();
	}

	public Player(String name) {

		this.name = name;
		this.firstname = name.split("_")[0];
		if (name.split("_").length > 1) {
			this.lastname = name.split("_")[1];
		}
		this.servewinvalue = 0.5;
		this.returnwinvalue = 0.5;
		this.v1 = 0.5;
		this.v2 = 0.5;
		this.g = 0;
		this.N = 0;
		//this.tensor = new ProbabilityTensor();
	}

	public Player(String name,double servewinvalue, double returnwinvalue) {

		this.name = name;
		//this.firstname = name.split("_")[0];
		//this.lastname = name.split("_")[1];
		this.servewinvalue = servewinvalue;
		this.returnwinvalue = returnwinvalue;
		this.v1 = 0.5;
		this.v2 = 0.5;
		this.g = 0;
		this.N = 0;
		//this.tensor = new ProbabilityTensor();
	}
	
	public Player(String name, double v1, int N, double v2, double g) {

		this.name = name;
		//this.firstname = name.split("_")[0];
		//this.lastname = name.split("_")[1];
		this.servewinvalue = 0.5;
		this.returnwinvalue = 0.5;
		this.v1 = v1;
		this.v2 = v2;
		this.g = g;
		this.N = N;
		//this.tensor = new ProbabilityTensor();
	}
	
	public Player(String name,double servewinvalue, double returnwinvalue, double v1, int N, double v2, double g) {

		this.name = name;
		//this.firstname = name.split("_")[0];
		//this.lastname = name.split("_")[1];
		this.servewinvalue = servewinvalue;
		this.returnwinvalue = returnwinvalue;
		this.v1 = v1;
		this.v2 = v2;
		this.g = g;
		this.N = N;
		//this.tensor = new ProbabilityTensor();
	}

	
	public void loadTensor(boolean withDirection) throws NumberFormatException, IOException{
		this.tensor = LoadValues.loadTensor("PlayerStats/" + this.name, withDirection);
	}
	public void loadCurrentTensor(boolean withDiretion) throws NumberFormatException, IOException{
		this.tensor = LoadValues.loadTensor("Current/" + this.name, withDiretion); // Fuer einen temporaer erstellen Tensor
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setFirstname(name.split(" ")[0]);
		setLastname(name.split(" ")[1]);
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public double getV1() {
		return v1;
	}

	public void setV1(double v1) {
		this.v1 = v1;
	}

	public double getV2() {
		return v2;
	}

	public void setV2(double v2) {
		this.v2 = v2;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public double getServewinvalue() {
		return servewinvalue;
	}

	public void setServewinvalue(double servewinvalue) {
		this.servewinvalue = servewinvalue;
	}

	public double getReturnwinvalue() {
		return returnwinvalue;
	}

	public void setReturnwinvalue(double returnwinvalue) {
		this.returnwinvalue = returnwinvalue;
	}

	public double[][][] getTP_shots() {
		return TP_shots;
	}

	public void setTP_shots(double[][][] tP_shots) {
		TP_shots = tP_shots;
	}

	public ProbabilityTensor getTensor() {
		return tensor;
	}

	public void setTensor(ProbabilityTensor tensor) {
		this.tensor = tensor;
	}

	public double getWinvalue() {
		return winvalue;
	}

	public void setWinvalue(double winvalue) {
		this.winvalue = winvalue;
	}

	public String getHand() {
		return hand;
	}

	public void setHand(String hand) {
		this.hand = hand;
	}

	public FeatureNode[][] getNodeSet() {
		return nodeSet;
	}

	public void setNodeSet(FeatureNode[][] nodeSet) {
		this.nodeSet = nodeSet;
	}

	public double[] getLabels() {
		return labels;
	}

	public void setLabels(double[] labels) {
		this.labels = labels;
	}

	public FeatureNode[] getNodes() {
		return nodes;
	}

	public void setNodes(FeatureNode[] nodes) {
		this.nodes = nodes;
	}


}
