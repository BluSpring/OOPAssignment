package admin;

import auth.Account;
import auth.AccountType;
import auth.AuthManager;

public class Admin {
    private static final AuthManager authManager = new AuthManager(AccountType.ADMINISTRATOR);
    private static final String DEFAULT_ADMIN_EMAIL = "root@assignment.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {
        // Create a default Administrator account, if it does not exist.
        if (authManager.getAccountByEmail(DEFAULT_ADMIN_EMAIL) == null) {
            authManager.create(DEFAULT_ADMIN_EMAIL, "Administrator", DEFAULT_ADMIN_PASSWORD);
        }
    }

    public static void create(Account account) {

    }
}
