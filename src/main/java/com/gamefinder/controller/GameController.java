package com.gamefinder.controller;

import com.sun.net.httpserver.HttpExchange; //Класс, представляющий один HTTP-обмен (запрос и ответ) между клиентом и сервером
import com.sun.net.httpserver.HttpHandler; //Интерфейс, который обязывает класс реализовать метод
import java.io.IOException;
import java.io.OutputStream; //Класс для записи байтов в выходной поток для отправки клиенту
import java.nio.charset.StandardCharsets; //Кодировка UTF-8, чтобы корректно передавать текст

public class GameController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException { //принимает объект с запросом и ответом

        String response = "{\"message\": \"GameController работает\"}"; //json ответ

        exchange.getResponseHeaders().set("Content-Type", "application/json"); //устанавливаем заголовок (тип контента - json)
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length); //отправляем статус(200 = ОК) и длину(кол-во байтов)

        try (OutputStream os = exchange.getResponseBody()) { //открываем поток
            os.write(response.getBytes(StandardCharsets.UTF_8)); //Записываем JSON в поток (превращаем строку в байты через UTF-8)
        }
    }
}
