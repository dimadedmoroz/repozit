package dbms;

import java.time.LocalDate;

/**
 * Поддерживаемые типы данных столбцов таблицы.
 */
public enum DataType {
    INT,
    DOUBLE,
    STRING,
    DATE,
    BOOLEAN;

    /**
     * Определяет тип данных по строковому названию из SQL-запроса.
     */
    public static DataType fromString(String typeStr) {
        String clean = typeStr.trim().toUpperCase();
        if (clean.startsWith("VARCHAR")) {
            return STRING;
        }
        if (clean.equals("INTEGER")) {
            return INT;
        }
        return DataType.valueOf(clean);
    }

    /**
     * Преобразует строковое значение из запроса в объект нужного типа Java.
     */
    public Object parse(String rawValue) {
        if (rawValue == null || rawValue.trim().equalsIgnoreCase("NULL")) {
            return null;
        }
        String val = rawValue.trim();
        switch (this) {
            case INT:
                return Integer.parseInt(val);
            case DOUBLE:
                return Double.parseDouble(val);
            case BOOLEAN:
                if (!val.equalsIgnoreCase("true") && !val.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Ожидалось TRUE или FALSE, получено: " + val);
                }
                return Boolean.parseBoolean(val);
            case DATE:
                return LocalDate.parse(stripQuotes(val));
            case STRING:
                return stripQuotes(val);
            default:
                throw new IllegalArgumentException("Неизвестный тип данных: " + this);
        }
    }

    private static String stripQuotes(String s) {
        if (s.length() >= 2 && s.startsWith("'") && s.endsWith("'")) {
            return s.substring(1, s.length() - 1);
        }
        throw new IllegalArgumentException("Строки и даты должны быть в одинарных кавычках: " + s);
    }
}