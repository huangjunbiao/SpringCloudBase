package com.huang.cloudbase.learn.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author huangjunbiao_cdv
 */
public class HashUtils {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
        'f'};

    public static char[] encodeHex(final byte[] bytes) {
        final char[] chars = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            final byte b = bytes[i / 2];
            chars[i] = HashUtils.HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HashUtils.HEX_CHARS[b & 0xf];
        }
        return chars;
    }

    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }

    public static String md5(final String src) {
        final MessageDigest md5 = HashUtils.getDigest("MD5");
        return new String(HashUtils.encodeHex(md5.digest(src.getBytes(StandardCharsets.UTF_8))));
    }

    public static String genAccessTokenSignature(final String sysId, final String userId, final String secretKey) {
        final MessageDigest md5 = HashUtils.getDigest("MD5");
        final String oo = sysId + userId + secretKey;
        return new String(HashUtils.encodeHex(md5.digest(oo.getBytes())));
    }
}
