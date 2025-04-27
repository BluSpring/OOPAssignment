package admin;

import auth.AccountType;
import auth.AuthManager;

public class Admin {
    private static final AuthManager authManager = new AuthManager(AccountType.ADMINISTRATOR);

    public static AuthManager getAuthManager() {
        return authManager;
    }

    public static void init() {

    }
}
