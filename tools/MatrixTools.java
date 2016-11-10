package tools;

import Jama.Matrix;
import scala.util.Random;

public class MatrixTools {

	// Erstellt eine Matrix mit Einsen und Nullen. ratio ist der Anteil der Einsen
	public static Matrix oneZeroMatrix(int n, int m, double ratio){
		
		Matrix M = new Matrix(n,m);
		Random r = new Random();
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++){
				if(r.nextDouble() < ratio){
					M.set(i, j, 1);
				}
				else{
					M.set(i, j, 0);
				}
			}
		}
		return M;
	}
	
	public static Matrix oneZeroMatrixSymmetric(int n, int m, double ratio){
		
		Matrix M = new Matrix(n,m);
		Random r = new Random();
		for(int i = 0; i < n; i++){
			for(int j = i; j < m; j++){
				if(r.nextDouble() < ratio){
					M.set(i, j, 1);
					M.set(j, i, 1);
				}
				else{
					M.set(i, j, 0);
					M.set(j, i, 0);
				}
			}
		}
		return M;
	}

	public static Matrix randomMatrix(int n, int m, double min, double max){
		Random r = new Random();
		Matrix matrix = new Matrix(n,m);
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++){
				matrix.set(i, j, min + r.nextDouble()*(max-min));
			}
		}
		return matrix;
	}
	
}
