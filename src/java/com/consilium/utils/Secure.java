package com.consilium.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * MD5 Secure
 */

public class Secure {
    private static class SecureHolder {
        private static Secure instance = new Secure();

        public static Secure getInstance() {
            return SecureHolder.instance;
        }
    }

    public static Secure getInstance() {
        return SecureHolder.getInstance();
    }

    public String digestMD5(String message) {
        return getDigest(message, "MD5");
    }

    public byte[] digestMD5(byte[] message) {
        return getDigest(message, "MD5");
    }

    // MD2,MD5,SHA-1,SHA-256,SHA-384,SHA-512

    public String getDigest(String message, String mdName) {
        if (message == null)
            return "";
        byte[] output = getDigest(message.getBytes(), mdName);
        return toHex(output);
    }

    public byte[] getDigest(byte[] message, String mdName) {
        if (message == null || message.length == 0)
            return null;
        MessageDigest md = null;
        if (mdName == null || mdName.length() == 0)
            mdName = "MD5";
        try {
            md = MessageDigest.getInstance(mdName);
        } catch (NoSuchAlgorithmException e) {
            return new byte[]{};
        }
        md.reset();
        md.update(message);
        return md.digest();
    }

    public String toHex(byte buffer[]) {
        if (buffer == null)
            return "";
        StringBuffer sb = new StringBuffer();
        String s = null;
        for (int i = 0; i < buffer.length; i++) {
            s = Integer.toHexString((int) buffer[i] & 0xff);
            if (s.length() < 2) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
