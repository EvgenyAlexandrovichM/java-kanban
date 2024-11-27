public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;

    }

    /* конструктор ниже выглядит бесполезным, потому что, не проставляя статусы подзадачам, ломается логика метода
     updateEpicStatus, из-за присвоения статусу null. Тут либо отработать этот момент в условии, либо не использовать
      конструктор ниже.
      */

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
