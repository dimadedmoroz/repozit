-- ====================================================================
-- Примеры SQL-запросов для консольной СУБД (dbms)
-- ====================================================================

-- 1. Просмотр стартовых таблиц предметной области
SELECT * FROM brands;
SELECT * FROM cars;

-- 2. Вставка записей в таблицу cars (с проверкой внешнего ключа на brand)
-- Полная вставка значений во все столбцы
INSERT INTO cars VALUES (1, 'Toyota', 25000.0, TRUE, '2026-11-10');

-- Вставка с явным указанием столбцов (остальные принимают значение NULL)
INSERT INTO cars (id, brand) VALUES (2, 'BMW');

-- Множественная вставка нескольких строк (кортежей) в одном запросе
INSERT INTO cars VALUES (3, 'Toyota', 18500.0, FALSE, '2026-11-12'), (4, 'Lada', 12000.0, TRUE, '2026-11-14');

-- 3. Выборка данных с фильтрацией (поддерживаются =, !=, <>, >, <, >=, <=)
SELECT brand, price FROM cars WHERE price > 20000;
SELECT * FROM cars WHERE available = TRUE;
SELECT id, brand, price FROM cars WHERE brand = 'Toyota';

-- 4. Обновление записей (UPDATE)
UPDATE cars SET price = 30000 WHERE id = 2;
UPDATE cars SET available = FALSE WHERE brand = 'Toyota';

-- 5. Удаление записей (DELETE)
DELETE FROM cars WHERE brand = 'Toyota';

-- 6. Создание индекса для ускорения выборок (на базе сбалансированного дерева TreeMap)
CREATE INDEX idx_cars_brand ON cars (brand);

-- 7. Повторная выборка по проиндексированному полю (использует индекс)
SELECT * FROM cars WHERE brand = 'BMW';

-- 8. Создание пользовательской таблицы (DDL)
CREATE TABLE owners (id INT PRIMARY KEY, full_name STRING, reg_date DATE, active BOOLEAN);

-- 9. Вставка и просмотр пользовательской таблицы
INSERT INTO owners VALUES (1, 'Ivanov I.I.', '2026-01-15', TRUE);
SELECT * FROM owners;

-- 10. Удаление таблицы
DROP TABLE owners;

-- 11. Встроенный стресс-тест производительности индексов (50 000 строк, 1000 запросов)
BENCHMARK

-- 12. Сохранение данных на диск и завершение работы
EXIT
