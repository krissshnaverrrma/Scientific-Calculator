package src;

public class ScientificEngine {
    public static final double LIGHT_SPEED = 299792458;

    public static double evaluate(double n1, double n2, String op) {
        switch (op) {
            case "+":
                return n1 + n2;
            case "-":
                return n1 - n2;
            case "*":
                return n1 * n2;
            case "/":
                return n2 != 0 ? n1 / n2 : Double.NaN;
            case "x^y":
                return Math.pow(n1, n2);
            default:
                return n2;
        }
    }

    public static String solveQuadratic(double a, double b, double c) {
        double d = b * b - 4 * a * c;
        if (d < 0)
            return "Complex Roots";
        double x1 = (-b + Math.sqrt(d)) / (2 * a);
        double x2 = (-b - Math.sqrt(d)) / (2 * a);
        return String.format("x1=%.2f, x2=%.2f", x1, x2);
    }

    public static double evaluateSci(double val, String func) {
        switch (func) {
            case "sin":
                return Math.sin(Math.toRadians(val));
            case "√":
                return Math.sqrt(val);
            case "log":
                return Math.log10(val);
            case "π":
                return Math.PI;
            default:
                return val;
        }
    }

    public static double force(double m, double a) {
        return m * a;
    }
}