package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.ui.RefineryUtilities;

public class PlotTools {

	public static void plot(double[] XData, double[] YData, String title) {
		new LineChartDemo6(title, XData, YData);
	}

	public static void plot(double[] XData, double[] YData) {
		new LineChartDemo6("", XData, YData);
	}

	public static void plot(double[] YData, String title) {
		new LineChartDemo6(title, YData);
	}

	public static void plot(double[] YData) {
		new LineChartDemo6("", YData);
	}

	public static void plotBar(double[] YData, ArrayList<String> strings, String title) {
		new BarChart(title, YData, strings);
	}

	public static void plotBar(double[] YData, ArrayList<String> strings) {
		new BarChart("", YData, strings);
	}

	public static void plotBar(double[] YData, String title) {
		new BarChart(title, YData);
	}

	public static void plotBar(double[] YData) {
		new BarChart("", YData);
	}
	
	public static void showImage(BufferedImage image){
		JFrame frame = new JFrame();
		frame.setSize(image.getWidth(), image.getHeight());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void showImage(MatrixImage image){
		JFrame frame = new JFrame();
		frame.setSize(image.getWidth(), image.getHeight());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		image.addAMouseListener(frame);
	}

	
	
}
