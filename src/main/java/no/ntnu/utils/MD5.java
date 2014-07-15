package no.ntnu.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

public class MD5 {
    private final AtomicLong al = new AtomicLong(1);
    private static final MD5 instance = new MD5();

    public static String hash(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(string.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            string = byteArrToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    private static String byteArrToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte aBArr : bArr) {
            int unsigned = aBArr & 0xff;
            if (unsigned < 0x10)
                sb.append("0");
            sb.append(Integer.toHexString(unsigned));
        }
        return sb.toString();
    }

    public static long generateID() {
        /*long retval;
        synchronized (al) {
            retval=al.incrementAndGet();
        }
        return retval;*/
        return instance.al.incrementAndGet();
    }
}

