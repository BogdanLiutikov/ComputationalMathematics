package NewtonInterpolation;

public class NewtonePolynom extends Polynom {

    private Polynom newtonePolynom;

    public NewtonePolynom(int n, double[] x, double[] y) {

        double[] dividedDifference = DividedDifference(n, x, y);
        dividedDifference[0] = 1;

        newtonePolynom = new Polynom(0, y[0]); // f(x0)

        Polynom polynom = new Polynom(0, 1); // (x - x[i])*...*(x-x[i+])
        Polynom X = new Polynom(1, 1); // x
        Polynom current = new Polynom(); // (x - x[i])

        for (int i = 1; i < n; i++) {
            current.sum(X, new Polynom(0, -x[i - 1]));
            polynom.multiply(polynom, current);
            polynom.multiply(dividedDifference[i] / dividedDifference[i - 1]);

            newtonePolynom.sum(newtonePolynom, polynom);
        }
    }

    private double[] DividedDifference(int n, double[] x, double[] f) {

        double[] dividedDifference = new double[n];
        double[] evencolumn = f.clone(); // 0 order
        double[] oddcolumn = new double[n];

        for (int i = 1; i < n; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < n - i; j++) {
                    evencolumn[j] = (oddcolumn[j] - oddcolumn[j + 1]) / (x[j] - x[j + i]);
                }

                dividedDifference[i] = evencolumn[0];
            } else {
                for (int j = 0; j < n - i; j++) {
                    oddcolumn[j] = (evencolumn[j] - evencolumn[j + 1]) / (x[j] - x[j + i]);
                }

                dividedDifference[i] = oddcolumn[0];
            }
        }
        return dividedDifference;
    }

    @Override
    public String toString() {
        return newtonePolynom.toString();
    }

    @Override
    public double calculate(double x) {
        return newtonePolynom.calculate(x);
    }
}
