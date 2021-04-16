package app;

import database.Database;
import entity.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App extends Application {
    Database dat = new Database();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        String date = format.format(new Date());
        Task task = new Task("Do the dishes", date, 3, false);
        //dat.insertTask(task);
        List<Task> list = dat.getAllTasks();
        for (Task t: list){
            System.out.println(t.getName() + " " + t.getPrice());
        }
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Task manager");
        stage.show();
    }
}
