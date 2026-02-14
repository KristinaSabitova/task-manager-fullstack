import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleServer {

    static List<Task> tasks = new ArrayList<>();
    static long nextId = 1;
    static final String FILE = "tasks.json";

    public static void main(String[] args) throws Exception {

        loadTasks();

        if (tasks.isEmpty()) {
            tasks.add(new Task(nextId++, "Aprender Java", false));
            tasks.add(new Task(nextId++, "Crear API", false));
            tasks.add(new Task(nextId++, "Conectar frontend", false));
            saveTasks();
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/tasks", exchange -> {
            addCors(exchange);

            String method = exchange.getRequestMethod();

            if (method.equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (method.equals("GET")) {
                sendJson(exchange, toJson());
            }

            else if (method.equals("POST")) {
                String body = readBody(exchange);
                String text = body.replace("{\"text\":\"", "").replace("\"}", "");

                tasks.add(new Task(nextId++, text, false));
                saveTasks();
                sendJson(exchange, toJson());
            }

            else if (method.equals("PUT")) {
                long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[2]);

                for (Task t : tasks) {
                    if (t.id == id) t.completed = !t.completed;
                }

                saveTasks();
                sendJson(exchange, toJson());
            }

            else if (method.equals("DELETE")) {
                long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[2]);

                tasks.removeIf(t -> t.id == id);
                saveTasks();
                sendJson(exchange, toJson());
            }
        });

        server.start();
        System.out.println("API en http://localhost:8080/tasks");
    }

    // -------------------------

    static void saveTasks() throws IOException {
        FileWriter fw = new FileWriter(FILE);
        fw.write(toJson());
        fw.close();
    }

    static void loadTasks() throws IOException {
        File f = new File(FILE);
        if (!f.exists()) return;

        String json = new BufferedReader(new FileReader(f))
                .lines().collect(Collectors.joining());

        if (json.length() < 2) return;

        json = json.substring(1, json.length() - 1);
        String[] items = json.split("\\},\\{");

        for (String item : items) {
            item = item.replace("{", "").replace("}", "");
            String[] parts = item.split(",");

            long id = Long.parseLong(parts[0].split(":")[1]);
            String text = parts[1].split(":")[1].replace("\"", "");
            boolean completed = Boolean.parseBoolean(parts[2].split(":")[1]);

            tasks.add(new Task(id, text, completed));
            nextId = Math.max(nextId, id + 1);
        }
    }

    static String toJson() {
        return tasks.stream()
                .map(t -> "{\"id\":" + t.id + ",\"text\":\"" + t.text + "\",\"completed\":" + t.completed + "}")
                .collect(Collectors.joining(",", "[", "]"));
    }

    static String readBody(HttpExchange ex) throws IOException {
        return new BufferedReader(new InputStreamReader(ex.getRequestBody()))
                .lines().collect(Collectors.joining());
    }

    static void sendJson(HttpExchange ex, String json) throws IOException {
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(200, data.length);
        ex.getResponseBody().write(data);
        ex.close();
    }

    static void addCors(HttpExchange ex) {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }
}