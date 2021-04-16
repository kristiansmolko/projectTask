package entity;

public class Task {
    private String name;
    private double price;
    private int priority;
    private boolean done;
    private String date;

    public Task(String name, String date, int priority, boolean done) {
        this.name = name;
        this.date = date;
        this.priority = priority;
        this.done = done;
    }

    public Task(String name, String date, int priority, boolean done, double price) {
        this(name, date, priority, done);
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public String getDate() {
        return date;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
