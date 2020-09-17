import java.util.Scanner;
import java.util.HashMap;

public class rowcolumn {

    public static void printMatrix(char[][] matrix, int row, int column){
        char ignore='1';
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                if(matrix[i][j] == ignore)
                    System.out.print(" " + "\t");
                else
                    System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
    public static String encrypt(String text, int[] key){
        String encrypted = "";
        int keyLength = key.length;
        int columns = keyLength;
        int rows = (int) (text.length() / columns);
        char[][] matrix = new char[rows][columns];
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i=0; i<keyLength; i++){
            map.put(key[i], i);
        }
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
               matrix[i][j] = '1'; 
            }
        }
        int index=0; boolean flag = false;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(index<text.length())
                    matrix[i][j] = text.charAt(index++);
                else{
                    flag = true;
                    break;
                }
            }
            if(flag)
                break;
        }
        System.out.println("ENCRYPTION MATRIX:");
        printMatrix(matrix, rows, columns);
        for(int i=0; i<keyLength; i++){
            // map[i] is column index
            // j is row index
            for(int j=0; j<rows; j++){
                encrypted += matrix[j][map.get(i)];
            }
        }
        return encrypted;
    }

    public static String decrypt(String text, int[] key){
        String decrypted = "";
        int keyLength = key.length;
        int columns = keyLength;
        int rows = (int) (text.length() / columns);
        char[][] matrix = new char[rows][columns];
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
               matrix[i][j] = '1'; 
            }
        }
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int i=0; i<keyLength; i++){
            map.put(key[i], i);
        }
        int index=0;
        for(int i=0; i<keyLength; i++){
            // key[i] is column index
            // j is row index
            for(int j=0; j<rows; j++){
                matrix[j][map.get(i)] = text.charAt(index++);
            }
        }
        System.out.println("DECRYPTION MATRIX:");
        printMatrix(matrix, rows, columns);
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
               decrypted += matrix[i][j];
            }
        }

        return decrypted;
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter text to encrypt:");
        String text = s.next();
        System.out.print("Enter key length:");
        int n; n = s.nextInt();
        while(text.length() % n !=0){
            System.out.println("Key Length should divide text length!");
            System.out.print("Enter key length:");
            n = s.nextInt();
        }
        System.out.println("Enter key (from 1 to " + n + "):");
        int[] key = new int[n];
        for(int i = 0; i < n; i++)
        {
            key[i] = s.nextInt() - 1; // -1 for zero indexing
        }
    
        String encrypted = encrypt(text, key);
        System.out.println("ENCRYPTED : " + encrypted); 
    
        String decrypted = decrypt(encrypted, key);
        System.out.println("DECRYPTED : " + decrypted); 
        
    }
}
