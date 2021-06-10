package LagrangeInterpolation;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        File file = null;
        try {
            file = new File("input.txt", "output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        double a = file.readDouble(); //left edge
        double b = file.readDouble(); //right edge
        int n = file.readInt(); //number of points

        double[] x = new double[n];
        double[] y = new double[n];

        x[0] = a;
        y[0] = function(x[0]);
        x[n - 1] = b;
        y[n - 1] = function(x[n - 1]);

        double delt = (b - a) / (n - 1);
        for (int i = 1; i < n - 1; i++) {
            x[i] = x[i - 1] + delt;
            y[i] = function(x[i]);
        }

        System.out.println("X: " + Arrays.toString(x));
        System.out.println("Y: " + Arrays.toString(y));
        System.out.println();

        LagrangePolynom intepolation = new LagrangePolynom(n, x, y);
        System.out.println("LagrangePolynom:\n      " + intepolation);

//        new Graph().graph(a, b, x, y, intepolation);

        System.out.println(out(x, y, intepolation));

        file.close();
    }

    public static double function(double x) {
        return Math.sin(x);
    }

    public static String out(double[] x, double[] y, Polynom polynom) {
        StringBuilder string = new StringBuilder();
        string.append(String.format("%15s %15s %15s %15s\n", "x", "y", "f(x)",
                "Lagrange(x)"));
        for (int i = 0; i < x.length * 2 - 1; i++) {
            if (i % 2 == 0) {
                string.append(String.format("%15.6E %15.6E %15.6E %15.6E\n", x[i / 2], y[i / 2],
                        function(x[i / 2]), polynom.calculate(x[i / 2])));
            } else {
                string.append(String.format("%15.6E %s %15.6E %15.6E\n", ((x[i / 2 + 1] + x[i / 2]) / 2), "   ------------",
                        function((x[i / 2 + 1] + x[i / 2]) / 2), polynom.calculate((x[i / 2 + 1] + x[i / 2]) / 2)));
            }
        }
        return string.toString();
    }
}