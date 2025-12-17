package src;

public class ConverterEngine {
    public static double convert(double val, String type) {
        switch (type) {
            case "USD to INR":
                return val * 90.50;
            case "C to F":
                return (val * 9 / 5) + 32;
            case "KM to Miles":
                return val * 0.621371;
            case "KG to Lbs":
                return val * 2.20462;
            default:
                return val;
        }
    }
}