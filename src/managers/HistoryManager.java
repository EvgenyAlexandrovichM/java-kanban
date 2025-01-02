package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int taskId);

    List<Task> getHistory();
}
