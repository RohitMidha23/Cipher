import java.util.Scanner;

public class extendedeuclid {
    public static void solve(long a, long b) {
        long maina = a, mainb = b;
        long x = 0, y = 1, lastx = 1, lasty = 0, temp;
        while (b != 0) {
            long q = a / b;
            long r = a % b;

            a = b;
            b = r;

            temp = x;
            x = lastx - q * x;
            lastx = temp;

            temp = y;
            y = lasty - q * y;
            lasty = temp;
        }
        System.out.println("Roots  x: " + lastx + " y:" + lasty);
        long gcd = (maina * lastx) + (mainb * lasty);
        System.out.println("GCD: " + gcd);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Extended Euclid Algorithm Test\n");

        System.out.println("Enter a b of ax + by = gcd(a, b)\n");
        long a = scan.nextLong();
        long b = scan.nextLong();
        solve(a, b);
    }
}