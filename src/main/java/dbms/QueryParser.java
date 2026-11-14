package dbms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер и исполнитель упрощённого SQL-подобного языка запросов.
 */
public class QueryParser {

    private static final Pattern SELECT_PATTERN = Pattern.compile(
            "^SELECT\\s+(.+?)\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(\\w+)\\s*(>=|<=|!=|<>|=|>|<)\\s*(.+))?$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern INSERT_PATTERN = Pattern.compile(
            "^INSERT\\s+INTO\\s+(\\w+)(?:\\s*\\(([^)]+)\\))?\\s+VALUES\\s*(.+)$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern UPDATE_PATTERN = Pattern.compile(
            "^UPDATE\\s+(\\w+)\\s+SET\\s+(\\w+)\\s*=\\s*(.+?)(?:\\s+WHERE\\s+(\\w+)\\s*(>=|<=|!=|<>|=|>|<)\\s*(.+))?$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern DELETE_PATTERN = Pattern.compile(
            "^DELETE\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(\\w+)\\s*(>=|<=|!=|<>|=|>|<)\\s*(.+))?$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern CREATE_TABLE_PATTERN = Pattern.compile(
            "^CREATE\\s+TABLE\\s+(\\w+)\\s*\\((.+)\\)$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern DROP_TABLE_PATTERN = Pattern.compile(
            "^DROP\\s+TABLE\\s+(\\w+)$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern CREATE_INDEX_PATTERN = Pattern.compile(
            "^CREATE\\s+INDEX\\s+(\\w+)\\s+ON\\s+(\\w+)\\s*\\((\\w+)\\)$",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Разбирает строку запроса, выполняет операцию над БД и при необходимости сохраняет изменения.
     */
    public void execute(String rawQuery, Database db, DatabaseRepository repo) {
        String query = rawQuery.trim();
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1).trim();
        }
        if (query.isEmpty()) {
            return;
        }

        Matcher m;

        if ((m = SELECT_PATTERN.matcher(query)).matches()) {
            handleSelect(m, db);
        } else if ((m = INSERT_PATTERN.matcher(query)).matches()) {
            handleInsert(m, db);
            repo.save(db);
        } else if ((m = UPDATE_PATTERN.matcher(query)).matches()) {
            handleUpdate(m, db);
            repo.save(db);
        } else if ((m = DELETE_PATTERN.matcher(query)).matches()) {
            handleDelete(m, db);
            repo.save(db);
        } else if ((m = CREATE_TABLE_PATTERN.matcher(query)).matches()) {
            handleCreateTable(m, db);
            repo.save(db);
        } else if ((m = DROP_TABLE_PATTERN.matcher(query)).matches()) {
            db.dropTable(m.group(1));
            repo.save(db);
            System.out.println("Таблица '" + m.group(1) + "' удалена.");
        } else if ((m = CREATE_INDEX_PATTERN.matcher(query)).matches()) {
            Table table = db.getTable(m.group(2));
            table.createIndex(m.group(1), m.group(3));
            repo.save(db);
            System.out.println("Индекс '" + m.group(1) + "' создан.");
        } else {
            throw new IllegalArgumentException("Синтаксическая ошибка в запросе: " + query);
        }
    }

    private void handleSelect(Matcher m, Database db) {
        String colsPart = m.group(1).trim();
        Table table = db.getTable(m.group(2));
        String whereCol = m.group(3);
        String whereOp = m.group(4);
        String whereVal = m.group(5);

        List<String> headers = new ArrayList<>();
        if (colsPart.equals("*")) {
            for (Column c : table.getColumns()) {
                headers.add(c.getName());
            }
        } else {
            for (String rawCol : colsPart.split(",")) {
                Column c = table.getColumn(rawCol.trim());
                headers.add(c.getName());
            }
        }

        List<Row> rows = table.select(whereCol, whereOp, whereVal);
        TablePrinter.print(headers, rows);
    }

    private void handleInsert(Matcher m, Database db) {
        Table table = db.getTable(m.group(1));
        String colsPart = m.group(2);
        String valuesPart = m.group(3).trim();

        List<String> targetCols = null;
        if (colsPart != null) {
            targetCols = new ArrayList<>();
            for (String c : colsPart.split(",")) {
                targetCols.add(c.trim());
            }
        }

        List<String> tuples = splitTuples(valuesPart);
        for (String tuple : tuples) {
            List<String> vals = splitArguments(tuple);
            table.insert(targetCols, vals, db);
        }
        System.out.println("Добавлено записей: " + tuples.size());
    }

    private void handleUpdate(Matcher m, Database db) {
        Table table = db.getTable(m.group(1));
        int updated = table.update(m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), db);
        System.out.println("Обновлено записей: " + updated);
    }

    private void handleDelete(Matcher m, Database db) {
        Table table = db.getTable(m.group(1));
        int deleted = table.delete(m.group(2), m.group(3), m.group(4));
        System.out.println("Удалено записей: " + deleted);
    }

    private void handleCreateTable(Matcher m, Database db) {
        String tableName = m.group(1);
        String[] colDefs = m.group(2).split(",");
        List<Column> columns = new ArrayList<>();

        for (String def : colDefs) {
            String[] tokens = def.trim().split("\\s+");
            if (tokens.length < 2) {
                throw new IllegalArgumentException("Некорректное описание столбца: " + def);
            }
            String colName = tokens[0];
            DataType type = DataType.fromString(tokens[1]);
            boolean pk = def.toUpperCase().contains("PRIMARY KEY");
            columns.add(new Column(colName, type, pk));
        }

        db.createTable(new Table(tableName, columns));
        System.out.println("Таблица '" + tableName + "' создана.");
    }

    /**
     * Разбивает блок "(a, b), (c, d)" на отдельные кортежи значений без скобок.
     */
    private List<String> splitTuples(String input) {
        List<String> tuples = new ArrayList<>();
        boolean inQuotes = false;
        int depth = 0;
        int start = -1;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '\'') {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                if (ch == '(') {
                    if (depth == 0) start = i + 1;
                    depth++;
                } else if (ch == ')') {
                    depth--;
                    if (depth == 0 && start != -1) {
                        tuples.add(input.substring(start, i));
                        start = -1;
                    }
                }
            }
        }
        if (tuples.isEmpty()) {
            throw new IllegalArgumentException("Некорректный формат VALUES: " + input);
        }
        return tuples;
    }

    /**
     * Разбивает аргументы внутри кортежа по запятой, игнорируя запятые внутри кавычек.
     */
    private List<String> splitArguments(String tuple) {
        List<String> args = new ArrayList<>();
        boolean inQuotes = false;
        int start = 0;

        for (int i = 0; i < tuple.length(); i++) {
            char ch = tuple.charAt(i);
            if (ch == '\'') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                args.add(tuple.substring(start, i).trim());
                start = i + 1;
            }
        }
        args.add(tuple.substring(start).trim());
        return args;
    }
}