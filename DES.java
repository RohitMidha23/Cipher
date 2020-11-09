public class DES {

    // Initial Permutation Table
    int[] IP = { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56,
            48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21,
            13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

    // Inverse Initial Permutation Table
    int[] IP1 = { 40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5,
            45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18,
            58, 26, 33, 1, 41, 9, 49, 17, 57, 25 };

    // 64 bits goes through a permutation called PC-1(permuted choice) resulting 56
    // bits.

    int[] PC1 = { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52,
            44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20,
            12, 4 };

    // second key-Permutation Table (compression permutation 56bit key --> 48 bit
    // key )
    int[] PC2 = { 14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31,
            37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 };

    // Expansion D-box Table
    int[] EP = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20,
            21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

    // Straight Permutation Table
    int[] P = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6,
            22, 11, 4, 25 };

    // S-box Table
    int[][][] sbox = {
            { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },

            { { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
            { { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
            { { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
            { { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
            { { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
            { { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
            { { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } };
    int[] shiftBits = { 1, 1, 2, 2, 2, 2, 2, 2, // 1,2,9,16 1 bit shift ; others 2 bit shift
            1, 2, 2, 2, 2, 2, 2, 1 };

    String hexToString(String hex_string) {
        StringBuilder str = new StringBuilder();
        String temp = "";
        for (int i = 0; i < hex_string.length(); i += 2) {
            temp = hex_string.substring(i, i + 2);
            str.append((char) Integer.parseInt(temp, 16));
        }
        return str.toString();
    }

    String stringToHex(String cipher_txt) {
        int x = 0;
        String temp1 = "", temp = "";
        for (int i = 0; i < cipher_txt.length(); i++) {
            temp = "";
            x = (int) cipher_txt.charAt(i);
            if (Integer.toHexString(x).length() < 2)
                temp += '0';
            temp += Integer.toHexString(x);
            temp1 += temp;
        }

        return temp1;
    }

    String hextoBin(String hex) {
        String temp = "";
        String result = "";
        char c;
        for (int i = 0; i < hex.length(); i++) {
            c = hex.charAt(i);
            temp = Integer.toBinaryString(Integer.parseInt(c + "", 16)) + "";
            for (int j = 0; j < (4 - temp.length()); j++)
                result += '0';
            result += temp;
            temp = "";
        }
        return result + "";
    }

    String binToHex(String bin) {
        String temp = "", result = "";
        for (int i = 0; i < bin.length(); i += 4) {
            temp = bin.substring(i, i + 4);
            temp = Integer.parseInt(temp, 2) + "";
            result += Integer.toString(Integer.parseInt(temp), 16);
        }
        return result;
    }

    String permutation(int[] sequence, String input) {
        String output = "";
        input = hextoBin(input);
        for (int i = 0; i < sequence.length; i++)
            output += input.charAt(sequence[i] - 1);
        output = binToHex(output);
        return output;
    }

    // xor 2 hexadecimal strings
    String xor(String a, String b) {
        // hexaecimal to decimal(base 10)
        long t_a = Long.parseUnsignedLong(a, 16);
        // hexadecimal to decimal(base 10)
        long t_b = Long.parseUnsignedLong(b, 16);
        // xor
        t_a = t_a ^ t_b;
        // decimal to hexadecimal
        a = Long.toHexString(t_a);
        // prepend 0's to maintain length
        while (a.length() < b.length())
            a = "0" + a;
        return a;
    }

    // left Circular Shifting bits
    String leftCircularShift(String input, int numBits) {
        int n = input.length() * 4;
        int perm[] = new int[n];
        for (int i = 0; i < n - 1; i++) // if n=8 i ranges from 0 to 6 (7 numbers)
            perm[i] = (i + 2); // 2,3,4,5,6,7,1
        perm[n - 1] = 1;
        while (numBits > 0) {
            input = permutation(perm, input);
            numBits = numBits - 1;
        }
        return input;
    }

    // preparing 16 keys for 16 rounds
    String[] getKeys(String key) {
        String keys[] = new String[16];
        // first key permutation
        key = permutation(PC1, key); // to get 56bit key(partity bits dropped)
        for (int i = 0; i < 16; i++) {
            key = leftCircularShift(key.substring(0, 7), shiftBits[i])
                    + leftCircularShift(key.substring(7, 14), shiftBits[i]);
            // second key permutation
            keys[i] = permutation(PC2, key);
        }
        return keys;
    }

    // s-box lookup
    String sBox(String input) {
        String output = "";
        input = hextoBin(input);
        for (int i = 0; i < 48; i += 6) {
            String temp = input.substring(i, i + 6);
            int num = i / 6;
            int row = Integer.parseInt(temp.charAt(0) + "" + temp.charAt(5), 2);
            int col = Integer.parseInt(temp.substring(1, 5), 2);
            output += Integer.toHexString(sbox[num][row][col]);
        }
        return output;
    }

    String round(String input, String key, int num) {
        String left = input.substring(0, 8);
        String temp = input.substring(8, 16);
        String right = temp;
        // Expansion permutation
        temp = permutation(EP, temp);
        // xor temp and round key
        temp = xor(temp, key);
        // lookup in s-box table
        temp = sBox(temp);
        // Straight D-box
        temp = permutation(P, temp);
        // xor
        left = xor(left, temp);

        // swapper
        return right + left;
    }

    String encrypt(String plainText, String key) {
        String temp1 = "", cipher_text = "";
        int val = plainText.length() / 16; // 16 hex chars = 8 bytes = 64 bits
        if (plainText.length() % 16 != 0) {
            val += 1;
        }
        for (int i = 0; i < val; i++) {
            try {
                temp1 = plainText.substring(i * 16, (i + 1) * 16);
            } catch (Exception e) {
                temp1 = plainText.substring(i * 16, plainText.length());
            }
            if (temp1.length() != 16) {
                for (int j = 0; j < temp1.length() % 16; j++)
                    temp1 += 0;

            }
            // System.out.println("Taking the 8 bytes:"+temp1);
            cipher_text += encryptBlock(temp1, key);
        }
        return cipher_text;

    }

    String encryptBlock(String plainText, String key) { // plainText and key in HEX
        int i;
        // get round keys
        String keys[] = getKeys(key);

        // initial permutation
        plainText = permutation(IP, plainText);
        // System.out.println( "After initial permutation: "+ plainText.toUpperCase());
        // System.out.println( "After splitting: L0="+ plainText.substring(0,
        // 8).toUpperCase() + " R0="+ plainText.substring(8, 16).toUpperCase() + "\n");

        // 16 rounds
        // System.out.println("Round No Left Right RoundKey");
        for (i = 0; i < 16; i++) {
            plainText = round(plainText, keys[i], i);
            // if(i<9)
            // { System.out.println("Round "+(i+1)+" "+plainText.substring(0,8)+"
            // "+plainText.substring(8,16)+" "+keys[i]);
            // }
            // else{
            // System.out.println("Round "+(i+1)+" "+plainText.substring(0,8)+"
            // "+plainText.substring(8,16)+" "+keys[i]);

            // }
        }

        // 32-bit swap
        plainText = plainText.substring(8, 16) + plainText.substring(0, 8);

        // final permutation
        plainText = permutation(IP1, plainText);
        return plainText;
    }

    String decrypt(String encryptedText, String key) {
        String temp1 = "", plainText = "";
        int val = encryptedText.length() / 16;
        for (int round = 0; round < val; round++) {
            temp1 = encryptedText.substring(round * 16, (round + 1) * 16);

            // System.out.println("Taking the 8 bytes:"+temp1);
            plainText += decryptBlock(temp1, key);
        }
        return plainText;

    }

    String decryptBlock(String plainText, String key) {
        int i;
        // get round keys
        String keys[] = getKeys(key);

        // initial permutation
        plainText = permutation(IP, plainText);
        // System.out.println( "After initial permutation: "+ plainText.toUpperCase());
        // System.out.println( "After splitting: L0="+ plainText.substring(0,
        // 8).toUpperCase() + " R0=" + plainText.substring(8, 16).toUpperCase() + "\n");

        // 16-rounds
        // System.out.println("Round No Left Right RoundKey");
        for (i = 15; i > -1; i--) {
            plainText = round(plainText, keys[i], 15 - i);
            // if(15-i<9)
            // { System.out.println("Round "+(16- i)+" "+plainText.substring(0,8)+"
            // "+plainText.substring(8,16)+" "+keys[i]);
            // }
            // else{
            // System.out.println("Round "+(16-i)+" "+plainText.substring(0,8)+"
            // "+plainText.substring(8,16)+" "+keys[i]);

            // }
        }

        // 32-bit swap
        plainText = plainText.substring(8, 16) + plainText.substring(0, 8);
        plainText = permutation(IP1, plainText);
        return plainText;
    }

    public static void main(String args[]) {
        DES des = new DES();
        System.out.println("==============DES==============");
        System.out.println("Enter Plain Text(STRING):");
        String msg = System.console().readLine();
        String msgHex = des.stringToHex(msg);// .toUpperCase();
        System.out.println("Plain Text in Hex:" + msgHex);
        // System.out.println("Plain Text Hex in Binary:"+des.hextoBin(msgHex));
        System.out.println("Enter Key:");
        String k = System.console().readLine();
        System.out.println("ENCRYPTION:");
        String encryptedText = des.encrypt(msgHex, k);
        System.out.println("Cipher Text: " + encryptedText.toUpperCase());
        System.out.println("DECRYPTION:");
        String decryptedText = des.decrypt(encryptedText, k);
        System.out.println("Plain Text in Hex: " + decryptedText.toUpperCase());
        System.out.println("Plain Text: " + des.hexToString(decryptedText));
        System.out.println("==========DOUBLE DES==========");
        System.out.println("Enter Key 1:");
        String k1 = System.console().readLine();
        System.out.println("Enter Key 2:");
        String k2 = System.console().readLine();
        System.out.println("ENCRYPTION:");
        String encryptedText1 = des.encrypt(msgHex, k1);
        String encryptedText2 = des.encrypt(encryptedText1, k2);
        System.out.println("Cipher Text: " + encryptedText2.toUpperCase());
        System.out.println("DECRYPTION:");
        String decryptedText1 = des.decrypt(encryptedText2, k2);
        String decryptedText2 = des.decrypt(decryptedText1, k1);
        System.out.println("Plain Text in Hex: " + decryptedText2.toUpperCase());
        System.out.println("Plain Text: " + des.hexToString(decryptedText2));
        System.out.println("==========TRIPLE DES==========");
        System.out.println("Enter Key 1:");
        String key1 = System.console().readLine();
        System.out.println("Enter Key 2:");
        String key2 = System.console().readLine();
        System.out.println("Enter Key 3:");
        String key3 = System.console().readLine();
        System.out.println("ENCRYPTION:");
        encryptedText1 = des.encrypt(msgHex, key1);
        encryptedText2 = des.decrypt(encryptedText1, key2);
        String encryptedText3 = des.encrypt(encryptedText2, key3);
        System.out.println("Cipher Text: " + encryptedText3.toUpperCase());
        System.out.println("DECRYPTION:");
        decryptedText1 = des.decrypt(encryptedText3, key3);
        decryptedText2 = des.encrypt(decryptedText1, key2);
        String decryptedText3 = des.decrypt(decryptedText2, key1);
        System.out.println("Plain Text in Hex: " + decryptedText3.toUpperCase());
        System.out.println("Plain Text: " + des.hexToString(decryptedText3));

    }
}