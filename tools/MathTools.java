package tools;

public class MathTools {

	public static double factorial(int n) {
	        int fact = 1; // this  will be the result
	        for (int i = 1; i <= n; i++) {
	            fact *= i;
	        }
	        return fact;
	}

	// n! / k!
	public static double factorialOverFactorial(int n, int k){
		int fact = 1; 
		for(int i = n; i > k; i--){
			fact *=i;
		}
		return fact;
	}

}
