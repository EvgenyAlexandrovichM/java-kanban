import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик1", "Эпик1");
        Subtask subtask1 = new Subtask("Сделать презентацию", "12 слайдов", 3, Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подготовить речь", "На 5-7 минут выступления", 3, Status.NEW, epic1.getId());
        assertEquals(subtask1, subtask2);
    }
}