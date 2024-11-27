public class Main {
    private static TaskManager taskManager = new TaskManager();

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
        System.out.println(taskManager.getAllEpics());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("-".repeat(50));
        Subtask doHomeWork4 = new Subtask("Лучше пойду в свитере", "В рубашке будет неудобно",
                doHomeWork3.getId(), Status.DONE, doHomeWork.getId());
        taskManager.updateSubtask(doHomeWork4);
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getAllEpics());
        System.out.println("-".repeat(50));
        taskManager.removeSubtaskById(doHomeWork3.getId());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getAllEpics());
        taskManager.removeEpicById(doHomeWork1.getId());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllEpics());
        Epic epic2 = new Epic("Сделать тз", "Сегодня крайний день", doHomeWork.getId());
        taskManager.updateEpic(epic2);
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllSubtasks());
        Subtask subtask1 = new Subtask("Подготовиться", "выучить", doHomeWork2.getId(), Status.NEW, doHomeWork.getId());
        taskManager.updateSubtask(subtask1);
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getAllSubtasks());
        taskManager.addEpic(epic2);
        System.out.println(taskManager.getAllEpics());
        taskManager.removeEpicById(15);
        System.out.println();
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.removeSubtaskById(doHomeWork1.getId());
        System.out.println();
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("-".repeat(50));
        System.out.println(taskManager.getSubtasksForEpic(doHomeWork.getId()));
    }
}