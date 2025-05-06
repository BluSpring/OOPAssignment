package app.product;

import app.util.data.DataSerializers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductManager {
    private static final ProductManager instance = new ProductManager();

    public ProductManager getInstance() {
        return instance;
    }

    private final List<Product> products = new ArrayList<>();
    private final File productsFile = new File("products.txt");

    private ProductManager() {
        this.load();
    }

    public Iterator<Product> products() {
        return this.products.iterator();
    }

    public Product getProduct(String barcode) {
        for (Product product : products) {
            if (product.getBarcode().equals(barcode)) {
                return product;
            }
        }

        return null;
    }

    public void load() {
        products.clear();

        DataSerializers.deserializeLines(Product.class, productsFile, products);
    }

    public void save() {
        DataSerializers.serializeValues(Product.class, productsFile, products);
    }
}
