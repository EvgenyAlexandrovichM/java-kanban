import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // добавлены переопределенные методы equals() и hashCode()
    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать +" +
                "влажную уборку", 3, Status.NEW);
        Task task2 = new Task("Сходить в спортзал", "Сегодня день ног и спины", 3, Status.NEW);
        assertEquals(task1, task2);
    }
}