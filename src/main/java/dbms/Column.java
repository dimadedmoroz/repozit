package dbms;

import java.io.Serializable;

/**
 * Описание столбца таблицы, включая ограничения первичного и внешнего ключей.
 */
public class Column implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final DataType type;
    private final boolean primaryKey;
    private final String refTable;
    private final String refColumn;

    public Column(String name, DataType type, boolean primaryKey) {
        this(name, type, primaryKey, null, null);
    }

    public Column(String name, DataType type, boolean primaryKey, String refTable, String refColumn) {
        this.name = name.toLowerCase();
        this.type = type;
        this.primaryKey = primaryKey;
        this.refTable = refTable != null ? refTable.toLowerCase() : null;
        this.refColumn = refColumn != null ? refColumn.toLowerCase() : null;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getRefTable() {
        return refTable;
    }

    public String getRefColumn() {
        return refColumn;
    }
}