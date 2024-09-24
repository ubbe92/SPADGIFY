package cs.umu.se.util;

import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.util.List;

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

    public void addToPlot(String seriesName, List<Long> values) {
        chart.addSeries(seriesName, values);
    }

    public void showPlot() {
        new SwingWrapper<>(chart).displayChart();
    }
}
