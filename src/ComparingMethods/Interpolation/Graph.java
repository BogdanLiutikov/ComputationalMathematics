package ComparingMethods.Interpolation;

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

    public void graph(double a, double b, double[] x, double[] y, Polynom lagrange, Polynom newtone) {

        XYSeries function = new XYSeries("Function");
        XYSeries Lagrangeinterpolation = new XYSeries("LagrangeInterpolation");
        XYSeries Newtoneinterpolation = new XYSeries("NewtoneInterpolation");
        XYSeries dots = new XYSeries("Dots");

        for (double i = a; i < b; i += 0.01) {
            function.add(i, Main.function(i));
        }

        XYDataset xyDataset = new XYSeriesCollection(function);

        for (double i = a; i < b; i += 0.01) {
            Lagrangeinterpolation.add(i, lagrange.calculate(i));
        }

        for (double i = a; i < b; i += 0.01) {
            Newtoneinterpolation.add(i, newtone.calculate(i));
        }

        for (int i = 0; i < x.length; i++) {
            dots.add(x[i], y[i]);
        }

        ((XYSeriesCollection) xyDataset).addSeries(Lagrangeinterpolation);
        ((XYSeriesCollection) xyDataset).addSeries(Newtoneinterpolation);
        ((XYSeriesCollection) xyDataset).addSeries(dots);

        //createScatterPlot
        JFreeChart chart = ChartFactory.createXYLineChart("", "x", "y", xyDataset);


        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, ChartColor.GREEN);
        renderer.setSeriesPaint(1, ChartColor.RED);
        renderer.setSeriesPaint(2, ChartColor.BLUE);
        renderer.setSeriesPaint(3, ChartColor.BLACK);

        //Dots
        renderer.setSeriesShape(3, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesShapesVisible(3, true);
        renderer.setSeriesShapesFilled(3, true);
        renderer.setSeriesLinesVisible(3, false);

        chart.getXYPlot().getRangeAxis().setLabelAngle(Math.PI / 180 * 90); // yAxis


        JFrame frame = new JFrame("Graph");

        // Помещаем график на фрейм
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
