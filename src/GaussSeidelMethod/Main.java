package GaussSeidelMethod;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("input.txt", "output.txt");
        Matrix matrix = new Matrix();
        matrix.init(file);

        matrix.print();

        double[] answ;

        int status = matrix.solve();

        if (status == Matrix.SOLVED) {
            file.write("Матрица решена!\n");
            System.out.println("Матрица решена!\n");
            answ = matrix.getSolution();
            for (int i = 0; i < answ.length; i++)
                System.out.printf("%15.6E", answ[i]);
            file.write(answ);
        } else {
            file.write("Матрицу невозможно решить методом Гаусса – Зейделя!\n");
            System.out.println("Матрицу невозможно решить методом Гаусса – Зейделя!\n");
        }

        file.close();
    }
}
