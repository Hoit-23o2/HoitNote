package com.example.hoitnote.utils.helpers;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ConvertHelper {
    /**
     * Im可序列化object转比特流
     * sendObejct中会用到
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj)
    {
        byte[] bytes = null;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(b);
            oos.writeObject(obj);
            bytes = b.toByteArray();
            //oos.flush();
        }
        catch(Exception e)
        {
            Log.e("Bluetooth", "Cast exception at sending end ...");
        }

        return bytes;
    }

    /**
     * Im比特流转可序列化对象
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes)
    {
        Object obj = null;
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        }
        catch(Exception ex)
        {
            Log.e("Bluetooth", "Cast exception at receiving end ...");
        }
        return obj;
    }


    /*
     * 二进制转整型的String
     * 暂时没用到
     * */
    public static String bytesToIntString(byte[] bytes) {
        String result = "";
        for (byte aByte : bytes) {
            String hexString = Integer.toString(aByte & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }
}
