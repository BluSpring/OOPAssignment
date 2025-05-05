package app.customer;

import app.Main;
import app.auth.Account;
import app.auth.AccountType;
import app.auth.AuthManager;

import java.awt.*;

public class Customer {
    private static final AuthManager authManager = new AuthManager(AccountType.CUSTOMER);

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {

    }

    public static void create(Account account) {
        var window = Main.getFrame();

        window.setPreferredSize(new Dimension(1024, 768));
        window.setLocationRelativeTo(null);
        window.pack();
    }
}
