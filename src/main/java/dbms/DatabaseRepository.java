package dbms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

/**
 * Репозиторий для сохранения и загрузки базы данных на диск (паттерн Repository).
 * Использует промежуточный файл для защиты от повреждения при сбоях.
 */
public class DatabaseRepository {
    private final Path dbFile;
    private final Path tempFile;

    public DatabaseRepository(String fileName) {
        this.dbFile = Paths.get(fileName);
        this.tempFile = Paths.get(fileName + ".tmp");
    }

    /**
     * Сохраняет текущее состояние БД на диск.
     */
    public void save(Database db) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile.toFile()))) {
            oos.writeObject(db);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи БД на диск: " + e.getMessage(), e);
        }

        try {
            Files.move(tempFile, dbFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка замены основного файла БД: " + e.getMessage(), e);
        }
    }

    /**
     * Загружает БД с диска или инициализирует стартовую схему при первом запуске.
     */
    public Database load() {
        if (Files.exists(dbFile)) {
            return readFromFile(dbFile);
        }
        if (Files.exists(tempFile)) {
            return readFromFile(tempFile);
        }
        Database initial = createDefaultDatabase();
        save(initial);
        return initial;
    }

    private Database readFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Database) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить файл БД '" + path + "': " + e.getMessage(), e);
        }
    }

    /**
     * Создаёт две связанные таблицы предметной области (brands и cars).
     */
    private Database createDefaultDatabase() {
        Database db = new Database();

        Table brands = new Table("brands", Arrays.asList(
                new Column("name", DataType.STRING, true),
                new Column("country", DataType.STRING, false)
        ));
        brands.insert(null, Arrays.asList("'Toyota'", "'Japan'"), db);
        brands.insert(null, Arrays.asList("'BMW'", "'Germany'"), db);
        brands.insert(null, Arrays.asList("'Lada'", "'Russia'"), db);
        db.createTable(brands);

        Table cars = new Table("cars", Arrays.asList(
                new Column("id", DataType.INT, true),
                new Column("brand", DataType.STRING, false, "brands", "name"),
                new Column("price", DataType.DOUBLE, false),
                new Column("available", DataType.BOOLEAN, false),
                new Column("added_date", DataType.DATE, false)
        ));
        db.createTable(cars);

        return db;
    }
}