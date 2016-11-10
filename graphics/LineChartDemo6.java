package graphics;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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
public class LineChartDemo6 extends ApplicationFrame {

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */

	double maxvalue = 1, minvalue = 0;
	XYSeriesCollection dataset = new XYSeriesCollection();

	public LineChartDemo6(final String title, double[] XData, double[] Ydata, String dataname) {

		super(title);

		final XYDataset dataset = addToDataset(XData, Ydata, dataname);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setDefaultCloseOperation(0);
		this.setVisible(true);
	}
	
	public LineChartDemo6(final String title, double[] XData, double[] YData) {
		super(title);
		new LineChartDemo6(title, XData, YData, "");
	}
	
	public LineChartDemo6(final String title, double[] Ydata, String dataname) {
		super(title);
		double[] XData = new double[Ydata.length];
		for (int i = 0; i < Ydata.length; i++) {
			XData[i] = i;
		}
		final XYDataset dataset = addToDataset(XData, Ydata, dataname);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		//new LineChartDemo6(title, Ydata, XData, dataname);
	}

	public LineChartDemo6(final String title, double[] Ydata) {
		super(title);
		double[] XData = new double[Ydata.length];
		for (int i = 0; i < Ydata.length; i++) {
			XData[i] = i;
		}

		new LineChartDemo6(title, XData, Ydata);
	}
	
	public LineChartDemo6(final String title) {
		super(title);


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

	public XYDataset addToDataset(double[] Xdata, double[] Ydata, String title) {
		//double maxvalue = 0;
		//double minvalue = 1;

		XYSeries series = new XYSeries(title);
		for (int i = 0; i < Ydata.length; i++) {
			if (Ydata[i] > maxvalue) {
				maxvalue = Ydata[i];
			}
			if (Ydata[i] < minvalue) {
				minvalue = Ydata[i];
			}
			series.add(Xdata[i], Ydata[i]);
		}
		
		dataset.addSeries(series);
		//this.maxvalue = maxvalue;
		//this.minvalue = minvalue;

		return dataset;
	}
	
	public XYDataset addToDataset(double[] Xdata, double[] Ydata) {
		return addToDataset(Xdata, Ydata, "");
	}

	public XYDataset addToDataset(double[] Ydata, String title) {
		double[] XData = new double[Ydata.length];
		for (int i = 0; i < Ydata.length; i++) {
			XData[i] = i;
		}
		return addToDataset(XData, Ydata, title);
	}
	
	public XYDataset addToDataset(double[] Ydata) {
		return addToDataset(Ydata, "");
	}
	

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("", // chart
																	// title
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
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(1, false);
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
		double[] testxarray = { 1, 1.1, 2, 2.5, 3, 5, 6, 7, 8, 9 };
		final LineChartDemo6 demo = new LineChartDemo6("Line Chart Demo 6", testdoublearray);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

}