package app;

import database.DatabaseImpl;
import entity.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App extends Application {
    DatabaseImpl dat = new DatabaseImpl();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String date = format.format(new Date());
    private ListView<String> list = new ListView<>();
    private BorderPane root = new BorderPane();
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        list.setMaxWidth(300);
        list.setMaxHeight(300);
        list.setEditable(false);

        GridPane buttonPane = new GridPane();
        buttonPane.setTranslateX(-10);
        buttonPane.setTranslateY(20);
        buttonPane.setVgap(15);

        Button allBtn = allMessages();
        Button newBtn = new Button("New task");
        newBtn.setMaxHeight(20); newBtn.setMaxWidth(100);
        newBtn.setOnAction(e -> {
            BorderPane root2 = newMessage();
            Scene scene2 = new Scene(root2, 600, 400);
            stage.setScene(scene2);
            stage.setTitle("New task");
            stage.showAndWait();
        });

        GridPane priority = new GridPane();
        priority.setMaxWidth(100);
        priority.setVgap(10);
        ComboBox<Integer> choosePriority = priorityBox();
        Button byBtn = byPriorityMessages(choosePriority);
        priority.addRow(0, byBtn);
        priority.addRow(1, choosePriority);

        buttonPane.addRow(0, allBtn);
        buttonPane.addRow(1, newBtn);
        buttonPane.addRow(2, priority);

        root.setCenter(list);
        root.setRight(buttonPane);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Task manager");
        stage.show();
    }

    private Button allMessages(){
        Button button = new Button("Get all");
        button.setMaxHeight(20); button.setMaxWidth(100);
        button.setOnAction(e2 -> {
            List<Task> listOfAll = dat.getAllTasks();
            addToList(listOfAll);
        });
        return button;
    }

    private ComboBox<Integer> priorityBox(){
        ObservableList<Integer> observable = FXCollections.observableArrayList();
        observable.add(1); observable.add(2); observable.add(3);
        ComboBox<Integer> box = new ComboBox<>(observable);
        box.setMaxWidth(30); box.setMaxHeight(20);
        return box;
    }

    private Button newMessage(TextField nameField, ComboBox<Integer> box, CheckBox checkBox, TextField priceField){
        Button create = new Button("New task");
        create.setMaxHeight(20); create.setMaxWidth(50);
        create.setOnAction(e2 -> {
            String taskName = nameField.getText().trim();
            int taskPriority = box.getValue();
            boolean taskDone = checkBox.isSelected();
            Task newTask = new Task(taskName, date, taskPriority, taskDone);
            double taskPrice;
            if (priceField.getText() != null && !(priceField.getText().equalsIgnoreCase(""))) {
                taskPrice = Double.parseDouble(priceField.getText());
                newTask.setPrice(taskPrice);
            } else
                taskPrice = 0;
            dat.insertTask(newTask);
        });
        return create;
    }

    private BorderPane newMessage(){
        BorderPane root2 = new BorderPane();
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setTranslateX(20);
        grid.setTranslateY(50);

        Label name = new Label("Task name");
        TextField nameField = new TextField();
        name.setLabelFor(nameField);

        Label priority = new Label("Priority");
        ComboBox<Integer> box = priorityBox();
        priority.setLabelFor(box);

        Label done = new Label("Done");
        CheckBox checkBox = new CheckBox();
        done.setLabelFor(checkBox);

        Label price = new Label("price");
        TextField priceField = new TextField();
        priceField.setPromptText("Not mandatory");
        price.setLabelFor(priceField);

        Button create = newMessage(nameField, box, checkBox, priceField);

        grid.addRow(0, name, priority, done, price);
        grid.addRow(1, nameField, box, checkBox, priceField);

        root2.setCenter(grid);
        root2.setBottom(create);
        return root2;
    }

    private Button byPriorityMessages(ComboBox<Integer> priority){
        Button button = new Button("Priority");
        button.setMaxWidth(100);
        button.setMaxHeight(20);
        button.setOnAction(e -> {
            if (priority.getValue() == null){
                alert.setContentText("Choose priority");
                alert.showAndWait();
            } else {
                int priorityValue = priority.getValue();
                List<Task> listPriority = dat.getTasksByPriority(priorityValue);
                addToList(listPriority);
            }
        });
        return button;
    }

    private void addToList(List<Task> taskList){
        list.getItems().clear();
        for (Task t: taskList){
            list.getItems().add(t.getDate() + ": \n" + t.getName() + ", Priority: " + t.getPriority() +
                    (t.getPrice()>0.0?(", Price: " + t.getPrice() + ": "):": ")
                    + (t.isDone()?"Done":"Not done") + "\n" +
                    "----------------------------------------------------\n");
        }
    }
}
