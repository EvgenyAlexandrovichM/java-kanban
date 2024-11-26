public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Убраться в комнате", "Протереть пыль, пропылесосить ковер, сделать влажную уборку");
        taskManager.addTask(task1);
        Task task2 = new Task("Доделать тз в яндекс практикум", "Желательно до конца своих выходных, ну камон, 5 спринт ждет");
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Купить новую машину", "Пожалуйста, только не китайца");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Накопить денег", "В текущих реалиях миллионов 15", 3);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Выбрать марку автомобиля", "15 миллионов буду собирать лет 30, а вот 200 тысяч на ладу весту - найду", 3);
        taskManager.addSubtask(subtask2);
        Epic epic2 = new Epic("Сделать ремонт", "А может и так сойдет?");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Поклеить новый обои", "На эти бабушкины же уже без слез не взглянешь", 6);
        taskManager.addSubtask(subtask3);

        System.out.println("Задачи:");
        System.out.println("-".repeat(50));
        taskManager.printAllTasks();
        System.out.println("-".repeat(50));
        System.out.println("Эпики:");
        System.out.println("-".repeat(50));
        taskManager.printAllEpics();
        System.out.println("-".repeat(50));
        System.out.println("Подзадачи:");
        System.out.println("-".repeat(50));
        taskManager.printAllSubtasks();
        System.out.println("-".repeat(50));
        System.out.println("Обновляем статус сначала одной подзадачи:");
        System.out.println("-".repeat(50));
        subtask1.setStatus(Status.DONE);
        taskManager.updateEpicStatus(epic1);
        System.out.println(taskManager.getEpicsById(3));
        System.out.println("-".repeat(50));
        System.out.println("Обновляем статус второй подзадачи:");
        System.out.println("-".repeat(50));
        subtask2.setStatus(Status.DONE);
        taskManager.updateEpicStatus(epic1);
        System.out.println(taskManager.getEpicsById(3));
        System.out.println("-".repeat(50));
        System.out.println("Удаляем одну из задач и один из эпиков:");
        System.out.println("-".repeat(50));
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(6);
        taskManager.printAllTasks();
        taskManager.printAllEpics();


    }
}
