package com.drinkhere.drinklykafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponAvailabilityRequest {

    private Long couponId;
    private Long userId;
}

