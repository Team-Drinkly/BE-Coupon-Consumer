package com.drinkhere.drinklykafka.domain.repository;

import com.drinkhere.drinklykafka.domain.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
}
