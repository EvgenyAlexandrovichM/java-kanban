/* package servertest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import utils.DurationTypeAdapter;
import utils.LocalDateTimeTypeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class HttpTaskServerTestBase {

    protected TaskManager taskManager;
    protected HttpTaskServer taskServer;
    protected Gson gson;
    protected HttpClient client;
    protected URI baseUri;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager, 8080);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gson = gsonBuilder.create();
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
        baseUri = URI.create("http://localhost:8080");

        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
        taskManager.removeAllSubtasks();

        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    protected HttpResponse<String> sendRequest(String path, String method, String body) throws IOException,
            InterruptedException {
        URI uri = baseUri.resolve(path);
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(uri);

        switch (method) {
            case "GET":
                builder.GET();
                break;
            case "POST":
                builder.POST(HttpRequest.BodyPublishers.ofString(body));
                break;
            case "DELETE":
                builder.DELETE();
                break;
            default:
                throw new IllegalArgumentException("Некорректный метод");
        }

        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> sendRequest(String path, String method) throws IOException, InterruptedException {
        return sendRequest(path, method, "");
    }
} */ //TODO доделать тесты, падают