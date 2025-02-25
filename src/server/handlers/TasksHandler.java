package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import exceptions.ManagerValidateException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private TaskManager taskManager;

    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("tasks")) {
            getTasks(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("tasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                getTasksById(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("tasks")) {
            addTask(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("tasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                updateTask(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        if (splitPath.length == 2 && splitPath[1].equals("tasks")) {
            removeTasks(exchange);
        } else if (splitPath.length == 3 && splitPath[1].equals("tasks")) {
            try {
                int id = Integer.parseInt(splitPath[2]);
                removeTaskById(exchange, id);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            }
        }

    }

    private void getTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getTasks();
        String response = gson.toJson(tasks);
        sendText(exchange, response, 200);
    }

    private void getTasksById(HttpExchange exchange, int taskId) throws IOException {
        Task task = taskManager.getTasksById(taskId);
        String response = gson.toJson(task);
        sendText(exchange, response, 200);

    }

    private void addTask(HttpExchange exchange) throws IOException {
        try {
            String body = readText(exchange);
            Task task = gson.fromJson(body, Task.class);
            int id = task.getId();
            String response = String.valueOf(id);
            taskManager.addTask(task);
            sendText(exchange, "Задача добавлена " + response, 201);
        } catch (ManagerValidateException e) {
            sendHasIntersections(exchange);
        }
    }

    private void updateTask(HttpExchange exchange, int taskId) throws IOException {
        try {
            String body = readText(exchange);
            Task task = gson.fromJson(body, Task.class);
            task.setId(taskId);
            String response = String.valueOf(taskId);
            taskManager.updateTask(task);
            sendText(exchange, "Задача обновлена " + response, 201);
        } catch (ManagerValidateException e) {
            sendHasIntersections(exchange);
        }
    }

    private void removeTasks(HttpExchange exchange) throws IOException {
        taskManager.removeAllTasks();
        sendText(exchange, "Задачи удалены", 201);
    }

    private void removeTaskById(HttpExchange exchange, int taskId) throws IOException {
        try {
            taskManager.removeTaskById(taskId);
            sendText(exchange, "Задача удалена", 201);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }
}
