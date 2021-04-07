package GaussSeidelMethod;

public class Matrix {

    private double[][] matrix;
    private double[] solution;
    private int n, m;
    private double EPS;
    public final static int SOLVED = 0;
    public final static int UNSOLVABLE = -1;

    public void init(File file) {
        this.EPS = file.readEps();
        this.matrix = file.readMatrix();
        n = matrix.length;
        m = matrix[0].length;
    }

    public int solve() {
        int status = SOLVED;

        //Проверяем на наличие нулей на диагонали
        if (!diagonalZero()) {

            //Проверяем на ДУС
            if (diagonalPredominance()) {
                // Решение
                double[] x = new double[n];
                solution = GaussSeidel(x);
            } else {
                // Решение с контролем
                status = controlSolve();
            }

        } else {

            //Пытаемся привести матрицу к виду с диагональным преобладанием
            modifyToDiagonalPredominance();

            //Проверяем на наличие нулей на диагонали
            if (diagonalZero()) {
                return UNSOLVABLE;
            } else {

                //Проверяем на ДУС
                if (diagonalPredominance()) {
                    // Решение
                    double[] x = new double[n];
                    solution = GaussSeidel(x);
                } else {
                    // Решение с контролем
                    status = controlSolve();
                }
            }
        }

        return status;
    }

    private double[] GaussSeidel(double[] initialApproximation) {

        double[] x = initialApproximation;
        boolean completionCondition = false;
        double prevX;
        int iterations = 0;
        while (!completionCondition) {
            iterations++;
            completionCondition = true;
            for (int i = 0; i < n; i++) {
                prevX = x[i];
                x[i] = (matrix[i][m - 1] - otherXSum(i, x)) / matrix[i][i];
                if (Math.abs(x[i] - prevX) < EPS)
                    continue;
                completionCondition = false;
            }
        }
        System.out.printf("Метод получил ответ за %d итераций\n", iterations);
        return x;
    }

    private double otherXSum(int i, double[] x) {
        double sum = 0;
        for (int j = 0; j < m - 1; j++) {
            sum += matrix[i][j] * x[j];
        }
        sum -= matrix[i][i] * x[i];
        return sum;
    }

    private void modifyToDiagonalPredominance() {
        int index;
        double max;
        for (int i = 0; i < m - 1; i++) {
            max = -1;
            index = -1;
            for (int j = 0; j < n; j++) {
                if (Math.abs(matrix[j][i]) > max) {
                    max = Math.abs(matrix[j][i]);
                    index = j;
                }
            }
            swapRows(i, index);
        }
    }

    private void swapRows(int i, int index) {
        double[] temp = matrix[i];
        matrix[i] = matrix[index];
        matrix[index] = temp;
    }

    private int controlSolve() {

        System.out.println("ДУС не выполняется.\n" +
                "Будет произведена поптыка решения системы под контролем.");

        int k = 10; //количество контролируемых итераций
        int m = 4; //последние m отслеживаемых на сходимость итераций

        double[] x = new double[n];
        int i;
        double prevX, diff = 0;

        // Просчитываем первые k-m итераций
        for (i = 0; i < k - m; i++) {
            for (int j = 0; j < n; j++) {
                prevX = x[j];
                x[j] = (matrix[j][this.m - 1] - otherXSum(j, x)) / matrix[j][j];
                diff = Math.abs(x[j] - prevX);
            }
        }

        double prevdiff = diff;
        // Следим за тем, чтобы разница монтонно уменьшалась
        for (i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                prevX = x[j];
                x[j] = (matrix[j][this.m - 1] - otherXSum(j, x)) / matrix[j][j];
                diff = Math.abs(x[j] - prevX);
                if (prevdiff - diff < 0)
                    return UNSOLVABLE;
                prevdiff = diff;
            }
        }
        //Отдаём нынешние корни на дорешивание
        GaussSeidel(x);
        return SOLVED;
    }

    /**
     * Возвращает true, если хотя бы один элемент диагонали равен нулю
     * Возвращает false, если на диагонали нет нулевых элементов
     *
     * @return Присутвуют ли на диагонали нули
     */
    private boolean diagonalZero() {
        for (int i = 0; i < n; i++) {
            if (Math.abs(matrix[i][i]) < EPS)
                return true;
        }
        return false;
    }

    /**
     * @return Является ли матрица матрицей с диагональным преобладанием.
     */
    private boolean diagonalPredominance() {
        for (int i = 0; i < n; i++) {
            if (absRowSum(matrix[i]) - 2 * Math.abs(matrix[i][i]) >= EPS)
                return false;
        }
        return true;
    }

    private double absRowSum(double[] row) {
        double sum = 0;
        for (int i = 0; i < m - 1; i++) {
            sum += Math.abs(row[i]);
        }
        return sum;
    }

    public double[] getSolution() {
        return solution;
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.printf("%15.6E", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

//    private double eps() {
//        double eps = 1.0;
//        while (1 + eps > 1) {
//            eps = eps / 10;
//        }
//        return eps;
//    }
}