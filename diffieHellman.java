import java.util.*; 
import java.io.*; 
import java.math.*;

public class diffieHellman {

    public static Boolean isPrime(int n){ 
        if (n <= 1) 
            return false; 
        if (n <= 3)  
            return true; 
        if (n % 2 == 0 || n % 3 == 0)  
            return false; 
  
        for (int i = 5; i * i <= n; i = i + 6)  
            if (n % i == 0 || n % (i + 2) == 0)  
                return false; 
        return true; 
    } 
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        BigInteger p, alpha, privA, privB;
        System.out.print("Enter value for p:");
        String x = s.next();
        while(!isPrime(Integer.parseInt(x))){
            System.out.print("Enter a prime value for p:");
            x = s.next();
        }
        p = new BigInteger(x); 
        System.out.print("Enter alpha value (alpha is a primitive root in Zp): ");
        x = s.next();
        alpha = new BigInteger(x);
        
        System.out.print("Enter the private key for A:");
        x = s.next();
        privA = new BigInteger(x);


        System.out.print("Enter the private key for B:");
        x = s.next();
        privB = new BigInteger(x);
        

        BigInteger publicA = alpha.modPow(privA, p);
        BigInteger publicB = alpha.modPow(privB, p);

        BigInteger sharedA = publicB.modPow(privA, p);
        BigInteger sharedB = publicA.modPow(privB, p);

        System.out.println("SHARED KEY FOR A:" + sharedA.toString());
        System.out.println("SHARED KEY FOR B:" + sharedB.toString());
    }
}