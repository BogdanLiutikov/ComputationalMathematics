package GaussMethod;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("input.txt", "output.txt");
        Matrix matrix = new Matrix();

        matrix.init(file);
        matrix.print();


        double[] x;
        // matrix.solve() возвращает статус решения матрицы
        switch (matrix.solve()) {
            case Matrix.SOLVED:
                file.write("Матрица решена!\n");
                System.out.println("Матрица решена!\n");
                x = matrix.getSolution();
                for (int i = 0; i < x.length; i++)
                    System.out.printf("%15.3f", x[i]);
                file.write(x);
                break;
            case Matrix.SINGULAR:
                file.write("Матрица оказалась вырожденной!\n");
                System.out.println("Матрица оказалась вырожденной!\n");
                break;
            case Matrix.INCONSISTENT:
                file.write("Матрица не имеет решений!\n");
                System.out.println("Матрица не имеет решений!\n");
                break;
            case Matrix.INFINITELY:
                file.write("Матрица имеет бесконечное количество решений!\n");
                System.out.println("Матрица имеет бесконечное количество решений!\n");
                break;
            case Matrix.ILLConditioned:
                file.write("Матрица плохообусловлена. Решение может содержать большую погрешность!\n");
                System.out.println("Матрица плохообусловлена. Решение может содержать большую погрешность!\n");
                x = matrix.getSolution();
                for (int i = 0; i < x.length; i++)
                    System.out.printf("%15.3f", x[i]);
                file.write(x);
                break;
        }

        file.close();
    }
}
