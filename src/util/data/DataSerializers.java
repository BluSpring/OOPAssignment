package util.data;

import java.util.HashMap;
import java.util.Map;

public class DataSerializers {
    private static final Map<String, DataSerializer<?>> serializers = new HashMap<>();

    public static DataSerializer<?> register(String name, DataSerializer<?> serializer) {
        return serializers.put(name, serializer);
    }

    public static DataSerializer<?> getSerializer(String name) {
        return serializers.get(name);
    }

    public static String getSerializerName(DataSerializer<?> serializer) {
        for (String key : serializers.keySet()) {
            var other = serializers.get(key);

            if (other == serializer) {
                return key;
            }
        }

        return null;
    }

    public static <T> DataSerializer<T> getSerializerFor(T value) {
        for (DataSerializer<?> serializer : serializers.values()) {
            if (serializer.getSerializableClass().isAssignableFrom(value.getClass())) {
                return (DataSerializer<T>) serializer;
            }
        }

        return null;
    }
}
