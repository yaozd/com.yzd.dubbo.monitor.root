package com.yzd.dubbo.monitor.common.seriazle;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.serializers.FSTDateSerializer;

import java.util.Date;

/**
 * How to use Serialization (2.x)
 * https://github.com/RuedigerMoeller/fast-serialization/wiki/Serialization
 * Created by zd.yao on 2017/6/27.
 */
public class FSTSeriazle {
    final static FSTConfiguration configuration = init();

    static FSTConfiguration init() {
        FSTConfiguration configuration = FSTConfiguration
                .createDefaultConfiguration();
        configuration.registerSerializer(Date.class, new FSTDateSerializer(), false);
        return configuration;
    }

    public static byte[] serialize(Object obj) {
        return configuration.asByteArray(obj);
    }

    public static Object deserialize(byte[] sec) {
        return configuration.asObject(sec);
    }

    public static void main(String[] args) {
        String objString = "null";
        byte[] t = FSTSeriazle.serialize(objString);
        System.out.println(t);
        String obj = (String) FSTSeriazle.deserialize(t);
        System.out.println(obj);
        Date now = new Date();
        byte[] t2 = FSTSeriazle.serialize(now);
        System.out.println(t2);
        Date objDate = (Date) FSTSeriazle.deserialize(t2);
        System.out.println(objDate);
    }


}
