package com.gamefinder.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppConfig {
    public static final int PORT = 8080;
    public static final String RAWG_API_URL = "https://api.rawg.io/api/games";
    public static final int PAGE_SIZE = 20;
    public static final String RAWG_API_KEY = System.getenv("RAWG_API_KEY");

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    private AppConfig() {}

    static {
        if (RAWG_API_KEY == null || RAWG_API_KEY.isEmpty()) {
            LOGGER.warn("ВНИМАНИЕ: Переменная окружения RAWG_API_KEY не задана!");
            LOGGER.warn("Запросы к RAWG API могут падать с ошибкой 401 Unauthorized.");
        }
    }
}
