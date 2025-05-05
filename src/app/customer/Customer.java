package app.customer;

import app.auth.Account;
import app.auth.AccountType;
import app.auth.AuthManager;

public class Customer {
    private static final AuthManager authManager = new AuthManager(AccountType.CUSTOMER);

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {

    }

    public static void create(Account account) {

    }
}
