@echo off
chcp 65001 > nul
echo [DBMS] Сборка проекта...
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src/main/java/dbms/*.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Ошибка при компиляции исходного кода.
    pause
    exit /b %ERRORLEVEL%
)
echo [DBMS] Запуск СУБД...
java -Dfile.encoding=UTF-8 -cp bin dbms.Main
