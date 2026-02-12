public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        manager.addTask(new Task(1, "Aprender Java"));
        manager.addTask(new Task(2, "Construir backend"));
        manager.addTask(new Task(3, "Crear portfolio"));

        System.out.println("Tareas iniciales:");
        for (Task task : manager.getTasks()) {
            System.out.println(task.getText());
        }

        manager.toggleTask(1);
        manager.deleteTask(2);

        System.out.println("\nDespués de cambios:");
        for (Task task : manager.getTasks()) {
            System.out.println(
                task.getText() + " - completada: " + task.isCompleted()
            );
        }
    }
}
