package com.example.xmlparser.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Утилитный класс для управления подключением к базе данных.
 * Загружает настройки из файла .env и предоставляет методы
 * для получения и закрытия соединений.
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private static final String url;
    private static final String username;
    private static final String password;

    // Статический блок инициализации — загружается один раз при старте приложения
    static {
        logger.info("Загрузка конфигурации подключения к базе данных из .env...");

        try {
            Dotenv dotenv = Dotenv.load();

            String host = dotenv.get("DB_HOST");
            String port = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            username = dotenv.get("DB_USERNAME");
            password = dotenv.get("DB_PASSWORD");

            if (host == null || port == null || dbName == null || username == null || password == null) {
                throw new RuntimeException("Отсутствуют обязательные параметры подключения в .env");
            }

            url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

            // Регистрация драйвера PostgreSQL
            Class.forName("org.postgresql.Driver");
            logger.info("Драйвер PostgreSQL зарегистрирован");
            logger.info("URL подключения к базе данных: {}", url);

        } catch (Exception e) {
            logger.error("Ошибка при инициализации DatabaseManager: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при инициализации DatabaseManager", e);
        }
    }

    /**
     * Получает новое соединение с базой данных.
     *
     * @return объект {@link Connection}
     * @throws SQLException если не удалось установить соединение
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Закрывает соединение, если оно не null.
     *
     * @param connection соединение, подлежащее закрытию
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Соединение успешно закрыто");
            } catch (SQLException e) {
                logger.warn("Ошибка при закрытии соединения с БД: {}", e.getMessage(), e);
            }
        } else {
            logger.debug("Соединение уже было null — закрытие не требуется");
        }
    }
}
