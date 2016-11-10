package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import graphics.PlotTools;
import objects.MatchDate;

public class Tools {

	public static void convertMatrixToImage(double[][] M, int height, int width, String filepath, boolean fileOutput){
	    BufferedImage image = new BufferedImage(height,width, BufferedImage.TYPE_INT_RGB);
	    double widthRatio = height / (double)M.length;
	    double heightRatio = width / (double)M[0].length;
		try {
		    //BufferedImage image;
		    for(int i=0; i<M.length; i++) {
		        for(int j=0; j < M[0].length; j++) {
		            int a = (int) (M[i][j]*255);
		            Color newColor = new Color(a,a,a);
		            for(int k = (int) (i*widthRatio); k < (i+1)*widthRatio; k++){
		            	for(int l = (int) (j*heightRatio); l < (j+1)*heightRatio; l++){
		            	image.setRGB(k,l,newColor.getRGB());
		            	}
		            }
		        }
		    }
		    if(fileOutput){
		    	File output = new File("C:/Users/Niklas/TennisStatsData/" + filepath + ".jpg");
		    	ImageIO.write(image, "jpg", output);
		    }
		    PlotTools.showImage(image);
		}
		catch(Exception e) {
			
		}

	}
	

	public static ArrayList<String> randomEntries(ArrayList<String> list, int noEntries){
		// Eine gewaehlte Zahl an Eintraegen einer ArrayList auswaehlen und ausgeben
		Random r = new Random();
		ArrayList<String> newlist = new ArrayList<String>();
		for(int i = 0; i < noEntries; i++){
			int entry = r.nextInt(list.size());
			if(!newlist.contains(list.get(entry))){
				newlist.add(list.get(entry));
			}
			else{
				i-= 1;
			}
		}
		return newlist;
	}
	
	
	public static boolean isInteger(String s) {
		boolean isValidInteger = false;
		try {
			Integer.parseInt(s);
			isValidInteger = true;
		} catch (NumberFormatException ex) {
		}
		return isValidInteger;
	}
	
	public static String nameConversion(String playername) {
		String[] names = playername.split("_");
		if (names.length > 1) {
			playername = names[0] + " " + names[1];
		} else {
			playername = names[0];
		}
		return playername;
	}

	public static MatchDate stringToDate(String s) {
		// s muss das Format yyyymmdd haben.
		String[] nums = s.split("");
		int year = Integer.parseInt(nums[0] + nums[1] + nums[2] + nums[3]);
		int month = Integer.parseInt(nums[4] + nums[5])-1; // Hier muss tatsaechlich "-1" hin. Weiﬂ nicht, warum er den Monat sonst 1 zu hoch waehlt
		int day = Integer.parseInt(nums[6] + nums[7]);
		@SuppressWarnings("deprecation")
		MatchDate date = new MatchDate(year, month, day);
		return date;
	}

	public static ArrayList<String> namesOfMethods(ArrayList<Method> methods){
		ArrayList<String> methodNames = new ArrayList<String>();
		for(Method method : methods){
			methodNames.add(method.getName());
		}
		return methodNames;
	}
	
	
}
