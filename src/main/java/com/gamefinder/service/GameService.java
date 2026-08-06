package com.gamefinder.service;

import com.gamefinder.model.Game;
import com.gamefinder.client.RawgApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private final RawgApiClient rawgClient;
    private final GameCache cache;

    public GameService() {
        this.rawgClient = new RawgApiClient();
        this.cache = new GameCache();
    }

    public List<Game> searchGames(String query) {

        String cleanQuery = query == null ? "" : query.trim().toLowerCase();

        // 1. Проверяем кэш
        List<Game> cachedGames = cache.get(cleanQuery);
        if (cachedGames != null) {
            LOGGER.info("Кэш для запроса: {}", query);
            return cachedGames;
        }

        // 2. Нет в кэше — идём в API
        LOGGER.info("ПРОМАХ КЭША для запроса: {}. Загрузка из RAWG API...", query);
        List<Game> games = rawgClient.fetchGames(cleanQuery);

        // 3. Сохраняем в кэш
        if (games != null && !games.isEmpty()) {
            cache.put(cleanQuery, games);
        }

        return games;
    }
}
