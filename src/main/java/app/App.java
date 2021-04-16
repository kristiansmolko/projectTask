package app;

import database.DatabaseImpl;
import entity.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class App extends Application {
    DatabaseImpl dat = new DatabaseImpl();
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextArea list = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        String date = format.format(new Date());

        list.setMaxWidth(300);
        list.setMaxHeight(300);
        list.setEditable(false);

        GridPane buttonPane = new GridPane();
        buttonPane.setTranslateX(-20);
        buttonPane.setTranslateY(20);
        buttonPane.setVgap(15);
        Button allBtn = new Button("Get all");
        Button newBtn = new Button("New task");
        allBtn.setMaxHeight(20); allBtn.setMaxWidth(100);
        allBtn.setOnAction(e2 -> {
            list.setText("");
            List<Task> listOfAll = dat.getAllTasks();
            for (Task t: listOfAll){
                list.appendText(t.getDate() + ": \n" + t.getName() + ", Priority: " + t.getPriority() +
                        (t.getPrice()>0.0?(", Price: " + t.getPrice() + ": "):": ")
                        + (t.isDone()?"Done":"Not done") + "\n" +
                        "----------------------------------------------------\n");
            }
        });

        newBtn.setMaxHeight(20); newBtn.setMaxWidth(100);
        newBtn.setOnAction(e -> {
            BorderPane root2 = new BorderPane();
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setTranslateX(20);
            grid.setTranslateY(50);

            Label name = new Label("Task name");
            TextField nameField = new TextField();
            name.setLabelFor(nameField);

            Label priority = new Label("Priority");
            ObservableList<Integer> observable = FXCollections.observableArrayList();
            observable.add(1); observable.add(2); observable.add(3);
            ComboBox<Integer> box = new ComboBox<>(observable);
            box.setMaxWidth(30); box.setMaxHeight(20);
            priority.setLabelFor(box);

            Label done = new Label("Done");
            CheckBox checkBox = new CheckBox();
            done.setLabelFor(checkBox);

            Label price = new Label("price");
            TextField priceField = new TextField();
            priceField.setPromptText("Not mandatory");
            price.setLabelFor(priceField);

            Button create = new Button("Add task");
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

            grid.addRow(0, name, priority, done, price);
            grid.addRow(1, nameField, box, checkBox, priceField);

            root2.setCenter(grid);
            root2.setBottom(create);

            Scene scene2 = new Scene(root2, 600, 400);
            stage.setScene(scene2);
            stage.setTitle("New task");
            stage.show();
        });

        buttonPane.addRow(0, allBtn);
        buttonPane.addRow(1, newBtn);

        root.setCenter(list);
        root.setRight(buttonPane);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Task manager");
        stage.show();
    }
}
