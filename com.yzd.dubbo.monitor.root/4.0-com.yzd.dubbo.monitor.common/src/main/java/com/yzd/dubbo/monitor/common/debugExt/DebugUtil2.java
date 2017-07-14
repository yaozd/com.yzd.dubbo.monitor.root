package com.yzd.dubbo.monitor.common.debugExt;

import com.yzd.dubbo.monitor.common.seriazle.FastJsonUtil;

/**
 * Created by zd.yao on 2017/7/14.
 */
public class DebugUtil2 {
    public static void println(String message,Object obj){
        System.out.println(message);
        System.out.println(FastJsonUtil.serialize(obj));
        System.out.println();
        System.out.println("//////////////////////");
    }
}
