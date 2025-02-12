/* package managerstests;

import managers.Managers;
import enums.Status;
import managers.InMemoryHistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager imhm;

    @BeforeEach
    public void setUp() {
        imhm = Managers.getDefaultHistory();
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать " +
                "влажную уборку", 1, Status.NEW);
        Epic epic1 = new Epic("Сделать дипломную работу", "Нужно успеть за месяц", 2);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", 3,
                Status.NEW, 2);
        imhm.addTask(task1);
        imhm.addTask(epic1);
        imhm.addTask(subtask1);
        List<Task> history = imhm.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(epic1, history.get(1));
        assertEquals(subtask1, history.get(2));

    }

    @Test
    public void shouldRemoveTaskIfItIsViewedAgain() {
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать " +
                "влажную уборку", 1, Status.NEW);
        Epic epic1 = new Epic("Сделать дипломную работу", "Нужно успеть за месяц", 2);
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", 3,
                Status.NEW, 2);
        imhm.addTask(task1);
        imhm.addTask(epic1);
        imhm.addTask(subtask1);
        Task task2 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать " +
                "влажную уборку", 1, Status.NEW);
        imhm.addTask(task2);
        List<Task> history = imhm.getHistory();
        assertEquals(3, history.size());
        assertEquals(task2, history.get(2));
    }
} */   //TODO доделать хисторименеджертесты