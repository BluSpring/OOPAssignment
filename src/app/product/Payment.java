package app.product;

import java.util.UUID;

public class Payment {
    private final UUID id;
    private final UUID orderId;
    private double amount;
    private PaymentStatus status;

    public Payment(UUID orderId, double amount) {
        this.id = UUID.randomUUID(); // UUID should be generated internally
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
