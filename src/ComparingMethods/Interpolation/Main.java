package ComparingMethods.Interpolation;

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

        double a = file.readDouble();
        double b = file.readDouble();
        int n = file.readInt();

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

        Polynom lagrangeintepolation = new LagrangePolynom(n, x, y);
        Polynom newtoneintepolation = new NewtonePolynom(n, x, y);

        System.out.println("LagrangePolynom");
        System.out.println("    " + lagrangeintepolation);
        System.out.println("NewtonePolynom");
        System.out.println("    " + newtoneintepolation);
        System.out.println();

        System.out.println(out(x, y, lagrangeintepolation, newtoneintepolation));

//        new Graph().graph(a, b, x, y, lagrangeintepolation, newtoneintepolation);

        file.close();
    }

    public static double function(double x) {
        return Math.sin(x);
    }

    public static String out(double[] x, double[] y, Polynom LagrangePolynom, Polynom NewtonePolynom) {
        StringBuilder string = new StringBuilder();
        string.append(String.format("%15s %15s %15s %15s      |       %s\n", "x", "y", "f(x)",
                "Lagrange(x)", "Newtone(x)"));
        for (int i = 0; i < x.length * 2 - 1; i++) {
            if (i % 2 == 0) {
                string.append(String.format("%15.6E %15.6E %15.6E %15.6E      |       %.6E\n", x[i / 2], y[i / 2], function(x[i / 2]),
                        LagrangePolynom.calculate(x[i / 2]), NewtonePolynom.calculate(x[i / 2])));
            } else {
                string.append(String.format("%15.6E %15s %15.6E %15.6E      |       %.6E\n", ((x[i / 2 + 1] + x[i / 2]) / 2), "------------",
                        function((x[i / 2 + 1] + x[i / 2]) / 2), LagrangePolynom.calculate((x[i / 2 + 1] + x[i / 2]) / 2),
                        NewtonePolynom.calculate((x[i / 2 + 1] + x[i / 2]) / 2)));
            }
        }
        return string.toString();
    }
}
