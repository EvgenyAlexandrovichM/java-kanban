import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;


    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

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

    public void updateTask(Task task) {
        int taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);

    }


    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        if (!epics.containsKey(epicId)) {
            return;
        }
        Epic epicToRemove = epics.get(epicId);
        epics.remove(epicToRemove.getId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);

    }

    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        if (!subtasks.containsKey(subtaskId)) {
            return;
        }
        Subtask subtaskToRemove = subtasks.get(subtaskId);
        int epicId = subtaskToRemove.getEpicId();
        subtasks.remove(subtaskToRemove.getId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(epicId);
        updateEpicStatus(epic);

    }

    public Task getTasksById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicsById(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtasksById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasksId();
        for (Integer id : epicSubtasks) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    public void removeSubtaskById(int subtaskId) {
        subtasks.remove(subtaskId);
        Subtask subtask = subtasks.get(subtaskId);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Integer> id = epic.getSubtasksId();
        id.remove(subtaskId);
        epic.setSubtasksId(id);
        updateEpicStatus(epic);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();

    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

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
