package com.example.xmlparser.validator;

import com.example.xmlparser.model.Catalog;
import com.example.xmlparser.model.Plant;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для проверки валидации объектов Catalog.
 */
public class CatalogValidatorTest {

    /**
     * Проверяет, что валидный каталог проходит валидацию без ошибок.
     */
    @Test
    public void testValidCatalog() {
        Catalog catalog = new Catalog("abc-123");
        catalog.setDeliveryDate(LocalDate.now());
        catalog.setCompany("ООО \"Зелёная планета\"");
        catalog.setPlants(List.of(new Plant()));

        List<String> errors = CatalogValidator.validate(catalog);
        assertTrue(errors.isEmpty());
    }

    /**
     * Проверяет, что если все поля каталога недопустимы,
     * то метод валидации возвращает список из 4 ошибок.
     */
    @Test
    public void testInvalidCatalog_allFieldsInvalid() {
        Catalog catalog = new Catalog("");
        catalog.setDeliveryDate(null);
        catalog.setCompany(" ");
        catalog.setPlants(Collections.emptyList());

        List<String> errors = CatalogValidator.validate(catalog);
        assertFalse(errors.isEmpty());
        assertEquals(4, errors.size());
    }

    /**
     * Проверяет, что слишком короткое название компании вызывает ошибку валидации.
     */
    @Test
    public void testInvalidCatalog_companyTooShort() {
        Catalog catalog = new Catalog("uuid-123");
        catalog.setDeliveryDate(LocalDate.now());
        catalog.setCompany("A");
        catalog.setPlants(List.of(new Plant()));

        List<String> errors = CatalogValidator.validate(catalog);
        assertTrue(errors.stream().anyMatch(e -> e.contains("длиной от 2 до 100")));
    }

    /**
     * Проверяет, что слишком длинное название компании вызывает ошибку валидации.
     */
    @Test
    public void testInvalidCatalog_companyTooLong() {
        Catalog catalog = new Catalog("uuid-456");
        catalog.setDeliveryDate(LocalDate.now());
        catalog.setCompany("A".repeat(101));
        catalog.setPlants(List.of(new Plant()));

        List<String> errors = CatalogValidator.validate(catalog);
        assertTrue(errors.stream().anyMatch(e -> e.contains("длиной от 2 до 100")));
    }

    /**
     * Проверяет, что компания с недопустимыми символами вызывает ошибку валидации.
     */
    @Test
    public void testInvalidCatalog_companyWithInvalidChars() {
        Catalog catalog = new Catalog("uuid-789");
        catalog.setDeliveryDate(LocalDate.now());
        catalog.setCompany("My Company @#$");
        catalog.setPlants(List.of(new Plant()));

        List<String> errors = CatalogValidator.validate(catalog);
        assertTrue(errors.stream().anyMatch(e -> e.contains("должно содержать только буквы")));
    }
}