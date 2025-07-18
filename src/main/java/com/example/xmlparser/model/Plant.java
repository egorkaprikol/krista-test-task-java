package com.example.xmlparser.model;

import java.math.BigDecimal;

/**
 * Модель растения, входящего в каталог.
 * Отражает структуру XML-элемента PLANT.
 */
public final class Plant {

    /**
     * Народное название растения (тег COMMON).
     */
    private String common;

    /**
     * Ботаническое (научное) название растения (тег BOTANICAL).
     */
    private String botanical;

    /**
     * Зона выращивания (тег ZONE), хранится в виде текста,
     * так как может быть повреждена или некорректна.
     */
    private String zoneText;

    /**
     * Условия освещения (тег LIGHT).
     */
    private String light;

    /**
     * Цена растения в долларах (тег PRICE), без знака $.
     */
    private BigDecimal price;

    /**
     * Количество растений в наличии (тег AVAILABILITY).
     */
    private int availability;

    /**
     * Идентификатор каталога, к которому принадлежит растение (связь с таблицей D_CAT_CATALOG).
     */
    private int catalogId;

    // --- Геттеры и сеттеры ---

    /**
     * @return народное название растения
     */
    public String getCommon() {
        return common;
    }

    /**
     * Устанавливает народное название растения.
     *
     * @param common народное название растения
     */
    public void setCommon(String common) {
        this.common = common;
    }

    /**
     * @return ботаническое (научное) название растения
     */
    public String getBotanical() {
        return botanical;
    }

    /**
     * Устанавливает ботаническое (научное) название растения.
     *
     * @param botanical ботаническое (научное) название растения
     */
    public void setBotanical(String botanical) {
        this.botanical = botanical;
    }

    /**
     * @return текстовое значение зоны выращивания
     */
    public String getZoneText() {
        return zoneText;
    }

    /**
     * Устанавливает текстовое значение зоны выращивания.
     *
     * @param zoneText текстовое значение зоны выращивания
     */
    public void setZoneText(String zoneText) {
        this.zoneText = zoneText;
    }

    /**
     * @return условия освещения
     */
    public String getLight() {
        return light;
    }

    /**
     * Устанавливает условия освещения.
     *
     * @param light условия освещения
     */
    public void setLight(String light) {
        this.light = light;
    }

    /**
     * @return цена растения
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Устанавливает цену растения.
     *
     * @param price цена растения
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return количество в наличии
     */
    public int getAvailability() {
        return availability;
    }

    /**
     * Устанавливает количество в наличии.
     *
     * @param availability количество в наличии
     */
    public void setAvailability(int availability) {
        this.availability = availability;
    }

    /**
     * @return идентификатор каталога, к которому относится растение
     */
    public int getCatalogId() {
        return catalogId;
    }

    /**
     * Устанавливает идентификатор каталога.
     *
     * @param catalogId идентификатор каталога
     */
    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    /**
     * Преобразует поле zoneText в целое число.
     * Если строка не является числом, возвращает -1.
     *
     * @return числовое значение зоны или -1, если невалидное
     */
    public int getZoneAsInt() {
        try {
            return Integer.parseInt(zoneText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
