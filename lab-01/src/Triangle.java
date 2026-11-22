import java.util.Objects;

public final class Triangle {

    private static final double EPSILON = 1e-9;

    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        validateSide(a);
        validateSide(b);
        validateSide(c);
        if (a + b <= c + EPSILON || a + c <= b + EPSILON || b + c <= a + EPSILON) {
            throw new IllegalArgumentException(
                    "Sides " + a + ", " + b + ", " + c + " do not form a triangle");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    private static void validateSide(double side) {
        if (!Double.isFinite(side) || side <= 0) {
            throw new IllegalArgumentException("Side must be a positive number: " + side);
        }
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getPerimeter() {
        return a + b + c;
    }

    public double getArea() {
        double s = getPerimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public boolean isEquilateral() {
        return almostEqual(a, b) && almostEqual(b, c);
    }

    private static boolean almostEqual(double x, double y) {
        return Math.abs(x - y) < EPSILON;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Triangle other)) {
            return false;
        }
        return Double.compare(a, other.a) == 0
                && Double.compare(b, other.b) == 0
                && Double.compare(c, other.c) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return "Triangle{a=" + a + ", b=" + b + ", c=" + c + "}";
    }
}
