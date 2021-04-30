package GaussSeidelMethod;

import java.util.Arrays;

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
            System.out.println("Будет произведена попытка приведения матрицы к матрице с диагональным преобладанием");
            //modifyToDiagonalPredominance();
            if (makeDominant())
                System.out.print("Удалось ");
            else
                System.out.print("Не удалось ");
            System.out.println("привести матрицу к матрице с диагональным преобладанием\n");

            print();

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
        while (!completionCondition && iterations < 1/EPS) {
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

        if(iterations == 1/EPS) {
            System.out.println("Метод не сходится!!!\n" +
                    "Ответ не имеет смысла!\n");
            return x;
        }
        System.out.printf("Метод получил ответ за %d итераций\n", iterations);
        return x;
    }

    /**
     * @param i номер строки матрицы
     * @param x текущий массив Х-ов
     * @return Возвращает сумму всех элементов строки за исключением диагонального
     */
    private double otherXSum(int i, double[] x) {
        double sum = 0;
        for (int j = 0; j < m - 1; j++) {
            sum += matrix[i][j] * x[j];
        }
        sum -= matrix[i][i] * x[i];
        return sum;
    }

    private void modifyToDiagonalPredominance() {

        System.out.println("Будет произведена попытка приведения матрицы к виду с диагональным преобладанием.\n");

        int index, numberOfMax;
        double max;
        boolean[] flexable = new boolean[n];

        for (int i = 0; i < n; i++) {
            flexable[i] = true;
        }

        for (int z = 0; z < m - 1; z++) {
            for (int i = 0; i < m - 1; i++) {

                max = -1;
                index = -1;
                numberOfMax = 0;
                for (int j = 0; j < n; j++) {

                    if (!flexable[j])
                        continue;

                    if (Math.abs(matrix[j][i]) > max) {
                        max = Math.abs(matrix[j][i]);
                        index = j;
                    }
                }

                for (int j = 0; j < n; j++) {

                    if (!flexable[j])
                        continue;

                    if (Math.abs(matrix[j][i]) == max)
                        numberOfMax++;
                }

                flexable[i] = numberOfMax > 1; // если максимумов в столбце > 1, то строка flexable

                if (!flexable[i] && index >= 0)
                    swapRows(i, index);
            }
        }

        int l;
        for (l = 0; l < n; l++) {
            if (!flexable[l])
                break;
        }
        if (l == n) {
            System.out.println("Нет такой перестановки\n");
            return;
        }

        print();
    }

    public boolean transformToDominant(int r, boolean[] visited, int[] rows) {
        int n = this.n;
        if (r == this.n) {
            double[][] T = new double[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++)
                    T[i][j] = matrix[rows[i]][j];
            }

            matrix = T;

            return true;
        }

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;

            if (absRowSum(matrix[i]) - 2 * Math.abs(matrix[i][r]) < EPS) { // diagonally dominant?
                visited[i] = true;
                rows[r] = i;

                if (transformToDominant(r + 1, visited, rows))
                    return true;

                visited[i] = false;
            }
        }
        return false;
    }

    /**
     * @return true если матрицу возможно преобразовать к матрице с диагональным преобладанием
     */
    public boolean makeDominant() {
        boolean[] visited = new boolean[matrix.length];
        int[] rows = new int[matrix.length];

        Arrays.fill(visited, false);

        return transformToDominant(0, visited, rows);
    }

    private void swapRows(int i, int index) {
        if (i == index)
            return;
        double[] temp = matrix[i];
        matrix[i] = matrix[index];
        matrix[index] = temp;
    }

    private int controlSolve() {

        System.out.println("ДУС не выполняется.\n" +
                "Будет произведена поптыка решения системы под контролем.");

        final int k = 10; //количество контролируемых итераций
        final int m = 4; //последние m отслеживаемых на сходимость итераций

        double[] x = new double[n];
        int i;
        double prevX, diff = 0;
        double prevdiff = diff;
        int counter = 0;

        // Следим за тем, чтобы разница монтонно уменьшалась
        for (i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                prevX = x[j];
                x[j] = (matrix[j][this.m - 1] - otherXSum(j, x)) / matrix[j][j];
                diff = Math.max(Math.abs(x[j] - prevX), diff);
            }
            counter++;
            if (prevdiff - diff < 0)
                counter = 0;
            prevdiff = diff;
        }

        if (counter < m)
            return UNSOLVABLE;

        //Отдаём нынешние корни на дорешивание
        solution = GaussSeidel(x);
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
            if (!diagonalPredominanceRow(i))
                return false;
        }
        return true;
    }

    private boolean diagonalPredominanceRow(int row) {
        return absRowSum(matrix[row]) - 2 * Math.abs(matrix[row][row]) < EPS;
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