package com.example.xmlparser.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Модель каталога растений, получаемого из XML-файла.
 * Каталог содержит метаинформацию и список {@link Plant}.
 */
public final class Catalog {

    /**
     * Уникальный идентификатор каталога (атрибут UUID из XML).
     */
    private final String uuid;

    /**
     * Дата доставки (атрибут date из XML).
     */
    private LocalDate deliveryDate;

    /**
     * Название компании (атрибут company из XML).
     */
    private String company;

    /**
     * Список растений, входящих в каталог.
     */
    private List<Plant> plants;

    /**
     * Создаёт экземпляр каталога с обязательным UUID.
     *
     * @param uuid уникальный идентификатор каталога (не должен быть null)
     */
    public Catalog(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Возвращает UUID каталога.
     *
     * @return строка UUID
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Возвращает дату доставки.
     *
     * @return дата доставки
     */
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Устанавливает дату доставки.
     *
     * @param deliveryDate дата доставки
     */
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Возвращает название компании.
     *
     * @return название компании
     */
    public String getCompany() {
        return company;
    }

    /**
     * Устанавливает название компании.
     *
     * @param company название компании
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Возвращает список растений в каталоге.
     *
     * @return список {@link Plant}
     */
    public List<Plant> getPlants() {
        return plants;
    }

    /**
     * Устанавливает список растений для каталога.
     *
     * @param plants список растений
     */
    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }
}
