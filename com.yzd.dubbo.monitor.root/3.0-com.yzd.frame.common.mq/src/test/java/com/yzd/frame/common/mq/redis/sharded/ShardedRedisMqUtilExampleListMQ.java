package com.yzd.frame.common.mq.redis.sharded;

import org.junit.Test;

/**
 * Created by zd.yao on 2017/6/27.
 */
public class ShardedRedisMqUtilExampleListMQ {
    String key="ShardedRedisMqUtilExampleListMQ";

    /**
     * 列表头部-插入
     * 将一个值插入到列表头部，value可以重复，返回列表的长度
     */
    @Test
    public void lpushExample() {
        ShardedRedisMqUtil redisUtil = ShardedRedisMqUtil.getInstance();
        for (int i=0;i<100;i++){
            Long result = redisUtil.lpush(key, "2017-03-07:"+i);
            System.out.println(result);
        }
    }

    /**
     * 列表头部-读取
     * 移出并获取列表的第一个元素，当列表不存在或者为空时，返回Null
     */
    @Test
    public void lpopExample1() {
        ShardedRedisMqUtil redisUtil = ShardedRedisMqUtil.getInstance();
        //String result= redisUtil.lpop(key);
        //System.out.println(result);
        for (int i=0;i<10000;i++){
            String result= redisUtil.lpop(key);
            System.out.println(result);
        }
    }
    /**
     * 列表头部-读取
     * 阻塞列表
     * 移出并获取列表的【第一个元素】， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    @Test
    public void blpopExample() {
        ShardedRedisMqUtil redisUtil = ShardedRedisMqUtil.getInstance();
        //String result= redisUtil.lpop(key);
        //System.out.println(result);
        for (int i=0;i<10000;i++){
            String result= redisUtil.blpop(5,key);
            System.out.println(result);
        }
    }

    /**
     * 列表尾部-读取
     * 移除并获取列表最后一个元素，当列表不存在或者为空时，返回Null
     */
    @Test
    public void rpopExample1() {
        ShardedRedisMqUtil redisUtil = ShardedRedisMqUtil.getInstance();
        //String result= redisUtil.lpop(key);
        //System.out.println(result);
        for (int i=0;i<10000;i++){
            String result= redisUtil.rpop(key);
            System.out.println(result);
        }
    }

    /**
     * 列表尾部-读取
     * 阻塞列表
     * 移出并获取列表的【最后一个元素】， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    @Test
    public void brpopExample() {
        ShardedRedisMqUtil redisUtil = ShardedRedisMqUtil.getInstance();
        //String result= redisUtil.lpop(key);
        //System.out.println(result);
        for (int i=0;i<10000; i++) {
            String result= redisUtil.brpop(5,key);
            System.out.println(result);
        }
    }
}

