package app.product;

import app.util.Utils;
import app.util.data.DataSerializer;
import app.util.data.DataSerializers;

import java.util.UUID;

public class Payment {
    private final UUID id;
    private final int orderId;
    private double amount;
    private PaymentStatus status;

    public Payment(UUID id, int orderId, double amount, PaymentStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public int getOrderId() {
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

    public static class Serializer extends DataSerializer<Payment> {
        public Serializer() {
            super(Payment.class);
        }

        @Override
        public String serialize(Payment value) {
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.getId(), value.getOrderId(), value.getAmount(), value.getStatus().name()));
        }

        @Override
        public Payment deserialize(String data) {
            var segments = DataSerializers.readSegmentedLine(data);
            return new Payment(UUID.fromString(segments.get(0)), Integer.parseInt(segments.get(1)), Double.parseDouble(segments.get(2)), PaymentStatus.valueOf(segments.get(3)));
        }
    }

    static {
        DataSerializers.register("payment", new Serializer());
    }

    public static void init() {}
}
