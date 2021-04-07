package GaussMethod;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("input.txt", "output.txt");
        Matrix matrix = new Matrix();

        matrix.init(file);
        matrix.print();


        double[] x;
        int status = matrix.solve();

        if (status == Matrix.SOLVED) {
            x = matrix.getSolution();
            file.write(matrix.getSolution());
            for (int i = 0; i < x.length; i++) {
                System.out.printf("%15.6E", x[i]);
            }
        } else {
            String message = matrix.getStatus();
            file.write(message);
            System.out.println(message);
        }


        file.close();
    }
}
