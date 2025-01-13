package com.blacktech.api.service;

import com.blacktech.api.domain.coupon.Coupon;
import com.blacktech.api.domain.coupon.CouponRequestDTO;
import com.blacktech.api.domain.event.Event;
import com.blacktech.api.repositories.CouponRepository;
import com.blacktech.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponServise {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());
        coupon.setValid(new Date(couponData.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

    public List<Coupon> consultCoupons(UUID eventId, Date currenteDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId,currenteDate);
    }
}
