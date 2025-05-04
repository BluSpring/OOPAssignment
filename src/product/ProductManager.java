package product;

import util.data.DataSerializers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductManager {
    private final List<Product> products = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    private final File productsFile = new File("products.txt");
    private final File ordersFile = new File("orders.txt");

    public ProductManager() {
        this.load();
    }

    public Iterator<Order> orders() {
        return this.orders.iterator();
    }

    public Iterator<Product> products() {
        return this.products.iterator();
    }

    public void load() {
        products.clear();
        orders.clear();

        DataSerializers.deserializeLines(Product.class, productsFile, products);
        DataSerializers.deserializeLines(Order.class, ordersFile, orders);
    }

    public void save() {
        DataSerializers.serializeValues(Product.class, productsFile, products);
        DataSerializers.serializeValues(Order.class, ordersFile, orders);
    }
}
