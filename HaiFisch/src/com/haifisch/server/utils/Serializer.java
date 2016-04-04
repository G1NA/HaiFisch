package com.haifisch.server.utils;

import java.io.*;

/**
 *
 */

public class Serializer {

    /**
     * Serialize an object
     *
     * @param obj The object to be serialized
     * @return the byte array that contains the object data
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            out.flush();
            return bos.toByteArray();
        }
    }

    /**
     * Deserialize an object
     *
     * @param bytes The object data
     * @return The deserialized object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }
}
