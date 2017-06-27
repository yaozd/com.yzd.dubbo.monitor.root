package com.yzd.dubbo.monitor.common.seriazle;

import org.nustaq.serialization.FSTConfiguration;

/**
 * Created by zd.yao on 2017/6/27.
 */
public class FSTSeriazle {
    static FSTConfiguration configuration = FSTConfiguration
            // .createDefaultConfiguration();
            .createStructConfiguration();

    public static byte[] serialize(Object obj) {
        return configuration.asByteArray(obj);
    }

    public static Object deserialize(byte[] sec) {
        return configuration.asObject(sec);
    }
    public static void main(String[] args){
        String objString="null";
        byte[] t= FSTSeriazle.serialize(objString);
        System.out.println(t);
        String obj= (String) FSTSeriazle.deserialize(t);
        System.out.println(obj);
    }


}
