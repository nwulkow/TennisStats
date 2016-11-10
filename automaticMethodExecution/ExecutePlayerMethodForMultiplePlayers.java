package automaticMethodExecution;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import analysisFormats.PlayerStatList;
import analysisFormats.PlayerStatListCollection;
import counts.Match;
import dataload.LoadValues;

public class ExecutePlayerMethodForMultiplePlayers {
	
	
	public static PlayerStatList averageResultForMultiplePlayers(Method method, ArrayList<String> playernames) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		PlayerStatList first_psl = AutomaticMethodExecution.executePSLMethod(method, playernames.get(0));
		PlayerStatList psl = new PlayerStatList("All players", new ArrayList<Double>(Collections.nCopies(first_psl.getStats().size(), 0d)), first_psl.getStatNames());
		for(String playername : playernames){
			PlayerStatList current_psl = AutomaticMethodExecution.executePSLMethod(method, playername);
			double totalInstancesPlayed = psl.getInstancesPlayed();
			double currentInstancesPlayed = current_psl.getInstancesPlayed();
			// Durchschnitt ueber bisherige und aktuelle Ergebnisse nehmen:
			for(int i = 0; i < psl.getStats().size(); i++){
				psl.getStats().set(i, (psl.getStats().get(i) * totalInstancesPlayed + current_psl.getStats().get(i) * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed));
			}
			psl.setInstancesPlayed(totalInstancesPlayed + currentInstancesPlayed);
		}
		return psl;
	}
	
	public static PlayerStatList averageResultForMultiplePlayers(String methodname, ArrayList<String> playernames) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return averageResultForMultiplePlayers(AMETools.loadMethod(methodname), playernames);
	}
	
	public static PlayerStatList averageResultForMultiplePlayers(ArrayList<Method> methods, ArrayList<String> playernames) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		PlayerStatList first_psl = AutomaticMethodExecution.executePSLMethod(methods, playernames.get(0));
		PlayerStatList psl = new PlayerStatList("All players", new ArrayList<Double>(Collections.nCopies(first_psl.getStats().size(), 0d)), first_psl.getStatNames());
		for(String playername : playernames){
			PlayerStatList current_psl = AutomaticMethodExecution.executePSLMethod(methods, playername);
			double totalInstancesPlayed = psl.getInstancesPlayed();
			double currentInstancesPlayed = current_psl.getInstancesPlayed();
			// Durchschnitt ueber bisherige und aktuelle Ergebnisse nehmen:
			for(int i = 0; i < psl.getStats().size(); i++){
				psl.getStats().set(i, (psl.getStats().get(i) * totalInstancesPlayed + current_psl.getStats().get(i) * currentInstancesPlayed) / (totalInstancesPlayed + currentInstancesPlayed));
			}
			psl.setInstancesPlayed(totalInstancesPlayed + currentInstancesPlayed);
		}
		return psl;
	}
	public static PlayerStatList averageResultForMultiplePlayers(String[] methodnames, ArrayList<String> playernames) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return averageResultForMultiplePlayers(AMETools.loadMethods(methodnames), playernames);
	}

	public static PlayerStatListCollection executePlayerStatListMethodMultiplePlayers(Method chosenMethod, ArrayList<String> playernames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		PlayerStatListCollection pslC = new PlayerStatListCollection();
		for(String playername : playernames){
			PlayerStatList psl = AutomaticMethodExecution.executePSLMethod(chosenMethod, playername);
			pslC.add(psl);
		}
		return pslC;
	}
	

	public static PlayerStatListCollection executePlayerStatListMethodMultiplePlayers(String methodname, ArrayList<String> playernames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executePlayerStatListMethodMultiplePlayers(AMETools.loadMethod(methodname), playernames);
	}
	
	public static PlayerStatListCollection executePlayerStatListMethodMultiplePlayers(ArrayList<Method> methods, ArrayList<String> playernames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		PlayerStatListCollection pslC = new PlayerStatListCollection();
		for(String playername : playernames){
			PlayerStatList psl = AutomaticMethodExecution.executePSLMethod(methods, playername);
			pslC.add(psl);
		}
		return pslC;
	}
	
	public static PlayerStatListCollection executePlayerStatListMethodMultiplePlayers(String[] methodnames, ArrayList<String> playernames) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException{
		return executePlayerStatListMethodMultiplePlayers(AMETools.loadMethods(methodnames), playernames);
	}
	
	
	
	
	
}
