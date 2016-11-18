package main;

import java.io.IOException;

import Jama.Matrix;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import tools.OutputTools;

public class TestClass {

	
	public static void main(String[] args) throws IOException, MatlabConnectionException, MatlabInvocationException{
		
		
	    MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
	    	    .setUsePreviouslyControlledSession(true)
	    	    .setHidden(true)
	    	    .setMatlabLocation(null).build(); 
	    MatlabProxyFactory factory = new MatlabProxyFactory(options);
	    MatlabProxy proxy = factory.getProxy();
	    
	    MatlabTypeConverter processor = new MatlabTypeConverter(proxy);

	    //proxy.eval("M = [2,1;4,3]");
	    //double[][] M_array = { {2,1}, {3,4} };
	    //MatlabNumericArray M = new MatlabNumericArray(M_array, new double[2][2]);//processor.getNumericArray("M");
	    double[][] M_array = { {2d,1d,3.4}, {3d,4d,1.2}, {2.3 , 4, 5.1} };
	    double[][] X = { {1,2,10}, {2,5,15}, {3,10,19}, {7,22,28}};
	    
	    //MatlabNumericArray M = processor.getNumericArray("M");
	    //System.out.println(M.getDimensions());
	    processor.setNumericArray("M_array_M", new MatlabNumericArray(M_array, null));
	    processor.setNumericArray("X_M", new MatlabNumericArray(X, null));
	    double[] res = ((double[])(proxy.returningFeval("reconstructTransitionProcess",1,M_array,X))[0]);
	    System.out.println(res[0]);
	    //proxy.eval("path");
	    //proxy.eval("M = M*2");
	   // proxy.eval("M = convertJava2DToMatlab(M_array)");
	   // proxy.eval("t = testJavaConnectionMethod(M)");
	  //  proxy.eval("M = convertJava2DToMatlab(M)");
	   // proxy.eval("t = testJavaConnectionMethod(M)");
	   // double t = ((double[]) proxy.getVariable("t"))[0];
	    //System.out.println(t);
	    //proxy.feval("convertJava2DToMatlab", new MatlabNumericArray(M_array, null));
	    //proxy.feval("testJavaConnectionMethod",new MatlabNumericArray(M_array, null));
	    //proxy.feval("sign", 2);
	    //Object[] o = proxy.returningFeval("@sin", 2);
	    //double[][] Mconv = (double[][])(proxy.returningFeval("cellArrayToMatrix",1, M_array));
	    //proxy.eval("size(M_array)");
	   // double[] inmem =  (double[])(proxy.returningEval("testJavaConnectionMethod(M_array)", 1)[0]);
	   // double[] length = (double[])(proxy.returningFeval("size",1,M_array)[0]);
	   // System.out.println(length[0]);

	   // MatlabNumericArray array = processor.getNumericArray("M_array_M");
	    //double t = ((double[]) proxy.getVariable("t"))[0];
	    //double[][] javaArray = array.getRealArray2D();
	    //OutputTools.printArray(inmem);


	    //Disconnect the proxy from MATLAB
	    proxy.disconnect();
		
	    /*
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
		*/
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
