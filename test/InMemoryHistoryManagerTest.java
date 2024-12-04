import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private TaskManager taskManager;
    private InMemoryHistoryManager imhm;

    @BeforeEach
    public void setUp() {
        taskManager = Managers.getDefault();
        imhm = Managers.getDefaultHistory();
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать " +
                "влажную уборку", Status.NEW);
        Epic epic1 = new Epic("Сделать дипломную работу", "Нужно успеть за месяц");
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", epic1.getId(),
                Status.NEW, epic1.getId());
        imhm.addTask(task1);
        imhm.addTask(epic1);
        imhm.addTask(subtask1);
        assertEquals(3, imhm.getHistory().size());
        assertTrue(imhm.getHistory().contains(task1));
        assertTrue(imhm.getHistory().contains(epic1));
        assertTrue(imhm.getHistory().contains(subtask1));

    }

    @Test
    void shouldRemoveFirstTaskWhenAddNewTaskAndListAreFull() {
        Task task11 = new Task("Задача 11", "Описание", Status.NEW);
        for (int i = 0; i < 10; i++) {
            imhm.addTask(new Task("Задача " + i, "Описание", Status.NEW));
        }
        imhm.addTask(task11);

        assertEquals(10, imhm.getHistory().size());
        assertEquals(task11, imhm.getHistory().get(9));

    }
}