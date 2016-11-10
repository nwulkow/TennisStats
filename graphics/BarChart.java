package graphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
//import org.jfree.ui.Spacer;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from an {@link XYDataset}.
 *
 */
public class BarChart extends ApplicationFrame {

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */

	double maxvalue, minvalue;

	public BarChart(final String title, double[] Ydata, ArrayList<String> strings) {

		super(title);

		final CategoryDataset dataset = makeDataset(Ydata, strings);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setDefaultCloseOperation(0);
		this.setVisible(true);
	}

	public BarChart(final String title, double[] Ydata) {
		super(title);
		//double[] XData = new double[Ydata.length];
		for (int i = 0; i < Ydata.length; i++) {
		//	XData[i] = i;
		}

		final DefaultCategoryDataset dataset = makeDataset(Ydata);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return a sample dataset.
	 */
	private XYDataset createDataset() {

		final XYSeries series1 = new XYSeries("First");
		series1.add(1.0, 1.0);
		series1.add(2.0, 4.0);
		series1.add(3.0, 3.0);
		series1.add(4.0, 5.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 7.0);
		series1.add(7.0, 7.0);
		series1.add(8.0, 8.0);

		final XYSeries series2 = new XYSeries("Second");
		series2.add(1.0, 5.0);
		series2.add(2.0, 7.0);
		series2.add(3.0, 6.0);
		series2.add(4.0, 8.0);
		series2.add(5.0, 4.0);
		series2.add(6.0, 4.0);
		series2.add(7.0, 2.0);
		series2.add(8.0, 1.0);

		final XYSeries series3 = new XYSeries("Third");
		series3.add(3.0, 4.0);
		series3.add(4.0, 3.0);
		series3.add(5.0, 2.0);
		series3.add(6.0, 3.0);
		series3.add(7.0, 6.0);
		series3.add(8.0, 3.0);
		series3.add(9.0, 4.0);
		series3.add(10.0, 3.0);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);

		return dataset;

	}

	private DefaultCategoryDataset makeDataset(double[] Ydata, ArrayList<String> strings) {
		double maxvalue = 0;
		double minvalue = 1;

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String category = "";
		for (int i = 0; i < Ydata.length; i++) {
			if (Ydata[i] > maxvalue) {
				maxvalue = Ydata[i];
			}
			if (Ydata[i] < minvalue) {
				minvalue = Ydata[i];
			}
			dataset.addValue(Ydata[i], category, strings.get(i));
		}
		this.maxvalue = maxvalue;
		this.minvalue = minvalue;

		return dataset;
	}

	private DefaultCategoryDataset makeDataset(double[] Ydata) {
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < Ydata.length; i++) {
			strings.add(i+"");
		}
		return makeDataset(Ydata, strings);
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final CategoryDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart("", // chart title
				"X", // x axis label
				"", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final BarRenderer renderer = new BarRenderer();
		renderer.setDrawBarOutline(false);
		plot.setRenderer(renderer);
		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
		rangeAxis.setRange(minvalue * 5 / 6, maxvalue * 6 / 5);
		rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		rangeAxis.setTickUnit(new NumberTickUnit(((maxvalue - minvalue) * 6 / 5) / 10));

		return chart;

	}

	// ****************************************************************************
	// * JFREECHART DEVELOPER GUIDE *
	// * The JFreeChart Developer Guide, written by David Gilbert, is available
	// *
	// * to purchase from Object Refinery Limited: *
	// * *
	// * http://www.object-refinery.com/jfreechart/guide.html *
	// * *
	// * Sales are used to provide funding for the JFreeChart project - please *
	// * support us so that we can continue developing free software. *
	// ****************************************************************************

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args
	 *            ignored.
	 */
	public static void main(final String[] args) {

		double[] testdoublearray = { 1, 4, 5, 2, 3, 2, 4, 8, 6 };
		ArrayList<String> strings = new ArrayList<String>(Arrays.asList("f", "v", "u", "r", "l", "h", "o", "j", "n"));
		double[] testxarray = { 1, 1.1, 2, 2.5, 3, 5, 6, 7, 8, 9 };
		final BarChart demo = new BarChart("Line Chart Demo 6", testdoublearray);
		// demo.pack();
		// RefineryUtilities.centerFrameOnScreen(demo);
		// demo.setVisible(true);

	}

}