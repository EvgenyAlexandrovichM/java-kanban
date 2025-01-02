package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private class CustomLinkedList {
        private final Map<Integer, Node> historyMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node node = new Node(task);
            if (tail == null) {
                head = node;
                tail = node;
            } else {
                node.setPrev(tail);
                tail.setNext(node);
                tail = node;
            }
        }

        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tasks.add(node.getTask());
                node = node.getNext();
            }
            return tasks;
        }

        private void removeNode(Node node) {
            if (node == null) {
                return;
            }
            if (node.getPrev() == null) {
                head = node.getNext();
            } else {
                node.getPrev().setNext(node.getNext());
            }
            if (node.getNext() == null) {
                tail = node.getPrev();
            } else {
                node.getNext().setPrev(node.getPrev());
            }
        }
    }


    private final CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void addTask(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int taskId) {
        historyList.removeNode(historyList.get);
    }
}
