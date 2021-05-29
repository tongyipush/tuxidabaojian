package com.atguigu.redis;

import redis.clients.jedis.*;

import javax.swing.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisUtil {
    public static void main(String[] args) {
        final JedisCluster jeidisCluster = RedisUtil.getJedisCluster();
        //jeids = new Jedis("hadoop102", 6379);
        //System.out.println(pong);
        //final Set<String> keyset = jeids.keys("*");
        /*for (String key : keyset) {
            System.out.println(key);

        final Map<String, String> usermap = jeids.hgetAll("user:1010");
        for (Map.Entry<String, String> entry : usermap.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());

        }

        final Set<Tuple> topTuple  = jeids.zrevrangeWithScores("z1", 0, 3);
        for (Tuple tuple : topTuple) {
            System.out.println(tuple.getElement()+" "+tuple.getScore());
        }

        jeids.set("k2000","v2000");

        System.out.println(jeids.ping());
        //todo 关闭资源
        jeids.close();

         */
        jeidisCluster.set("k250","v250");
        System.out.println(jeidisCluster.get("k250"));
    }
    //todo 更改此处，哇哈哈哈 星期六，版本更新
    private static JedisPool jedisPool = null;
    private static JedisSentinelPool jedisSentinelPool = null;
    public static Jedis getJedis(){
        if (jedisPool==null){
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            //todo 连接池总连接数
            jedisPoolConfig.setMaxTotal(100);
            //todo 最小空闲，最大空闲
            jedisPoolConfig.setMaxIdle(30);
            jedisPoolConfig.setMinIdle(20);
            //资源耗尽等待时间
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setMaxWaitMillis(5000);
            //从连接池连接后要进行测试
            //导致连接池中的连接坏掉：1.服务器端重启过 2.网断过 3.服务器端维持空闲连接超时
            jedisPoolConfig.setTestOnBorrow(true);

             JedisPool jedisPool = new JedisPool("hadoop102", 6379);
        }
         Jedis jeids = jedisPool.getResource();
        return jeids;
    }
    // could not get resuce from pool
    //1.检查端口地址
    //2.检查bind是否注掉了
    //3.检查连接池是否耗尽，jields使用后，没有通过close还给池子

    public static Jedis getJedisFromSentinel(){
        if (jedisSentinelPool == null){
             HashSet<String> sentinelSet = new HashSet();
             sentinelSet.add("192.168.118.102:26379");
             JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
             //最大可用连接数
            jedisPoolConfig.setMaxTotal(100);
            //最大闲置连接数
            jedisPoolConfig.setMaxIdle(30);
            //最小闲置连接数
            jedisPoolConfig.setMinIdle(20);
            //连接耗尽是否等待
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setTestOnBorrow(true);

            jedisPoolConfig.setMaxWaitMillis(5000);
            jedisPoolConfig.setTestOnBorrow(true);

            jedisSentinelPool = new JedisSentinelPool("mymaster", sentinelSet, jedisPoolConfig);

        }
        Jedis jedis = jedisSentinelPool.getResource();
        return jedis;

    }
    private static JedisCluster jedisCluster =null;
    public static JedisCluster getJedisCluster(){
        if (jedisCluster==null){
            Set<HostAndPort> hotAndPortSet = new HashSet();
            hotAndPortSet.add(new HostAndPort("192.168.118.102",6379));
            hotAndPortSet.add(new HostAndPort("192.168.118.102",63780));


            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            //最大可用连接数
            jedisPoolConfig.setMaxTotal(100);
            //最大闲置连接数
            jedisPoolConfig.setMaxIdle(30);
            //最小闲置连接数
            jedisPoolConfig.setMinIdle(20);
            //连接耗尽是否等待
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setTestOnBorrow(true);

            jedisPoolConfig.setMaxWaitMillis(5000);
            jedisPoolConfig.setTestOnBorrow(true);

            jedisCluster = new JedisCluster( hotAndPortSet,jedisPoolConfig);
        }
        return jedisCluster;
    }

}
