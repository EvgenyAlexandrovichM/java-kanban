package managerstests;

import exceptions.ManagerSaveException;
import managers.FileBackendTaskManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileBackendTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public FileBackendTaskManager createTaskManager() {
        return new FileBackendTaskManager(new File("test.csv"));
    }

    @Test
    void shouldThrowExceptionWhenFileNotExists() {
        File file = new File("nonexistent_file.csv");
        assertThrows(ManagerSaveException.class, () -> FileBackendTaskManager.loadFromFile(file));
    }

    @Test
    void shouldThrowExceptionWhenFileHasIncorrectData() throws IOException {
        File file = new File("incorrect_data.csv");
        Files.write(Paths.get(file.getPath()), "id,type,name,status,description\n1,TASK,Test task,NEW,Test task description,incorrect_date".getBytes());
        assertThrows(ManagerSaveException.class, () -> FileBackendTaskManager.loadFromFile(file));
        Files.delete(Paths.get(file.getPath()));
    }

    @Test
    void shouldLoadTasksFromFile() throws IOException {
        File file = new File("test_load.csv");
        String content = """
                id,type,name,status,description,startTime,duration,epic
                1,TASK,Test task,NEW,Test task description,2024-03-27T10:00,30,
                2,EPIC,Test epic,NEW,Test epic description,null,0,
                3,SUBTASK,Test subtask,NEW,Test subtask description,2024-03-27T10:30,15,2
                """;
        Files.write(Paths.get(file.getPath()), content.getBytes());

        FileBackendTaskManager taskManager = FileBackendTaskManager.loadFromFile(file);

        assertEquals(1, taskManager.getTasks().size());
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getSubtasks().size());

        Task task = taskManager.getTasksById(1);
        assertEquals("Test task", task.getName());

        Files.delete(Paths.get(file.getPath()));
    }
}
