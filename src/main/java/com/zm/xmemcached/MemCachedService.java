package com.zm.xmemcached;

import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2019/1/9.
 */
@Slf4j
public class MemCachedService {

    private XMemcachedClient memcachedClient;

    public XMemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(XMemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    /**
     * 缓存数据
     * @param key 缓存key
     * @param exp 有效时间，0为永久，单位秒
     * @param value 需要缓存的数据
     * @return boolean true:成功，false:失败
     */
    public boolean set(final String key, final int exp, final Object value) {
        try {
            return memcachedClient.set(key, exp, value);
        } catch (TimeoutException e) {
            log.error("MemCached => TimeoutException", e);
        } catch (InterruptedException e) {
            log.error("MemCached => InterruptedException", e);
        } catch (MemcachedException e) {
            log.error("MemCached => MemcachedException", e);
        }
        return false;

    }

    /**
     * 缓存数据(持久化)
     * @param key 缓存key
     * @param value 需要缓存的数据
     * @return boolean true:成功，false:失败
     */
    public boolean set(String key, Object value) {
        return this.set(key, 0, value);
    }


    /**
     * 删除缓存
     * @param key key
     * @return boolean true:成功，false:失败
     */
    public boolean delete(String key) {
        try {
            return memcachedClient.delete(key);
        } catch (TimeoutException e) {
            log.error("MemCached => TimeoutException", e);
        } catch (InterruptedException e) {
            log.error("MemCached => InterruptedException", e);
        } catch (MemcachedException e) {
            log.error("MemCached => MemcachedException", e);
        }
        return false;
    }

    /**
     * 获取缓存
     * @param key 缓存key
     * @param timeout 操作超时时间
     * @return object 未获取到或超时返回null
     */
    public Object get(String key, int timeout) {
        try {
            if (timeout > 0) {
                return memcachedClient.get(key, timeout);
            } else {
                return memcachedClient.get(key);
            }
        } catch (TimeoutException e) {
            log.error("MemCached => TimeoutException", e);
        } catch (InterruptedException e) {
            log.error("MemCached => InterruptedException", e);
        } catch (MemcachedException e) {
            log.error("MemCached => MemcachedException", e);
        }
        return null;
    }

    /**
     * 获取缓存，使用默认操作超时时间
     * @param key 缓存key
     * @return object 未获取到或超时返回null
     */
    public Object get(String key) {
        return this.get(key, 0);
    }

    /**
     * 获取集合缓存
     * @param key 缓存key
     * @return object 未获取到返回null
     */
    public List getList(String key) {
        try {
            return memcachedClient.get(key);
        } catch (TimeoutException e) {
            log.error("MemCached => TimeoutException", e);
        } catch (InterruptedException e) {
            log.error("MemCached => InterruptedException", e);
        } catch (MemcachedException e) {
            log.error("MemCached => MemcachedException", e);
        }
        return null;
    }

    /**
     * 遍历并删除MemCached中key值包含给定值的缓存
     * @param key key
     */
    public void deleteOnConditionKey(String key) {
        try {
            Collection<InetSocketAddress> collection = memcachedClient.getAvailableServers();
            if (collection != null && collection.size() > 0) {
                for (InetSocketAddress socketAddress : collection) {
                    KeyIterator keyIterator = memcachedClient.getKeyIterator(socketAddress);
                    while(keyIterator.hasNext()){
                        String cachedKey = keyIterator.next();
                        if(!StringUtils.isEmpty(cachedKey) && cachedKey.contains(key)){
                            memcachedClient.delete(cachedKey);
                        }
                    }
                }
            }
        } catch (TimeoutException e) {
            log.error("MemCached => TimeoutException", e);
        } catch (InterruptedException e) {
            log.error("MemCached => InterruptedException", e);
        } catch (MemcachedException e) {
            log.error("MemCached => MemcachedException", e);
        }
    }

}
