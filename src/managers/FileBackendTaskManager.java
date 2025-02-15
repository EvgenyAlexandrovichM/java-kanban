package managers;

import exceptions.ManagerSaveException;
import enums.Status;
import enums.TaskType;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackendTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackendTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id, type, name, status, description, startTime, duration, epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении данных в файл");
        }
    }

    private static String toString(Task task) {
        /* String startTimeString;
        if (task.getStartTime() != null) {
            startTimeString = task.getStartTime().toString();
        } else {
            startTimeString = "null";
        } */
        String startTimeString = (task.getStartTime() != null) ? task.getStartTime().toString() : "null";
        String endTimeString = (task.getEndTime() != null) ? task.getEndTime().toString() : "null";
        String durationString = (task.getDuration() != null) ? task.getDuration().toString() : "null";
        String[] toJoin = {
                Integer.toString(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                startTimeString,
                durationString
        };
        return String.join(",", toJoin);
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        try {
            int id = Integer.parseInt(parts[0]);
            TaskType type = TaskType.valueOf(parts[1]);
            String name = parts[2];
            Status status = Status.valueOf(parts[3]);
            String description = parts[4];
            LocalDateTime startTime = null;
            if (!parts[5].equals("null")) {
                try {
                    startTime = LocalDateTime.parse(parts[5]);
                } catch (java.time.format.DateTimeParseException e) {
                    throw new ManagerSaveException("Ошибка при парсинге даты");
                }
            }
            Duration duration = Duration.ofMinutes(Long.parseLong(parts[6]));

            if (type.equals(TaskType.TASK)) {
                return new Task(name, description, id, status, startTime, duration);

            } else if (type.equals(TaskType.EPIC)) {
                return new Epic(name, description, id, status);

            } else {
                int epicId = Integer.parseInt(parts[7]);
                return new Subtask(name, description, id, status, startTime, duration, epicId);
            }
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при парсинге строки");
        }
    }


    public static FileBackendTaskManager loadFromFile(File file) {
        FileBackendTaskManager taskManager = new FileBackendTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            int maxId = 1;
            while (reader.ready()) {
                line = reader.readLine();
                Task task = fromString(line);
                if (task != null) {
                    taskManager.loadTask(task);
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                }
            }
            taskManager.setNextId(maxId + 1);
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
}
