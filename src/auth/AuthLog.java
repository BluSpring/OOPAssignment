package auth;

import util.Utils;
import util.data.DataSerializer;
import util.data.DataSerializers;

import java.util.UUID;

public record AuthLog(
    UUID uuid,
    Type type,
    long timestamp,
    String extraData
) {
    public AuthLog(UUID uuid, Type type, long timestamp) {
        this(uuid, type, timestamp, "");
    }

    public enum Type {
        LOGIN,
        REGISTER,
        CHANGE_PASSWORD,
        CHANGE_EMAIL,
        CHANGE_DISPLAY_NAME
    }

    public static class Serializer extends DataSerializer<AuthLog> {
        public Serializer() {
            super(AuthLog.class);
        }

        @Override
        public String serialize(AuthLog value) {
            return Utils.join(",", value.uuid(), value.type().name(), value.timestamp(), value.extraData());
        }

        @Override
        public AuthLog deserialize(String data) {
            var split = data.split(",");

            return new AuthLog(UUID.fromString(split[0]), Type.valueOf(split[1]), Long.getLong(split[2]), split[3]);
        }
    }

    static {
        DataSerializers.register("auth_log", new Serializer());
    }
}
