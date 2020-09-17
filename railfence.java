import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class railfence {

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
    public static String encrypt(String text, int key){ 
        String encrypted = "";
        boolean check = false;
        int j=0; 
        int row = key;
        int column = text.length();

        char[][] matrix = new char[row][column];
        for(int i=0; i<row; i++){
            for(int k=0; k<column; k++){
                matrix[i][k] = '1';
            }
        }
        for(int i=0; i<column; i++){
            if(j==0 || j==key-1){
                check = !check;
            }
            matrix[j][i] = text.charAt(i);
            if(check){
                j++;
            } else{
                j--;
            }
        }
        for(int i=0; i<row; i++){
            for(int k=0; k<column; k++){
                if(matrix[i][k] != '1')
                    encrypted += matrix[i][k];
            }
        }
        System.out.println("ENCRYPTION MATRIX:");
        printMatrix(matrix, row, column);
        return encrypted;
    }
    
    public static String decrypt(String text, int key){
        String decrypted = "";
        boolean check = false;
        int j=0; 
        int row = key;
        int column = text.length();

        char[][] matrix = new char[row][column];
        for(int i=0; i<row; i++){
            for(int k=0; k<column; k++){
                matrix[i][k] = '1';
            }
        }
        for(int i=0; i<column; i++){
            if(j==0 || j==key-1){
                check = !check;
            }
            matrix[j][i] = '*';
            if(check){
                j++;
            } else{
                j--;
            }
        }
        printMatrix(matrix, row, column);
        int index=0;
        for(int i=0; i<row; i++){
            for(int k=0; k<column; k++){
                if(matrix[i][k]=='*' && index < column){
                    matrix[i][k] = text.charAt(index);
                    index++;
                }
            }
        }
        System.out.println("DECRYPTION MATRIX:");
        printMatrix(matrix, row, column);
        j=0; check = false;
        for(int i=0; i<column; i++){
            if(j==0 || j==key-1){
                check = !check;
            }
            decrypted += matrix[j][i];
            if(check){
                j++;
            } else{
                j--;
            }
        }

        return decrypted;
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
    
  }
}
