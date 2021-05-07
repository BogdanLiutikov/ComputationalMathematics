package LagrangeInterpolation;

public class LagrangePolynom extends Polynom {

    private Polynom lagrangePolynom;

    public LagrangePolynom(int n, double[] x, double[] y) {

        Polynom Lagrange = new Polynom();
        Polynom P = new Polynom();
        Polynom currentPolynom = new Polynom();
        for (int i = 0; i < n; i++) {
            P.head = null;
            P.add(0, 1);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    currentPolynom.head = null;
                    currentPolynom.add(0, -x[j]);
                    currentPolynom.add(1, 1);
                    P = Polynom.multiply(P, currentPolynom);
                    P.multiply(1 / (x[i] - x[j]));
                }
            }
            P.multiply(y[i]);
            Lagrange = Polynom.sum(Lagrange, P);
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
