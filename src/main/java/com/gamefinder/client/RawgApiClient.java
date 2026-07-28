package com.gamefinder.client;

import com.gamefinder.config.AppConfig;
import com.gamefinder.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray; // Работа с JSON-массивом
import org.json.JSONObject; // Работа с JSON-объектом
import java.net.URLEncoder; // Кодирование строк для формата URL
import java.nio.charset.StandardCharsets;

import java.net.URI; // Превращаем строку в адрес чтобы можно было передать в http запрос
import java.net.http.HttpClient; // Клиент для отправки HTTP-запросов
import java.net.http.HttpRequest; // Класс для формирования HTTP-запроса (какой адрес, какой метод (GET, POST), какие заголовки)
import java.net.http.HttpResponse; // Класс для получения HTTP-ответа (статус (200, 404), тело (JSON))
import java.util.ArrayList; // Реализация списка, в который мы будем складывать игры
import java.util.List; // // Интерфейс списка (чтобы возвращать список игр)

public class RawgApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawgApiClient.class);
    private final HttpClient httpClient;

    public RawgApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Game> fetchGames(String query) {
        // Формируем URL
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = AppConfig.RAWG_API_URL +
                "?key=" + AppConfig.RAWG_API_KEY +
                "&search=" + encodedQuery +
                "&page_size=" + AppConfig.PAGE_SIZE;

        LOGGER.info("Отправка запроса к RAWG API: {}", url);

        try {
            // Создаём и отправляем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Проверяем статус ответа

            if (response.statusCode() != 200) {
                LOGGER.error("RAWG API вернул ошибку: {}", response.statusCode());
                return new ArrayList<>();
            }
            // Возвращаем ответ
            return parseGames(response.body());
        }

        catch (Exception e) {
            LOGGER.error("Ошибка при запросе к RAWG API: ", e);
            return new ArrayList<>();
        }
    }

    public List<Game> parseGames(String jsonResponse) {
        List<Game> games = new ArrayList<>();

        try {
            // Превращаем строку в JSON-объект
            JSONObject json = new JSONObject(jsonResponse);

            // Достаём массив "results" (в нём лежат игры)
            JSONArray results = json.getJSONArray("results");

            // Проходим по массиву и каждую игру превращаем в объект Game
            for (int i = 0; i < results.length(); i++) {
                JSONObject gameJson = results.getJSONObject(i);

                Game game = new Game();

                game.setName(gameJson.optString("name", "Неизвестно"));

                game.setReleased(gameJson.optString("released", ""));

                game.setRating(gameJson.optDouble("rating", 0.0));

                game.setBackgroundImage(gameJson.optString("background_image", ""));

                games.add(game);
            }

            LOGGER.info("Получено {} игр из RAWG API", games.size());
        }

        catch (Exception e) {
            LOGGER.error("Ошибка при парсинге JSON: ", e);
        }

        return games;
    }
}
