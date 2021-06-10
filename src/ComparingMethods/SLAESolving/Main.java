package ComparingMethods.SLAESolving;

import GaussSeidelMethod.Matrix;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        File file = null;
        try {
            file = new File("input.txt", "output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gauss gaussmatrix = new Gauss();
        GaussSeidel gaussSeidelmatrix = new GaussSeidel();

        double eps = file.readEps();
        double[][] matrix = file.readMatrix();
        gaussmatrix.init(eps, matrix);
        gaussSeidelmatrix.init(eps, matrix);

        System.out.println("Matrix");
        gaussmatrix.print();


        System.out.println("GaussMethod");
        double[] gaussX;
        int status = gaussmatrix.solve();

        if (status == Gauss.SOLVED) {
            gaussX = gaussmatrix.getSolution();
            for (int i = 0; i < gaussX.length; i++) {
                System.out.printf("%15.6E", gaussX[i]);
            }
        } else {
            String message = gaussmatrix.getStatus();
            System.out.println(message);
        }

        System.out.println("\n   ------------------------------------------------------------------------\n");
        System.out.println("GaussSeidelMethod");
        double[] gaussSeidelX;
        status = gaussSeidelmatrix.solve();

        if (status == GaussSeidel.SOLVED) {
            System.out.println("Матрица решена!\n");
            gaussSeidelX = gaussSeidelmatrix.getSolution();
            for (int i = 0; i < gaussSeidelX.length; i++)
                System.out.printf("%15.6E", gaussSeidelX[i]);
        } else {
            System.out.println("Матрицу невозможно решить методом Гаусса – Зейделя!\n");
        }

        file.close();
    }
}
