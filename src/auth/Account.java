package auth;

import util.StringUtils;
import util.data.DataSerializer;
import util.data.DataSerializers;

public record Account(
    AccountType accountType,
    String email,
    String displayName,
    String passwordHash
) {
    public static class Serializer extends DataSerializer<Account> {
        public Serializer() {
            super(Account.class);
        }

        @Override
        public String serialize(Account value) {
            return StringUtils.join(",", value.accountType().name(), value.email(), value.displayName(), value.passwordHash());
        }

        @Override
        public Account deserialize(String data) {
            var split = data.split(",");
            return new Account(AccountType.valueOf(split[0]), split[1], split[2], split[3]);
        }
    }

    static {
        DataSerializers.register("account", new Account.Serializer());
    }
}
