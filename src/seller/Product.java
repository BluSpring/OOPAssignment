package seller;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getid() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public void updateProduct(int stock, double price) {
        this.stock = stock;
        this.price = price;
    }

}


      /*  Product p = new Product(0001, "iPhone 16", 3999.00, 100);

        System.out.println(String.format("Product ID: %d | Name: %s | RM %.2f | Stock: %d",
                p.getid(), p.getName(), p.getPrice(), p.getStock()));

// Self Note from Naz: Just do individual names, combine them together in the display,
// you don't need the function for it - also, you can use String.format,
// but you have to look up how to use it, it's a bit different from Python */




