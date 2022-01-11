package com.catherine.libnetwork.cache;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

public class CacheManager {
    public static final String TAG = "Class: CacheManager";
    public static <T> void save(String key, T body) {
        Log.d(TAG, "save(): "+key);

        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        Log.d(TAG, "save():cache.data =toByteArray(body) ");
        CacheDatabase.get().getCache().save(cache);
        Log.d(TAG, "save(): CacheDatabase.get().getCache().save(cache) ");

    }

    public static Object getCache(String key) {
        Log.d(TAG, "getCache():");

        Cache cache = CacheDatabase.get().getCache().getCache(key);
        Log.d(TAG, "getCache: CacheDatabase.get().getCache().getCache(key)");
        if (cache != null && cache.data != null) {
            return toObject(cache.data);
        }
        return null;
    }

    private static Object toObject(byte[] data) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(data);
            // 创建从指定 InputStream 读取的 ObjectInputStream。
            ois = new ObjectInputStream(bais);
            // 读取保存对象的文件,反序列化为原来的对象
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static <T> byte[] toByteArray(T body) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            //将指定的对象body进行序列化写入字节数组输出
            oos.writeObject(body);
            oos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }


}
