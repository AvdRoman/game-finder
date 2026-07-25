package com.gamefinder;

import com.gamefinder.controller.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer; //умеет слушать порт, принимать соединения, управлять потоками
import com.gamefinder.config.AppConfig;

public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private HttpServer server;

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(AppConfig.PORT), 0);
            server.setExecutor(null); //Использвать стандартный пул потоков для обработки запросов

            server.createContext("/api/games", new GameController());

            LOGGER.info("Сервер запускается на порту {}...", AppConfig.PORT);
            server.start();

            LOGGER.info("Сервер запущен на порту {}", AppConfig.PORT);
            LOGGER.info("Сервер готов принимать запросы");
        }

        catch (IOException e) {
            LOGGER.error("Не удалось запустить сервер на порту {}: ", AppConfig.PORT, e);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            LOGGER.info("Сервер остановлен");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}
