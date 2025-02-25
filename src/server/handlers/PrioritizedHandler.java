package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private InMemoryTaskManager inMemoryTaskManager;
    private Gson gson;

    public PrioritizedHandler(InMemoryTaskManager inMemoryTaskManager) {
        this.inMemoryTaskManager = inMemoryTaskManager;
        this.gson = new Gson();
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("prioritized")) {
            getPrioritizedTasks(exchange);
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

    private void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        try {
            List<Task> prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();
            String response = gson.toJson(prioritizedTasks);
            sendText(exchange, response, 200);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}
