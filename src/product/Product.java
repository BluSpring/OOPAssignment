package product;

import util.Utils;
import util.data.DataSerializer;
import util.data.DataSerializers;

import java.util.UUID;

public class Product {
    private final UUID seller;
    private final String barcode;
    private final String name;
    private double price;
    private int stock;
    private double discount;

    public Product(UUID seller, String barcode, String name, double price, int stock, double discount) {
        this.seller = seller;
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
    }

    public UUID getSeller() {
        return seller;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public static class Serializer extends DataSerializer<Product> {
        public Serializer() {
            super(Product.class);
        }

        @Override
        public String serialize(Product value) {
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.getSeller(), value.getBarcode(), value.getName(), value.getPrice(), value.getStock(), value.getDiscount()));
        }

        @Override
        public Product deserialize(String data) {
            var split = DataSerializers.readSegmentedLine(data);
            return new Product(UUID.fromString(split.get(0)), split.get(1), split.get(2), Double.parseDouble(split.get(3)), Integer.parseInt(split.get(4)), Double.parseDouble(split.get(5)));
        }
    }

    static {
        DataSerializers.register("product", new Serializer());
    }

    public static void init() {}
}
