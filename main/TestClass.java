package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.flink.graph.Vertex;
import org.glassfish.grizzly.http.server.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tools.OutputTools;


public class TestClass {

	
	public static void main(String[] args) throws IOException{
		
		FileReader fr = new FileReader(new File("D:/Work/Studium/SHK_2015/PRIBOR3M.txt"));
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(new File("D:/Work/Studium/SHK_2015/PRIBOR3M_data.txt"));
		BufferedWriter bw = new BufferedWriter(fw);
		String line = br.readLine();
		while((line = br.readLine()) != null){
			String[] row = line.split("\\|");
			fw.write(row[1] + ",");
			fw.write("\r\n");
			System.out.println(row[1]);
		}
		fr.close();
		br.close();
		fw.close();
		//bw.close();
		
		//OutputTools.deleteFilesInFolder("C:/Users/Niklas/testfolder1");
		
		/*URL url = new URL("https://mybigpoint.tennis.de/web/niklas.wulkow/~/10555/lk-portrait");
		url = new URL("https://twitter.com/");
		URLConnection conn = url.openConnection();

		// open the stream and put it into BufferedReader
		BufferedReader br = new BufferedReader(
                           new InputStreamReader(conn.getInputStream()));

		String text = "";
        String inputLine;
        while ((inputLine = br.readLine()) != null){
        	text = text + inputLine + "\n";
        	if(inputLine.contains("Niklas")){
        		System.out.println("JETZT");
        	}
            //System.out.println(inputLine);
        }
        br.close();
        System.out.println(text);
         
         */
		
	/*	Document doc = Jsoup.connect("http://tvbb.liga.nu/cgi-bin/WebObjects/nuLigaTENDE.woa/wa/meetingReport?meeting=7506917&federation=TVBB&championship=TVBB+Sommer+2016").get();
		
		Elements links = doc.select("a[href]"); // a with href
		//System.out.println(links);
		Elements pngs = doc.select("img[src$=.png]");
		  // img with src ending .png
		//System.out.println(pngs);
		Element masthead = doc.select("div.masthead").first();
		  // div with class=masthead
		//System.out.println(masthead);
		Elements resultLinks = doc.select("h3.r > a"); // direct a after h3
		//System.out.println(resultLinks);
		
		Elements numbers = doc.select("p > h1");
		System.out.println(numbers);*/
		
		
	}
	
}
