import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void deleteTask(long id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    public void toggleTask(long id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.toggleCompleted();
            }
        }
    }
}
