import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
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
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
    }

    public void updateTask(Task task, int taskId) {
        task.setId(taskId);
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic, int epicId) {
        epic.setId(epicId);
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask, int subtaskId) {
        subtask.setId(subtaskId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
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

    public void printAllTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void printAllEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    public void printAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        epics.remove(epicId);
    }

    public void removeSubtaskById(int subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();

    }

    public void removeAllSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIdToRemove = new ArrayList<>(epic.getSubtasksId());
        for (int subtaskId : subtaskIdToRemove) {
            subtasks.remove(subtaskId);
        }
    }

    public ArrayList<Subtask> printSubtasksForEpic(int epicId) {
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

    public void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        if (subtasksId.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allDone = true;
        boolean allNew = true;
        for (int subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            if(subtask.getStatus() != Status.DONE) {
                allDone = false;
            } else if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }
        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
