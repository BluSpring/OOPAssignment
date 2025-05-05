package app.product;

import java.util.*;

public class PaymentHandler {
    private final Map<UUID, Payment> payments = new HashMap<>();

    public Payment createPayment(UUID orderId, double amount) {
        Payment payment = new Payment(orderId, amount);
        payments.put(payment.getId(), payment);
        return payment;
    }

    public Payment getPayment(UUID paymentId) {
        return payments.get(paymentId);
    }

    public void markAsPaid(UUID paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.PAID);
        }
    }

    public void markAsFailed(UUID paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.FAILED);
        }
    }

    public Collection<Payment> getAllPayments() {
        return payments.values();
    }
}
