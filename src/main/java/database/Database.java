package database;

import entity.Task;

import java.util.List;

public interface Database {
    public void insertTask(Task task);

    public void completeTask(String id);

    public List<Task> getAllTasks();

    public List<Task> getAllTasks(boolean done);

    public List<Task> getTasksByPriority(int priority);

    public Task getTaskByName(String name);

    public void deleteDoneTasks();
}
