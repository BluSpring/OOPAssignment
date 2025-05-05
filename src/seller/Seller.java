package seller;

import auth.Account;
import auth.AccountType;
import auth.AuthManager;

public class Seller {
    private static final AuthManager authManager = new AuthManager(AccountType.SELLER);

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {

    }

    public static void create(Account account) {

    }
}
