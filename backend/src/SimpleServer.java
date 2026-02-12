import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

public class SimpleServer {

    private static TaskManager manager = new TaskManager();

    public static void main(String[] args) throws Exception {

        manager.addTask(new Task(1, "Aprender Java"));
        manager.addTask(new Task(2, "Crear API"));
        manager.addTask(new Task(3, "Conectar frontend"));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/tasks", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                String json = manager.getTasks()
                        .stream()
                        .map(t -> "{ \"id\": " + t.getId() +
                                ", \"text\": \"" + t.getText() +
                                "\", \"completed\": " + t.isCompleted() + " }")
                        .collect(Collectors.joining(", ", "[", "]"));

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.length());

                OutputStream os = exchange.getResponseBody();
                os.write(json.getBytes());
                os.close();
            }
        });

        server.start();
        System.out.println("API en http://localhost:8080/tasks");
    }
}
