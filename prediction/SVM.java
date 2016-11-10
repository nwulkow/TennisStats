package prediction;

import java.io.File;
import java.io.IOException;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class SVM {
	
	// Konvertiert double[][] zu FeatureNode[][]
	public static FeatureNode[] arrayToFeatureNodes(double[] array){
		FeatureNode[] featureNode = new FeatureNode[array.length];
		for(int i = 0; i < array.length; i++){
			featureNode[i] = new FeatureNode(i+1,array[i]);
		}
		return featureNode;
	}
	
	public static FeatureNode[][] arrayToFeatureNodeSet(double[][] array){
		
		FeatureNode[][] featureNodes = new FeatureNode[array.length][array[0].length];
		for(int i = 0; i < array.length; i++){
			FeatureNode[] node = arrayToFeatureNodes(array[i]);
			featureNodes[i] = node;
		}
		return featureNodes;	
	}
	
	public static Model trainModel(FeatureNode[][] featureNodes, double[] labels, SolverType solver, boolean fileOutput) throws IOException{
		
		Problem problem = new Problem();

        // number of training examples
        problem.l = featureNodes.length;

        // number of features
        problem.n = featureNodes[0].length;

        // problem.x = ... // feature nodes
        problem.x = featureNodes;

        // problem.y = ... // target values
        problem.y = labels;

        //SolverType solver = SolverType.L1R_L2LOSS_SVC; // -s 0
        double C = 50; // cost of constraints violation
        double eps = 0.01; // stopping criteria

        Parameter parameter = new Parameter(solver, C, eps);
        Model model = Linear.train(problem, parameter);
        if(fileOutput){
        	File modelFile = new File("C:/Users/Niklas/TennisStatsData/model");
            model.save(modelFile);
        }
        return model;
	}
	
	public static Model trainModel(double[][] array, double[] labels, SolverType solver, boolean fileOutput) throws IOException{
		return trainModel(arrayToFeatureNodeSet(array), labels, solver, fileOutput);
	}
	
	public static double predictInstance(FeatureNode[] instance, Model model){
		 double prediction = Linear.predict(model, instance);
		 return prediction;
	}
	
	public static double predictInstance(double[] instance, Model model){
		return predictInstance(arrayToFeatureNodes(instance), model);
	}
	
	public static double predictInstance(FeatureNode[][] featureNodes, double[] labels, SolverType solver, double[] instance) throws IOException{
		Model model = trainModel(featureNodes, labels, solver, false);
		return predictInstance(instance, model);
	}
	
	public static double predictInstance(double[][] array, double[] labels, SolverType solver, double[] instance) throws IOException{
		return predictInstance(arrayToFeatureNodeSet(array), labels, solver, instance);
	}


}
