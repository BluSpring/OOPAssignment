package util;

import java.util.HashMap;
import java.util.Map;

public class DataSerializers {
    private final Map<String, DataSerializer<?>> serializers = new HashMap<>();

    public DataSerializer<?> register(String name, DataSerializer<?> serializer) {
        return this.serializers.put(name, serializer);
    }

    public DataSerializer<?> getSerializer(String name) {
        return this.serializers.get(name);
    }

    public String getSerializerName(DataSerializer<?> serializer) {
        for (String key : this.serializers.keySet()) {
            var other = this.serializers.get(key);

            if (other == serializer) {
                return key;
            }
        }

        return null;
    }

    public <T> DataSerializer<T> getSerializerFor(T value) {
        for (DataSerializer<?> serializer : this.serializers.values()) {
            if (serializer.getSerializableClass().isAssignableFrom(value.getClass())) {
                return (DataSerializer<T>) serializer;
            }
        }

        return null;
    }
}
