package managerstests;

import enums.Status;
import exceptions.ManagerValidateException;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void shouldEpicStatusBeInProgress() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        Subtask subtask2 = new Subtask("Test subtask2 name", "Test subtask2 description", Status.DONE,
                LocalDateTime.now().plus(Duration.ofMinutes(30)), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicsById(epic.getId()).getStatus());
    }

    @Test
    void shouldEpicStatusBeDone() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test subtask2 name", "Test subtask2 description", Status.DONE,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask);
        assertEquals(Status.DONE, taskManager.getEpicsById(epic.getId()).getStatus());
    }

    @Test
    void shouldAddPrioritizedTaskWhenNoIntersections() {
        Task task = new Task(
                "Test task",
                "Test task description",
                Status.NEW,
                LocalDateTime.of(2024, Month.MARCH, 27, 10, 0),
                Duration.ofMinutes(30));
        assertDoesNotThrow(() -> taskManager.addPrioritizedTask(task));
    }

    @Test
    void shouldThrowExceptionWhenAddingIntersectingTask() {
        Task task = new Task("Test task",
                "Test task description 1",
                Status.NEW,
                LocalDateTime.of(2024, Month.MARCH, 27, 10, 0),
                Duration.ofMinutes(30));
        taskManager.addTask(task);
        Task task2 = new Task("Test task 2",
                "Test task description 2",
                Status.NEW,
                LocalDateTime.of(2024, Month.MARCH, 27, 10, 15),
                Duration.ofMinutes(30));
        assertThrows(ManagerValidateException.class, () -> taskManager.addPrioritizedTask(task2));
    }

    @Test
    void shouldReturnPrioritizedTasksInCorrectOrder() {
        Task task = new Task("Test task",
                "Test description task",
                Status.NEW,
                LocalDateTime.of(2024, Month.MARCH, 27, 10, 0),
                Duration.ofMinutes(30));
        Task task2 = new Task("Test task task 2",
                "description 2",
                Status.NEW,
                LocalDateTime.of(2024, Month.MARCH, 27, 9, 0),
                Duration.ofMinutes(30));
        taskManager.addTask(task);
        taskManager.addTask(task2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(2, prioritizedTasks.size());
        assertEquals(task2, prioritizedTasks.get(0));
        assertEquals(task, prioritizedTasks.get(1));
    }

    @Test
    void shouldNotIncludeTasksWithNullStartTime() {
        Task task = new Task("Test task",
                "Test description 1",
                Status.NEW,
                null,
                Duration.ofMinutes(30));
        Task task2 = new Task("Test task 2",
                "Test description 2",
                Status.NEW,
                LocalDateTime.of(2024, Month.MARCH, 27, 9, 0),
                Duration.ofMinutes(30));
        taskManager.addTask(task);
        taskManager.addTask(task2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(1, prioritizedTasks.size());
        assertEquals(task2, prioritizedTasks.getFirst());
    }
}
