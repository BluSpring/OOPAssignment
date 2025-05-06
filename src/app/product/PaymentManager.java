package app.product;

import app.util.data.DataSerializers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PaymentManager {
    private static final PaymentManager instance = new PaymentManager();

    public static PaymentManager getInstance() {
        return instance;
    }

    private final List<Payment> payments = new ArrayList<>();
    private final File paymentsFile = new File("payments.txt");

    private PaymentManager() {
        this.load();
    }

    public Payment createPayment(int orderId, double amount) {
        Payment payment = new Payment(UUID.randomUUID(), orderId, amount, PaymentStatus.PENDING);
        payments.add(payment);
        return payment;
    }

    public Payment getPaymentFor(int orderId) {
        for (Payment payment : this.payments) {
            if (payment.getOrderId() == orderId)
                return payment;
        }

        return null;
    }

    public Payment getPayment(UUID paymentId) {
        for (Payment payment : payments) {
            if (payment.getId().equals(paymentId))
                return payment;
        }

        return null;
    }

    public void markAsPaid(UUID paymentId) {
        Payment payment = this.getPayment(paymentId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.COMPLETED);
        }
    }

    public void markAsFailed(UUID paymentId) {
        Payment payment = this.getPayment(paymentId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.FAILED);
        }
    }

    public Iterator<Payment> payments() {
        return payments.iterator();
    }

    public void load() {
        payments.clear();

        DataSerializers.deserializeLines(Payment.class, paymentsFile, payments);
    }

    public void save() {
        DataSerializers.serializeValues(Payment.class, paymentsFile, payments);
    }
}
