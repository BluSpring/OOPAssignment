package app.product;

import java.util.*;

public class OrderManager {
    private final Map<UUID, List<Order>> customerCarts = new HashMap<>();
    private final Map<UUID, List<Order>> customerOrderHistory = new HashMap<>();

    public void addToCart(UUID customerId, Product product, int quantity) {
        List<Order> cart = customerCarts.computeIfAbsent(customerId, k -> new ArrayList<>());
        Order order = new Order(UUID.randomUUID(), customerId, product.getId(), quantity, OrderStatus.PENDING, PaymentStatus.PENDING);
        cart.add(order);
    }

    public void removeFromCart(UUID customerId, UUID orderId) {
        List<Order> cart = customerCarts.get(customerId);
        if (cart != null) {
            cart.removeIf(order -> order.getId().equals(orderId));
        }
    }

    public void placeOrder(UUID customerId) {
        List<Order> cart = customerCarts.getOrDefault(customerId, new ArrayList<>());
        List<Order> history = customerOrderHistory.computeIfAbsent(customerId, k -> new ArrayList<>());

        for (Order order : cart) {
            order.setStatus(OrderStatus.PLACED);
            history.add(order);
        }

        cart.clear();
    }

    public List<Order> getCart(UUID customerId) {
        return customerCarts.getOrDefault(customerId, new ArrayList<>());
    }

    public List<Order> getOrderHistory(UUID customerId) {
        return customerOrderHistory.getOrDefault(customerId, new ArrayList<>());
    }
}
