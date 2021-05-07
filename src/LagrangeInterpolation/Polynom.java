package LagrangeInterpolation;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynom {

    public Monomial head;
    private final static double EPS = 1e-16;

    public static class Monomial {
        int p;
        double a;
        Monomial next;

        public Monomial(int p, double a, Monomial next) {
            this.p = p;
            this.a = a;
            this.next = next;
        }
    }

    public Polynom(String polynom) {

        head = null;
        Pattern polynomPattern = Pattern.compile(".[+-]+");
        Pattern monomialPattern = Pattern.compile("x\\^");
        Matcher matcher = polynomPattern.matcher(polynom);

        ArrayList<String> signs = new ArrayList<>();

        String[] monomials = polynomPattern.split(polynom);
        String[] monomial;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            signs.add(polynom.substring(start, end).trim());
        }

        signs.add(0, "+");

        for (int i = 0; i < monomials.length; i++) {
            monomial = monomialPattern.split(monomials[i]);
            add(Integer.parseInt(monomial[1].trim()), (signs.get(i).equals("+") ? 1 : -1) * Double.parseDouble(monomial[0]));
        }
        revers();
    }

    public Polynom() {
        head = null;
    }

    public void add(int p, double a) {
        if (Math.abs(a) >= EPS)
            head = new Monomial(p, a, head);
    }

    public static Polynom sum(Polynom polynom1, Polynom polynom2) {

        Polynom polynom = new Polynom();
        Monomial polynomPointer1 = polynom1.head;
        Monomial polynomPointer2 = polynom2.head;
        while (polynomPointer1 != null && polynomPointer2 != null) {

            if (polynomPointer1.p > polynomPointer2.p) {
                polynom.add(polynomPointer1.p, polynomPointer1.a);
                polynomPointer1 = polynomPointer1.next;
            } else if (polynomPointer1.p < polynomPointer2.p) {
                polynom.add(polynomPointer2.p, polynomPointer2.a);
                polynomPointer2 = polynomPointer2.next;
            } else {
                polynom.add(polynomPointer1.p, polynomPointer1.a + polynomPointer2.a);
                polynomPointer1 = polynomPointer1.next;
                polynomPointer2 = polynomPointer2.next;
            }
        }

        while (polynomPointer1 != null) {
            polynom.add(polynomPointer1.p, polynomPointer1.a);
            polynomPointer1 = polynomPointer1.next;

        }
        while (polynomPointer2 != null) {
            polynom.add(polynomPointer2.p, polynomPointer2.a);
            polynomPointer2 = polynomPointer2.next;
        }

        polynom.revers();
        return polynom;
    }

    public static Polynom multiply(Polynom polynom1, Polynom polynom2) {

        Polynom polynom = new Polynom();
        Monomial polynomPointer1 = polynom1.head;
        Monomial polynomPointer2 = polynom2.head;

        while (polynomPointer1 != null) {
            while (polynomPointer2 != null) {
                polynom.add(polynomPointer1.p + polynomPointer2.p, polynomPointer1.a * polynomPointer2.a);
                polynomPointer2 = polynomPointer2.next;
            }
            polynomPointer2 = polynom2.head;
            polynomPointer1 = polynomPointer1.next;
        }

        polynom.revers();
        polynom.similarMembers();
        return polynom;
    }

    public void multiply(double a) {
        Monomial h = head;
        while (h != null) {
            h.a *= a;
            h = h.next;
        }
    }

    private void revers() {
        Monomial lasth = null;
        Monomial nexth;

        if (head != null)
            nexth = head.next;
        else return;

        while (nexth != null) {
            head.next = lasth;
            lasth = head;
            head = nexth;
            nexth = head.next;
        }
        head.next = lasth;
    }

    private void similarMembers() {
        Monomial h = head;
        Monomial next = null;
        while (h != null) {
            next = h.next;
            while (next != null) {
                if (h.p == next.p) {
                    h.a += next.a;
                    h.next = next.next;
                }
                next = next.next;
            }
            h = h.next;
        }
    }

    public void print() {
        Monomial h = head;

        if (h.a < 0)
            System.out.print("-");

        while (h != null) {

            //Коэфф
            if (Math.abs(h.a) != 1)
                System.out.print(Math.abs(h.a));
            else if (h.p == 0)
                System.out.print(Math.abs(h.a));

            //Степень
            if (h.p >= 2)
                System.out.print("x^" + h.p);
            else if (h.p == 1)
                System.out.print("x");

            //Знак
            if (h.next != null)
                if (h.next.a > 0)
                    System.out.print(" + ");
                else
                    System.out.print(" - ");

            h = h.next;
        }
        System.out.println("\n");
    }

    public double calculate(double x) {

        Monomial h = head;
        double result = 0;
        double mon;
        while (h != null){
            mon = Math.pow(x, h.p);
            mon *= h.a;
            result += mon;
            h = h.next;
        }
        return result;
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
