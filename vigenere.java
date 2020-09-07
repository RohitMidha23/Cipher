import java.util.Scanner;
import java.util.*;
import java.io.*;

public class vigenere {

    public static int mod26(int a){ 
        int result = (a%26);
        if(result<0){
            // java % returns negative numbers
            result += 26;
        }
        return result;
    }

    public static String keyGenerate(String text, String key){

        int x = text.length(); 
        for (int i = 0; ; i++) 
        { 
            if (x == i) 
                i = 0; 
            if (key.length() == text.length()) 
                break; 
            key+=(key.charAt(i)); 
        } 
        return key; 
    }

    
    public static String encrypt(String text, String key){
        String encrypted = "";
  
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++)
        {            
            int a = text.charAt(i) - 'A';
            int b = key.charAt(i) - 'A';
            encrypted += (char) (mod26(a + b) + 'A');
        }
        return encrypted;  
    }

    public static String decrypt(String text, String key){ 
        String decrypted = ""; 
      
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++)
        {
            decrypted += (char) (mod26(text.charAt(i) - key.charAt(i) + 26) + 'A');;
        }
        return decrypted; 
    } 

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter text to encrypt:");
        String text = s.next();
        System.out.print("Enter key:");
        String keyword = s.next();
        String key = keyGenerate(text, keyword);

        String encrypted = encrypt(text, key);
        System.out.println("ENCRYPTED : " + encrypted); 
    
        String decrypted = decrypt(encrypted, key);
        System.out.println("DECRYPTED : " + decrypted);     
    }
    
}
