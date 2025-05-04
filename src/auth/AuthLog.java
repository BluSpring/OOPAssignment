package auth;

import util.Utils;
import util.data.DataSerializer;
import util.data.DataSerializers;

public record AuthLog(
    String email,
    long timestamp
) {
    public static class Serializer extends DataSerializer<AuthLog> {
        public Serializer() {
            super(AuthLog.class);
        }

        @Override
        public String serialize(AuthLog value) {
            return Utils.join(",", value.email(), value.timestamp());
        }

        @Override
        public AuthLog deserialize(String data) {
            var split = data.split(",");

            return new AuthLog(split[0], Long.getLong(split[1]));
        }
    }

    static {
        DataSerializers.register("auth_log", new Serializer());
    }
}
