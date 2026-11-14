package dbms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Индекс по столбцу таблицы на основе красно-чёрного дерева (TreeMap).
 */
public class TableIndex implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String columnName;
    private final TreeMap<Comparable<Object>, List<Row>> tree = new TreeMap<>();

    public TableIndex(String name, String columnName) {
        this.name = name.toLowerCase();
        this.columnName = columnName.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getColumnName() {
        return columnName;
    }

    /**
     * Добавляет запись в индекс.
     */
    @SuppressWarnings("unchecked")
    public void add(Object key, Row row) {
        if (key == null) {
            return;
        }
        Comparable<Object> compKey = (Comparable<Object>) key;
        tree.computeIfAbsent(compKey, k -> new ArrayList<>()).add(row);
    }

    /**
     * Полностью перестраивает индекс по актуальному списку строк таблицы.
     */
    public void rebuild(List<Row> rows) {
        tree.clear();
        for (Row row : rows) {
            add(row.get(columnName), row);
        }
    }

    /**
     * Выполняет быстрый поиск строк по оператору сравнения.
     */
    @SuppressWarnings("unchecked")
    public List<Row> search(String operator, Object targetValue) {
        if (targetValue == null) {
            return Collections.emptyList();
        }
        Comparable<Object> key = (Comparable<Object>) targetValue;
        List<Row> result = new ArrayList<>();

        switch (operator) {
            case "=":
                List<Row> exact = tree.get(key);
                if (exact != null) {
                    result.addAll(exact);
                }
                break;
            case ">":
                collectAll(tree.tailMap(key, false), result);
                break;
            case ">=":
                collectAll(tree.tailMap(key, true), result);
                break;
            case "<":
                collectAll(tree.headMap(key, false), result);
                break;
            case "<=":
                collectAll(tree.headMap(key, true), result);
                break;
            default:
                throw new IllegalArgumentException("Индекс не поддерживает оператор: " + operator);
        }
        return result;
    }

    private void collectAll(Map<Comparable<Object>, List<Row>> subMap, List<Row> dest) {
        for (List<Row> bucket : subMap.values()) {
            dest.addAll(bucket);
        }
    }
}