package be.reactiveprogramming.springrabbitmqshowcase.springamqp.binaryconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class BinaryConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromBinary(byte[] object, Class<T> resultType) {
        try {
            return objectMapper.readValue(object, resultType);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] toBinary(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
