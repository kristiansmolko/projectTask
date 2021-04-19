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
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String date = format.format(new Date());
    private final ListView<String> list = new ListView<>();
    private final Alert alert = new Alert(Alert.AlertType.WARNING);
    private final Button check = markDoneBtn();
    private Stage ourStage = new Stage();
    private final BorderPane root = rootPane();
    private final Scene mainScene = new Scene(root, 500, 400);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ourStage = stage;
        ourStage.setScene(mainScene);
        ourStage.setTitle("Task manager");
        ourStage.show();
    }

    private Button allMessages(){
        Button button = new Button("Get all");
        button.setMaxHeight(20); button.setMaxWidth(100);
        button.setOnAction(e2 -> {
            List<Task> listOfAll = dat.getAllTasks();
            check.setVisible(true);
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
        create.setTranslateX(100);
        create.setTranslateY(-50);
        create.setMaxHeight(50); create.setMaxWidth(200);
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
            ourStage.setScene(mainScene);
            ourStage.show();
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
                check.setVisible(true);
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

    private Button markDoneBtn(){
        Button check = new Button("Done");
        check.setVisible(false);
        check.setOnAction(e -> {
            String[] temp;
            ObservableList<String> selected = list.getSelectionModel().getSelectedItems();
            for (String o: selected) {
                temp = o.split(":");
                String[] temp2 = temp[3].split(",");
                String name = temp2[0].strip();
                Task task = dat.getTaskByName(name);
                if (!task.isDone())
                    dat.completeTask(task.getId());
            }
        });
        return check;
    }

    private Button byNameMessage(TextField field){
        Button button = new Button("Name");
        button.setMaxWidth(100);
        button.setMaxHeight(20);
        button.setOnAction(e -> {
            if (field.getText().equals("") || field.getText() == null){
                alert.setContentText("Enter valid name");
                alert.showAndWait();
            } else {
                check.setVisible(true);
                list.getItems().clear();
                String name = field.getText();
                Task t = dat.getTaskByName(name);
                list.getItems().add(t.getDate() + ": \n" + t.getName() + ", Priority: " + t.getPriority() +
                        (t.getPrice() > 0.0 ? (", Price: " + t.getPrice() + ": ") : ": ")
                        + (t.isDone() ? "Done" : "Not done") + "\n" +
                        "----------------------------------------------------\n");
            }
        });
        return button;
    }

    private GridPane priorityPane(){
        GridPane priority = new GridPane();
        priority.setMaxWidth(100);
        priority.setVgap(10);
        ComboBox<Integer> choosePriority = priorityBox();
        Button byBtn = byPriorityMessages(choosePriority);
        priority.addRow(0, byBtn);
        priority.addRow(1, choosePriority);
        return priority;
    }

    private GridPane namePane(){
        GridPane byName = new GridPane();
        byName.setMaxWidth(100);
        byName.setVgap(10);
        TextField name = new TextField();
        name.setMaxWidth(100);
        Button btnByName = byNameMessage(name);
        byName.addRow(0, btnByName);
        byName.addRow(1, name);
        return byName;
    }

    private GridPane buttons(){
        GridPane buttonPane = new GridPane();
        buttonPane.setMaxWidth(100);
        buttonPane.setTranslateX(-10);
        buttonPane.setTranslateY(20);
        buttonPane.setVgap(15);

        Button allBtn = allMessages();
        Button newBtn = new Button("New task");
        newBtn.setMaxHeight(20); newBtn.setMaxWidth(100);
        newBtn.setOnAction(e -> {
            BorderPane root2 = newMessage();
            Scene scene2 = new Scene(root2, 600, 400);
            ourStage.setScene(scene2);
            ourStage.setTitle("New task");
            ourStage.show();
        });

        GridPane priority = priorityPane();

        GridPane byName = namePane();

        Button deleteDone = btnDelete();

        buttonPane.addRow(0, allBtn);
        buttonPane.addRow(1, newBtn);
        buttonPane.addRow(2, priority);
        buttonPane.addRow(3, byName);
        buttonPane.addRow(4, check);
        buttonPane.addRow(5, deleteDone);

        return buttonPane;
    }

    private Button btnDelete() {
        Button button = new Button("Delete done");
        button.setMaxWidth(100);
        button.setMaxHeight(20);
        button.setOnAction(e -> {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete all done tasks?");
            alert.showAndWait().ifPresent((buttonType -> {
                if (buttonType == ButtonType.OK)
                    dat.deleteDoneTasks();
                else
                    alert.close();
            }));
        });
        return button;
    }

    private BorderPane rootPane(){
        BorderPane root = new BorderPane();
        list.setMaxWidth(370);
        list.setMaxHeight(300);
        list.setEditable(false);

        GridPane buttonPane = buttons();

        root.setCenter(list);
        root.setRight(buttonPane);
        return root;
    }
}
