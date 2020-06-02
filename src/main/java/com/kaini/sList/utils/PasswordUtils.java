package com.kaini.sList.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Encrypt password tool
 */
public class PasswordUtils {


    /**
     * JAVA6 supports any of the following algorithms
     * PBEWITHMD5ANDDES
     * PBEWITHMD5ANDTRIPLEDES
     * PBEWITHSHAANDDESEDE
     * PBEWITHSHA1ANDRC2_40
     * PBKDF2WITHHMACSHA1
     * */

    /**
     * The algorithm used is defined as: PBEWITHMD5andDES algorithm
     */
    public static final String ALGORITHM = "PBEWithMD5AndDES";

    /**
     * Define 1000 iterations
     */
    private static final int ITERATIONCOUNT = 1000;

    /**
     * Get the salt value that is used in the encryption algorithm.
     * The salt value that is used in decryption must be the same value used in encryption to complete the operation.
     * <p>
     * Salt length must be 8 bytes
     *
     * @return byte[] salt
     */
    public static byte[] getSalt() throws Exception {
        // Instantiate a secure random number
        SecureRandom random = new SecureRandom();
        // Output salt
        return random.generateSeed(8);
    }

    /**
     * Generate a key based on the PBE password
     *
     * @param password The password used when generating the key
     * @return Key PBE Algorithm key
     */
    private static Key getPBEKey(String password) throws Exception {
        //Algorithm used for instantiation
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        //Set PBE key parameters
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        // Generate key
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        return secretKey;
    }

    /**
     * Encrypted plaintext string
     *
     * @param plaintext Plaintext string to be encrypted
     * @param password  The password used when generating the key
     * @param salt      Salt
     * @return Encrypted ciphertext string
     * @throws Exception
     */
    public static String encrypt(String plaintext, String password, byte[] salt) throws Exception {
        Key key = getPBEKey(password);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte encipheredData[] = cipher.doFinal(plaintext.getBytes());
        return bytesToHexString(encipheredData);
    }

    /**
     * Decrypting a ciphertext string
     *
     * @param ciphertext The ciphertext string to be decrypted
     * @param password   The password that is used to generate the key (for decryption, this parameter needs to be the same as that used for encryption)
     * @param salt       Salt value (for decryption, this parameter needs to be the same as that used for encryption)
     * @return Decrypted plaintext string
     * @throws Exception
     */
    public static String decrypt(String ciphertext, String password, byte[] salt) throws Exception {
        Key key = getPBEKey(password);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] passDec = cipher.doFinal(hexStringToBytes(ciphertext));
        return new String(passDec);
    }

    /**
     * Convert byte array to hex string
     *
     * @param src Byte array
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte array
     *
     * @param hexString Hex String
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}