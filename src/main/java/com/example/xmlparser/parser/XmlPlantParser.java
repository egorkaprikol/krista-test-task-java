package com.example.xmlparser.parser;

import com.example.xmlparser.model.Catalog;
import com.example.xmlparser.model.Plant;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Утилитный класс для парсинга XML-файлов с каталогами растений.
 */
public final class XmlPlantParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPlantParser.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private XmlPlantParser() {
        // Запрещаем создание экземпляров утилитного класса
    }

    /**
     * Парсит все XML-файлы в указанной директории и возвращает список каталогов.
     *
     * @param folderPath путь к директории с XML-файлами
     * @return список обработанных каталогов
     * @throws Exception при ошибках чтения или разбора XML
     */
    public static List<Catalog> parseAll(String folderPath) throws Exception {
        LOGGER.info("Начало парсинга каталога: " + folderPath);

        List<Catalog> catalogs = new ArrayList<>();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            String msg = "Указанный путь не является директорией: " + folderPath;
            LOGGER.error("msg");
            throw new IllegalArgumentException(msg);
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        if (files == null || files.length == 0) {
            LOGGER.warn("В папке нет XML-файлов: " + folderPath);
            return catalogs;
        }

        for (File xmlFile : files) {
            LOGGER.info("Обработка файла: " + xmlFile.getName());
            try {
                Catalog catalog = parseSingle(xmlFile);
                catalogs.add(catalog);
                LOGGER.info("Файл успешно обработан: " + xmlFile.getName());
            } catch (Exception e) {
                LOGGER.error("Ошибка при разборе файла " + xmlFile.getName(), e);
            }
        }

        LOGGER.info("Парсинг завершён. Обработано файлов: " + catalogs.size());
        return catalogs;
    }

    /**
     * Парсит одиночный XML-файл в объект Catalog.
     *
     * @param xmlFile XML-файл каталога
     * @return объект Catalog
     * @throws Exception если структура файла некорректна
     */
    private static Catalog parseSingle(File xmlFile) throws Exception {
        LOGGER.debug("Начало разбора XML-файла: " + xmlFile.getName());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        Element catalogElement = doc.getDocumentElement();
        if (!"CATALOG".equals(catalogElement.getTagName())) {
            String msg = "Ожидался корневой элемент <CATALOG>, найдено: " + catalogElement.getTagName();
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        String uuid = catalogElement.getAttribute("uuid");
        String company = catalogElement.getAttribute("company");
        String dateStr = catalogElement.getAttribute("date");

        LocalDate deliveryDate = LocalDate.parse(dateStr, DATE_FORMAT);

        Catalog catalog = new Catalog(uuid);
        catalog.setCompany(company);
        catalog.setDeliveryDate(deliveryDate);

        NodeList plantNodes = catalogElement.getElementsByTagName("PLANT");
        List<Plant> plants = new ArrayList<>();

        LOGGER.debug("Найдено растений в XML: " + plantNodes.getLength());

        for (int i = 0; i < plantNodes.getLength(); i++) {
            Node node = plantNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element plantElement = (Element) node;
                Plant plant = new Plant();

                plant.setCommon(getText(plantElement, "COMMON"));
                plant.setBotanical(getText(plantElement, "BOTANICAL"));
                plant.setZoneText(getText(plantElement, "ZONE"));
                plant.setLight(getText(plantElement, "LIGHT"));

                String priceStr = getText(plantElement, "PRICE");
                if (priceStr != null) {
                    try {
                        priceStr = priceStr.replace("$", "").trim();
                        plant.setPrice(new BigDecimal(priceStr));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Невозможно разобрать цену у растения: " + priceStr);
                    }
                }

                String availabilityStr = getText(plantElement, "AVAILABILITY");
                if (availabilityStr != null) {
                    try {
                        plant.setAvailability(Integer.parseInt(availabilityStr));
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Невозможно разобрать количество у растения: " + availabilityStr);
                    }
                }

                plants.add(plant);
            }
        }

        catalog.setPlants(plants);
        LOGGER.info("Каталог успешно создан: " + uuid + ", растений: " + plants.size());
        return catalog;
    }

    /**
     * Вспомогательный метод: извлекает текстовое содержимое указанного тега.
     *
     * @param parent  родительский элемент
     * @param tagName имя подэлемента
     * @return текстовое содержимое или null, если тег отсутствует
     */
    private static String getText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            Node node = list.item(0);
            String text = node.getTextContent().trim();
            LOGGER.trace("Получено значение <" + tagName + ">: " + text);
            return text;
        } else {
            LOGGER.trace("Тег <" + tagName + "> не найден");
            return null;
        }
    }
}
