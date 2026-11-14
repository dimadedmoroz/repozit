package dbms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Таблица базы данных. Реализует Iterable для последовательного обхода строк.
 */
public class Table implements Serializable, Iterable<Row> {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Column> columns;
    private final Map<String, Column> columnMap = new LinkedHashMap<>();
    private final List<Row> rows = new ArrayList<>();
    private final Map<String, TableIndex> indexes = new HashMap<>();

    public Table(String name, List<Column> columns) {
        this.name = name.toLowerCase();
        this.columns = new ArrayList<>(columns);
        for (Column col : columns) {
            if (columnMap.containsKey(col.getName())) {
                throw new IllegalArgumentException("Дублирующийся столбец: " + col.getName());
            }
            columnMap.put(col.getName(), col);
        }
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public Column getColumn(String colName) {
        Column col = columnMap.get(colName.toLowerCase());
        if (col == null) {
            throw new IllegalArgumentException("Столбец '" + colName + "' не найден в таблице '" + name + "'");
        }
        return col;
    }

    public boolean hasIndex(String colName) {
        return indexes.containsKey(colName.toLowerCase());
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    /**
     * Создаёт индекс по указанному столбцу и наполняет его текущими данными.
     */
    public void createIndex(String indexName, String colName) {
        Column col = getColumn(colName);
        if (indexes.containsKey(col.getName())) {
            throw new IllegalArgumentException("Индекс для столбца '" + col.getName() + "' уже существует");
        }
        TableIndex index = new TableIndex(indexName, col.getName());
        index.rebuild(rows);
        indexes.put(col.getName(), index);
    }

    /**
     * Вставляет новую запись с проверкой первичного и внешнего ключей.
     */
    public void insert(List<String> targetColNames, List<String> rawValues, Database db) {
        Row row = new Row();
        for (Column c : columns) {
            row.set(c.getName(), null);
        }

        if (targetColNames == null || targetColNames.isEmpty()) {
            if (rawValues.size() > columns.size()) {
                throw new IllegalArgumentException("Передано больше значений, чем столбцов в таблице");
            }
            for (int i = 0; i < rawValues.size(); i++) {
                Column col = columns.get(i);
                row.set(col.getName(), col.getType().parse(rawValues.get(i)));
            }
        } else {
            if (targetColNames.size() != rawValues.size()) {
                throw new IllegalArgumentException("Количество столбцов не совпадает с количеством значений");
            }
            for (int i = 0; i < targetColNames.size(); i++) {
                Column col = getColumn(targetColNames.get(i));
                row.set(col.getName(), col.getType().parse(rawValues.get(i)));
            }
        }

        validateRow(row, null, db);
        rows.add(row);

        for (TableIndex index : indexes.values()) {
            index.add(row.get(index.getColumnName()), row);
        }
    }

    /**
     * Выполняет выборку строк с учётом условия WHERE и доступных индексов.
     */
    public List<Row> select(String whereCol, String whereOp, String whereRawVal) {
        if (whereCol == null) {
            return new ArrayList<>(rows);
        }

        Column col = getColumn(whereCol);
        Object targetVal = col.getType().parse(whereRawVal);

        // Если по столбцу есть индекс и оператор поддерживается деревом — используем индекс
        if (indexes.containsKey(col.getName()) && !whereOp.equals("!=") && !whereOp.equals("<>")) {
            return indexes.get(col.getName()).search(whereOp, targetVal);
        }

        // Иначе выполняем полный проход с помощью итератора
        List<Row> result = new ArrayList<>();
        for (Row row : this) {
            if (matches(row.get(col.getName()), whereOp, targetVal)) {
                result.add(row);
            }
        }
        return result;
    }

    /**
     * Обновляет записи, подходящие под условие WHERE.
     */
    public int update(String setColName, String setRawVal, String whereCol, String whereOp, String whereRawVal, Database db) {
        Column setCol = getColumn(setColName);
        Object newVal = setCol.getType().parse(setRawVal);

        List<Row> matched = select(whereCol, whereOp, whereRawVal);
        for (Row row : matched) {
            Object oldVal = row.get(setCol.getName());
            row.set(setCol.getName(), newVal);
            try {
                validateRow(row, row, db);
            } catch (RuntimeException e) {
                row.set(setCol.getName(), oldVal);
                throw e;
            }
        }

        if (!matched.isEmpty() && !indexes.isEmpty()) {
            for (TableIndex index : indexes.values()) {
                index.rebuild(rows);
            }
        }
        return matched.size();
    }

    /**
     * Удаляет записи, удовлетворяющие условию WHERE.
     */
    public int delete(String whereCol, String whereOp, String whereRawVal) {
        if (whereCol == null) {
            int count = rows.size();
            rows.clear();
            for (TableIndex index : indexes.values()) {
                index.rebuild(rows);
            }
            return count;
        }

        Column col = getColumn(whereCol);
        Object targetVal = col.getType().parse(whereRawVal);

        int removed = 0;
        Iterator<Row> it = rows.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            if (matches(row.get(col.getName()), whereOp, targetVal)) {
                it.remove();
                removed++;
            }
        }

        if (removed > 0 && !indexes.isEmpty()) {
            for (TableIndex index : indexes.values()) {
                index.rebuild(rows);
            }
        }
        return removed;
    }

    private void validateRow(Row candidate, Row ignoreSelf, Database db) {
        for (Column col : columns) {
            Object val = candidate.get(col.getName());

            if (col.isPrimaryKey()) {
                if (val == null) {
                    throw new IllegalArgumentException("Первичный ключ '" + col.getName() + "' не может быть NULL");
                }
                for (Row existing : rows) {
                    if (existing != ignoreSelf && Objects.equals(existing.get(col.getName()), val)) {
                        throw new IllegalArgumentException("Нарушение уникальности первичного ключа: " + val);
                    }
                }
            }

            if (col.getRefTable() != null && val != null && db != null) {
                Table parentTable = db.getTable(col.getRefTable());
                boolean found = false;
                for (Row parentRow : parentTable) {
                    if (Objects.equals(parentRow.get(col.getRefColumn()), val)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException(
                            "Нарушение внешнего ключа: значение '" + val + "' отсутствует в "
                                    + col.getRefTable() + "(" + col.getRefColumn() + ")"
                    );
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean matches(Object actual, String op, Object target) {
        if (actual == null || target == null) {
            if (op.equals("=")) return actual == target;
            if (op.equals("!=") || op.equals("<>")) return actual != target;
            return false;
        }
        Comparable<Object> a = (Comparable<Object>) actual;
        int cmp = a.compareTo(target);
        switch (op) {
            case "=":  return cmp == 0;
            case "!=":
            case "<>": return cmp != 0;
            case ">":  return cmp > 0;
            case ">=": return cmp >= 0;
            case "<":  return cmp < 0;
            case "<=": return cmp <= 0;
            default:   throw new IllegalArgumentException("Неподдерживаемый оператор: " + op);
        }
    }
}