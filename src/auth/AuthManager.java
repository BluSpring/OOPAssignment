package auth;

import util.ByteArrayUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuthManager implements Iterable<Account> {
    private final List<Account> accounts = new ArrayList<>();
    private final AccountType type;

    public AuthManager(AccountType type) {
        this.type = type;
    }

    public AccountType getType() {
        return type;
    }

    public Iterator<Account> iterator() {
        return this.accounts.iterator();
    }

    public Account getAccountByEmail(String email) {
        // Search for an account with a given email
        for (Account account : accounts) {
            if (account.email().equals(email)) {
                return account;
            }
        }

        return null;
    }

    public Account create(String email, String displayName, String password) {
        if (this.getAccountByEmail(email) != null)
            throw new IllegalArgumentException("An account with that email already exists!");

        var account = new Account(this.getType(), email, displayName, hashPassword(password));
        this.accounts.add(account);
        return account;
    }

    private String hashPassword(String password) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return ByteArrayUtils.bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
