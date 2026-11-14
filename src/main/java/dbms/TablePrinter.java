package dbms;

import java.util.ArrayList;
import java.util.List;

/**
 * Утилита для вывода результатов выборки в виде ASCII-таблицы.
 */
public class TablePrinter {

    public static void print(List<String> headers, List<Row> rows) {
        int colCount = headers.size();
        int[] widths = new int[colCount];

        for (int i = 0; i < colCount; i++) {
            widths[i] = headers.get(i).length();
        }

        List<List<String>> formattedRows = new ArrayList<>();
        for (Row row : rows) {
            List<String> line = new ArrayList<>(colCount);
            for (int i = 0; i < colCount; i++) {
                Object val = row.get(headers.get(i));
                String text = (val == null) ? "NULL" : val.toString();
                line.add(text);
                if (text.length() > widths[i]) {
                    widths[i] = text.length();
                }
            }
            formattedRows.add(line);
        }

        String separator = buildSeparator(widths);
        System.out.println(separator);
        System.out.println(buildRowLine(headers, widths));
        System.out.println(separator);

        for (List<String> line : formattedRows) {
            System.out.println(buildRowLine(line, widths));
        }
        System.out.println(separator);
        System.out.println("Найдено записей: " + rows.size());
    }

    private static String buildSeparator(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            for (int i = 0; i < w + 2; i++) {
                sb.append('-');
            }
            sb.append('+');
        }
        return sb.toString();
    }

    private static String buildRowLine(List<String> cells, int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < cells.size(); i++) {
            sb.append(' ').append(String.format("%-" + widths[i] + "s", cells.get(i))).append(" |");
        }
        return sb.toString();
    }
}