package LagrangeInterpolation;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("input.txt", "output.txt");
        Scanner scanner = new Scanner(System.in);

//        Polynom p1 = new Polynom();
//        p1.add(0,-2);
//        p1.add(1,1);
//        p1.add(2, 2);
//        Polynom p2 = new Polynom();
//        p2.add(3,3);
//        p2.add(4,4);
//        p2.add(5,5);
//        p2.add(6,6);
//        p1.print();
//        System.out.println();
//        p2.print();
//        Polynom p3 = Polynom.sum(p1, p2);
//        System.out.println();
//        p3.print();
//        System.out.println();
//        Polynom p4 = Polynom.multiply(p1,p2);
//        p4.print();
//        Polynom p5 = new Polynom("12.0x^8 + 16.0x^7 - 7.0x^6 - 10.0x^5 - 8.0x^4 - 6.0x^3");


        ///////Testing


        double a = file.readDouble();
        double b = file.readDouble();
        int n = file.readInt();

        double[] x = new double[n + 2];
        double[] y = new double[n + 2];

        x[0] = a;
        y[0] = function(x[0]);
        x[n + 1] = b;
        y[n + 1] = function(x[n + 1]);

        for (int i = 1; i <= n; i++) {
            x[i] = file.readDouble();
            y[i] = function(x[i]);
        }
        System.out.println(Arrays.toString(x));
        System.out.println(Arrays.toString(y));
        System.out.println();

//        Polynom p1 = new Polynom("-2.0x^2 + 1x^1 - 2.0x^0");
//        Polynom p2 = new Polynom("6.0x^6 + 5.0x^5 + 4.0x^4 + 3.0x^3");
//
//        p1.print();
//        p2.print();
//        System.out.println();
//        Polynom.sum(p1, p2).print();
//        System.out.println();
//        Polynom.multiply(p1, p2).print();
//        p1.multiply(5);
//        p1.print();

        LagrangePolynom intepolation = new LagrangePolynom(n + 2, x, y);
        intepolation.print();

        file.write(out(x, y, intepolation));

        new Graph().graph(a, b, x, y, intepolation);

        file.close();
    }

    public static double function(double x) {
        return Math.sin(x);
    }

    public static String out(double[] x, double[] y, LagrangePolynom polynom) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < x.length * 2 - 1; i++) {
            if (i % 2 == 0) {
                System.out.printf("%15.6f %15.6f %15.6f %15.6f\n", x[i / 2], y[i / 2], function(x[i / 2]), polynom.calculate(x[i / 2]));
                string.append(String.format("%15.6E %15.6E %15.6E %15.6E\n", x[i / 2], y[i / 2], function(x[i / 2]), polynom.calculate(x[i / 2])));
            } else {
                System.out.printf("%15.6f %s %15.6f %15.6f\n", ((x[i / 2 + 1] + x[i / 2]) / 2), "   ------------", function((x[i / 2 + 1] + x[i / 2]) / 2), polynom.calculate((x[i / 2 + 1] + x[i / 2]) / 2));
                string.append(String.format("%15.6E %s %15.6E %15.6E\n", ((x[i / 2 + 1] + x[i / 2]) / 2), "   ------------", function((x[i / 2 + 1] + x[i / 2]) / 2), polynom.calculate((x[i / 2 + 1] + x[i / 2]) / 2)));
            }
        }
        return string.toString();
    }
}