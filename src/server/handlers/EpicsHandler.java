package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerSaveException;
import exceptions.ManagerValidateException;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {

    private TaskManager taskManager;


    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("epics")) {
            getEpics(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("epics")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                getEpicsById(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            addEpic(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                updateEpic(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }


    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            removeEpic(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                removeEpicById(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getEpics();
        String response = gson.toJson(epics);
        sendText(exchange, response, 200);
    }

    private void getEpicsById(HttpExchange exchange, int epicId) throws IOException {
        Epic epic = taskManager.getEpicsById(epicId);
        String response = gson.toJson(epic);
        sendText(exchange, response, 200);
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        try {
            String body = readText(exchange);
            Epic epic = gson.fromJson(body, Epic.class);
            taskManager.addEpic(epic);
            int id = epic.getId();
            String response = String.valueOf(id);
            sendText(exchange, "Задача добавлена " + response, 201);
        } catch (ManagerValidateException e) {
            sendHasIntersections(exchange);
        }
    }

    private void updateEpic(HttpExchange exchange, int epicId) throws IOException {
        try {
            String body = readText(exchange);
            Epic epic = gson.fromJson(body, Epic.class);
            epic.setId(epicId);
            String response = String.valueOf(epicId);
            taskManager.updateEpic(epic);
            sendText(exchange, "Задача обновлена " + response, 201);
        } catch (ManagerValidateException e) {
            sendHasIntersections(exchange);
        }
    }

    private void removeEpic(HttpExchange exchange) throws IOException {
        taskManager.removeAllEpics();
        sendText(exchange, "Задачи удалены", 201);
    }

    private void removeEpicById(HttpExchange exchange, int epicId) throws IOException {
        try {
            taskManager.removeEpicById(epicId);
            sendText(exchange, "Задача удалена", 201);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}
