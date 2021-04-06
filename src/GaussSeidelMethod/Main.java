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
        switch (matrix.solve()) {
            case Matrix.SOLVED:
                file.write("Матрица решена!\n");
                System.out.println("Матрица решена!\n");
                answ = matrix.getSolution();
                for (int i = 0; i < answ.length; i++)
                    System.out.printf("%15.3f", answ[i]);
                file.write(answ);
                break;
            case Matrix.UNSOLVABLE:
                file.write("Матрицу невозможно решить методом Гаусса – Зейделя!\n");
                System.out.println("Матрицу невозможно решить методом Гаусса – Зейделя!\n");
                break;
        }


        file.close();
    }
}
