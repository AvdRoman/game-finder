package com.gamefinder.controller;

import com.gamefinder.model.Game;
import com.gamefinder.service.GameService;
import com.sun.net.httpserver.HttpExchange; //Класс, представляющий один HTTP-обмен (запрос и ответ) между клиентом и сервером
import com.sun.net.httpserver.HttpHandler; //Интерфейс, который обязывает класс реализовать метод
import java.io.IOException;
import java.io.OutputStream; //Класс для записи байтов в выходной поток для отправки клиенту
import java.nio.charset.StandardCharsets; //Кодировка UTF-8, чтобы корректно передавать текст
import java.net.URLDecoder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class GameController implements HttpHandler {

    private final GameService gameService = new GameService();


    @Override
    public void handle(HttpExchange exchange) throws IOException { //принимает объект с запросом и ответом

        // Добавляем заголовки CORS для всех ответов
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");

        // Обработка preflight-запроса OPTIONS от браузера
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Проверяем, что это GET-запрос
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "{\"error\": \"Метод не поддерживается\"}");
            return;
        }

        String query = extractSearchParam(exchange);
        if (query == null || query.trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\": \"Параметр search обязателен\"}");
            return;
        }

        try {
            // Вызываем сервис для поиска
            List<Game> games = gameService.searchGames(query);

            // Превращаем список в JSON
            String jsonResponse = gamesToJson(games);

            // Отправляем ответ
            sendResponse(exchange, 200, jsonResponse);
        }

        catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\": \"Внутренняя ошибка сервера\"}");
        }

    }

    // Достаём параметр search из URL
    private String extractSearchParam(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) return null;

        for (String param : query.split("&")) {
            if (param.startsWith("search=")) {
                String rawValue = param.substring(7);
                return URLDecoder.decode(rawValue, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    // Превращаем список игр в JSON
    private String gamesToJson(List<Game> games) {
        JSONObject response = new JSONObject();
        JSONArray results = new JSONArray();

        for (Game game : games) {
            JSONObject gameJson = new JSONObject();
            gameJson.put("name", game.getName());
            gameJson.put("released", game.getReleased());
            gameJson.put("rating", game.getRating());
            gameJson.put("backgroundImage", game.getBackgroundImage());
            results.put(gameJson);
        }

        response.put("results", results); // массив игр
        response.put("count", games.size()); // кол-во игр
        return response.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
