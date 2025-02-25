package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private TaskManager taskManager;
    private Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("history")) {
            getHistory(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        sendText(exchange, "Метод не поддерживается", 405);
    }


    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        sendText(exchange, "Метод не поддерживается", 405);
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        try {
            List<Task> history = taskManager.getHistory();
            String response = gson.toJson(history);
            sendText(exchange, response, 200);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}
