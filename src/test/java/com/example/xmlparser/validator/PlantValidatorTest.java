package com.example.xmlparser.validator;

import com.example.xmlparser.model.Plant;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для проверки валидации объектов Plant.
 */
public class PlantValidatorTest {

    /**
     * Проверяет, что корректное растение проходит валидацию без ошибок.
     */
    @Test
    public void testValidPlant() {
        Plant plant = new Plant();
        plant.setCommon("Фиалка, \"Пёсий Клык\"");
        plant.setBotanical("Erythronium americanum");
        plant.setZoneText("4");
        plant.setLight("Тень");
        plant.setPrice(new BigDecimal("9.99"));
        plant.setAvailability(10);
        plant.setCatalogId(1);

        List<String> errors = PlantValidator.validate(plant);
        assertTrue(errors.isEmpty());
    }

    /**
     * Проверяет, что растение с отсутствующими и недопустимыми значениями
     * вызывает соответствующие ошибки валидации.
     */
    @Test
    public void testInvalidPlant_emptyRequiredFields() {
        Plant plant = new Plant();
        plant.setBotanical(null);
        plant.setZoneText("0");
        plant.setLight("");
        plant.setPrice(new BigDecimal("-5"));
        plant.setAvailability(-1);
        plant.setCatalogId(0);

        List<String> errors = PlantValidator.validate(plant);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("Научное название не может быть пустым")));
    }

    /**
     * Проверяет, что слишком короткое общее название вызывает ошибку валидации.
     */
    @Test
    public void testInvalidPlant_commonTooShort() {
        Plant plant = new Plant();
        plant.setCommon("A");
        plant.setBotanical("Valid");
        plant.setZoneText("1");
        plant.setLight("Свет");
        plant.setPrice(BigDecimal.ONE);
        plant.setAvailability(5);
        plant.setCatalogId(1);

        List<String> errors = PlantValidator.validate(plant);
        assertTrue(errors.stream().anyMatch(e -> e.contains("длиной от 2 до 100")));
    }

    /**
     * Проверяет, что общее название с цифрами и спецсимволами вызывает ошибку.
     */
    @Test
    public void testInvalidPlant_commonWithNumbersAndSymbols() {
        Plant plant = new Plant();
        plant.setCommon("123$$$");
        plant.setBotanical("Valid");
        plant.setZoneText("1");
        plant.setLight("Свет");
        plant.setPrice(BigDecimal.ONE);
        plant.setAvailability(5);
        plant.setCatalogId(1);

        List<String> errors = PlantValidator.validate(plant);
        assertTrue(errors.stream().anyMatch(e -> e.contains("должно содержать только буквы")));
    }

    /**
     * Проверяет, что слишком длинное ботаническое название вызывает ошибку валидации.
     */
    @Test
    public void testInvalidPlant_botanicalTooLong() {
        Plant plant = new Plant();
        plant.setCommon("Valid");
        plant.setBotanical("A".repeat(101));
        plant.setZoneText("1");
        plant.setLight("Свет");
        plant.setPrice(BigDecimal.ONE);
        plant.setAvailability(5);
        plant.setCatalogId(1);

        List<String> errors = PlantValidator.validate(plant);
        assertTrue(errors.stream().anyMatch(e -> e.contains("длиной от 2 до 100")));
    }
}