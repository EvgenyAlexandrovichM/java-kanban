package managers;

import enums.Status;
import enums.TaskType;
import exceptions.ManagerValidateException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private int nextId = 1;


    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        addPrioritizedTask(task);
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
        updateTimeEpic(epic);
        addPrioritizedTask(subtask);
    }

    @Override
    public void updateTask(Task task) {
        int taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.removeIf(t -> t.getId() == task.getId());
        addPrioritizedTask(task);
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
        if (epicId == subtask.getEpicId()) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(epicId);
            updateEpicStatus(epic);
            // здесь по логике тоже нужно обновлять время эпика updateTimeEpic(epic);
            prioritizedTasks.removeIf(t -> t.getId() == subtaskId);
        }
        addPrioritizedTask(subtask);
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
        if (epic != null) {
            historyManager.addTask(epic);
        }
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtasksById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.addTask(subtask);
        }
        return subtasks.get(subtaskId);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeTaskById(int taskId) {
        if (!tasks.containsKey(taskId)) {
            return;
        }
        tasks.remove(taskId);
        historyManager.remove(taskId);
        prioritizedTasks.removeIf(t -> t.getId() == taskId);
    }

    @Override
    public void removeEpicById(int epicId) {
        if (!epics.containsKey(epicId)) {
            return;
        }
        ArrayList<Integer> epicSubtasks = epics.get(epicId).getSubtasksId();
        for (Integer subtaskId : epicSubtasks) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
            prioritizedTasks.removeIf(t -> t.getId() == subtaskId);
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
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
                        updateTimeEpic(epic);
                    }
                }
            }
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
            prioritizedTasks.removeIf(t -> t.getId() == subtaskId);
        }
    }

    @Override
    public void removeAllTasks() {
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
            prioritizedTasks.removeIf(t -> t.getId() == taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (int epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        for (int subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
            prioritizedTasks.removeIf(t -> t.getId() == subtaskId);
        }
        epics.clear();
        subtasks.clear();

    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksId();
            updateEpicStatus(epic);
            updateTimeEpic(epic);
        }
        for (int subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
            prioritizedTasks.removeIf(task -> task.getId() == subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public List<Subtask> getSubtasksForEpic(int epicId) {
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtasksId = epic.getSubtasksId();
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

    private void updateTimeEpic(Epic epic) {
        List<Subtask> subtasks = getSubtasksForEpic(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
            epic.setEndtime(null);
            return;
        }

        LocalDateTime startTime = subtasks.getFirst().getStartTime();
        LocalDateTime endTime = subtasks.getFirst().getEndTime();
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null) {
                if (startTime != null && subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (endTime != null && subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }
        epic.setStartTime(startTime);
        epic.setEndtime(endTime);
        epic.setDuration(totalDuration);
    }

    protected void loadTask(Task task) {
        if (task.getType().equals(TaskType.TASK)) {
            tasks.put(task.getId(), task);
        } else if (task.getType().equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
            epics.put(epic.getId(), epic);
        } else if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.addSubtask(subtask.getId());
        }
    }

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private boolean isValidate(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(t -> t.getStartTime().isBefore(task.getEndTime())
                        && task.getStartTime().isBefore(t.getEndTime()));
    }

    public void addPrioritizedTask(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        if (isValidate(task)) {
            throw new ManagerValidateException("Задача № " + task.getId() + " пересекается с другими задачами");
        }
        prioritizedTasks.add(task);
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
