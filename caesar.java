import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

class caesar {
  public static int mod26(int a){ 
    int result = (a%26);
    if(result<0){
        // java % returns negative numbers
        result += 26;
    }
    return result;
  }
  public static String encrypt(String text, int key){ 
    String encrypted = "";
    for(int i=0; i<text.length(); i++){
      Character ch = text.charAt(i);
      String res = "";
      if(Character.isLowerCase(ch)){
        int r = (int)ch;
        r = mod26(r + key - 97);
        res = Character.toString((char) (r + 97));
      }
      else{
        int r = (int)ch;
        r = mod26(r + key - 65);
        res = Character.toString((char) (r + 65));
      }
      encrypted += res;
    }
    return encrypted;
  }

  public static String decrypt(String text, int key){
    String decrypted = "";
    for(int i=0; i<text.length(); i++){
      Character ch = text.charAt(i);
      String res = "";
      if(Character.isLowerCase(ch)){
        int r = (int)ch;
        r = mod26(r - key - 97);
        res = Character.toString((char) (r + 97));
      }
      else{
        int r = (int)ch;
        r = mod26(r - key - 65);
        res = Character.toString((char) (r + 65));
      }
      decrypted += res;
    }
    return decrypted;
  }

  public static void cryptAnalysis(String text){
        // text is encrypted
        int flag=0;
        ArrayList<String> commonWords = new ArrayList<String>(Arrays.asList(
        "the","he","at","but",
        "there", "of","was","be","not","use",
        "and","for","this","what","an",
        "a","on","have","all","each",
        "to","are","from","were","which",
        "in","as","or","we","she",
        "is","with","ine","when","do",
        "you","his","had","your","how",
        "that","they","by","can","their",
        "it","I","word","said","if",
        "hello", "hi", "bye", "good", "ten"));
        for(int i=0; i<26; i++){
            String xHat = decrypt(text, i);
            if(commonWords.contains(xHat)){
                System.out.println("Cipher broken...");
                System.out.println("Key:" + i);
                System.out.println("Encrypted Text:" + xHat);
                flag = 1;
            }
        }
        if(flag==0){
            System.out.println("Cipher couldn't be broken!");
        }
  }

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    System.out.print("Enter text to encrypt:");
    String text = s.next();
    System.out.print("Enter key:");
    int key = s.nextInt();

    String encrypted = encrypt(text, key);
    System.out.println("ENCRYPTED : " + encrypted); 

    String decrypted = decrypt(encrypted, key);
    System.out.println("DECRYPTED : " + decrypted); 
    System.out.println("-----CRYPT ANALYSIS-----");
    System.out.print("Enter text to break:");
    encrypted = s.next();
    cryptAnalysis(encrypted);
  }
}