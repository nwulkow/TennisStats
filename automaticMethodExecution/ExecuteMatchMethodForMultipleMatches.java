package automaticMethodExecution;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import analysisFormats.MatchStatList;
import analysisFormats.MatchStatListCollection;
import analysisFormats.PlayerStatList;
import analysisFormats.PlayerStatListCollection;
import counts.Match;
import dataload.LoadValues;
import tools.OutputTools;

public class ExecuteMatchMethodForMultipleMatches {
	
	
	public static MatchStatList averageResultForMultipleMatches(Method method, ArrayList<Match> matches) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		MatchStatList first_msl = AutomaticMethodExecution.executeMSLMethod(method, matches.get(0));
		MatchStatList msl = new MatchStatList("All matches", "" , new ArrayList<double[]>(Collections.nCopies(first_msl.getStats().size(), new double[2])), first_msl.getStatNames());
		for(Match m : matches){
			MatchStatList current_msl = AutomaticMethodExecution.executeMSLMethod(method, m);
			double totalInstancesPlayed = msl.getInstancesPlayed();
			double currentInstancesPlayed = current_msl.getInstancesPlayed();
			// Durchschnitt ueber bisherige und aktuelle Ergebnisse nehmen:
			for(int i = 0; i < msl.getStats().size(); i++){
				double[] temp_result = {(msl.getStats().get(i)[0] * totalInstancesPlayed + current_msl.getStats().get(i)[0] * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed),
								(msl.getStats().get(i)[1] * totalInstancesPlayed + current_msl.getStats().get(i)[1] * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed)};
				msl.getStats().set(i, temp_result);
				}
			msl.setInstancesPlayed(totalInstancesPlayed + currentInstancesPlayed);
		}
		return msl;
	}
	
	public static MatchStatList averageResultForMultipleMatches(String methodname, ArrayList<Match> matches) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return averageResultForMultipleMatches(AMETools.loadMethod(methodname), matches);
	}
	
	public static MatchStatList averageResultForMultipleMatches(ArrayList<Method> methods, ArrayList<Match> matches) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		MatchStatList first_msl = AutomaticMethodExecution.executeMSLMethod(methods, matches.get(0));
		MatchStatList msl = new MatchStatList("All matches", "" , new ArrayList<double[]>(Collections.nCopies(first_msl.getStats().size(), new double[2])), first_msl.getStatNames());
		for(Match m : matches){
			MatchStatList current_msl = AutomaticMethodExecution.executeMSLMethod(methods, m);
			double totalInstancesPlayed = msl.getInstancesPlayed();
			double currentInstancesPlayed = current_msl.getInstancesPlayed();
			// Durchschnitt ueber bisherige und aktuelle Ergebnisse nehmen:
			for(int i = 0; i < msl.getStats().size(); i++){
				double[] temp_result = {(msl.getStats().get(i)[0] * totalInstancesPlayed + current_msl.getStats().get(i)[0] * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed),
								(msl.getStats().get(i)[1] * totalInstancesPlayed + current_msl.getStats().get(i)[1] * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed)};
				msl.getStats().set(i, temp_result);
				}
			msl.setInstancesPlayed(totalInstancesPlayed + currentInstancesPlayed);
		}
		return msl;
	}

	public static MatchStatList averageResultForMultipleMatches(String[] methodnames, ArrayList<Match> matches) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return averageResultForMultipleMatches(AMETools.loadMethods(methodnames), matches);
	}
	
	public static MatchStatListCollection executeMatchStatListMethodMultipleMatches(Method chosenMethod, ArrayList<Match> matches) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		MatchStatListCollection mslC = new MatchStatListCollection();
		for(Match m : matches){
			MatchStatList psl = AutomaticMethodExecution.executeMSLMethod(chosenMethod, m);
			mslC.add(psl);
		}
		return mslC;
	}

	public static MatchStatListCollection executeMatchStatListMethodMultipleMatches(String methodname, ArrayList<Match> matches) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executeMatchStatListMethodMultipleMatches(AMETools.loadMethod(methodname), matches);
	}

	public static MatchStatListCollection executeMatchStatListMethodMultipleMatches(ArrayList<Method> methods, ArrayList<Match> matches) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		MatchStatListCollection mslC = new MatchStatListCollection();
		for(Match m : matches){
			MatchStatList msl = AutomaticMethodExecution.executeMSLMethod(methods, m);
			mslC.add(msl);
		}
		return mslC;
	}
	public static MatchStatListCollection executeMatchStatListMethodMultipleMatches(String[] methodnames, ArrayList<Match> matches) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executeMatchStatListMethodMultipleMatches(AMETools.loadMethods(methodnames), matches);
	}
	
	
}
