import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;


    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return;
        }

        subtask.setId(nextId++);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        int taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.put(task.getId(), task);

    }


    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        if (!epics.containsKey(epicId)) {
            return;
        }
        Epic updatedEpic = epics.get(epicId);
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        if (!subtasks.containsKey(subtaskId)) {
            return;
        }
        Subtask subtaskToRemove = subtasks.get(subtaskId);
        int epicId = subtaskToRemove.getEpicId();
        if(epicId == subtask.getEpicId()) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(epicId);
            updateEpicStatus(epic);
        }
    }

    @Override
    public Task getTasksById(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.addTask(task);
        }
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicsById(int epicId) {
        Epic epic = epics.get(epicId);
        if(epic != null) {
          historyManager.addTask(epic);
        }
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtasksById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if(subtask != null) {
            historyManager.addTask(subtask);
        }
        return subtasks.get(subtaskId);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeTaskById(int taskId) {
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.remove(taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasksId();
        for (Integer id : epicSubtasks) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            if (epicId != -1) {
                Epic epic = epics.get(epicId);
                if (epic != null) {
                    ArrayList<Integer> id = epic.getSubtasksId();
                    if (id != null) {
                        epic.removeSubtaskById(subtaskId);
                        updateEpicStatus(epic);
                    }
                }
            }
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();

    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public ArrayList<Subtask> getSubtasksForEpic(int epicId) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtasksId()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    subtaskList.add(subtask);
                }
            }
        }
        return subtaskList;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        if (subtasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int allDoneCount = 0;
        int allNewCount = 0;
        for (int id : subtasksId) {
            Subtask subtask = subtasks.get(id);
            if (subtask.getStatus() == Status.DONE) {
                allDoneCount++;
            }
            if (subtask.getStatus() == Status.NEW) {
                allNewCount++;
            }
        }
        if (allDoneCount == subtasksId.size()) {
            epic.setStatus(Status.DONE);
        } else if (allNewCount == subtasksId.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
