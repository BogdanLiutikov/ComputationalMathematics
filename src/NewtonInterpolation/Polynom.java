package NewtonInterpolation;

public class Polynom {

    private Monomial head;
    private final static double EPS = 1e-16;

    private static class Monomial {
        int p;
        double a;
        Monomial next;

        public Monomial(int p, double a, Monomial next) {
            this.p = p;
            this.a = a;
            this.next = next;
        }
    }

    public Polynom() {
        head = null;
    }

    public Polynom(int p, double a) {
        head = new Monomial(p, a, null);
    }

    private void add(int p, double a, Monomial addTo) {
        addTo.next = new Monomial(p, a, null);
    }

    public void sum(Polynom polynom1, Polynom polynom2) {

        Polynom polynom = new Polynom(0, 0);
        Monomial polynomPointer = polynom.head;
        Monomial polynomPointer1 = polynom1.head;
        Monomial polynomPointer2 = polynom2.head;

        while (polynomPointer1 != null && polynomPointer2 != null) {

            if (polynomPointer1.p > polynomPointer2.p) {
                if (nonZero(polynomPointer1.a)) {
                    polynom.add(polynomPointer1.p, polynomPointer1.a, polynomPointer);
                    polynomPointer = polynomPointer.next;
                }
                polynomPointer1 = polynomPointer1.next;
            } else if (polynomPointer1.p < polynomPointer2.p) {
                if (nonZero(polynomPointer2.a)) {
                    polynom.add(polynomPointer2.p, polynomPointer2.a, polynomPointer);
                    polynomPointer = polynomPointer.next;
                }
                polynomPointer2 = polynomPointer2.next;
            } else {
                if (nonZero(polynomPointer1.a + polynomPointer2.a)) {
                    polynom.add(polynomPointer1.p, polynomPointer1.a + polynomPointer2.a, polynomPointer);
                    polynomPointer = polynomPointer.next;
                }
                polynomPointer1 = polynomPointer1.next;
                polynomPointer2 = polynomPointer2.next;
            }
        }

        if (polynomPointer1 != null)
            polynomPointer.next = copy(polynomPointer1);
        else if (polynomPointer2 != null)
            polynomPointer.next = copy(polynomPointer2);

        this.head = polynom.head.next;
    }

    private Monomial copy(Monomial polynomPointerFrom) {
        Polynom polynomTail = new Polynom(0, 0);
        Monomial polymonPointer = polynomTail.head;
        while (polynomPointerFrom != null) {
            if (nonZero(polynomPointerFrom.a)) {
                polynomTail.add(polynomPointerFrom.p, polynomPointerFrom.a, polymonPointer);
                polymonPointer = polymonPointer.next;
            }
            polynomPointerFrom = polynomPointerFrom.next;
        }
        return polynomTail.head.next;
    }

    public void multiply(Polynom polynom1, Polynom polynom2) {

        Polynom polynom = new Polynom(0, 0);
        Monomial polynomPointer1 = polynom1.head;
        Monomial polynomPointer2 = polynom2.head;
        Polynom current = new Polynom(0, 0);
        Monomial currentPointer;

        while (polynomPointer1 != null) {
             currentPointer= current.head;
            while (polynomPointer2 != null) {
                if (nonZero(polynomPointer1.a * polynomPointer2.a)) {
                    current.add(polynomPointer1.p + polynomPointer2.p, polynomPointer1.a * polynomPointer2.a, currentPointer);
                    currentPointer = currentPointer.next;
                }
                polynomPointer2 = polynomPointer2.next;
            }
            current.head = current.head.next;
            polynom.sum(polynom, current);
            polynomPointer2 = polynom2.head;
            polynomPointer1 = polynomPointer1.next;
        }

        this.head = polynom.head;
    }

    /**
     * @param a multiply polynom by a
     */
    public void multiply(double a) {
        Monomial h = head;
        while (h != null) {
            h.a *= a;
            h = h.next;
        }
    }

    private boolean nonZero(double a) {
        return Math.abs(a) >= EPS;
    }

    public void print() {
        System.out.println(this.toString());
    }

    public double calculate(double x) {
        Monomial h = head;
        Monomial previous = head;
        double b = 0;
        for (int i = h.p; i >= 0; i--) {
            if (h != null) {
                if (previous.p - h.p <= 1) {
                    b = h.a + x * b;
                    previous = h;
                    h = h.next;
                } else {
                    b = 0 + x * b;
                    previous = h;
                }
            } else {
                b = 0 + x * b;
            }
        }
        return b;
    }

    @Override
    public String toString() {
        Monomial h = head;
        StringBuilder string = new StringBuilder();

        if (h != null && h.a < 0)
            string.append("-");

        while (h != null) {

            //Коэфф
            if (Math.abs(h.a) != 1)
                string.append(Math.abs(h.a));
            else if (h.p == 0)
                string.append(Math.abs(h.a));

            //Степень
            if (h.p >= 2)
                string.append("x^" + h.p);
            else if (h.p == 1)
                string.append("x");

            //Знак
            if (h.next != null)
                if (h.next.a > 0)
                    string.append(" + ");
                else
                    string.append(" - ");

            h = h.next;
        }
        string.append("\n");

        return string.toString();
    }
}
