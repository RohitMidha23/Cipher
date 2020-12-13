import java.util.HashMap;
import java.util.Scanner;

class Main {
    HashMap<Integer, Character> intToAlpha = new HashMap<Integer, Character>(26);
    HashMap<Character, Integer> alphaToInt = new HashMap<Character, Integer>(26);

    Integer[][] keyMatrix;
    Integer[][] keyMatrixInverse;
    Double keyMatrixSize;

    public Main() {
        System.out.println("INIT CONSTRUCTOR");
        for (int i = 0; i < 26; i++) {
            intToAlpha.put(i, (char) (i + 65));
            alphaToInt.put((char) (i + 65), i);
        }
    }

    public static void printMatrix(Integer[][] matrix, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println("\n");
        }
    }

    public void initKeyMatrix(String key) {
        int len = key.length();
        keyMatrixSize = Math.floor(Math.sqrt(len));

        keyMatrix = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];
        keyMatrixInverse = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];
        int index = 0;
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                keyMatrix[i][j] = alphaToInt.get(key.charAt(index));
                index++;
                // System.out.println(keyMatrix[i][j]);
            }
        }
        System.out.println("KEY MATRIX:");
        printMatrix(keyMatrix, keyMatrixSize.intValue(), keyMatrixSize.intValue());
    }

    public int sign(int n) {
        if (n % 2 == 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public int modInverse(int a, int n) {
        a = a % n;
        for (int i = 1; i < n; i++) {
            if (Math.floorMod(a * i, 26) == 1) {
                return i;
            }
        }
        return -1;
    }

    public Integer[][] getSubMatrix(Integer[][] matrix, Integer excludingRow, Integer excludingCol) {
        Integer[][] subM = new Integer[(matrix.length - 1)][(matrix.length - 1)];

        int r = -1;
        for (int i = 0; i < matrix.length; i++) {
            if (i == excludingRow)
                continue;
            r++;
            int c = -1;
            for (int j = 0; j < matrix.length; j++) {
                if (j == excludingCol)
                    continue;
                subM[r][++c] = matrix[i][j];
            }
        }
        return subM;
    }

    public Integer getDeterminant(Integer[][] matrix) {
        if (matrix.length == 1) {
            return Math.floorMod(matrix[0][0], 26);
        } else if (matrix.length == 2) {
            return Math.floorMod((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]), 26);
        } else {
            Integer det = 0;
            for (int i = 0; i < matrix.length; i++) {
                det += sign(i) * matrix[0][i] * getDeterminant(getSubMatrix(matrix, 0, i));
            }
            return Math.floorMod(det.intValue(), 26);
        }
    }

    public Integer[][] getAdjointMatrix(Integer[][] matrix) {
        Integer[][] adj = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];
        Integer[][] adjT = new Integer[keyMatrixSize.intValue()][keyMatrixSize.intValue()];

        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                adj[i][j] = sign(i) * sign(j) * getDeterminant(getSubMatrix(matrix, i, j));
            }
        }

        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                adjT[i][j] = adj[j][i];
            }
        }
        return adjT;
    }

    public void initKeyMatrixInverse() {
        Integer det = getDeterminant(keyMatrix);
        Integer detInv = modInverse(det, 26);
        if (detInv == -1) {
            System.out.println("Determinant: " + det);
            System.out.println("[ERR] Determinant Modular Inverse can't be calculated...");
            System.exit(-1);
        }
        Integer[][] adjoint = getAdjointMatrix(keyMatrix);
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < keyMatrixSize; j++) {
                keyMatrixInverse[i][j] = Math.floorMod(detInv * adjoint[i][j], 26);
            }
        }
        System.out.println("KEY MATRIX INVERSE");
        printMatrix(keyMatrixInverse, keyMatrixSize.intValue(), keyMatrixSize.intValue());
    }

    public Integer[][] matrixMultiplication(Integer[][] matrix, Integer[][] vec) {
        Integer[][] result = new Integer[keyMatrixSize.intValue()][1];
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < 1; j++) {
                result[i][j] = 0;
                for (int k = 0; k < keyMatrixSize; k++) {
                    result[i][j] += matrix[i][k] * vec[k][j];
                }
            }
        }
        for (int i = 0; i < keyMatrixSize; i++) {
            for (int j = 0; j < 1; j++) {
                int temp = result[i][j];
                result[i][j] = Math.floorMod(temp, 26);
            }
        }
        return result;
    }

    public String encrypt(String text) {
        String encrypted = "";
        // padding
        if (text.length() % keyMatrixSize.intValue() != 0) {
            for (int i = 0; i < text.length() % keyMatrixSize.intValue(); i++) {
                text += "Z";
            }
        }
        System.out.println("TEXT TO ENCRYPT:" + text);
        for (int i = 0; i < Math.floorDiv(text.length(), keyMatrixSize.intValue())
                * keyMatrixSize.intValue(); i += keyMatrixSize.intValue()) {
            Integer[][] vec = new Integer[keyMatrixSize.intValue()][1];
            for (int j = 0; j < keyMatrixSize; j++) {
                vec[j][0] = alphaToInt.get(text.charAt(i + j));
            }
            Integer[][] encryptedVec = matrixMultiplication(keyMatrix, vec);

            for (int j = 0; j < keyMatrixSize; j++) {
                encrypted += intToAlpha.get(encryptedVec[j][0]);
            }
        }
        return encrypted;
    }

    public String decrypt(String text) {
        String decrypted = "";
        for (int i = 0; i < Math.floorDiv(text.length(), keyMatrixSize.intValue())
                * keyMatrixSize.intValue(); i += keyMatrixSize.intValue()) {
            Integer[][] vec = new Integer[keyMatrixSize.intValue()][1];
            for (int j = 0; j < keyMatrixSize; j++) {
                vec[j][0] = alphaToInt.get(text.charAt(i + j));
            }
            Integer[][] decryptedVec = matrixMultiplication(keyMatrixInverse, vec);
            for (int j = 0; j < keyMatrixSize; j++) {
                decrypted += intToAlpha.get(decryptedVec[j][0]);
            }
        }
        return decrypted;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String key, text, encrypted, decrypted;
        System.out.print("ENTER KEY: ");
        key = s.next();
        Main hillCipher = new Main();
        hillCipher.initKeyMatrix(key);
        hillCipher.initKeyMatrixInverse();

        System.out.print("ENTER TEXT: ");
        text = s.next();
        encrypted = hillCipher.encrypt(text);
        System.out.println("ENCRYPTED TEXT: " + encrypted);

        decrypted = hillCipher.decrypt(encrypted);
        System.out.println("DECRYPTED TEXT: " + decrypted);
    }
}