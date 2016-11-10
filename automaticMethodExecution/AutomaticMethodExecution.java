package automaticMethodExecution;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import analysisFormats.MatchStatList;
import analysisFormats.PlayerStatList;
import counts.Match;
import dataload.LoadValues;

public class AutomaticMethodExecution {
	
	public static PlayerStatList executePSLMethod(Method chosenMethod, Object input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		if(chosenMethod != null){
			double[] result = null;
			PlayerStatList result_psl = null;
			ArrayList<Match> matches = new ArrayList<Match>();
			if(input.getClass().getName().equals("java.lang.String")){
				matches = LoadValues.loadAllMatchesOfPlayer((String)input);
			}
			// Entscheiden, welchen return type die Methode gibt
			if(chosenMethod.getReturnType().getName().equals("[D")){ // return Type = double[]
				result = (double[])chosenMethod.invoke(null, input, matches);
				return AMETools.convertDoubleArrayToPSL(result, (String)input);
			}
			if(chosenMethod.getReturnType().getName().equals("double")){
				result = new double[1];
				result[0] = (double)chosenMethod.invoke(null, input, matches);
				return AMETools.convertDoubleArrayToPSL(result, (String)input);
			}
			if(chosenMethod.getReturnType().getName().equals("analysisFormats.PlayerStatList")){
				result_psl = (PlayerStatList)chosenMethod.invoke(null, (String)input, matches);
				return result_psl;
			}
			return result_psl;
		}
		else{
			return null;
		}
	}
		
	public static PlayerStatList executePSLMethod(String methodname, Object playername) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executePSLMethod(AMETools.loadMethod(methodname), playername);
	}
	
	public static PlayerStatList executePSLMethod(ArrayList<Method> methods, Object playername) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		PlayerStatList psl = new PlayerStatList();
		for(Method method : methods){
			PlayerStatList current_psl = executePSLMethod(method, playername);
			psl.setPlayername(current_psl.getPlayername());
			psl.getStats().addAll(current_psl.getStats());
			psl.getStatNames().addAll(current_psl.getStatNames());
			psl.setInstancesPlayed(current_psl.getInstancesPlayed());
		}
		return psl;
	}
	public static PlayerStatList executePSLMethod(ArrayList<String> methodnames, String playername) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executePSLMethod(AMETools.loadMethods(methodnames), playername);
	}
	public static PlayerStatList executePSLMethod(String[] methodnames, String playername) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executePSLMethod(AMETools.loadMethods(methodnames), playername);
	}
	
	public static MatchStatList executeMSLMethod(Method chosenMethod, Object input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		if(chosenMethod != null){
			double[] result = null;
			MatchStatList result_msl = null;			
			// Entscheiden, welchen return type die Methode gibt
			if(chosenMethod.getReturnType().getName().equals("[D")){ // return Type = double[]
				result = (double[])chosenMethod.invoke(null, input);
				return AMETools.convertDoubleArrayToMSL(result, (Match)input);
			}
			if(chosenMethod.getReturnType().getName().equals("analysisFormats.MatchStatList")){
				result_msl = (MatchStatList)chosenMethod.invoke(null, input);
				return result_msl;
			}
			return result_msl;
		}
		else{
			return null;
		}
	}
	
	public static MatchStatList executeMSLMethod(String methodname, Object input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executeMSLMethod(AMETools.loadMethod(methodname), input);
	}
	
	public static MatchStatList executeMSLMethod(ArrayList<Method> methods, Object match) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		MatchStatList msl = new MatchStatList();
		for(Method method : methods){
			MatchStatList current_msl = executeMSLMethod(method, match);
			msl.setPlayer1name(current_msl.getPlayer1name());
			msl.setPlayer2name(current_msl.getPlayer2name());
			msl.getStats().addAll(current_msl.getStats());
			msl.getStatNames().addAll(current_msl.getStatNames());
			msl.setInstancesPlayed(current_msl.getInstancesPlayed());
		}
		return msl;
	}
	public static MatchStatList executeMSLMethod(ArrayList<String> methodnames, Match match) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executeMSLMethod(AMETools.loadMethods(methodnames), match);
	}
	public static MatchStatList executeMSLMethod(String[] methodnames, Match match) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executeMSLMethod(AMETools.loadMethods(methodnames), match);
	}

}
