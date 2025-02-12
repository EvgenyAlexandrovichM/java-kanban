package tasks;

import enums.Status;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endtime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);

    }

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);
    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.endtime = super.getEndTime();
    }

    public Epic(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, id, status, startTime, duration);
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId != this.getId()) {
            subtasksId.add(subtaskId);
        }
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtasksId() {
        subtasksId.clear();
    }

    public void removeSubtaskById(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

    public void setEndtime(LocalDateTime endTime) {
        this.endtime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endtime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

}
