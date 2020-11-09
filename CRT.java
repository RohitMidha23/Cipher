import java.math.BigInteger;

public class CRT {
    public static int solve(Integer[] num, Integer[] rem, Integer k) {
        Integer prod = 1;
        for (int i = 0; i < k; i++)
            prod *= num[i];
        BigInteger M = BigInteger.valueOf(prod.intValue());
        // System.out.println("M: " + M.intValue());
        int result = 0;

        for (int i = 0; i < k; i++) {
            BigInteger mi = BigInteger.valueOf(num[i].intValue());
            BigInteger ai = BigInteger.valueOf(rem[i].intValue());
            BigInteger Mi = M.divide(mi);
            // System.out.println("ai: " + ai.intValue());
            // System.out.println("Mi: " + Mi.intValue());
            BigInteger yi = Mi.modInverse(mi);
            // System.out.println("yi: " + yi.intValue());

            BigInteger val = ai.multiply(yi.multiply(Mi));
            // System.out.println("VAL:" + val.intValue() + "\n");
            result += val.intValue();

        }
        return result % M.intValue();

    }

    public static void main(String[] args) {
        {
            Integer num[] = { 3, 5, 7 }; // all mi's
            Integer rem[] = { 2, 3, 2 }; // all ai's
            Integer k = num.length;
            System.out.println("x is " + solve(num, rem, k));
        }
    }
}
