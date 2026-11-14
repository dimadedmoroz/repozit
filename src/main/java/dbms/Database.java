package dbms;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Контейнер таблиц базы данных.
 */
public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Table> tables = new LinkedHashMap<>();

    public void createTable(Table table) {
        if (tables.containsKey(table.getName())) {
            throw new IllegalArgumentException("Таблица '" + table.getName() + "' уже существует");
        }
        tables.put(table.getName(), table);
    }

    public void dropTable(String name) {
        String key = name.toLowerCase();
        if (!tables.containsKey(key)) {
            throw new IllegalArgumentException("Таблица '" + name + "' не найдена");
        }
        tables.remove(key);
    }

    public Table getTable(String name) {
        Table table = tables.get(name.toLowerCase());
        if (table == null) {
            throw new IllegalArgumentException("Таблица '" + name + "' не существует");
        }
        return table;
    }
}