import managers.Managers;
import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import managers.TaskManager;

public class Main {
    private static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        addTasks();

    }

    private static void addTasks() {
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать " +
                "влажную уборку", Status.NEW);
        taskManager.addTask(task1);

        Task updateTask1 = new Task("Не забыть убраться в комнате", "Можно без влажной уборки",
                task1.getId(), Status.IN_PROGRESS);
        taskManager.updateTask(updateTask1);
        taskManager.addTask(new Task("Сходить в спортзал", "Сегодня день ног и спины", Status.NEW));


        Epic doHomeWork = new Epic("Сделать дипломную работу", "Нужно успеть за месяц");
        taskManager.addEpic(doHomeWork);
        Subtask doHomeWork1 = new Subtask("Сделать презентацию", "12 слайдов", doHomeWork.getId(),
                Status.DONE, doHomeWork.getId());
        Subtask doHomeWork2 = new Subtask("Подготовить речь", "На 5-7 минут выступления",
                doHomeWork.getId(), Status.DONE, doHomeWork.getId());
        Subtask doHomeWork3 = new Subtask("Погладить рубашку", "Желательно черную", doHomeWork.getId(),
                Status.NEW, doHomeWork.getId());
        taskManager.addSubtask(doHomeWork1);
        taskManager.addSubtask(doHomeWork2);
        taskManager.addSubtask(doHomeWork3);
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        Subtask doHomeWork4 = new Subtask("Лучше пойду в свитере", "В рубашке будет неудобно",
                doHomeWork3.getId(), Status.DONE, doHomeWork.getId());
        taskManager.updateSubtask(doHomeWork4);
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(50));
        taskManager.removeSubtaskById(doHomeWork3.getId());
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getEpics());
        taskManager.removeEpicById(doHomeWork1.getId());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getEpics());
        Epic epic2 = new Epic("Сделать тз", "Сегодня крайний день", doHomeWork.getId(), Status.NEW);
        taskManager.updateEpic(epic2);
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasks());
        Subtask subtask1 = new Subtask("Подготовиться", "выучить", doHomeWork2.getId(), Status.NEW, doHomeWork.getId());
        taskManager.updateSubtask(subtask1);
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasks());
        taskManager.addEpic(epic2);
        System.out.println(taskManager.getEpics());
        taskManager.removeEpicById(15);
        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        taskManager.removeSubtaskById(doHomeWork1.getId());
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasksForEpic(doHomeWork.getId()));
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getTasksById(task1.getId()));
        System.out.println(taskManager.getSubtasksById(doHomeWork2.getId()));
        System.out.println(taskManager.getSubtasksById(doHomeWork2.getId()));
        System.out.println(taskManager.getEpicsById(doHomeWork1.getEpicId()));
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getHistory());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getTasksById(task1.getId()));
        System.out.println(taskManager.getSubtasksById(doHomeWork2.getId()));
        System.out.println(taskManager.getEpicsById(doHomeWork1.getEpicId()));
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getHistory());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getTasksById(task1.getId()));
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getHistory());
        taskManager.removeTaskById(task1.getId());
        System.out.println(taskManager.getHistory());
    }
}