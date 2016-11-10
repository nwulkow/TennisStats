package dataload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import analytics.Metrics;
import counts.Match;
import counts.MatchInfo;
import objects.OutputFinals;
import player.Player;
import tools.OutputTools;

public class LoadAllTAMatches {

	public static void loadAllTAMatches(String filename) throws IOException, InterruptedException {

		String line = "";
		// String previousline = " ";
		// int matchcounter = 0;
		// int filecounter = 0;
		FileWriter fw = new FileWriter("C:/Users/Niklas/TennisStatsData/first.csv");
		String name1 = "";
		String name2 = "";
		String date = "";

		File folder = new File(filename);
		for (File file : folder.listFiles()) {
			// filecounter++;
			FileReader csvFileToRead = new FileReader(file);
			BufferedReader br = new BufferedReader(csvFileToRead);
			while ((line = br.readLine()) != null) {
				// Thread.sleep((long) 1);
				String[] names = line.split("-");
				if (!names[4].equals(name1) || !names[5].split(",")[0].equals(name2) || !names[0].equals(date)) {
					// matchcounter++;
					name1 = names[4];
					name2 = names[5];
					name2 = name2.split(",")[0];
					date = names[0];
					// System.out.println("name2 = " + name2 + " , names[5] = "
					// + names[5] );
					fw.close();
					fw = new FileWriter(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + name1 + "-"
							+ name2 + "-" + date + ".csv"));
				}

				// previousline = line;
				fw.write(line + "\n");
				fw.write("\n");
			}
			br.close();
		}
		fw.close();
	}

	public static void copyFileContent(String filename) throws IOException, InterruptedException {

		String line = "";
		String previousline = "  ";
		FileWriter fw = new FileWriter("C:/Users/Niklas/TennisStatsData/first.csv");
		String name1 = "";
		String name2 = "";

		FileReader csvFileToRead = new FileReader(filename);
		BufferedReader br = new BufferedReader(csvFileToRead);
		while ((line = br.readLine()) != null) {
			String[] names = line.split("-");
			/*
			 * if(!names[4].equals(name1) ||
			 * !names[5].split(",")[0].equals(name2)){ name1 = names[4]; name2 =
			 * names[5]; name2 = name2.split(",")[0];
			 * //System.out.println("name2  = " + name2 + "  , names[5] = " +
			 * names[5] ); fw = new FileWriter(new
			 * File("C:/Users/Niklas/TennisStatsData/SackmannRawData_Nochmal/"+
			 * name1+"_"+name2+".csv")); System.out.println(previousline); }
			 */
			previousline = line;
			fw.write(line + "\n");
			fw.write("\n");
		}
		br.close();
		fw.close();
	}

	/*
	 * public static void analyzeAll_vorher(String pathname) throws IOException{
	 * 
	 * File folder = new File(pathname); ArrayList<String> playernames = new
	 * ArrayList<String>(); ArrayList<Double> playersV1s = new
	 * ArrayList<Double>(); ArrayList<Double> playersV2s = new
	 * ArrayList<Double>(); ArrayList<Integer> playersNs = new
	 * ArrayList<Integer>(); ArrayList<Double> playersGs = new
	 * ArrayList<Double>(); int counter = 0; for (File file :
	 * folder.listFiles()){ System.out.println("counter = " + counter);
	 * System.out.println(file.getName()); String filepath = file.toString();
	 * //String[] filepath_array = filepath.split("/"); //String filename =
	 * filepath_array[filepath_array.length-1]; String[] names =
	 * file.getName().replace(".csv", "").split("_"); String player1name =
	 * names[0] + " " + names[1]; String player2name = names[names.length-2] +
	 * " " + names[names.length-1];
	 * 
	 * MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath); Match m
	 * = new Match(im); double[] V1s = Metrics.V1(m,true); double[] V2s =
	 * Metrics.V2(m, 0.5, 0.5, true); //System.out.println("v2_p1 = " + V2s[0] +
	 * "  ,  v2_p2 = " + V2s[1]);
	 * 
	 * double newP1V2value = 0.5; double newP2V2value = 0.5;
	 * 
	 * if( playernames.contains(player1name) &&
	 * playernames.contains(player2name)){ int p1index =
	 * playernames.indexOf(player1name); int p2index =
	 * playernames.indexOf(player2name); V2s =
	 * Metrics.V2(m,playersV2s.get(p1index),playersV2s.get(p2index), true);
	 * //System.out.println("IF BOTH : v2_p1 = " + V2s[0] + "  ,  v2_p2 = " +
	 * V2s[1] + "  g1 = " + V2s[2] + "   g2 = " + V2s[3] +
	 * "  playersV2s.indexOf(p1index)*playersGs.get(p1index) = " +
	 * playersV2s.indexOf(p1index)*playersGs.get(p1index) +
	 * "  playersV2s.indexOf(p2index)*playersGs.get(p2index) = " +
	 * playersV2s.indexOf(p2index)*playersGs.get(p2index)); newP1V2value =
	 * (V2s[0]*V2s[2] + playersV2s.indexOf(p1index)*playersGs.get(p1index)) /
	 * (V2s[2] + playersGs.get(p1index)); newP2V2value = (V2s[1]*V2s[3] +
	 * playersV2s.indexOf(p2index)*playersGs.get(p2index)) / (V2s[3] +
	 * playersGs.get(p2index)); //System.out.println("newP1V2value = " +
	 * newP1V2value + "  ,  newP2V2value = " + newP1V2value);
	 * playersGs.set(p1index, playersGs.indexOf(p1index) + V2s[2]);
	 * playersGs.set(p2index, playersGs.indexOf(p2index) + V2s[3]);
	 * playersV2s.set(p1index, newP1V2value); playersV2s.set(p2index,
	 * newP2V2value); }
	 * 
	 * if(playernames.contains(player1name)){ int p1index =
	 * playernames.indexOf(player1name); double newP1value =
	 * (playersV1s.get(p1index)*playersNs.get(p1index) + V1s[0]*V1s[2]) /
	 * (playersNs.get(p1index)+ V1s[2]); playersV1s.set(p1index , newP1value);
	 * playersNs.set(p1index, (int) (playersNs.get(p1index)+V1s[2]));
	 * newP1V2value = (V2s[0]*V2s[2] +
	 * playersV2s.indexOf(p1index)*playersGs.get(p1index)) / (V2s[2] +
	 * playersGs.get(p1index)); playersV2s.set(p1index, newP1V2value);
	 * playersGs.set(p1index, playersGs.indexOf(p1index) + V2s[2]); } else{
	 * playernames.add(player1name); playersV1s.add(V1s[0]);
	 * playersNs.add((int)V1s[2]); playersV2s.add(V2s[0]);
	 * playersGs.add(V2s[2]); } if(playernames.contains(player2name)){ int
	 * p2index = playernames.indexOf(player2name); double newP2value =
	 * (playersV1s.get(p2index)*playersNs.get(p2index) + V1s[1]*V1s[2])/
	 * (playersNs.get(p2index)+ V1s[2]); playersV1s.set(p2index , newP2value);
	 * playersNs.set(p2index, (int) (playersNs.get(p2index)+V1s[2]));
	 * newP2V2value = (V2s[1]*V2s[3] +
	 * playersV2s.indexOf(p2index)*playersGs.get(p2index)) / (V2s[3] +
	 * playersGs.get(p2index)); playersV2s.set(p2index, newP2V2value);
	 * playersGs.set(p2index, playersGs.indexOf(p2index) + V2s[3]); } else{
	 * playernames.add(player2name); playersV1s.add(V1s[1]);
	 * playersNs.add((int)V1s[2]); playersV2s.add(V2s[1]);
	 * playersGs.add(V2s[3]); }
	 * 
	 * 
	 * if(newP1V2value <0 || newP2V2value < 0 ){ //
	 * System.out.println(newP1V2value + "   ,   " + newP2V2value);
	 * //System.out.println("g1  = " +V2s[2] + "  g2 = " + V2s[3]); } counter++;
	 * } //System.out.println(playernames.get(3));
	 * //System.out.println(playersNs.get(3)); printAna(playernames, players);
	 * 
	 * }
	 */

	public static void analyzeAll(String pathname) throws IOException {

		File folder = new File(pathname);
		ArrayList<String> playernames = new ArrayList<String>();
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Double> playersV1s = new ArrayList<Double>();
		ArrayList<Double> playersV2s = new ArrayList<Double>();
		ArrayList<Integer> playersNs = new ArrayList<Integer>();
		ArrayList<Double> playersGs = new ArrayList<Double>();
		int counter = 0;
		for (File file : folder.listFiles()) {
			System.out.println(file.getName());
			String filepath = file.toString();
			// String[] filepath_array = filepath.split("/");
			// String filename = filepath_array[filepath_array.length-1];
			String[] names = file.getName().replace(".csv", "").split("_");
			String player1name = names[0] + " " + names[1];
			String player2name = names[names.length - 2] + " " + names[names.length - 1];

			MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
			Match m = new Match(im);

			int p1index = -1;
			int p2index = -1;
			if (playernames.contains(player1name)) {
				p1index = playernames.indexOf(player1name);
				m = new Match(im, players.get(p1index), m.getPlayers()[1]);
			}
			if (playernames.contains(player2name)) {
				p2index = playernames.indexOf(player2name);
				m = new Match(im, m.getPlayers()[0], players.get(p2index));
			}
			double[] V1s = Metrics.V1(m, true);
			double[] V2s = Metrics.V2(m, true);
			// System.out.println(V2s[0] + " , " + V2s[1] + " , " + V2s[2] + " ,
			// " + V2s[3]);

			// V1 & V2
			// P1
			if (playernames.contains(player1name)) {
				double newG1 = players.get(p1index).getG() + V2s[2];
				double new_p1_v2 = (players.get(p1index).getV2() * players.get(p1index).getG() + V2s[0] * V2s[2])
						/ newG1;
				players.get(p1index).setG(newG1);
				players.get(p1index).setV2(new_p1_v2);

				double newP1value = (players.get(p1index).getV1() * players.get(p1index).getN() + V1s[0] * V1s[2])
						/ (players.get(p1index).getN() + V1s[2]);
				players.get(p1index).setV1(newP1value);
				players.get(p1index).setN((int) (players.get(p1index).getN() + V1s[2]));
			} else {
				players.add(new Player(player1name, V1s[0], (int) V1s[2], V2s[0], V2s[2]));
				playernames.add(player1name);

			}
			// P2
			if (playernames.contains(player2name)) {
				double newG2 = players.get(p2index).getG() + V2s[3];
				double new_p2_v2 = (players.get(p2index).getV2() * players.get(p2index).getG() + V2s[1] * V2s[3])
						/ newG2;
				players.get(p2index).setG(newG2);
				players.get(p2index).setV2(new_p2_v2);

				double newP2value = (players.get(p2index).getV1() * players.get(p2index).getN() + V1s[1] * V1s[2])
						/ (players.get(p2index).getN() + V1s[2]);
				players.get(p2index).setV1(newP2value);
				players.get(p2index).setN((int) (players.get(p2index).getN() + V1s[2]));
			} else {
				players.add(new Player(player2name, V1s[1], (int) V1s[2], V2s[1], V2s[3]));
				playernames.add(player2name);
			}
			// -----

		}
		printAna(playernames, players);

	}

	
	// Erstellt aus den lokal verfuegbaren Daten eine Matrix. In dieser bedeutet der i,j-te Eintrag die Anzahl der Siege von
	// Spieler i gegen Spieler j. Anschlieﬂened werden die Verhaeltnisse von Siegen der beiden Spieler in einer analogen Matrix gespeichert
	public static double[][] printWinnersMatrix(String pathname, boolean fileOutput) throws IOException {
		File folder = new File(pathname);
		ArrayList<String> playernames = new ArrayList<String>();

		for (File file : folder.listFiles()) {

			String[] names = file.getName().replace(".csv", "").split("-");
			String player1name = names[0];
			String player2name = names[1];
			if (!playernames.contains(player1name))
				playernames.add(player1name);
			if (!playernames.contains(player2name))
				playernames.add(player2name);
		}
		//playernames = new ArrayList<String>(Arrays.asList("Rafael_Nadal","Dustin_Brown","Andrey_Kuznetsov"));
		playernames = new ArrayList<String>(Arrays.asList("Rafael_Nadal","Roger_Federer","Novak_Djokovic"));

		int n = playernames.size();
		double[][] M = new double[n][n];

		for (File file : folder.listFiles()) {
			
			String filepath = file.toString();
			String[] names = file.getName().replace(".csv", "").split("-");
			String player1name = names[0];
			String player2name = names[1];
			if(playernames.contains(player1name) && playernames.contains(player2name)){
				MatchInfo im = LoadMatchFromTennisAbstract.readCSVFile(filepath);
				Match m = new Match(im);

				if (m.getPoints().get(m.getPoints().size() - 1).getWinner().equals(m.getPlayers()[0])) {
					M[playernames.indexOf(player1name)][playernames.indexOf(player2name)]++;
				}
				
				if (m.getPoints().get(m.getPoints().size() - 1).getWinner().equals(m.getPlayers()[1])) {
					M[playernames.indexOf(player2name)][playernames.indexOf(player1name)]++;
				}
			}
		}
		double[][] resultRatioMatrix = new double[M.length][M[0].length];
		for(int i = 0; i < M.length; i++){
			for(int j = i; j < M[0].length; j++){
				if(M[i][j] + M[j][i] == 0){
					resultRatioMatrix[i][j] = 0;
					resultRatioMatrix[j][i] = 0;
				}
				else{
					resultRatioMatrix[i][j] = M[i][j] / (M[j][i] + M[i][j]);
					resultRatioMatrix[j][i] = M[j][i] / (M[j][i] + M[i][j]);
				}
			}
		}
		if(fileOutput){
			OutputTools.writeMatrixToFile(resultRatioMatrix, "Matrix");
		}
		return resultRatioMatrix;
	}

	public static void printAna(ArrayList<String> playernames, ArrayList<Player> players) throws IOException {

		File f = new File("C:/Users/Niklas/TennisStatsData/allPlayers.csv");
		FileWriter fw = new FileWriter(f);
		fw.write(OutputFinals.name + "  " + OutputFinals.v1name + "  " + OutputFinals.v2name);
		fw.write("\n");
		for (int i = 0; i < playernames.size(); i++) {
			fw.write(playernames.get(i) + "  " + players.get(i).getV1() + "  " + players.get(i).getV2());
			fw.write("\n");
		}
		fw.close();
	}

}
