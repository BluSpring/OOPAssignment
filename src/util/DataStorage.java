package util;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DataStorage {
    private final Map<String, Object> internalMap = new HashMap<>();
    private final Path path;

    public DataStorage(Path path) {
        this.path = path;
    }

    public File getFile() {
        return this.path.toFile();
    }

    public Iterable<String> keys() {
        return this.internalMap.keySet();
    }

    public boolean contains(String key) {
        return this.internalMap.containsKey(key);
    }

    public <T> T getValue(String key) {
        return (T) this.internalMap.get(key);
    }

    public <T> T getValueOrElse(String key, T defaultValue) {
        return (T) this.internalMap.getOrDefault(key, defaultValue);
    }

    public int getInt(String key) {
        return this.getValue(key);
    }

    public float getFloat(String key) {
        return this.getValue(key);
    }

    public double getDouble(String key) {
        return this.getValue(key);
    }

    public String getString(String key) {
        return this.getValue(key);
    }

    public <T> void setValue(String key, T value) {
        this.internalMap.put(key, value);
    }

    public void load() {
        var file = this.getFile();
        if (file.exists()) {
            
        }
    }
}
