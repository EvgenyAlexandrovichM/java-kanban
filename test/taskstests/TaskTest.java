package taskstests;

import enums.Status;
import tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать +" +
                "влажную уборку", 3, Status.NEW);
        Task task2 = new Task("Сходить в спортзал", "Сегодня день ног и спины", 3, Status.NEW);
        assertEquals(task1, task2);
    }
}