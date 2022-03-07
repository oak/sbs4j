package dev.oak3.sbs4j;

import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Deserializing methods
 *
 * @since 0.1.0
 */
public class DeserializerBuffer {

    private final ByteBuffer buffer;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializerBuffer.class);

    private static final String LOG_BUFFER_INIT_MESSAGE_HEX_STRING = "Initializing with hexString: {} and byte order {}";
    private static final String LOG_BUFFER_INIT_MESSAGE = "Initializing with bytes: {} and byte order {}";
    private static final String LOG_BUFFER_VALUE_MESSAGE_STRING = "Buffer value: {}";
    private static final String LOG_SERIALIZED_VALUE_MESSAGE_STRING = "Deserialized value for {}: {}";
    private static final String SERIALIZE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING = "Value %s out of bounds for expected type %s";

    /**
     * Initializes buffer with serialized bytes from hex-encoded {@link String}
     *
     * @param hexString hex-encoded {@link String} to deserialize and read from
     */
    public DeserializerBuffer(String hexString) {
        this(hexString.length() != 0 ? new BigInteger(hexString, 16).toByteArray() : new byte[]{});
    }

    /**
     * Initializes buffer with serialized bytes from hex-encoded {@link String}
     *
     * @param hexString hex-encoded {@link String} to deserialize and read from
     * @param byteOrder the byte order to be using
     */
    public DeserializerBuffer(String hexString, ByteOrder byteOrder) {
        this(hexString.length() != 0 ? new BigInteger(hexString, 16).toByteArray() : new byte[]{}, byteOrder);

        LOGGER.debug(LOG_BUFFER_INIT_MESSAGE_HEX_STRING, hexString, byteOrder);
    }

    /**
     * Initializes buffer with serialized bytes and {@link ByteOrder#LITTLE_ENDIAN}
     *
     * @param bytes byte array to deserialize and read from
     */
    public DeserializerBuffer(byte[] bytes) {
        this(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Initializes buffer with serialized bytes and byte order
     *
     * @param bytes     byte array to deserialize and read from
     * @param byteOrder the byte order to be using
     */
    public DeserializerBuffer(byte[] bytes, ByteOrder byteOrder) {
        this.buffer = ByteBuffer.wrap(bytes);
        this.buffer.order(byteOrder);
        this.buffer.mark();

        LOGGER.debug(LOG_BUFFER_INIT_MESSAGE, Arrays.toString(bytes), byteOrder);
    }

    /**
     * Reads a Boolean value
     *
     * @return true if 1, while false if 0
     * @throws ValueDeserializationException exception holding information of failure to deserialize a value
     */
    public Boolean readBool() throws ValueDeserializationException {
        byte buf = this.buffer.get();

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        if (buf == 1) {
            return true;
        } else if (buf == 0) {
            return false;
        } else {
            throw new ValueDeserializationException(
                    String.format(SERIALIZE_EXCEPTION_OUT_OF_BOUNDS_MESSAGE_STRING, buf, Boolean.class.getSimpleName()));
        }
    }

    /**
     * Reads a byte from buffer
     *
     * @return the byte
     */
    public byte readU8() {
        byte u8 = this.buffer.get();

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), u8);

        return u8;
    }

    /**
     * Reads a byte[] from buffer
     *
     * @param length the length of the array
     * @return the byte array as byte[]
     */
    public byte[] readByteArray(int length) {
        byte[] bytes = readBytes(length);

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Byte.class.getSimpleName(), bytes);

        return bytes;
    }

    /**
     * Reads a float of 32 bits (4 bytes)
     *
     * @return the number as a float
     */
    public float readF32() {
        float floatNumber = this.buffer.getFloat();

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Float.class.getSimpleName(), floatNumber);

        return floatNumber;
    }

    /**
     * Reads a float of 64 bits (8 bytes)
     *
     * @return the number as a double
     */
    public double readF64() {
        double doubleNumber = this.buffer.getDouble();

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Double.class.getSimpleName(), doubleNumber);

        return doubleNumber;
    }

    /**
     * Reads a signed int of 32 bits (4 bytes)
     *
     * @return the number as an int
     */
    public int readI32() {
        int integerNumber = this.buffer.getInt();

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Integer.class.getSimpleName(), integerNumber);

        return integerNumber;
    }

    /**
     * Reads an unsigned int of 32 bits (4 bytes)
     *
     * @return the number as a long
     */
    public long readU32() {
        long unsignedInteger = this.buffer.getLong();

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), unsignedInteger);

        return unsignedInteger;
    }

    /**
     * Reads a signed int of 64 bits (8 bytes)
     *
     * @return the number as a long
     */
    public long readI64() {
        long longNumber = this.buffer.getLong();

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, Long.class.getSimpleName(), longNumber);

        return longNumber;
    }

    /**
     * Reads an unsigned int of 64 bits (8 bytes)
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU64() {
        // Since this is a positive (unsigned) number, we should prefix with a zero
        // byte to parse correctly
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.put((byte) 0);
        bb.putLong(this.buffer.getLong());
        BigInteger unsignedLong = new BigInteger(bb.array());

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), unsignedLong);

        return unsignedLong;
    }

    /**
     * Reads an unsigned int of 128 bits (16 bytes) max
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU128() {
        return this.readBigInteger();
    }

    /**
     * Reads U256 from buffer
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU256() {
        return this.readBigInteger();
    }

    /**
     * Reads U512 from buffer
     *
     * @return the number as a BigInteger
     */
    public BigInteger readU512() {
        return this.readBigInteger();
    }

    /**
     * Larger numeric values (e.g., U128, U256, U512) serialize as one byte of the
     * length of the next number, followed by the two’s complement representation
     * with little-endian byte order.
     *
     * @return the number as a BigInteger
     */
    protected BigInteger readBigInteger() {
        byte lengthOfNextNumber = this.buffer.get();

        LOGGER.debug("Length of next number: {}", lengthOfNextNumber);

        byte[] buf = new byte[lengthOfNextNumber + 1];

        this.buffer.get(buf, 1, lengthOfNextNumber);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        if (this.buffer.order() == ByteOrder.LITTLE_ENDIAN) {
            ByteUtils.reverse(buf, 1, buf.length - 1);
        }

        BigInteger bigInt = new BigInteger(buf);

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, BigInteger.class.getSimpleName(), bigInt);

        return bigInt;
    }

    /**
     * Reads a String value from buffer
     *
     * @return the String read
     */
    public String readString() {
        int length = this.buffer.getInt();

        LOGGER.debug("Reading string of length: {}", length);

        byte[] bufString = new byte[length];

        this.buffer.get(bufString, 0, length);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, bufString);

        String string = new String(bufString);

        LOGGER.debug(LOG_SERIALIZED_VALUE_MESSAGE_STRING, String.class.getSimpleName(), string);

        return string;
    }

    /**
     * Reads a specified number of bytes
     *
     * @param length the number of bytes to read
     * @return bytes read
     */
    protected byte[] readBytes(int length) {
        byte[] buf = new byte[length];

        this.buffer.get(buf, 0, length);

        LOGGER.debug(LOG_BUFFER_VALUE_MESSAGE_STRING, buf);

        return buf;
    }
}
