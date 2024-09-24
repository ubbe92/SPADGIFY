package cs.umu.se.util;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.List;

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

    public void addToPlot(String seriesName, List<Long> values) {
        chart.addSeries(seriesName, values);
    }

    public void showPlot() {
        new SwingWrapper<>(chart).displayChart();
    }
}
