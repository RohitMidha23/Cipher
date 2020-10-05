import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
 
public class aes {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            System.out.println("[ERR]: " + e.toString());
        } 
        catch (UnsupportedEncodingException e) {
            System.out.println("[ERR]: " + e.toString());
        
        }
    }
 
    public static String encrypt(String text, String key) 
    {
        try
        {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("[ERR]: " + e.toString());
        }
        return "";
    }
 
    public static String decrypt(String text, String key) 
    {
        try
        {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        } 
        catch (Exception e) 
        {
            System.out.println("[ERR]: " + e.toString());
        }
        return "";
    }
    public static void main(String[] args) 
    {   
        Scanner s = new Scanner(System.in);
        String text, key;
        System.out.print("Enter text to encrypt: ");
        text = s.nextLine();

        System.out.print("Enter key: ");
        key = s.nextLine();

        String encrypted = aes.encrypt(text, key) ;
        System.out.println("ENCRYPTED TEXT: " + encrypted);

        String decrypted = aes.decrypt(encrypted, key) ;
        System.out.println("DECRYPTED TEXT: " + decrypted);	
    }
}