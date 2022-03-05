package dev.oak3.sbs4j.exception;

/**
 * Thrown in case of an invalid (unparseable) serialized byte string
 *
 * @since 0.1.0
 */
public class InvalidByteStringException extends ValueDeserializationException {
    public InvalidByteStringException(String message) {
        super(message);
    }
}
