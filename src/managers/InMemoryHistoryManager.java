package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int LAST_VIEWED_TASKS = 10;
    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (historyList.size() == LAST_VIEWED_TASKS) {
            historyList.removeFirst();
        }
        historyList.addLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
