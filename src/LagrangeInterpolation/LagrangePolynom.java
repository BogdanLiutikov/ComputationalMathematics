package LagrangeInterpolation;

public class LagrangePolynom extends Polynom {

    private Polynom lagrangePolynom;

    public LagrangePolynom(int n, double[] x, double[] y) {

        Polynom Lagrange = new Polynom();
        Polynom P; // Polynom for current x[i] point
        Polynom currentPolynom = new Polynom(); // (x - x[j])
        Polynom X = new Polynom(1,1); // x

        for (int i = 0; i < n; i++) {
            P = new Polynom(0,1);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    currentPolynom.sum(X, new Polynom(0, -x[j]));
                    P.multiply(P, currentPolynom);
                    P.multiply(1 / (x[i] - x[j]));
                }
            }
            P.multiply(y[i]);
            Lagrange.sum(Lagrange, P);
        }
        lagrangePolynom = Lagrange;
    }

    @Override
    public double calculate(double x) {
        return lagrangePolynom.calculate(x);
    }

    @Override
    public void print() {
        lagrangePolynom.print();
    }

    @Override
    public String toString() {
        return lagrangePolynom.toString();
    }
}
