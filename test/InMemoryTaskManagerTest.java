import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать " +
                "влажную уборку", Status.NEW);
        task2 = new Task("Сходить в спортзал", "Сегодня день ног и спины", Status.NEW);
        epic1 = new Epic("Сделать дипломную работу", "Нужно успеть за месяц");
        epic2 = new Epic("Сделать тз", "Сегодня крайний день");

    }

    @Test
    void shouldAddAndGetNewTasks() {
        taskManager.addTask(task1);
        Task task = taskManager.getTasksById(task1.getId());
        assertNotNull(task);
        assertEquals(task1, task);
    }

    @Test
    void shouldGetAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        ArrayList<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void shouldUpdateTaskToNewTask() {
        taskManager.addTask(task1);
        Task updateTask1 = new Task("Не забыть убраться в комнате", "Можно без влажной уборки",
                task1.getId(), Status.IN_PROGRESS);
        taskManager.updateTask(updateTask1);
        ArrayList<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Не забыть убраться в комнате", tasks.getFirst().getName());

    }

    @Test
    void shouldRemoveAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();
        ArrayList<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void shouldRemoveTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeTaskById(task1.getId());
        ArrayList<Task> tasks = taskManager.getTasks();
        assertFalse(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void shouldAddAndGetNewEpics() {
        taskManager.addEpic(epic1);
        Epic epic = taskManager.getEpicsById(epic1.getId());
        assertNotNull(epic);
        assertEquals(epic1, epic);


    }

    @Test
    void shouldGetAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        ArrayList<Epic> epics = taskManager.getEpics();
        assertNotNull(epics);
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void shouldUpdateEpicToNewEpic() {
        taskManager.addEpic(epic1);
        Epic updateEpic1 = new Epic("Съездить в отпуск в июне", "В приоритете в Германию, попить нормального пива",
                epic1.getId());
        taskManager.updateEpic(updateEpic1);
        ArrayList<Epic> epics = taskManager.getEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals("Съездить в отпуск в июне", epics.getFirst().getName());

    }

    @Test
    void shouldRemoveAllEpicsAlsoShouldRemoveAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить речь", "На 5-7 минут выступления", epic1.getId(),
                Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.removeAllEpics();
        ArrayList<Epic> epics = taskManager.getEpics();
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(epics.isEmpty());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void shouldRemoveEpicByIdAlsoShouldRemoveAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить речь", "На 5-7 минут выступления", epic1.getId(),
                Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.removeEpicById(epic1.getId());
        ArrayList<Epic> epics = taskManager.getEpics();
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertFalse(epics.contains(epic1));
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void shouldAddAndGetNewSubtasks() {
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask = taskManager.getSubtasksById(subtask1.getId());
        assertNotNull(subtask1);
        assertEquals(subtask1, subtask);
    }

    @Test
    void shouldGetAllSubtasks() {
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить речь", "На 5-7 минут выступления", epic1.getId(),
                Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void shouldUpdateSubtaskToNewSubtaskAlsoShouldChangeEpicStatus() {
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить речь", "На 5-7 минут выступления", epic1.getId(),
                Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Status epic1Status = epic1.getStatus();
        Subtask subtask3 = new Subtask("Погладить рубашку", "Желательно черную", subtask2.getId(),
                Status.DONE, epic1.getId());
        taskManager.updateSubtask(subtask3);
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        Status actualEpicStatus = epic1.getStatus();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask3));
        assertNotEquals(epic1Status, actualEpicStatus);
    }

    @Test
    void shouldRemoveSubtaskByIdAlsoShouldChangeEpicStatus() {
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить речь", "На 5-7 минут выступления", epic1.getId(),
                Status.DONE, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Status epic1Status = epic1.getStatus();
        taskManager.removeSubtaskById(subtask1.getId());
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        Status actualEpicStatus = epic1.getStatus();
        assertNotNull(subtasks);
        assertEquals(1, subtasks.size());
        assertTrue(subtasks.contains(subtask2));
        assertNotEquals(epic1Status, actualEpicStatus);

    }
}