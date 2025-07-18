package com.example.xmlparser;

import com.example.xmlparser.service.CatalogRepository;
import com.example.xmlparser.service.PlantRepository;
import com.example.xmlparser.model.Catalog;
import com.example.xmlparser.model.Plant;
import com.example.xmlparser.parser.XmlPlantParser;
import com.example.xmlparser.validator.CatalogValidator;
import com.example.xmlparser.validator.PlantValidator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Точка входа в приложение XML Parser.
 * <p>
 * Загружает и парсит XML-файлы из папки ресурсов "data", валидирует каталоги и растения,
 * сохраняет валидные данные в базу данных.
 */
public final class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Основной метод запуска приложения.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            // 1. Поиск папки с данными
            String folderPath;

            if (args.length > 0) {
                folderPath = args[0];
            } else {
                var resource = Main.class.getClassLoader().getResource("data");
                if (resource == null) {
                    throw new RuntimeException("Папка data не найдена в ресурсах и не передана явно!");
                }
                folderPath = resource.getPath();
            }
            logger.info("Запуск обработки XML-файлов в папке: " + folderPath);

            // 2. Парсинг всех XML-файлов
            List<Catalog> catalogs = XmlPlantParser.parseAll(folderPath);
            logger.info("Спарсено каталогов: " + catalogs.size());

            int catalogsSaved = 0;
            int plantsSaved = 0;

            // 3. Обработка каждого каталога
            for (Catalog catalog : catalogs) {
                List<String> catalogErrors = CatalogValidator.validate(catalog);
                if (catalogErrors.isEmpty()) {
                    int catalogId = CatalogRepository.save(catalog);
                    catalogsSaved++;

                    // 4. Проставляем catalogId растениям
                    for (Plant plant : catalog.getPlants()) {
                        plant.setCatalogId(catalogId);
                    }

                    // 5. Валидация и сохранение растений
                    for (Plant plant : catalog.getPlants()) {
                        List<String> plantErrors = PlantValidator.validate(plant);
                        if (plantErrors.isEmpty()) {
                            PlantRepository.save(plant);
                            plantsSaved++;
                        }
                    }

                } else {
                    logger.warn("Пропущен каталог UUID: " + catalog.getUuid() + " из-за ошибок валидации");
                }
            }

            logger.info("Обработка завершена");
            logger.info("Каталогов сохранено: " + catalogsSaved);
            logger.info("Растений сохранено: " + plantsSaved);

        } catch (Exception e) {
            logger.error("Ошибка при выполнении приложения: " + e.getMessage(), e);
        }
    }
}
