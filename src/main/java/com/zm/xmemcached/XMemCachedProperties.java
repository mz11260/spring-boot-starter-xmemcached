package com.zm.xmemcached;

import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/9.
 */
@ConfigurationProperties(prefix = "spring.memcached")
public class XMemCachedProperties {

    private String name;

    private List<SocketAddress> socketAddresses;

    private int[] weights;

    private int poolSize = 2;

    private boolean failureMode = false;

    private long opTimeout = MemcachedClient.DEFAULT_OP_TIMEOUT;

    private long connectTimeout = MemcachedClient.DEFAULT_CONNECT_TIMEOUT;

    private boolean enableHealSession = true;

    private long healSessionInterval = MemcachedClient.DEFAULT_HEAL_SESSION_INTERVAL;

    private int maxQueuedNoReplyOperations = MemcachedClient.DEFAULT_MAX_QUEUED_NOPS;

    private boolean sanitizeKeys = false;


    public List<InetSocketAddress> getInetSocketAddresses() {
        final List<InetSocketAddress> list = new ArrayList<>();
        if (this.socketAddresses != null && this.socketAddresses.size() > 0) {
            this.socketAddresses.forEach(address ->
                    list.add(new InetSocketAddress(address.getHostname(), address.getPort()))
            );
        }
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SocketAddress> getSocketAddresses() {
        return socketAddresses;
    }

    public void setSocketAddresses(List<SocketAddress> socketAddresses) {
        this.socketAddresses = socketAddresses;
    }

    public int[] getWeights() {
        return weights;
    }

    public void setWeights(int[] weights) {
        this.weights = weights;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public boolean isFailureMode() {
        return failureMode;
    }

    public void setFailureMode(boolean failureMode) {
        this.failureMode = failureMode;
    }

    public long getOpTimeout() {
        return opTimeout;
    }

    public void setOpTimeout(long opTimeout) {
        this.opTimeout = opTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isEnableHealSession() {
        return enableHealSession;
    }

    public void setEnableHealSession(boolean enableHealSession) {
        this.enableHealSession = enableHealSession;
    }

    public long getHealSessionInterval() {
        return healSessionInterval;
    }

    public void setHealSessionInterval(long healSessionInterval) {
        this.healSessionInterval = healSessionInterval;
    }

    public int getMaxQueuedNoReplyOperations() {
        return maxQueuedNoReplyOperations;
    }

    public void setMaxQueuedNoReplyOperations(int maxQueuedNoReplyOperations) {
        this.maxQueuedNoReplyOperations = maxQueuedNoReplyOperations;
    }

    public boolean isSanitizeKeys() {
        return sanitizeKeys;
    }

    public void setSanitizeKeys(boolean sanitizeKeys) {
        this.sanitizeKeys = sanitizeKeys;
    }

    public static class SocketAddress {
        private String hostname;
        private int port;

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
