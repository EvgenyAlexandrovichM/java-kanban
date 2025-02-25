package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    protected abstract void handleGet(HttpExchange exchange) throws IOException;

    protected abstract void handlePost(HttpExchange exchange) throws IOException;

    protected abstract void handleDelete(HttpExchange exchange) throws IOException;

    protected String readText(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.sendResponseHeaders(statusCode, resp.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(resp);
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "Ресурс не найден";
        sendText(exchange, response, 404);
    }

    protected void sendHasIntersections(HttpExchange exchange) throws IOException { // я если правильно понимаю, то мне
        // нужно добавить метод boolean doesIntersect() в TaskManager,
        // чтобы у меня приложение в целом проверяло наличие пересечений,иначе я не очень понимаю как вообще пересечения проверять,
        // потому что пока у меня в реализации подобного нет, я что-то в прошлых тз пропустил?)))
        String response = "Задача пересекается с существующими";
        sendText(exchange, response, 406);


    }
}
