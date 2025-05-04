package util.data;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static <T> DataSerializer<T> getSerializerFor(Class<T> clazz) {
        for (DataSerializer<?> serializer : serializers.values()) {
            if (serializer.getSerializableClass().isAssignableFrom(clazz)) {
                return (DataSerializer<T>) serializer;
            }
        }

        return null;
    }

    public static <T> void serializeValues(Class<T> serializable, File file, List<T> list) {
        try {
            var serializer = getSerializerFor(serializable);

            if (!file.exists()) {
                file.createNewFile();
            }

            var lines = new ArrayList<String>();

            for (T value : list) {
                lines.add(serializer.serialize(value));
            }

            Files.write(file.toPath(), lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void deserializeLines(Class<T> serializable, File file, List<T> list) {
        try {
            if (file.exists()) {
                var serializer = getSerializerFor(serializable);
                var lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

                for (String line : lines) {
                    list.add(serializer.deserialize(line));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
