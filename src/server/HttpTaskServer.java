package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import server.handlers.*;
import utils.DurationTypeAdapter;
import utils.LocalDateTimeTypeAdapter;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private TaskManager taskManager;
    private InMemoryTaskManager inMemoryTaskManager;
    private Gson gson;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault(), PORT, createGson());
    }

    public HttpTaskServer(TaskManager taskManager, int port, Gson gson) throws IOException {
        this.taskManager = taskManager;
        this.gson = gson;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        setupContex();
    }

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        return gsonBuilder.create();
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.startServer();
    }

    public void startServer() {
        httpServer.start();
        System.out.println("Сервер запущен на " + PORT + " порту");
    }

    public void stopServer() {
        httpServer.stop(1);
        System.out.println("Сервер остановлен на " + PORT + " порту");
    }

    private void setupContex() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(inMemoryTaskManager, gson));
    }
}

