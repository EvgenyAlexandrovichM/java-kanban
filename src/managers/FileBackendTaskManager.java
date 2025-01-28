package managers;

import exceptions.ManagerSaveException;
import statuses.Status;
import statuses.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackendTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackendTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(subtask.toString() + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл");
        }
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        if (type.equals(TaskType.TASK)) {
            return new Task(name, description, id, status, type);

        } else if (type.equals(TaskType.EPIC)) {
            Epic epic = new Epic(name, description, id, status, type);
            epic.setStatus(status);
            return epic;
        } else {
            int epicId = Integer.parseInt(parts[5]);
            return new Subtask(name, description, id, status, type, epicId);
        }
    }

    public static FileBackendTaskManager loadFromFile(File file) {
        FileBackendTaskManager taskManager = new FileBackendTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task != null) {
                    if (task.getType().equals(TaskType.TASK)) {
                        taskManager.addTask(task);
                    } else if (task.getType().equals(TaskType.EPIC)) {
                        taskManager.addEpic((Epic) task);
                    } else if (task.getType().equals(TaskType.SUBTASK)) {
                        taskManager.addSubtask((Subtask) task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время загрузки из файла");
        }
        return taskManager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task getTasksById(int taskId) {
        return super.getTasksById(taskId);
    }

    @Override
    public Epic getEpicsById(int epicId) {
        return super.getEpicsById(epicId);
    }

    @Override
    public Subtask getSubtasksById(int subtaskId) {
        return super.getSubtasksById(subtaskId);
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        super.removeSubtaskById(subtaskId);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public List<Subtask> getSubtasksForEpic(int epicId) {
        return super.getSubtasksForEpic(epicId);
    }
}
