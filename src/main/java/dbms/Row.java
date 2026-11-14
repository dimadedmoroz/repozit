package dbms;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Одна запись (строка) в таблице.
 */
public class Row implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Object> values = new LinkedHashMap<>();

    public Object get(String columnName) {
        return values.get(columnName.toLowerCase());
    }

    public void set(String columnName, Object value) {
        values.put(columnName.toLowerCase(), value);
    }
}