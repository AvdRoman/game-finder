package com.gamefinder.service;

import com.gamefinder.config.AppConfig;
import com.gamefinder.model.Game;

import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap; // многопоточная хэш-таблица

public class GameCache {

    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // получение данных из кэша
    public List<Game> get(String query) {
        CacheEntry entry = cache.get(query);
        if (entry == null) {
            return null; // записи нет
        }

        long now = System.currentTimeMillis();
        long ttlMillis = TimeUnit.MINUTES.toMillis(AppConfig.CACHE_TTL_MINUTES); // Переводим минуты в миллисекунды

        // Проверяем, не истекло ли время жизни
        if (now - entry.getTimestamp() > ttlMillis) {
            cache.remove(query);
            return null;
        }

        return entry.getGames(); // данные свежие, возвращаем список игр
    }

    // сохрание данных в кэш
    public void put(String query, List<Game> games) {
        if (query == null || games == null || games.isEmpty()) {
            return;
        }
        cache.put(query, new CacheEntry(games));
    }

    // чистка кэша
    public void clear() {
        cache.clear();
    }

    private static class CacheEntry {
        private final List<Game> games;
        private final long timestamp; // время добавления

        public CacheEntry(List<Game> games) {
            this.games = games;
            this.timestamp = System.currentTimeMillis();
        }

        public List<Game> getGames() {
            return games;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

}
