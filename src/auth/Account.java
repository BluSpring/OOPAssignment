package auth;

public record Account(
    AccountType accountType,
    String email,
    String displayName,
    String passwordHash
) {
}
