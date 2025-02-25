package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ManagerValidateException;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private TaskManager taskManager;


    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            getSubtasks(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                getSubtasksById(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            addSubtask(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                updateSubtask(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }


    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("subtasks")) {
            removeSubtasks(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("subtasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                removeSubtaskById(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getSubtasks();
        String response = gson.toJson(subtasks);
        sendText(exchange, response, 200);
    }

    private void getSubtasksById(HttpExchange exchange, int subtaskId) throws IOException {
        Subtask subtask = taskManager.getSubtasksById(subtaskId);
        String response = gson.toJson(subtask);
        sendText(exchange, response, 200);
    }

    private void addSubtask(HttpExchange exchange) throws IOException {
        try {
            String body = readText(exchange);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask.getEpicId() == 0) {
                sendNotFound(exchange);
            }
            int id = subtask.getId();
            String response = String.valueOf(id);
            taskManager.addSubtask(subtask);
            sendText(exchange, "Задача добавлена " + response, 201);
        } catch (ManagerValidateException e) {
            sendHasIntersections(exchange);
        }
    }

    private void updateSubtask(HttpExchange exchange, int subtakId) throws IOException {
        try {
            String body = readText(exchange);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            subtask.setId(subtakId);
            String response = String.valueOf(subtakId);
            taskManager.updateSubtask(subtask);
            sendText(exchange, "Задача обновлена " + response, 201);
        } catch (ManagerValidateException e) {
            sendHasIntersections(exchange);
        }
    }

    private void removeSubtasks(HttpExchange exchange) throws IOException {
        taskManager.removeAllSubtasks();
        sendText(exchange, "Задачи удалены", 201);
    }

    private void removeSubtaskById(HttpExchange exchange, int subtaskId) throws IOException {
        try {
            taskManager.removeSubtaskById(subtaskId);
            sendText(exchange, "Задача удалена", 201);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}

