package product;

import util.Utils;
import util.data.DataSerializer;
import util.data.DataSerializers;

import java.util.UUID;

public class Order {
    private final UUID account;
    private final int orderId;
    private final long orderTimestamp;
    private long deliveredTimestamp = -1;
    private OrderStatus status = OrderStatus.PENDING;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    public Order(UUID account, int orderId, long orderTimestamp) {
        this.account = account;
        this.orderId = orderId;
        this.orderTimestamp = orderTimestamp;
    }

    public Order(UUID account, int orderId, long orderTimestamp, long deliveredTimestamp, OrderStatus status, PaymentStatus paymentStatus) {
        this(account, orderId, orderTimestamp);
        this.deliveredTimestamp = deliveredTimestamp;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public long getDeliveredTimestamp() {
        return deliveredTimestamp;
    }

    public long getOrderTimestamp() {
        return orderTimestamp;
    }

    public int getOrderId() {
        return orderId;
    }

    public UUID getAccountUUID() {
        return account;
    }

    public void setDeliveredTimestamp(long deliveredTimestamp) {
        this.deliveredTimestamp = deliveredTimestamp;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public static class Serializer extends DataSerializer<Order> {
        public Serializer() {
            super(Order.class);
        }

        @Override
        public String serialize(Order value) {
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.getAccountUUID(), value.getOrderId(), value.getOrderTimestamp(), value.getDeliveredTimestamp(), value.getStatus().name(), value.getPaymentStatus().name()));
        }

        @Override
        public Order deserialize(String data) {
            var split = DataSerializers.readSegmentedLine(data);

            return new Order(UUID.fromString(split.get(0)), Integer.parseInt(split.get(1)), Long.parseLong(split.get(2)), Long.parseLong(split.get(3)), OrderStatus.valueOf(split.get(4)), PaymentStatus.valueOf(split.get(5)));
        }
    }

    static {
        DataSerializers.register("order", new Serializer());
    }

    public static void init() {}
}
