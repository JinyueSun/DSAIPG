package com.phasmidsoftware.dsaipg.adt.pq;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class HeapBenchmarkPlot extends JFrame {

    public HeapBenchmarkPlot(String title, XYSeriesCollection dataset, String yLabel) {
        super(title);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Input Size (log scale)",
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Set log scale
        XYPlot plot = chart.getXYPlot();
        plot.setDomainAxis(new LogAxis("Input Size"));
        plot.setRangeAxis(new LogAxis(yLabel));

        // Set renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        // Display chart
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private static XYSeriesCollection createDataset(Map<String, Double> benchmarkData) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Map.Entry<String, Double> entry : benchmarkData.entrySet()) {
            XYSeries series = new XYSeries(entry.getKey());
            series.add(1000, entry.getValue() * (1000.0 / 16000));  // Scale data
            series.add(5000, entry.getValue() * (5000.0 / 16000));
            series.add(10000, entry.getValue() * (10000.0 / 16000));
            series.add(50000, entry.getValue() * (50000.0 / 16000));
            series.add(100000, entry.getValue() * (100000.0 / 16000));
            dataset.addSeries(series);
        }
        return dataset;
    }

    public static void generatePlot(Map<String, Double> insertionData, Map<String, Double> deletionData) {
        SwingUtilities.invokeLater(() -> {
            HeapBenchmarkPlot insertPlot = new HeapBenchmarkPlot(
                    "Heap Benchmark: Insertion Time (Log/Log)",
                    createDataset(insertionData),
                    "Insertion Time (ms)"
            );
            insertPlot.setSize(800, 600);
            insertPlot.setLocationRelativeTo(null);
            insertPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            insertPlot.setVisible(true);

            HeapBenchmarkPlot deletePlot = new HeapBenchmarkPlot(
                    "Heap Benchmark: Deletion Time (Log/Log)",
                    createDataset(deletionData),
                    "Deletion Time (ms)"
            );
            deletePlot.setSize(800, 600);
            deletePlot.setLocationRelativeTo(null);
            deletePlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            deletePlot.setVisible(true);
        });
    }
}
