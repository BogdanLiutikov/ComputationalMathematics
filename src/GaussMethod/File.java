package GaussMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class File {

    //    private java.io.File in;
//    private java.io.File out;
    private Scanner scanner;
    private PrintWriter printWriter;

    public File(String input, String output) throws IOException {
        java.io.File in = new java.io.File(input);
//        out = new java.io.File(output);
        scanner = new Scanner(in);
        printWriter = new PrintWriter(output);
    }

    public double readEps() {
        return Double.parseDouble(scanner.nextLine());
    }

    public double[][] read() {
        String[] str = scanner.nextLine().split(" ");
        int n = Integer.parseInt(str[0]);
        int m = Integer.parseInt(str[1]);
        double[][] matrix = new double[n][m + 1]; // colum b


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m + 1; j++) {
                matrix[i][j] = Double.parseDouble(scanner.next());
            }
//            matrix[i] = Arrays.stream(scanner.nextLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
        }
        return matrix;
    }

    public void write(double[] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            printWriter.printf("%15.3E", matrix[i]);
        }
        printWriter.flush();
    }

    public void write(String s) {
        printWriter.print(s);
        printWriter.flush();
    }

    public void close()
    {
        scanner.close();
        printWriter.close();
    }

}
