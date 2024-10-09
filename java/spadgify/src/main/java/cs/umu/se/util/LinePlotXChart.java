package cs.umu.se.util;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.List;

/**
 * The LinePlotXChart class is responsible for creating and managing a line plot chart using the XChart library.
 * It allows users to create a new plot, add series of data to the plot, and display the plot.
 */
public class LinePlotXChart {

    private XYChart chart;

    public LinePlotXChart() {
    }

    public void createPlot(String title, String labelX, String labelY) {
        chart = new XYChartBuilder()
                .height(800)
                .title(title)
                .xAxisTitle(labelX)
                .yAxisTitle(labelY)
                .build();

    }

    /**
     * Adds a series of data points to the existing plot.
     *
     * @param seriesName the name of the data series to be added.
     * @param values the list of Y-values for the data series.
     */
    public void addToPlot(String seriesName, List<Long> values) {
        chart.addSeries(seriesName, values);
    }

    public void showPlot() {
        new SwingWrapper<>(chart).displayChart();
    }
}
