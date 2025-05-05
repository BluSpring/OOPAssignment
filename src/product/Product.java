package product;

import util.Utils;
import util.data.DataSerializer;
import util.data.DataSerializers;

public class Product {
    private final String barcode;
    private final String name;
    private double price;
    private int stock;
    private double discount;

    public Product(String barcode, String name, double price, int stock, double discount) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
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
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.getBarcode(), value.getName(), value.getPrice(), value.getStock(), value.getDiscount()));
        }

        @Override
        public Product deserialize(String data) {
            var split = DataSerializers.readSegmentedLine(data);
            return new Product(split.get(0), split.get(1), Double.parseDouble(split.get(2)), Integer.parseInt(split.get(3)), Double.parseDouble(split.get(4)));
        }
    }

    static {
        DataSerializers.register("product", new Serializer());
    }
}
