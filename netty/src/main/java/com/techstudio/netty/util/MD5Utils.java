package com.techstudio.netty.util;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Utils {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(MD5Utils.class);

    private MD5Utils() {
    }

    public static String encrypt(File file) {
        try (InputStream in = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];//10k
            int readLen;
            while ((readLen = in.read(buffer)) != -1) {
                digest.update(buffer, 0, readLen);
            }
            return toHex(digest.digest());
        } catch (Exception e) {
            logger.error(e);
            return "";
        }
    }


    public static String encrypt(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes(StandardCharsets.UTF_8));
            return toHex(digest.digest());
        } catch (Exception e) {
            logger.error(e);
            return "";
        }
    }

    public static String encrypt(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            return toHex(digest.digest());
        } catch (Exception e) {
            logger.error(e);
            return "";
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            buffer.append(Character.forDigit((aByte & 240) >> 4, 16));
            buffer.append(Character.forDigit(aByte & 15, 16));
        }

        return buffer.toString();
    }

    public static String hmacSha1(String data, String encryptKey) {
        final String hmacSha1 = "HmacSHA1";
        try {
            SecretKeySpec signingKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), hmacSha1);
            Mac mac = Mac.getInstance(hmacSha1);
            mac.init(signingKey);
            mac.update(data.getBytes(StandardCharsets.UTF_8));
            return toHex(mac.doFinal());
        } catch (Exception e) {
            logger.error(e);
            return "";
        }
    }

    public static String sha1(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return toHex(digest.digest(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error(e);
            return "";
        }
    }
}
