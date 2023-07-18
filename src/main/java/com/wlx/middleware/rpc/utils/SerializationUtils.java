package com.wlx.middleware.rpc.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {

    private static Map<Class<?>, Schema<?>> schemaMap = new HashMap<>();

    private static Objenesis objenesis = new ObjenesisStd();

    public static <T> byte[] serialize(T t) {
        Class<T> clazz = (Class<T>) t.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(t, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            T t = objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, t, schema);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        }

    }


    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(clazz);
            schemaMap.put(clazz, schema);
        }
        return schema;
    }


}
