package com.example.xmlparser.validator;

import com.example.xmlparser.model.Catalog;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Валидатор для объекта {@link Catalog}.
 * Выполняет проверку полей каталога: uuid, дата доставки, название компании и наличие растений.
 */
public final class CatalogValidator {

    private static final Logger logger = LoggerFactory.getLogger(CatalogValidator.class);

    /**
     * Минимальная допустимая длина названия компании
     */
    private static final int MIN_COMPANY_LENGTH = 2;

    /**
     * Максимальная допустимая длина названия компании
     */
    private static final int MAX_COMPANY_LENGTH = 100;

    /**
     * Разрешённые символы в названии компании: буквы, пробелы, тире, кавычки, запятые
     */
    private static final String COMPANY_NAME_PATTERN = "^[\\p{L}\\-\\s,\"“”«»']+$";

    private CatalogValidator() {
        // Приватный конструктор — класс утилитарный и не должен инстанцироваться
    }

    /**
     * Выполняет валидацию полей каталога.
     *
     * @param catalog объект {@link Catalog} для проверки
     * @return список строк с описаниями ошибок. Если ошибок нет — список пуст.
     */
    public static List<String> validate(Catalog catalog) {
        logger.info("Валидация каталога: " + catalog.getCompany());
        List<String> errors = new ArrayList<>();

        // UUID
        if (catalog.getUuid() == null || catalog.getUuid().isBlank()) {
            errors.add("UUID каталога не может быть пустым");
            logger.warn("Ошибка валидации: UUID пустой");
        }

        // Delivery Date
        if (catalog.getDeliveryDate() == null) {
            errors.add("Дата доставки не может быть пустой");
            logger.warn("Ошибка валидации: дата доставки не указана");
        }

        // Company
        String company = catalog.getCompany();
        if (company == null || company.isBlank()) {
            errors.add("Название компании не может быть пустым");
            logger.warn("Ошибка валидации: название компании пустое");
        } else {
            if (company.length() < MIN_COMPANY_LENGTH || company.length() > MAX_COMPANY_LENGTH) {
                errors.add("Название компании должно быть длиной от 2 до 100 символов");
                logger.warn("Ошибка валидации: название компании должно быть длиной от 2 до 100 символов");
            }
            if (!company.matches(COMPANY_NAME_PATTERN)) {
                errors.add("Название компании должно содержать только буквы, тире, запятые, кавычки или пробел");
                logger.warn("Ошибка валидации: название компании содержит недопустимые символы");
            }
        }

        // Plants presence
        if (catalog.getPlants() == null || catalog.getPlants().isEmpty()) {
            errors.add("Каталог должен содержать хотя бы одно растение");
            logger.warn("Ошибка валидации: каталог не содержит растений");
        }

        if (errors.isEmpty()) {
            logger.info("Каталог прошел валидацию успешно: " + catalog.getCompany());
        } else {
            logger.info("Каталог не прошел валидацию. Кол-во ошибок: " + errors.size());
        }

        return errors;
    }
}
