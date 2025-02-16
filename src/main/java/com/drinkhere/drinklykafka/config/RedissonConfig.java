package com.drinkhere.drinklykafka.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")  // Redis 주소 (Docker 실행 시 변경 가능)
                .setPassword(null);  // Redis 비밀번호 (설정한 경우 입력)
        config.setCodec(new StringCodec());  // 문자열 데이터 처리
        return Redisson.create(config);
    }
}
