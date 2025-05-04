package auth;

import util.ByteArrayUtils;
import util.data.DataSerializable;
import util.data.DataSerializers;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AuthManager implements Iterable<Account>, DataSerializable {
    private final List<Account> accounts = new ArrayList<>();
    private final List<AuthLog> authLogs = new ArrayList<>();
    private final AccountType type;

    private final File accountFile;
    private final File authLogsFile;

    public AuthManager(AccountType type) {
        this.type = type;

        this.accountFile = new File(type.name().toLowerCase(Locale.ROOT) + "_accounts.txt");
        this.authLogsFile = new File(type.name().toLowerCase(Locale.ROOT) + "_auth_logs.txt");

        this.load();
    }

    public AccountType getType() {
        return type;
    }

    public Iterator<Account> iterator() {
        return this.accounts.iterator();
    }

    public Iterator<AuthLog> authLogIterator() {
        return this.authLogs.iterator();
    }

    @Override
    public void load() {
        accounts.clear();
        authLogs.clear();

        DataSerializers.deserializeLines(Account.class, accountFile, accounts);
        DataSerializers.deserializeLines(AuthLog.class, authLogsFile, authLogs);
    }

    @Override
    public void save() {
        DataSerializers.serializeValues(Account.class, accountFile, accounts);
        DataSerializers.serializeValues(AuthLog.class, authLogsFile, authLogs);
    }

    public Account getAccountByEmail(String email) {
        // Search for an account with a given email
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                return account;
            }
        }

        return null;
    }

    public Account getAccountByUUID(UUID uuid) {
        // Search for an account with a given UUID
        for (Account account : accounts) {
            if (account.getUUID().equals(uuid)) {
                return account;
            }
        }

        return null;
    }

    public Account create(String email, String displayName, String password) {
        if (this.getAccountByEmail(email) != null)
            throw new IllegalArgumentException("An account with that email already exists!");

        // Generate a random UUID. This UUID is used to uniquely identify an account.
        var uuid = UUID.randomUUID();

        // Ensure that the UUID is actually unique, as UUIDs are bound to collide even if it is an incredibly low chance of doing so.
        do {
            if (this.getAccountByUUID(uuid) != null)
                uuid = UUID.randomUUID();
            else break;
        } while (true);

        var account = new Account(this.getType(), uuid, email, displayName, hashPassword(password));
        this.accounts.add(account);
        this.addAuthLog(account, AuthLog.Type.REGISTER);
        this.save();

        return account;
    }

    public Account login(String email, String password) {
        var account = this.getAccountByEmail(email);

        if (account == null) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        if (!account.getPasswordHash().equals(this.hashPassword(password))) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        this.addAuthLog(account, AuthLog.Type.LOGIN);
        this.save();

        return account;
    }

    public void changePassword(Account account, String oldPassword, String newPassword) {
        if (!account.getPasswordHash().equals(this.hashPassword(oldPassword))) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        if (account.getPasswordHash().equals(this.hashPassword(newPassword))) {
            throw new IllegalArgumentException("New password is the same as the old password!");
        }

        account.setPasswordHash(this.hashPassword(newPassword));
        this.addAuthLog(account, AuthLog.Type.CHANGE_PASSWORD);
        this.save();
    }

    public void addAuthLog(Account account, AuthLog.Type type) {
        this.addAuthLog(account, type, "");
    }

    public void addAuthLog(Account account, AuthLog.Type type, String extraData) {
        this.authLogs.add(new AuthLog(account.getUUID(), type, System.currentTimeMillis(), extraData));
    }

    private String hashPassword(String password) {
        try {
            // Get a SHA-256 message digest algorithm for hashing
            var digest = MessageDigest.getInstance("SHA-256");
            // Run the string through the algorithm to hash it with SHA-256
            var hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the hashed string into a hexadecimal string, for storage.
            return ByteArrayUtils.bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
