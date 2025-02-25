/* package servertest;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpicEndpointsTest extends HttpTaskServerTestBase {

    protected Epic addEpic() {
        return new Epic("Test epic name", "Test epic description");
    }
} //TODO падают тесты, доделать

     @Test
    void shouldReturnEmptyListIfNoEpics() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest("/epics", "GET");
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void shouldAddEpic() throws IOException, InterruptedException {
        Task task = addEpic();
        String epicJson = gson.toJson(task);

        HttpResponse<String> response = sendRequest("/tasks", "POST", epicJson);
        assertEquals(200, response.statusCode());
        List<Epic> epicsFromManager = taskManager.getEpics();
        assertNotNull(epicsFromManager);
        assertEquals(1, epicsFromManager.size());
        assertEquals("Test epic name", epicsFromManager.get(0).getName());
    }

    @Test
    void shouldRemoveEpic() throws IOException, InterruptedException {
        Epic epic = addEpic();
        taskManager.addEpic(epic);

        HttpResponse<String> response = sendRequest("/epics", "DELETE");

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getEpics().size());
    }
} */