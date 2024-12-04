import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    Task getTasksById(int taskId);

    Epic getEpicsById(int epicId);

    Subtask getSubtasksById(int subtaskId);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void removeTaskById(int taskId);

    void removeEpicById(int epicId);

    void removeSubtaskById(int subtaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    ArrayList<Subtask> getSubtasksForEpic(int epicId);

    ArrayList<Task> getHistory();
}
