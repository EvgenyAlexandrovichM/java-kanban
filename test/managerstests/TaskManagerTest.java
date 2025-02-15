package managerstests;

import enums.Status;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    public abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    protected Task addTask() {
        return new Task("Test task name", "Test task description", Status.NEW, LocalDateTime.now()
                , Duration.ofMinutes(30));
    }

    protected Epic addEpic() {
        return new Epic("Test epic name", "Test epic description");
    }

    protected Subtask addSubtask(Epic epic) {
        return new Subtask("Test subtask name", "Test subtask description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
    }

    @Test
    public void shouldAddTask() {
        Task task = addTask();
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTasksById(task.getId()));
        assertEquals(Status.NEW, taskManager.getTasksById(task.getId()).getStatus());
    }

    @Test
    public void shouldAddEpic() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpicsById(epic.getId()));
        assertEquals(Status.NEW, taskManager.getEpicsById(epic.getId()).getStatus());
    }

    @Test
    public void shouldAddSubtask() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        taskManager.addSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtasksById(subtask.getId()));
        assertEquals(Status.NEW, taskManager.getSubtasksById(subtask.getId()).getStatus());
        assertEquals(epic.getId(), subtask.getEpicId());
    }

    @Test
    public void shouldUpdateTask() {
        Task task = addTask();
        taskManager.addTask(task);
        taskManager.getTasksById(task.getId()).setStatus(Status.DONE);
        taskManager.updateTask(task);
        assertEquals(Status.DONE, taskManager.getTasksById(task.getId()).getStatus());
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        taskManager.getEpicsById(epic.getId()).setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        assertEquals(Status.DONE, taskManager.getEpicsById(epic.getId()).getStatus());
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void ShouldUpdateSubtask() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        taskManager.addSubtask(subtask);
        taskManager.getSubtasksById(subtask.getId()).setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        assertEquals(Status.DONE, taskManager.getSubtasksById(subtask.getId()).getStatus());
        assertEquals(Status.DONE, subtask.getStatus());
    }

    @Test
    public void shouldGetTaskById() {
        Task task = addTask();
        taskManager.addTask(task);
        Task savedTask = taskManager.getTasksById(task.getId());
        assertEquals(task, savedTask);
        assertNotNull(savedTask);
    }

    @Test
    public void shouldGetEpicById() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Epic savedEpic = taskManager.getEpicsById(epic.getId());
        assertEquals(epic, savedEpic);
        assertNotNull(savedEpic);
    }

    @Test
    public void shouldGetSubtaskById() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        taskManager.addSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtasksById(subtask.getId());
        assertEquals(subtask, savedSubtask);
        assertNotNull(savedSubtask);
    }

    @Test
    public void shouldGetAllTasks() {
        Task task = addTask();
        Task task2 = new Task("Test task2 name", "Test task2 description", Status.NEW,
                LocalDateTime.now().plus(Duration.ofMinutes(30)),
                Duration.ofMinutes(30));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test
    public void shouldGetAllEpics() {
        Epic epic = addEpic();
        Epic epic2 = new Epic("Test epic2 name", "Test epic2 description");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics);
        assertEquals(2, epics.size());
    }

    @Test
    public void shouldGetAllSubtasks() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        Subtask subtask2 = new Subtask("Test subtask2 name", "Test subtask2 description", Status.NEW,
                LocalDateTime.now().plus(Duration.ofMinutes(30)), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
    }

    @Test
    public void shouldRemoveTaskById() {
        Task task = addTask();
        taskManager.addTask(task);
        taskManager.removeTaskById(task.getId());
        assertNull(taskManager.getTasksById(task.getId()));
    }

    @Test
    public void shouldRemoveEpicById() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        taskManager.removeEpicById(epic.getId());
        assertNull(taskManager.getEpicsById(epic.getId()));
    }

    @Test
    public void shouldRemoveSubtaskById() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        taskManager.addSubtask(subtask);
        taskManager.removeSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtasksById(subtask.getId()));
    }

    @Test
    public void shouldRemoveAllTasks() {
        Task task = addTask();
        Task task2 = new Task("Test task2 name", "Test task2 description", Status.NEW,
                LocalDateTime.now().plus(Duration.ofMinutes(30)),
                Duration.ofMinutes(30));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void shouldRemoveAllEpics() {
        Epic epic = addEpic();
        Epic epic2 = new Epic("Test epic2 name", "Test epic2 description");
        Subtask subtask = addSubtask(epic);
        taskManager.addSubtask(subtask);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void shouldRemoveAllSubtasks() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        Subtask subtask2 = new Subtask("Test subtask2 name", "Test subtask2 description", Status.NEW,
                LocalDateTime.now().plus(Duration.ofMinutes(30)), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.removeAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void shouldGetSubtasksForEpic() {
        Epic epic = addEpic();
        taskManager.addEpic(epic);
        Subtask subtask = addSubtask(epic);
        Subtask subtask2 = new Subtask("Test subtask2 name", "Test subtask2 description", Status.NEW,
                LocalDateTime.now().plus(Duration.ofMinutes(30)), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getSubtasksForEpic(epic.getId());
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
    }

}
