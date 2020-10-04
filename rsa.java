import java.util.*; 
import java.io.*; 
import java.math.*;

public class rsa {

    static BigInteger N, p, q, phi, e, d; 
    static Random r;
    static int bitlength = 1024;

    public static void init(){
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }
    public static byte[] encrypt(byte[] text)
    {
        return (new BigInteger(text)).modPow(e, N).toByteArray();
    }
 
    public static byte[] decrypt(byte[] text)
    {
        return (new BigInteger(text)).modPow(d, N).toByteArray();
    }

    private static String bytesToString(byte[] byteArray)
    {
        String text = "";
        for (byte b : byteArray)
        {
            text += Byte.toString(b);
        }
        return text;
    }

    public static void main(String[] args){ 
        String text; 
        Scanner s = new Scanner(System.in);
        init();
        System.out.print("Enter text to encrypt:"); 
        text = s.nextLine();

        System.out.println("BYTES TO ENCRYPT:" + bytesToString(text.getBytes()));

        byte[] encrypted = encrypt(text.getBytes());
        System.out.println("ENCRYPTED BYTES: " + bytesToString(encrypted));

        byte[] decrypted = decrypt(encrypted);
        System.out.println("BYTES DECRYPTED:" + bytesToString(decrypted));
        System.out.println("DECRYPTED TEXT: " + new String(decrypted));
    }
}
