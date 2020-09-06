import java.util.Scanner;

class playfair {

  public static int mod5(int a){ 
    int result = (a%5);
    if(result<0){
        // java % returns negative numbers
        result += 5;
    }
    return result;
  }

  public static char[][] generateKeyTable(String skey){
    int i, j, k;
    int[] map = new int[26];
    for(int x=0;x<26;x++){
      map[x] = 0;
    }
    char[] key = skey.toCharArray();
    char[][] keyTable = new char[5][5];
    // map contains the number of times a letter is used 
    // 2 means it's part of key. 0 (init) means not part of key
    // only j has 1 
    for (i = 0; i < skey.length(); i++) { 
        if (key[i] != 'j') 
            map[key[i] - 97] = 2; 
    } 
    // j is not used in the 5x5 table
    map['j' - 97] = 1;

    // go through key and start filling table
    i = 0; j=0;
    for(k=0; k<skey.length(); k++){
      if(map[key[k] - 97] == 2){ 
        map[key[k] - 97] -=1; 
        keyTable[i][j] = key[k];
        j++;
        if(j==5){
          i++;
          j = 0;
        }
      }
    }

    for(k=0; k<26; k++){
      if(map[k] == 0){
        keyTable[i][j] = (char)(k+97);
        j++;
        if(j==5){
          i++;
          j = 0;
        }
      }
    }
    return keyTable;
  }

  
  public static int[] getIndexes(char[][] keyTable, char a, char b){
    int[] indexes = new int[4];
    int i,j;
    if (a == 'j') 
        a = 'i'; 
    else if (b == 'j') 
        b = 'i'; 
    for (i = 0; i < 5; i++) {
      for (j = 0; j < 5; j++) { 
        if (keyTable[i][j] == a) { 
          indexes[0] = i; 
          indexes[1] = j; 
        } 
        else if (keyTable[i][j] == b) { 
          indexes[2] = i; 
          indexes[3] = j; 
        } 
      } 
    }

    return indexes;
  }
  public static String encryptHelper(String stext, char[][] keyTable){
    int i;
    int[] a = new int[4];
    char[] text = stext.toCharArray();
    char[] charEncrypted = stext.toCharArray();
    for(i=0; i<stext.length()-1; i+=2){
      a = getIndexes(keyTable, text[i], text[i+1]);
      
      if (a[0] == a[2]) {
        // same row
        charEncrypted[i] = (char) keyTable[a[0]][mod5(a[1] + 1)];
        charEncrypted[i + 1] = (char) keyTable[a[0]][mod5(a[3] + 1)];
      }
      else if (a[1] == a[3]) {
        // same column
        charEncrypted[i] = (char) keyTable[mod5(a[0] + 1)][a[1]]; 
        charEncrypted[i + 1] = (char) keyTable[mod5(a[2] + 1)][a[1]]; 
      } 
      else { 
        // diff row diff column
        charEncrypted[i] = (char) keyTable[a[0]][a[3]]; 
        charEncrypted[i + 1] = (char) keyTable[a[2]][a[1]]; 
      } 
    }
    String encrypted = new String(charEncrypted);
    return encrypted;
  }

  public static void printKeyTable(char[][] keyTable){
    for(int i=0;i<5;i++){
      for(int j=0; j<5; j++){
        System.out.print(keyTable[i][j] + " ");
      }
      System.out.println();
    }
  }
  public static String encrypt(String text, String key){
    key = key.toLowerCase();
    text = text.toLowerCase();
    String fixedText = "";
    char char1, char2; 
    for(int i=0;i<text.length();) {
            char1=text.charAt(i);
            if(i+1<text.length()) {
                char2=text.charAt(i+1);
            }
            else {
                char2='z';
            }
            String tempString="";
            if(char1!=char2) {
                tempString=""+char1+char2;
                i+=2;
            }
            else if(char1==char2){
                tempString=""+char1+"x";
                i++;
            }
            fixedText += tempString;
        }
    // for odd length text add z
    if(fixedText.length() % 2 != 0){
      fixedText += "z";
    }
    System.out.println("STRING FOR ENCRYPTION:" + fixedText);
    char[][] keyTable = generateKeyTable(key);
    // printKeyTable(keyTable);
    // System.out.println();
    String encrypted = encryptHelper(fixedText, keyTable);
    return encrypted;
  }

  public static String decryptHelper(String stext, char[][] keyTable){
    int i;
    int[] a = new int[4];
    char[] text = stext.toCharArray();
    char[] charDecrypted = stext.toCharArray();
    for(i=0; i<stext.length()-1; i+=2){
      a = getIndexes(keyTable, text[i], text[i+1]);
      if (a[0] == a[2]) { 
            charDecrypted[i] = keyTable[a[0]][mod5(a[1] - 1)]; 
            charDecrypted[i + 1] = keyTable[a[0]][mod5(a[3] - 1)]; 
        } 
        else if (a[1] == a[3]) { 
            charDecrypted[i] = keyTable[mod5(a[0] - 1)][a[1]]; 
            charDecrypted[i + 1] = keyTable[mod5(a[2] - 1)][a[1]]; 
        } 
        else { 
            charDecrypted[i] = keyTable[a[0]][a[3]]; 
            charDecrypted[i + 1] = keyTable[a[2]][a[1]]; 
        } 
    }
      String decrypted = new String(charDecrypted);
      return decrypted;
  }
  public static String decrypt(String text, String key){
    key = key.toLowerCase();
    text = text.toLowerCase();
    char[][] keyTable = generateKeyTable(key);
    printKeyTable(keyTable);
    System.out.println();
    String decrypted = decryptHelper(text, keyTable);
    return decrypted;
  }

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    System.out.print("Enter text to encrypt:");
    String text = s.next();
    System.out.print("Enter key:");
    String key = s.next();

    String encrypted = encrypt(text, key);
    System.out.println("ENCRYPTED : " + encrypted); 

    String decrypted = decrypt(encrypted, key);
    System.out.println("DECRYPTED : " + decrypted); 

  }
}