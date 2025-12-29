package ProtocoleMRPS;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

public class MyCrypto {
    public static byte[] CryptSymAES(SecretKey cle, byte[] data) throws Exception {
        Cipher chiffrement = Cipher.getInstance("Rijndael/CBC/PKCS5Padding", "BC");
        byte[] vecteurInit = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(vecteurInit);
        IvParameterSpec iv = new IvParameterSpec(vecteurInit);

        chiffrement.init(Cipher.ENCRYPT_MODE, cle, iv);
        byte[] cipher = chiffrement.doFinal(data);

        // on concatène IV + données chiffrées pour pouvoir déchiffrer côté serveur
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(vecteurInit);
        baos.write(cipher);
        return baos.toByteArray();
    }

    public static byte[] DecryptSymAES(SecretKey cle, byte[] data) throws Exception {
        byte[] ivBytes = Arrays.copyOfRange(data, 0, 16);
        byte[] cipher = Arrays.copyOfRange(data, 16, data.length);

        Cipher chiffrement = Cipher.getInstance("Rijndael/CBC/PKCS5Padding", "BC");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        chiffrement.init(Cipher.DECRYPT_MODE, cle, iv);
        return chiffrement.doFinal(cipher);
    }

    // Chiffrement asymétrique RSA (pour la clé publique)
    public static byte[] CryptAsymRSA(PublicKey cle, byte[] data) throws Exception {
        Cipher chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrement.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrement.doFinal(data);
    }

    // Déchiffrement asymétrique RSA (pour la clé privée)
    public static byte[] DecryptAsymRSA(PrivateKey cle, byte[] data) throws Exception {
        Cipher chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        chiffrement.init(Cipher.DECRYPT_MODE, cle);
        return chiffrement.doFinal(data);
    }
}
