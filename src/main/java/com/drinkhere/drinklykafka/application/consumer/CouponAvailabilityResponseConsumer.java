package com.drinkhere.drinklykafka.application.consumer;

import com.drinkhere.drinklykafka.domain.entity.IssuedCoupon;
import com.drinkhere.drinklykafka.domain.repository.IssuedCouponRepository;
import com.drinkhere.drinklykafka.dto.CouponAvailabilityResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponAvailabilityResponseConsumer {

    private final IssuedCouponRepository issuedCouponRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "coupon-availability-response", groupId = "coupon-availability-group")
    @Transactional
    public void consumeCouponAvailabilityResponse(String message) {

        try {
            // JSON 메시지를 CouponAvailabilityResponse 객체로 변환
            CouponAvailabilityResponse response = objectMapper.readValue(message, CouponAvailabilityResponse.class);

            if (response.isAvailable()) {
                IssuedCoupon issuedCoupon = new IssuedCoupon(response.getUserId(), response.getCouponId());
                issuedCouponRepository.save(issuedCoupon);
                System.out.println("쿠폰 발급 완료: couponId=" + response.getCouponId());
            } else {
                System.out.println("쿠폰 발급 실패: 재고 없음");
            }
        } catch (Exception e) {
            System.err.println("Kafka 메시지 역직렬화 오류: " + e.getMessage());
        }
    }
}
