package com.example.xmlparser.service;

import com.example.xmlparser.model.Catalog;
import com.example.xmlparser.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Репозиторий для работы с таблицей d_cat_catalog в базе данных.
 * Отвечает за сохранение информации о каталоге.
 */
public final class CatalogRepository {

    private static final Logger logger = LoggerFactory.getLogger(CatalogRepository.class);

    private CatalogRepository() {
        // Закрытый конструктор — утилитный класс
    }

    /**
     * Сохраняет каталог в базу данных и возвращает сгенерированный идентификатор.
     *
     * @param catalog объект Catalog для сохранения
     * @return сгенерированный ID записи в таблице
     * @throws RuntimeException если сохранение не удалось
     */
    public static int save(final Catalog catalog) {
        final String sql = "INSERT INTO d_cat_catalog (uuid, company, delivery_date) VALUES (?, ?, ?) RETURNING id";

        logger.debug("Попытка сохранить каталог: {}", catalog.getCompany());

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, catalog.getUuid());
            stmt.setString(2, catalog.getCompany());
            stmt.setDate(3, Date.valueOf(catalog.getDeliveryDate()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    final int id = rs.getInt("id");
                    logger.info("Каталог успешно сохранён с ID = {}", id);
                    return id;
                } else {
                    logger.error("Сохранение каталога не вернуло ID");
                    throw new SQLException("Сохранение каталога не вернуло ID");
                }
            }

        } catch (SQLException e) {
            logger.error("Ошибка при сохранении каталога: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении каталога", e);
        }
    }
}
