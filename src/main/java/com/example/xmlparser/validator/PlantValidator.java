package com.example.xmlparser.validator;

import com.example.xmlparser.model.Plant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Валидатор для объектов {@link Plant}.
 * Проверяет корректность полей растения перед сохранением в базу данных.
 */
public final class PlantValidator {

    private static final Logger logger = LoggerFactory.getLogger(PlantValidator.class);

    /**
     * Минимальная допустимая длина названия растения
     */
    private static final int MIN_NAME_LENGTH = 2;

    /**
     * Максимальная допустимая длина названия растения
     */
    private static final int MAX_NAME_LENGTH = 100;

    /**
     * Разрешённые символы в названии растения: буквы, пробелы, тире, кавычки, запятые
     */
    private static final String NAME_PATTERN = "^[\\p{L}\\-\\s,\"“”«»']+$";

    private PlantValidator() {
        // Приватный конструктор — класс утилитарный и не должен инстанцироваться
    }

    /**
     * Валидирует переданный объект {@link Plant}.
     *
     * @param plant объект растения
     * @return список ошибок (если пустой — валидация успешна)
     */
    public static List<String> validate(Plant plant) {
        List<String> errors = new ArrayList<>();
        logger.info("Начало валидации растения:" + plant.getBotanical());

        // COMMON (опциональное поле)
        String common = plant.getCommon();
        if (common != null) {
            if (common.isBlank()) {
                errors.add("Народное название заполнено, но состоит только из пробелов");
                logger.warn("Народное название состоит из пробелов");
            } else {
                if (common.length() < MIN_NAME_LENGTH || common.length() > MAX_NAME_LENGTH) {
                    errors.add("Народное название должно быть длиной от 2 до 100 символов");
                    logger.warn("Народное название должно быть длиной от 2 до 100 символов: " + common.length());
                }
                if (!common.matches(NAME_PATTERN)) {
                    errors.add("Народное название должно содержать только буквы, тире, запятую, кавычки или пробел");
                    logger.warn("Народное название содержит недопустимые символы: " + common);
                }
            }
        }

        // BOTANICAL (обязательное поле)
        String botanical = plant.getBotanical();
        if (botanical == null || botanical.isBlank()) {
            errors.add("Научное название не может быть пустым");
            logger.warn("Научное название отсутствует");
        } else {
            if (botanical.length() < MIN_NAME_LENGTH || botanical.length() > MAX_NAME_LENGTH) {
                errors.add("Научное название должно быть длиной от 2 до 100 символов");
                logger.warn("Научное название должно быть длиной от 2 до 100 символов: " + botanical.length());
            }
            if (!botanical.matches(NAME_PATTERN)) {
                errors.add("Научное название должно содержать только буквы, тире, запятую, кавычки или пробел");
                logger.warn("Научное название содержит недопустимые символы: " + botanical);
            }
        }

        // ZONE
        int zone = plant.getZoneAsInt();
        if (zone <= 0) {
            errors.add("Зона должна быть положительным числом, но значение: " + plant.getZoneText());
            logger.warn("Некорректная зона: " + plant.getZoneText());
        }

        // LIGHT
        String light = plant.getLight();
        if (light == null || light.isBlank()) {
            errors.add("Описание освещения не может быть пустым");
            logger.warn("Поле light пустое");
        }

        // PRICE
        BigDecimal price = plant.getPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Цена должна быть положительной");
            logger.warn("Цена должна быть положительной: " + price);
        }

        // AVAILABILITY
        int availability = plant.getAvailability();
        if (availability <= 0) {
            errors.add("Количество должно быть положительным числом");
            logger.warn("Количество должно быть положительным числом: " + availability);
        }

        // CATALOG ID
        int catalogId = plant.getCatalogId();
        if (catalogId <= 0) {
            errors.add("catalogId не должен быть пустым или отрицательным");
            logger.warn("catalogId не должен быть пустым или отрицательным: " + catalogId);
        }

        if (errors.isEmpty()) {
            logger.info("Валидация растения прошла успешно.");
        } else {
            logger.info("Валидация завершена с ошибками: " + errors);
        }

        return errors;
    }
}
