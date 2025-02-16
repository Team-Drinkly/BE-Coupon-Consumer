package com.drinkhere.drinklykafka.application.consumer;

import com.drinkhere.drinklykafka.dto.CouponAvailabilityRequest;
import com.drinkhere.drinklykafka.dto.CouponRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "coupon-issue", groupId = "coupon-group")
    public void consumeCouponRequest(String message) {

        try {
            // JSON 문자열을 CouponRequest 객체로 변환
            CouponRequest request = objectMapper.readValue(message, CouponRequest.class);

            String lockKey = "coupon-lock:" + request.getCouponId();
            RLock lock = redissonClient.getLock(lockKey);

            try {
                System.out.println("🔒 Lock 획득 시도: " + lockKey);

                if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                    System.out.println("✅ Lock 획득 성공: " + lockKey);

                    // 쿠폰 개수 확인을 위해 Kafka 토픽 전송
                    String jsonRequest = objectMapper.writeValueAsString(new CouponAvailabilityRequest(request.getCouponId(), request.getUserId()));
                    kafkaTemplate.send("coupon-availability", jsonRequest);
                } else {
                    System.err.println("❌ Lock 획득 실패: " + lockKey);
                }
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    System.out.println("🔓 Lock 해제: " + lockKey);
                }
            }
        } catch (Exception e) {
            System.err.println("Kafka 메시지 역직렬화 오류: " + e.getMessage());
        }
    }
}
