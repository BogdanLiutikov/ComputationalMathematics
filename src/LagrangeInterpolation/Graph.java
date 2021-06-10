package LagrangeInterpolation;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.geom.Ellipse2D;


public class Graph {

    public void graph(double a, double b, double[] x, double[] y, Polynom polynom) {

        XYSeries function = new XYSeries("Function");
        XYSeries interpolation = new XYSeries("LagrangeInterpolation");
        XYSeries dots = new XYSeries("Dots");

        for (double i = a; i < b; i += 0.01) {
            function.add(i, Main.function(i));
        }

        XYDataset xyDataset = new XYSeriesCollection(function);

        for (double i = a; i < b; i += 0.01) {
            interpolation.add(i, polynom.calculate(i));
        }

        for (int i = 0; i < x.length; i++) {
            dots.add(x[i], y[i]);
        }

        ((XYSeriesCollection) xyDataset).addSeries(interpolation);
        ((XYSeriesCollection) xyDataset).addSeries(dots);

        //createScatterPlot
        JFreeChart chart = ChartFactory.createXYLineChart("", "x", "y", xyDataset);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, ChartColor.BLUE);
        renderer.setSeriesPaint(1, ChartColor.RED);
        renderer.setSeriesPaint(2, ChartColor.BLACK);
        //Dots
        renderer.setSeriesShape(2, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShapesFilled(2, true);
        renderer.setSeriesLinesVisible(2, false);

        chart.getXYPlot().getRangeAxis().setLabelAngle(Math.PI / 180 * 90); // yAxis


        JFrame frame = new JFrame("Graph");

        // Помещаем график на фрейм
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
