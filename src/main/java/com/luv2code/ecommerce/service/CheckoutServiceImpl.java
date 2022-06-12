package com.luv2code.ecommerce.service;

import com.luv2code.ecommerce.dao.CustomerRepository;
import com.luv2code.ecommerce.dto.Purchase;
import com.luv2code.ecommerce.dto.PurchaseResponse;
import com.luv2code.ecommerce.entity.Customer;
import com.luv2code.ecommerce.entity.Order;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    private final TrackingNumberGenerator trackingNumberGenerator;

    public CheckoutServiceImpl(CustomerRepository customerRepository, TrackingNumberGenerator trackingNumberGenerator) {
        this.customerRepository = customerRepository;
        this.trackingNumberGenerator = trackingNumberGenerator;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with order items
        order.setOrderItems(purchase.getOrderItems());

        // populate order with billingAddress
        order.setBillingAddress(purchase.getBillingAddress());

        // populate order with shippingAddress
        order.setShippingAddress(purchase.getShippingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();
        customer.addOrder(order);

        // save to the db
        customerRepository.save(customer);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID
        return trackingNumberGenerator.get();
    }
}
