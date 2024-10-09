package cs.umu.se.util;

import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.util.List;

/**
 * This class is responsible for creating and displaying a box plot chart using the XChart library.
 * It provides methods to create a box plot with specific labels and title,
 * add data series to the plot, and display the plot.
 */
public class BoxPlotXChart {

    private BoxChart chart;

    public BoxPlotXChart() {
    }

    public void createPlot(String title, String labelX, String labelY) {
        chart = new BoxChartBuilder()
            .width(800)
            .height(800)
            .title(title)
            .xAxisTitle(labelX)
            .yAxisTitle(labelY)
            .build();
    }

    /**
     * Adds a new data series to the plot.
     *
     * @param seriesName the name of the data series to be added to the plot
     * @param values the list of values to be included in the data series
     */
    public void addToPlot(String seriesName, List<Long> values) {
        chart.addSeries(seriesName, values);
    }

    public void showPlot() {
        new SwingWrapper<>(chart).displayChart();
    }
}
