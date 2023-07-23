package com.gexingw.shop.common.redis.util;

import com.gexingw.shop.common.core.util.SpringUtil;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 22:16
 */
@SuppressWarnings({"unchecked", "DataFlowIssue", "unused"})
public class RedisUtil {


    private static final RedisTemplate<String, Object> REDIS_TEMPLATE = SpringUtil.getBean(RedisTemplate.class);

    private static final ValueOperations<String, Object> VALUE_OPERATIONS = REDIS_TEMPLATE.opsForValue();

    private static final ZSetOperations<String, Object> ZSET_OPERATIONS = REDIS_TEMPLATE.opsForZSet();

    private static final SetOperations<String, Object> SET_OPERATIONS = REDIS_TEMPLATE.opsForSet();

    private static final HashOperations<String, Object, Object> HASH_OPERATIONS = REDIS_TEMPLATE.opsForHash();

    private static final ListOperations<String, Object> LIST_OPERATIONS = REDIS_TEMPLATE.opsForList();

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return 执行结果
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        try {
            return REDIS_TEMPLATE.getExpire(key, TimeUnit.SECONDS);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String key) {
        try {
            return REDIS_TEMPLATE.hasKey(key);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public static void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    REDIS_TEMPLATE.delete(key[0]);
                } else {
                    REDIS_TEMPLATE.delete((Collection<String>) CollectionUtils.arrayToList(key));
                }
            }
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }

    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static <v> v get(String key) {
        try {
            return key == null ? null : (v) REDIS_TEMPLATE.opsForValue().get(key);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        try {
            VALUE_OPERATIONS.set(key, value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }

    }

    /**
     * key不存在则插入
     *
     * @param key   键
     * @param value 值
     * @return 执行结果
     */
    public static boolean setNx(String key, Object value) {
        try {
            return VALUE_OPERATIONS.setIfAbsent(key, value);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                VALUE_OPERATIONS.set(key, value, time, TimeUnit.SECONDS);
                return true;
            }

            return set(key, value);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 执行结果
     */
    public static long incr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递增因子必须大于0");
            }
            return VALUE_OPERATIONS.increment(key, delta);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 执行结果
     */
    public static long decr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new RuntimeException("递减因子必须大于0");
            }
            return VALUE_OPERATIONS.increment(key, -delta);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hGet(String key, String item) {
        try {
            return HASH_OPERATIONS.get(key, item);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hMSet(String key, Map<String, Object> map) {
        try {
            HASH_OPERATIONS.putAll(key, map);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hMSet(String key, Map<String, Object> map, long time) {
        try {
            HASH_OPERATIONS.putAll(key, map);
            if (time > 0) {
                return expire(key, time);
            }

            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hSet(String key, String item, Object value) {
        try {
            HASH_OPERATIONS.put(key, item, value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hSet(String key, String item, Object value, long time) {
        try {
            HASH_OPERATIONS.put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hDel(String key, Object... item) {
        try {
            HASH_OPERATIONS.delete(key, item);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item) {
        try {
            return HASH_OPERATIONS.hasKey(key, item);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 执行结果
     */
    public static double hIncr(String key, String item, double by) {
        try {
            return HASH_OPERATIONS.increment(key, item, by);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return 执行结果
     */
    public static double hDecr(String key, String item, double by) {
        try {
            return HASH_OPERATIONS.increment(key, item, -by);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return 执行结果
     */
    public static Set<Object> sGet(String key) {
        try {
            return SET_OPERATIONS.members(key);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        try {
            return SET_OPERATIONS.isMember(key, value);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String key, Object... values) {
        try {
            return SET_OPERATIONS.add(key, values);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = SET_OPERATIONS.add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 缓存长度
     */
    public static long sGetSetSize(String key) {
        try {
            return SET_OPERATIONS.size(key);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String key, Object... values) {
        try {
            return SET_OPERATIONS.remove(key, values);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束， -1：代表所有值
     * @return 缓存内容
     */
    public static List<Object> lGet(String key, long start, long end) {
        try {
            return LIST_OPERATIONS.range(key, start, end);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 缓存长度
     */
    public static long lGetListSize(String key) {
        try {
            return LIST_OPERATIONS.size(key);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return list中的值
     */
    public static Object lGetIndex(String key, long index) {
        try {
            return LIST_OPERATIONS.index(key, index);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 执行结果
     */
    public static boolean lSet(String key, Object value) {
        try {
            LIST_OPERATIONS.rightPush(key, value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 执行结果
     */
    public static boolean lSet(String key, Object value, long time) {
        try {
            LIST_OPERATIONS.rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 执行结果
     */
    public static boolean lSet(String key, List<Object> value) {
        try {
            LIST_OPERATIONS.rightPushAll(key, value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 执行结果
     */
    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            LIST_OPERATIONS.rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 执行结果
     */
    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            LIST_OPERATIONS.set(key, index, value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String key, long count, Object value) {
        try {
            return LIST_OPERATIONS.remove(key, count, value);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 模糊搜索Key，可用scan
     * 慎用，很危险！！！
     *
     * @param keyPattern 匹配规则
     * @return key集合
     */
    public static List<String> keys(String keyPattern) {
        return scan(keyPattern);
    }

    /**
     * 操作zset添加值
     *
     * @param key  键
     * @param item 值
     * @return 执行结果
     */
    public static Boolean zSet(String key, Set<ZSetOperations.TypedTuple<Object>> item) {
        try {
            ZSET_OPERATIONS.add(key, item);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 操作zset累加值
     *
     * @param key   键
     * @param item  值
     * @param value 分值
     * @return 执行结果
     */
    public static Boolean incrZSet(String key, String item, Double value) {
        try {
            ZSET_OPERATIONS.incrementScore(key, item, value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 操作zset递减
     *
     * @param key   键
     * @param item  值
     * @param value 分值
     * @return 执行结果
     */
    public static Boolean descZSet(String key, String item, Double value) {
        try {
            ZSET_OPERATIONS.incrementScore(key, item, -value);
            return true;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 获取区间排名
     *
     * @param key   键
     * @param start 起始索引
     * @param end   终止索引
     * @return 值
     */
    public static Object getScores(String key, long start, long end) {
        try {
            return ZSET_OPERATIONS.reverseRangeWithScores(key, start, end);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 是否存在zset对应的key
     *
     * @param key  键
     * @param item 值
     * @return 执行结果
     */
    public static Boolean zSetHasKey(String key, String item) {
        try {
            return ZSET_OPERATIONS.score(key, item) != null;
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * 获取单个信息
     *
     * @param key  键
     * @param item 值
     * @return 执行结果
     */
    public static Double getZSet(String key, String item) {
        try {
            return ZSET_OPERATIONS.score(key, item);
        } finally {
            RedisConnectionUtils.unbindConnection(REDIS_TEMPLATE.getConnectionFactory());
        }
    }

    /**
     * scan命令
     *
     * @param pattern 表达式
     * @return 执行结果
     */
    public static List<String> scan(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        RedisConnectionFactory factory = REDIS_TEMPLATE.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(new String(cursor.next()));
        }
        RedisConnectionUtils.releaseConnection(rc, factory);

        return result;
    }

    /**
     * 执行lcr脚本，加锁
     *
     * @param script    脚本
     * @param finalKeys 键
     * @param values    值
     * @return 执行结果
     */
    public static <T> T execute(RedisScript<T> script, List<String> finalKeys, Object... values) {
        return REDIS_TEMPLATE.execute(script, finalKeys, values);
    }


    /**
     * 执行lcr脚本，解锁
     *
     * @param args SessionCallback
     * @return 执行结果
     */
    public static List<Object> executePipelined(SessionCallback<?> args) {
        return REDIS_TEMPLATE.executePipelined(args);
    }

}
