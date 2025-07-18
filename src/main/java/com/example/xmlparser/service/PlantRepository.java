package com.example.xmlparser.service;

import com.example.xmlparser.model.Plant;
import com.example.xmlparser.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Репозиторий для работы с таблицей f_cat_plants.
 * Отвечает за сохранение объектов Plant в базу данных.
 */
public final class PlantRepository {

    private static final Logger logger = LoggerFactory.getLogger(PlantRepository.class);

    private PlantRepository() {
        // Закрытый конструктор для утилитного класса
    }

    /**
     * Сохраняет объект Plant в таблицу f_cat_plants.
     *
     * @param plant объект растения, который необходимо сохранить
     * @throws RuntimeException если произошла ошибка при выполнении SQL-запроса
     */
    public static void save(final Plant plant) {
        final String sql = """
                INSERT INTO f_cat_plants (common, botanical, zone, light, price, availability, catalog_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        logger.debug("Попытка сохранить растение: {}", plant.getBotanical());

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plant.getCommon());
            stmt.setString(2, plant.getBotanical());
            stmt.setInt(3, plant.getZoneAsInt());
            stmt.setString(4, plant.getLight());
            stmt.setBigDecimal(5, plant.getPrice());
            stmt.setInt(6, plant.getAvailability());
            stmt.setInt(7, plant.getCatalogId());

            stmt.executeUpdate();
            logger.info("Сохранено растение с научным названием: {}", plant.getBotanical());

        } catch (SQLException e) {
            logger.error("Ошибка при сохранении растения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении растения", e);
        }
    }
}
