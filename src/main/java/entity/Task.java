package entity;

import java.util.Date;

public class Task {
    private String name;
    private double price;
    private int priority;
    private boolean done;
    private Date date;

    public Task(String name, double price, int priority, boolean done) {
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.done = done;
    }

    public Task(String name, Date date, double price, int priority, boolean done) {
        this(name, price, priority, done);
        this.date = date;
    }
}
