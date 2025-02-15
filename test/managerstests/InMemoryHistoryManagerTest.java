package managerstests;

import managers.Managers;
import enums.Status;
import managers.InMemoryHistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private int nextId = 1;

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    private int setId() {
        return nextId++;
    }

    private Task addTask() {
        Task task = new Task("Test task", "Test task description",
                Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        int taskId = setId();
        task.setId(taskId);
        return task;
    }

    private Epic addEpic() {
        Epic epic = new Epic("Epic test", "Test epic description");
        int epicId = setId();
        epic.setId(epicId);
        return epic;
    }

    private Subtask addSubtask(Epic epic) {
        Subtask subtask = new Subtask("Subtask test", "Test subtask description",
                Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30),
                epic.getId());
        int subtaskId = setId();
        subtask.setId(subtaskId);
        return subtask;
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task = addTask();
        Epic epic = addEpic();
        Subtask subtask = addSubtask(epic);
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task, history.get(0));
        assertEquals(epic, history.get(1));
        assertEquals(subtask, history.get(2));
    }

    /*  @Test
    public void shouldRemoveTaskIfItIsViewedAgain() {
        Task task = addTask();
        Epic epic = addEpic();
        Subtask subtask = addSubtask(epic);
        historyManager.addTask(task);
        historyManager.addTask(epic);
        historyManager.addTask(subtask);
        List<Task> history = historyManager.getHistory();
        assertEquals(task, history.getFirst());
        historyManager.addTask(task);
        assertEquals(3, history.size());
        assertEquals(task, history.getLast());
    } */ //TODO переделать тест, не заменяет уже добавленную таску
}