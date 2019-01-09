package com.zm.xmemcached;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2019/1/9.
 */
@Configuration
public class XMemCachedAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(XMemCachedProperties.class)
    @ConditionalOnProperty(prefix = "spring.memcached", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class XMemcachedClientConfig {
        private XMemCachedProperties properties;
        @Autowired
        public void setProperties(XMemCachedProperties properties) {
            this.properties = properties;
        }

        @Bean
        public XMemcachedClientBuilder xMemcachedClientBuilder() {

            XMemcachedClientBuilder clientBuilder = new XMemcachedClientBuilder(properties.getInetSocketAddresses(), properties.getWeights());
            clientBuilder.setConnectionPoolSize(properties.getPoolSize());
            clientBuilder.setCommandFactory(textCommandFactory());
            clientBuilder.setSessionLocator(ketamaMemcachedSessionLocator());
            clientBuilder.setTranscoder(serializingTranscoder());

            clientBuilder.setFailureMode(properties.isFailureMode());
            clientBuilder.setConnectTimeout(properties.getConnectTimeout());
            clientBuilder.setOpTimeout(properties.getOpTimeout());
            clientBuilder.setEnableHealSession(properties.isEnableHealSession());
            clientBuilder.setHealSessionInterval(properties.getHealSessionInterval());
            if (!StringUtils.isEmpty(properties.getName())) {
                clientBuilder.setName(properties.getName());
            }
            clientBuilder.setMaxQueuedNoReplyOperations(properties.getMaxQueuedNoReplyOperations());
            clientBuilder.setSanitizeKeys(properties.isSanitizeKeys());


            return clientBuilder;
        }

        @Bean
        public TextCommandFactory textCommandFactory() {
            return new TextCommandFactory();
        }

        @Bean
        public KetamaMemcachedSessionLocator ketamaMemcachedSessionLocator() {
            return new KetamaMemcachedSessionLocator();
        }

        @Bean
        public SerializingTranscoder serializingTranscoder() {
            return new SerializingTranscoder();
        }

        /**
         * XMemCachedClient 配置
         * @return XMemCachedClient
         * @throws IOException IOException
         */
        @Bean(destroyMethod = "shutdown")
        public XMemcachedClient memcachedClient() throws IOException {
            return (XMemcachedClient) xMemcachedClientBuilder().build();
        }
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public MemCachedService memCachedService () {
        return new MemCachedService();
    }

}
