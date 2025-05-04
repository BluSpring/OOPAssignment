package product;

import auth.Account;
import util.Utils;
import util.data.DataSerializer;
import util.data.DataSerializers;

public class Order {
    private final Account account;
    private final int orderId;
    private final long orderTimestamp;
    private long deliveredTimestamp = -1;
    private OrderStatus status = OrderStatus.PENDING;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    public Order(Account account, int orderId, long orderTimestamp) {
        this.account = account;
        this.orderId = orderId;
        this.orderTimestamp = orderTimestamp;
    }

    public Order(Account account, int orderId, long orderTimestamp, long deliveredTimestamp, OrderStatus status, PaymentStatus paymentStatus) {
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

    public Account getAccount() {
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
            return Utils.join(",", value.getAccount().getUUID(), value.getOrderId(), value.getOrderTimestamp(), value.getDeliveredTimestamp(), value.getStatus(), value.getPaymentStatus());
        }

        @Override
        public Order deserialize(String data) {
            return null;
        }
    }

    static {
        DataSerializers.register("order", new Serializer());
    }
}
