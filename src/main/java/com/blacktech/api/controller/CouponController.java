package com.blacktech.api.controller;

import com.blacktech.api.domain.coupon.Coupon;
import com.blacktech.api.domain.coupon.CouponRequestDTO;
import com.blacktech.api.service.CouponServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponServise couponServise;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data) {
        Coupon coupon = couponServise.addCouponToEvent(eventId, data);
        return ResponseEntity.ok(coupon);
    }
}
