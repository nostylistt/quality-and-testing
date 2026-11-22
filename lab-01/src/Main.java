public class Main {

    public static void main(String[] args) {
        Triangle triangle = new Triangle(3, 4, 5);
        System.out.println(triangle);
        System.out.println("Perimeter: " + triangle.getPerimeter());
        System.out.println("Area: " + triangle.getArea());
        System.out.println("Equilateral: " + triangle.isEquilateral());

        check("perimeter of 3-4-5 is 12", triangle.getPerimeter() == 12);
        check("area of 3-4-5 is 6", Math.abs(triangle.getArea() - 6) < 1e-9);
        check("3-4-5 is not equilateral", !triangle.isEquilateral());
        check("0.1+0.2 triangle is equilateral",
                new Triangle(0.1 + 0.2, 0.3, 0.3).isEquilateral());
        check("equal triangles are equal", triangle.equals(new Triangle(3, 4, 5)));
        check("negative side is rejected", throwsOnCreate(-1, 2, 2));
        check("zero side is rejected", throwsOnCreate(0, 2, 2));
        check("1-2-10 is rejected", throwsOnCreate(1, 2, 10));
        check("degenerate 1-2-3 is rejected", throwsOnCreate(1, 2, 3));
    }

    private static boolean throwsOnCreate(double a, double b, double c) {
        try {
            new Triangle(a, b, c);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void check(String name, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + name);
    }
}
