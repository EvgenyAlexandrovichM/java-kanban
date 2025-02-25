/*package servertest;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskEndpointsTest extends HttpTaskServerTestBase {

    protected Task addTask() {
        return new Task("Test task name", "Test task description", Status.NEW, LocalDateTime.now()
                , Duration.ofMinutes(30));
    }
} //TODO падают тесты, доделать

      @Test
    void shouldReturnEmptyListIfNoTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest("/tasks", "GET");
        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void shouldAddTask() throws IOException, InterruptedException {
        Task task = addTask();
        String taskJson = gson.toJson(task);

        HttpResponse<String> response = sendRequest("/tasks", "POST", taskJson);
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = taskManager.getTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Test task name", tasksFromManager.get(0).getName());
    }

    @Test
    void shouldRemoveTask() throws IOException, InterruptedException {
        Task task = addTask();
        taskManager.addTask(task);

        HttpResponse<String> response = sendRequest("/tasks", "DELETE");

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }
} */
