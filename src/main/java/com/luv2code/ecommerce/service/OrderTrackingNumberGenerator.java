package com.luv2code.ecommerce.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderTrackingNumberGenerator implements TrackingNumberGenerator{

    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
