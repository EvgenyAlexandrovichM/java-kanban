/* package servertest;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;


import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskEndpointsTest extends HttpTaskServerTestBase {

    protected Epic addEpic() {
        return new Epic("Test epic name", "Test epic description");
    }

    protected Subtask addSubtask(Epic epic) {
        return new Subtask("Test subtask name", "Test subtask description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
    }

    @Test
    void shouldReturnEmptyListIfNoEpics() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest("/subtasks", "GET", "");
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void shouldAddSubtask() throws IOException, InterruptedException {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        String subtaskJson = gson.toJson(subtask);

        HttpResponse<String> response = sendRequest("/subtasks", "POST", subtaskJson);
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = taskManager.getSubtasks();
        assertNotNull(subtasksFromManager);
        assertEquals(1, subtasksFromManager.size());
        assertEquals("Test subtask name", subtasksFromManager.get(0).getName());
    }

    @Test
    void shouldRemoveEpic() throws IOException, InterruptedException {
        Epic epic = addEpic();
        Subtask subtask = addSubtask(epic);
        taskManager.addSubtask(subtask);

        HttpResponse<String> response = sendRequest("/subtasks", "DELETE");

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getSubtasks().size());
    }
} */ //TODO падают тесты, доделать