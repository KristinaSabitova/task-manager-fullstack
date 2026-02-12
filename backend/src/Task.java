public class Task {

    private long id;
    private String text;
    private boolean completed;

    public Task(long id, String text) {
        this.id = id;
        this.text = text;
        this.completed = false;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
    }
}
