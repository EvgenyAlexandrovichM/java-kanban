package tasks;

import statuses.Status;
import statuses.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);


    }

    public Epic(String name, String description, int id) {
        super(name, description, id, Status.NEW);
    }

    public Epic(String name, String description, int id, Status status, TaskType type) {
        super(name, description, id, status, type);
        this.subtasksId = subtasksId;
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

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + ",";
    }
}
