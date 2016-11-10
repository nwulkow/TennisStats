package automaticMethodExecution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import analysisFormats.MatchStatList;
import analysisFormats.PlayerStatList;
import analytics.PlayerStandardStats;
import analytics.StandardStats;
import analytics.Testing;
import counts.Match;
import tools.ArrayTools;

// Automatic Method Execution Tools
public class AMETools {

	public static Method loadMethod(String methodname){
		ArrayList<Method[]> methodlist = new ArrayList<Method[]>();
		
		methodlist.add(PlayerStandardStats.class.getDeclaredMethods());
		methodlist.add(Testing.class.getDeclaredMethods());
		methodlist.add(StandardStats.class.getDeclaredMethods());
		Method chosenMethod = null;
		for(Method[] methods : methodlist){
			for(Method method : methods){
				if(method.getName().equals(methodname)){
					chosenMethod = method;
					return chosenMethod;
				}
			}
		}
		return chosenMethod;
	}
	
	public static ArrayList<Method> loadMethods(ArrayList<String> methodnames){
		ArrayList<Method> methods = new ArrayList<Method>();
		for(String methodname : methodnames){
			methods.add(loadMethod(methodname));
		}
		return methods;
	}
	
	public static ArrayList<Method> loadMethods(String[] methodnames){
		ArrayList<Method> methods = new ArrayList<Method>();
		for(String methodname : methodnames){
			methods.add(loadMethod(methodname));
		}
		return methods;
	}

	public static PlayerStatList convertDoubleArrayToPSL(double[] array, String playername){
		PlayerStatList pls = new PlayerStatList(playername, ArrayTools.ArrayToArrayList(array), new ArrayList<String>());
		return pls;
	}

	public static MatchStatList convertDoubleArrayToMSL(double[] array, Match m){
		MatchStatList msl = new MatchStatList(m.getPlayers()[0].getName(), m.getPlayers()[1].getName(), new ArrayList<double[]>(Arrays.asList(array)), new ArrayList<String>(),m.getPoints().size());
		return msl;
	}
	
	public static MatchStatList executeMethodToMSL(Method method, Match m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		MatchStatList msl = new MatchStatList(m, new double[2], "");
		if(method.getGenericReturnType().getTypeName().equals("double[]")){
			double[] result = (double[])method.invoke(null, m);
			msl.getStats().set(0,result);
			return msl;
		}
		if(method.getGenericReturnType().getTypeName().equals("analysisFormats.MatchStatList")){
			msl = (MatchStatList)method.invoke(null, m);
			return msl;
		}
		
		return msl;
		
	}
	
}
