package no.ntnu.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

/**
 * The utility class for working with message digests.
 * <p/>
 * The first version supports only password-based encryption based on DES algorithm.
 * <p>
 *  Added support for computing SHA1 hash for a given string.
 * </p>
 * @author <a href="mailto:naimdjon@gmail.com">Naimdjon Takhirov</a>
 * @version $Id: Encryption.java 64653 2011-04-27 09:07:51Z johnarne $
 * @since 16.03.2012
 */
public class Encryption implements  java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1890284093101651567L;
	/**
     * The password phrase to ecrypt/decrypt strings
     */
    private String passPhrase;

    /**
     * Constructor initialized with the specified password.
     *
     * @param password the password phrase to be used for ecnryption/decryption.
     */
    public Encryption(String password) {
        if(password==null)throw new NullPointerException("Password cannot be null");
        this.passPhrase = password;
    }

    /**
     * @return
     */
    public static synchronized String gen6Pass() {
        java.util.Random random = new java.util.Random();
        int count = 6;
        StringBuffer s = new StringBuffer();
        int end = 'z' + 1;
        int start = ' ';
        int gap = end - start;
        while (count-- != 0) {
            char c = (char) (random.nextInt(gap) + start);
            if (Character.isLetterOrDigit(c))
                s.append(c);
            else
                count++;
        }
        return s.toString();
    }

    /**
     * Returns default <tt>Encryption</tt> instance with default passphrase.
     *
     * @return Encryption instance
     */
    public static Encryption create() {
        return new Encryption(gen6Pass());
    }
    private byte[] getSalt() {
        byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
        return salt;
    }

    /**
     * Encrypts this string using <code>DES</code> algorithm.
     *
     * @param str string to encrypt.
     * @return the encrypted String
     * @throws InvalidKeyException
     */
    public String encrypt(String str) {
        try {
            byte[] salt = getSalt();
            int iterationCount = 19;
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            Cipher cip = Cipher.getInstance(key.getAlgorithm());
            cip.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            byte[] enc = cip.doFinal(str.getBytes("UTF8"));
            return Base64.encodeBytes(enc);
            //return new sun.misc.BASE64Encoder().encode(enc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypts this string using <code>DES</code> algorithm.
     *
     * @param str string to be decrypted
     * @return return decrypted String.
     * @throws InvalidKeyException
     * @throws GeneralSecurityException
     */
    public String decrypt(String str) throws RuntimeException {
        try {
        byte[] salt =getSalt();
        int iterationCount = 19;
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        Cipher cip = Cipher.getInstance(key.getAlgorithm());
        cip.init(Cipher.DECRYPT_MODE, key, paramSpec);

            //byte[] bytes = cip.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(str));
            byte[] bytes = cip.doFinal(Base64.decode(str));
            return new String(bytes, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (byte aData : data) {
            int halfbyte = (aData >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = aData & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Computes the message digest hash for the given string.
     * @param text
     * @return
     * @throws RuntimeException
     */
    public static String digest(String text) throws RuntimeException{
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("utf-8"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
