import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Scanner;

public class dss {
   PublicKey pubk;
   private PrivateKey prvk;

   dss() throws NoSuchAlgorithmException
   {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
      kpg.initialize(2048); 
      KeyPair kp = kpg.generateKeyPair();
      pubk = kp.getPublic();
      prvk = kp.getPrivate();
   }
   public String createSignature(String text)
   {  try{
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initSign(prvk);
            byte[] bytes = text.getBytes();
            sign.update(bytes);
            byte[] signature = sign.sign();
            return bytesToHex(signature);
      }
      catch(Exception e)
      {
         System.out.println("[ERR]:"+e.getMessage());
         return "";
      }
      
   }
   public String verifySignature(String text,String signatureReceived)
   {   try{ 
         
         Signature sign = Signature.getInstance("SHA256withDSA");
         byte[] bytes = text.getBytes();
         sign.initVerify(pubk);
         sign.update(bytes);
         boolean bool = sign.verify(hextoBytes(signatureReceived));
         if(bool==true)
            return "Signature Verified!";
         else
           return "Signature Verification Failed...";
      }
      catch(Exception e)
      {
         System.out.println("[ERR]:"+e.getMessage());
         return "";
      }

   }
   private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
   public static String bytesToHex(byte[] bytes) {
      char[] hexChars = new char[bytes.length * 2];
      for ( int j = 0; j < bytes.length; j++ ) {
          int v = bytes[j] & 0xFF;
          hexChars[j * 2] = hexArray[v >>> 4];
          hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
  }
  public static byte[] hextoBytes(String hexString)
  {       byte[] val = new byte[hexString.length() / 2];
         for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            val[i] = (byte) j;
         }

         return val;
  }
   public static void main(String args[]) throws Exception {
      Scanner sc = new Scanner(System.in);
      dss DSS = new dss();

      System.out.print("Enter plain text: ");
      String text = sc.nextLine();
      String signature = DSS.createSignature(text);
      System.out.println("DIGITAL SIGNATURE IN HEX:"+ signature);
      
      System.out.println("Running Verification Algorithm on original data and signature...");
      System.out.println(DSS.verifySignature(text, signature));
      System.out.println("Running Verification Algorithm on data as 'sample_fake_data' and signature...");
      System.out.println(DSS.verifySignature("sample_fake_data", signature));

      sc.close();
   }
}