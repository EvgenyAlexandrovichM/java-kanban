package taskstests;

import tasks.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void setUp() {
        epic1 = new Epic("Сделать дипломную работу", "Нужно успеть за месяц", 3);
        epic2 = new Epic("Сделать тз", "Сегодня крайний день", 3);
    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        assertEquals(epic1, epic2);
    }

    // была реализована невозможность добавления эпика в список айди сабтасков
    @Test
    void shouldReturnFalseWhenAddingEpicAsSubtaskToItself() {
        int subtasksSize = epic1.getSubtasksId().size();
        epic1.addSubtask(3);
        assertEquals(subtasksSize, epic1.getSubtasksId().size());
    }
}