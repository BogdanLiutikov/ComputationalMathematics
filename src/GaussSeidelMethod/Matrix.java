package GaussSeidelMethod;

public class Matrix {

    private double[][] matrix;
    private double[] solution;
    private double[] absRowSum;
    private int n, m;
    private double EPS;
    public final static int SOLVED = 0;
    public final static int UNSOLVABLE = -1;

    public void init(File file) {
        this.EPS = file.readEps();
        this.matrix = file.readMatrix();
        n = matrix.length;
        m = matrix[0].length;
        absRowSum = new double[n];
    }

    public int solve() {
        int status = SOLVED;

        int nextStep = diagonalPredominance();
        final int diagonalZero = -1;
        final int diagonalPredominance = 0;
        final int nondiagonalPredominance = 1;

        //Присутствуют ли диагональные нули
        if (nextStep != diagonalZero) {
            // Решение
            if (nextStep == diagonalPredominance) {
                double[] x = new double[n];
                status = GaussSeidel(x);
            } else {
                // Решение с контролем
                status = controlSolve();
            }
        } else {

            //Пытаемся привести матрицу к виду с диагональным преобладанием
            System.out.println("Будет произведена попытка приведения матрицы к матрице с диагональным преобладанием");
            if (makeDominant())
                System.out.println("Удалось привести матрицу к матрице с диагональным преобладанием");
            else {
                System.out.println("Не удалось привести матрицу к матрице с диагональным преобладанием");
                System.out.println("Попробуем просто убарать нули с диагонали.");

                print();
                System.out.println();
                if (killZero()) {
                    System.out.print("Удалось ");
                    System.out.println();
                } else {
                    System.out.print("Не удалось ");
                    System.out.println("убарть нули с диагонали\n");
                }
            }

            print();

            nextStep = diagonalPredominance();
            if (nextStep == nondiagonalPredominance)
                return UNSOLVABLE;
            else {
                //Проверяем на ДУС
                if (nextStep == diagonalPredominance) {
                    // Решение
                    double[] x = new double[n];
                    status = GaussSeidel(x);
                } else {
                    // Решение с контролем
                    status = controlSolve();
                }
            }
        }

        return status;
    }

    private int GaussSeidel(double[] initialApproximation) {

        double[] x = initialApproximation;
        boolean completionCondition = false;
        double diff = 0;
        int iterations = 0;
        while (!completionCondition && iterations < 1 / EPS) {
            iterations++;
            diff = nextIteration(x);
            if (diff < EPS)
                completionCondition = true;
        }

        if (iterations == 1 / EPS)
            return UNSOLVABLE;

        solution = x;
        return SOLVED;
    }

    private double nextIteration(double[] currentIteration) {
        double[] x = currentIteration;
        double diff = 0, prevX;
        for (int j = 0; j < n; j++) {
            prevX = x[j];
            x[j] = (matrix[j][this.m - 1] - otherXSum(j, x)) / matrix[j][j];
            diff = Math.max(Math.abs(x[j] - prevX), diff);
        }
        return diff;
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

    private boolean modifyToDiagonalPredominance(int r, boolean[] visited, int[] rows) {
        int n = this.n;
        if (r == n) {
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


            if (absRowSum(i) - 2 * Math.abs(matrix[i][r]) <= EPS) {
                visited[i] = true;
                rows[r] = i;

                if (modifyToDiagonalPredominance(r + 1, visited, rows))
                    return true;

                visited[i] = false;
            }
        }
        return false;
    }

    /**
     * @return true если матрицу возможно преобразовать к матрице с диагональным преобладанием
     */
    private boolean makeDominant() {
        boolean[] visited = new boolean[n];
        int[] rows = new int[n];

        return modifyToDiagonalPredominance(0, visited, rows);
    }

    private boolean killZero() {
        boolean[] visited = new boolean[matrix.length];
        int[] rows = new int[matrix.length];

        return tryToKillZero(0, visited, rows);
    }

    private boolean tryToKillZero(int r, boolean[] visited, int[] rows) {
        int n = this.n;
        if (r == n) {
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


            if (Math.abs(matrix[i][r]) > EPS) {
                visited[i] = true;
                rows[r] = i;

                if (tryToKillZero(r + 1, visited, rows))
                    return true;

                visited[i] = false;
            }
        }
        return false;
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
        double diff = 0;
        double prevdiff;
        int counter = 0;

        // Следим за тем, чтобы разница монтонно уменьшалась
        for (i = 0; i < k; i++) {
            prevdiff = diff;
            diff = nextIteration(x);

            counter++;
            if (diff > prevdiff)
                counter = 0;
        }

        if (counter < m)
            return UNSOLVABLE;

        //Отдаём нынешние корни на дорешивание
        return GaussSeidel(x);
    }

    /**
     * @return <ul>
     * <li>-1 - На диагонали присутсвует 0</li>
     * <li> 0 - Матрица с диагональным преобладанием</li>
     * <li> 1 - Матрица не имеет свойства диагонального преобладания</li>
     * </ul>
     */
    private int diagonalPredominance() {
        fillAbsRowSum();
        for (int i = 0; i < n; i++) {
            if (Math.abs(matrix[i][i]) < EPS)
                return -1;
            if (!diagonalPredominanceRow(i))
                return 1;
        }
        return 0;
    }

    private boolean diagonalPredominanceRow(int row) {
        return absRowSum(row) - 2 * Math.abs(matrix[row][row]) <= EPS;
    }

    private void fillAbsRowSum() {
        for (int i = 0; i < n; i++) {
            absRowSum[i] = absRowSum(matrix[i]);
        }
    }

    private double absRowSum(double[] row) {
        double sum = 0;
        for (int i = 0; i < m - 1; i++) {
            sum += Math.abs(row[i]);
        }
        return sum;
    }

    private double absRowSum(int indexRow) {
        return absRowSum[indexRow];
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

}