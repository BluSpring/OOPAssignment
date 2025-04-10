import admin.Admin;
import customer.Customer;
import seller.Seller;

public class Main {
    public static void main(String[] args) {
        Admin.init();
        Customer.init();
        Seller.init();
    }
}
