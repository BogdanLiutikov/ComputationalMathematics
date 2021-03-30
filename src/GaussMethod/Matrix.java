package GaussMethod;

public class Matrix {

    private double[][] matrix;
    private double[] solution;
    private int n, m;
    private final double EPS;
    public final static int SOLVED = 0;
    public final static int SINGULAR = -1;
    public final static int INCONSISTENT = 1;
    public final static int INFINITELY = 2;

    public Matrix() {
        EPS = eps();
    }


    public void init(double[][] matrix) {
        this.matrix = matrix;
        n = matrix.length;
        m = matrix[0].length;
    }

    public double[] getSolution() {
        return solution;
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.printf("%15.3E", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public int solve() {

        int status;
        status = forwardelimination();

        print();
        if (status == SINGULAR)
            return SINGULAR;

        if (Math.abs(matrix[n - 1][n - 1]) < EPS) {
            if (Math.abs(matrix[n - 1][m - 1]) < EPS) {
                return INFINITELY;
            } else {
                return INCONSISTENT;
            }
        }

        solution = backsubstitution();
        return status;
    }

    private int forwardelimination() {

        int i, k;
        for (k = 0; k < m - 2; k++) {

            // Ищем строку с ненулевым коэффициентом при Xk
            i = nonZeroRow(k);
            if (i == n)
                return SINGULAR;

            // Выставляем в k-ую строку, строку с ненулевым коэффициентом при Xk
            if (i != k)
                swapRows(i, k);

            //пересчитываем коэффициенты нижестоящих строк
            updateCoffs(k);
        }
        return SOLVED;
    }

    private int nonZeroRow(int k) {
        int i = k;
        while (Math.abs(matrix[i][k]) < EPS) {
            i++;
            if (i >= n)
                return n;
        }
        return i;
    }

    private void updateCoffs(int k) {
        int i, j;
        // Коэффициент при Xk
        double a = matrix[k][k];

        // Избавляемся от Xk в строчках ниже k-ой
        for (i = k + 1; i < n; i++) {
            double m = matrix[i][k] / a;
            matrix[i][k] = 0;
            for (j = k + 1; j < this.m; j++) {
                matrix[i][j] -= matrix[k][j] * m;
            }
        }
    }

    private void swapRows(int i, int k) {
        double[] temp = matrix[i];
        matrix[i] = matrix[k];
        matrix[k] = temp;
    }

    private double[] backsubstitution() {
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * x[j];
            }
            x[i] = (matrix[i][m - 1] - sum) / matrix[i][i];
        }
        return x;
    }

    private double eps() {
        double eps = 1.0;
        while (1 + eps > 1) {
            eps = eps / 10;
        }
        return eps;
    }
}