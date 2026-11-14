package dbms;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Точка входа в консольное приложение СУБД.
 */
public class Main {

    public static void main(String[] args) {
        DatabaseRepository repository = new DatabaseRepository("database.dat");
        Database database = repository.load();
        QueryParser parser = new QueryParser();

        System.out.println("СУБД запущена. Доступны таблицы: brands, cars.");
        System.out.println("Введите SQL-запрос, BENCHMARK для теста индексов или EXIT для выхода.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("sql> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.equalsIgnoreCase("EXIT") || line.equalsIgnoreCase("QUIT")) {
                    repository.save(database);
                    System.out.println("Данные сохранены. Выход.");
                    break;
                }
                if (line.equalsIgnoreCase("BENCHMARK")) {
                    runBenchmark(database);
                    continue;
                }

                try {
                    parser.execute(line, database, repository);
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Сравнивает время поиска с индексом и без индекса на 50 000 записей.
     */
    private static void runBenchmark(Database db) {
        String tempName = "bench_temp";
        try {
            Table bench = new Table(tempName, Arrays.asList(
                    new Column("id", DataType.INT, true),
                    new Column("code", DataType.STRING, false)
            ));
            db.createTable(bench);

            int totalRows = 50_000;
            for (int i = 0; i < totalRows; i++) {
                bench.insert(null, Arrays.asList(String.valueOf(i), "'CODE_" + i + "'"), db);
            }

            long startNoIdx = System.nanoTime();
            for (int i = 0; i < 1_000; i++) {
                bench.select("code", "=", "'CODE_45000'");
            }
            long timeNoIdxMs = (System.nanoTime() - startNoIdx) / 1_000_000;

            bench.createIndex("idx_bench_code", "code");

            long startWithIdx = System.nanoTime();
            for (int i = 0; i < 1_000; i++) {
                bench.select("code", "=", "'CODE_45000'");
            }
            long timeWithIdxMs = (System.nanoTime() - startWithIdx) / 1_000_000;

            System.out.println("Тест на " + totalRows + " записях (1000 запросов SELECT):");
            System.out.println("Без индекса: " + timeNoIdxMs + " мс");
            System.out.println("С индексом:  " + timeWithIdxMs + " мс");
        } finally {
            try {
                db.dropTable(tempName);
            } catch (Exception ignored) {
            }
        }
    }
}