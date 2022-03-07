package dev.oak3.sbs4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ByteUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteUtils.class);

    /**
     * Reverses a byte array, used to change endian
     *
     * @param bytes array of bytes to reverse
     */
    public static void reverse(byte[] bytes) {
        reverse(bytes, 0, bytes.length - 1);
    }

    /**
     * Reverses a byte array, used to change endian
     *
     * @param bytes array of bytes to reverse
     * @param from  first byte position to swap
     * @param to    last byte position to swap
     */
    public static void reverse(byte[] bytes, int from, int to) {
        LOGGER.debug("Reversing {}", bytes);
        for (int i = from; i < Math.abs((to - from) / 2f) + from; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[to - i + from];
            bytes[to - i + from] = temp;
        }
        LOGGER.debug("Reversed {}", bytes);
    }
}
