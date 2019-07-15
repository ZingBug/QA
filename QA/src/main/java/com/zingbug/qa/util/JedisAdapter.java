package com.zingbug.qa.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.form.SelectTag;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

/**
 * Created by ZingBug on 2019/7/2.
 */
@Service
@Slf4j
public class JedisAdapter implements InitializingBean {

    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    /**
     * 插入Redis-Set节点
     */
    public long sadd(String key, String value) {
        try (Jedis jedis = pool.getResource()) {

            return jedis.sadd(key, value);
        } catch (Exception e) {
            log.error("插入Redis-Set异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 删除Redis-Set节点
     */
    public long srem(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            log.error("删除Redis-Set异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 返回Redis-Set节点数量
     */
    public long scard(String key) {

        try (Jedis jedis = pool.getResource()) {

            return jedis.scard(key);
        } catch (Exception e) {
            log.error("返回Redis-Set节点数量异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 判断Redis-Set是否存在该节点
     */
    public boolean sismember(String key, String value) {

        try (Jedis jedis = pool.getResource()) {

            return jedis.sismember(key, value);
        } catch (Exception e) {
            log.error("判断Redis-Set节点异常" + e.getMessage());
        }

        return false;
    }

    /**
     * 阻塞返回Redis-List元素
     */
    public List<String> brpop(int timeout, String key) {

        try (Jedis jedis = pool.getResource()) {

            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            log.error("阻塞返回Redis-List元素异常" + e.getMessage());
        }

        return null;
    }

    /**
     * 添加Redis-List头元素
     */
    public long lpush(String key, String value) {

        try (Jedis jedis = pool.getResource()) {

            return jedis.lpush(key, value);
        } catch (Exception e) {
            log.error("阻塞返回Redis-List元素异常" + e.getMessage());
        }

        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        try (Jedis jedis = pool.getResource()) {

            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        }

        return null;
    }

    /**
     * 在线程池获取jedis链接
     */
    public Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 返回事务性
     */
    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        } finally {
        }
        return null;
    }

    /**
     * 执行事务性
     */
    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        } finally {
            if (tx != null) {
                tx.close();
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 返回有序集中，指定区间内的成员，其中成员的位置按分数值递增(从小到大)来排序
     */
    public Set<String> zrange(String key, int start, int end) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        }

        return null;
    }

    /**
     * 返回有序集中，指定区间内的成员，其中成员的位置按分数值递减(从大到小)来排序
     */
    public Set<String> zrevrange(String key, int start, int end) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        }
        return null;
    }

    /**
     * 返回集合中元素的数量
     */
    public long zcard(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zcard(key);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 null 。
     */
    public Double zscore(String key, String member) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zscore(key, member);
        } catch (Exception e) {
            log.error("发生异常" + e.getMessage());
        }
        return null;
    }
}
