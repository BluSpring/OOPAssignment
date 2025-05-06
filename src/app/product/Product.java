package app.product;

import app.util.Utils;
import app.util.data.DataSerializer;
import app.util.data.DataSerializers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public static class MapSerializer extends DataSerializer<Map<String, Integer>> {
        public MapSerializer() {
            super(null);
        }

        @Override
        public String serialize(Map<String, Integer> value) {
            var list = new ArrayList<String>();
            value.forEach((barcode, quantity) -> {
                list.add(barcode + ":" + quantity);
            });

            return DataSerializers.writeSegmentedLine(list);
        }

        @Override
        public Map<String, Integer> deserialize(String data) {
            var segments = DataSerializers.readSegmentedLine(data);
            var products = new HashMap<String, Integer>();

            for (String s : segments) {
                var split = s.split(":");

                products.put(split[0], Integer.parseInt(split[1]));
            }

            return products;
        }
    }

    static {
        DataSerializers.register("product_map", new MapSerializer());
        DataSerializers.register("product", new Serializer());
    }

    public static void init() {}
}
