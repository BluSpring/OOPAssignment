package auth;

import util.ByteArrayUtils;
import util.data.DataSerializable;
import util.data.DataSerializers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
    }

    public AccountType getType() {
        return type;
    }

    public Iterator<Account> iterator() {
        return this.accounts.iterator();
    }

    @Override
    public void load() {
        accounts.clear();
        authLogs.clear();

        try {
            if (accountFile.exists()) {
                var serializer = DataSerializers.getSerializerFor(Account.class);
                var lines = Files.readAllLines(accountFile.toPath(), StandardCharsets.UTF_8);

                for (String line : lines) {
                    accounts.add(serializer.deserialize(line));
                }
            }

            if (authLogsFile.exists()) {
                var serializer = DataSerializers.getSerializerFor(AuthLog.class);
                var lines = Files.readAllLines(authLogsFile.toPath(), StandardCharsets.UTF_8);

                for (String line : lines) {
                    authLogs.add(serializer.deserialize(line));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            if (!accountFile.exists()) {
                accountFile.createNewFile();
            }

            if (!authLogsFile.exists()) {
                authLogsFile.createNewFile();
            }

            var accountSerializer = DataSerializers.getSerializerFor(Account.class);
            var authLogSerializer = DataSerializers.getSerializerFor(AuthLog.class);

            var serializedAccounts = new ArrayList<String>();
            for (Account account : accounts) {
                serializedAccounts.add(accountSerializer.serialize(account));
            }

            var serializedAuthLogs = new ArrayList<String>();
            for (AuthLog authLog : authLogs) {
                serializedAuthLogs.add(authLogSerializer.serialize(authLog));
            }

            Files.write(accountFile.toPath(), serializedAccounts, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
            Files.write(authLogsFile.toPath(), serializedAuthLogs, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public Account login(String email, String password) {
        var account = this.getAccountByEmail(email);

        if (account == null) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        if (!account.passwordHash().equals(this.hashPassword(password))) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        this.authLogs.add(new AuthLog(email, System.currentTimeMillis()));

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
