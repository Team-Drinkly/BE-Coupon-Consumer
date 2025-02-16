package com.drinkhere.drinklykafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // 모든 Kafka 메시지를 String으로 받도록 설정
    private ConsumerFactory<String, String> createConsumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer());
    }

    // CouponRequest Consumer 설정 (String으로 받고 직접 JSON 파싱)
    @Bean
    public ConsumerFactory<String, String> couponRequestConsumerFactory() {
        return createConsumerFactory("coupon-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> couponRequestListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(couponRequestConsumerFactory());
        return factory;
    }

    // CouponAvailabilityResponse Consumer 설정 (String으로 받고 직접 JSON 파싱)
    @Bean
    public ConsumerFactory<String, String> couponAvailabilityResponseConsumerFactory() {
        return createConsumerFactory("coupon-availability-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> couponAvailabilityResponseListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(couponAvailabilityResponseConsumerFactory());
        return factory;
    }
}
