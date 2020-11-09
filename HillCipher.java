import java.math.BigInteger;
import java.util.*;

public class HillCipher {
    HashMap<Character, Integer> alphaToInt = new HashMap<Character, Integer>(26);
    HashMap<Integer, Character> intToAlpha = new HashMap<Integer, Character>(26);

    Integer[][] keyMatrix;
    Integer[][] keyMatrixInverse;
    Double keyMatrixSize;

    public HillCipher() {
        for (int i = 0; i < 26; i++) {
            alphaToInt.put((char) (i + 65), i);
            intToAlpha.put(i, (char) (i + 65));
        }
    }

    public static void printMatrix(Integer[][] matrix, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println("\n");
        }
    }

    public void initKeyMatrix(String key) {
        Integer len = key.length();
        keyMatrixSize = Math.floor(Math.sqrt(len));

        keyMatrix = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];
        keyMatrixInverse = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];

        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                keyMatrix[i][j] = alphaToInt.get(key.charAt(i));
            }
        }
        System.out.println("KEY MATRIX");
        printMatrix(keyMatrix, keyMatrixSize.intValue(), keyMatrixSize.intValue());
    }

    public Integer[][] matrixMultiplication(Integer[][] matrix, Integer[][] vec) {
        Integer[][] c = new Integer[keyMatrixSize.intValue()][1];
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < 1; j++) {
                c[i][j] = 0;
                for (int k = 0; k < keyMatrixSize; k++) {
                    c[i][j] += matrix[i][k] + vec[k][j];
                }
                c[i][j] = Math.floorMod(c[i][j], 26);
            }
        }
        return c;
    }

    public Integer modInverse(Integer a, Integer n) {
        BigInteger N = BigInteger.valueOf(n);
        try {
            BigInteger mi = BigInteger.valueOf(a).modInverse(N);
            return mi.intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public Integer[][] getSubMatrix(Integer[][] matrix, Integer excludeRow, Integer excludeCol){
        Integer[][] subM = new Integer[(matrix.length - 1)][(matrix.length - 1)];
        int r=-1;
        for(int i=0; i<matrix.length; i++){
            if(i==excludeRow.intValue()){
                continue;
            }
            r++;
            int c = -1
            for(int j=0; j<matrix.length; j++){
                if(j==excludeCol.intValue()){
                    continue;
                }
                subM[r][++c] = matrix[i][j];
            }
        }
        return subM;
    }

    public Integer signChange(Integer n) {
        return n % 2 == 0 ? 1 : -1;
    }

    public Integer getDeterminant(Integer[][] matrix) {
        if (matrix.length == 1) {
            return Math.floorMod(matrix[0][0], 26);
        } else if (matrix.length == 2) {
            return Math.floorMod((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]), 26);
        } else {
            Double sum = 0;
            for (int i = 0; i < matrix.length; i++) {
                sum += signChange(i) * matrix[0][i] * getDeterminant(getSubMatrix(matrix, 0, i));
            }
            return Math.floorMod(sum.intValue(), 26);
        }
    }

    public Integer[][] getAdjointMatrix(Integer[][] matrix) {
        Integer[][] adj = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                adj[i][j] = signChange(i) * signChange(j) * getDeterminant(getSubMatrix(matrix, i, j));
            }
        }
        Integer[][] transposedAdj = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                transposedAdj[i][j] = adj[i][j];
            }
        }
        return transposedAdj;
    }

    public void initKeyMatrixInverse() {
        Integer det = getDeterminant(keyMatrix);
        Integer inv = modInverse(det, 26);
        if (det == -1) {
            System.out.println("[ERR[ MATRIX CANT BE INVERTED...");
            System.exit(-1);
        }
        Integer[][] adjoint = getAdjointMatrix(keyMatrix);
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                keyMatrixInverse[i][j] = Math.floorMod(inv * adjoint[i][j], 26);
            }
        }
        System.out.println("INVERSE KEY MATRIX");
        printMatrix(keyMatrixInverse, keyMatrixSize.intValue(), keyMatrixSize.intValue());

    }

    public String encrypt(String text) {
        String encrypted = ""
        if (text.length() % keyMatrixSize.intValue() != 0) {
            for (int i = 0; text.length() % keyMatrixSize.intValue() == 0; i++) {
                text += "X";
            }
        }
        for (int i = 0; i < Math.floorDiv(text.length(), keyMatrixSize.intValue())
                * keyMatrixSize.intValue(); i += keyMatrixSize.intValue()) {
                    Integer[][] vec = new Integer[keyMatrixSize.intValue()][1];
                    for(int j=0; j<keyMatrixSize; j++){ 
                        vec[j][0] = alphaToInt.get(text.charAt(i+j));
                    }
                    Integer[][] encryptedVec = matrixMultiplication(keyMatrix, vec);
                    for(int j=0; j<keyMatrixSize; j++){
                        encrypted += intToAlpha.get(encryptedVec[j][0]);
                    }
        }
        return encrypted;
    }

    public String decrypt(String text) {
        String decrypted = ""
        for (int i = 0; i < Math.floorDiv(text.length(), keyMatrixSize.intValue())
                * keyMatrixSize.intValue(); i += keyMatrixSize.intValue()) {
                    Integer[][] vec = new Integer[keyMatrixSize.intValue()][1];
                    for(int j=0; j<keyMatrixSize; j++){ 
                        vec[j][0] = alphaToInt.get(text.charAt(i+j));
                    }
                    Integer[][] decryptedVec = matrixMultiplication(keyMatrixInverse, vec);
                    for(int j=0; j<keyMatrixSize; j++){
                        decrypted += intToAlpha.get(decryptedVec[j][0]);
                    }
        }
        return decrypted;
    }

    public static void main(String[] args) {
        HillCipher hillCipher = new HillCipher();
        Scanner s = new Scanner(System.in);
        String text = "";

        System.out.println("Enter Plain Text:");
        text = s.nextLine();

        text = text.replaceAll(" ", "");
        String keyword = "";

        System.out.println("Enter KeyWord:");
        keyword = s.nextLine();

        keyword = keyword.replaceAll(" ", "");
        hillCipher.initKeyMatrix(keyword);
        String encryptedText = hillCipher.encrypt(text);

        hillCipher.initKeyMatrixInverse();
        System.out.println("Encrypted Text:" + encryptedText);
        String decryptedText = hillCipher.decrypt(encryptedText);
        System.out.println("Decrypted Text:" + decryptedText);
        s.close();
    }

}