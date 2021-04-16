package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.Task;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;


public class DatabaseImpl implements Database {
    private MongoClient client = new MongoClient();
    private MongoDatabase database = client.getDatabase("myFirstDb");
    private MongoCollection<Document> collection = database.getCollection("tasks");

    @Override
    public void insertTask(Task task){
        Document doc = new Document();
        doc.put("name", task.getName());
        doc.put("date", task.getDate());
        doc.put("priority", task.getPriority());
        doc.put("done", task.isDone());
        if (task.getPrice() > 0)
            doc.put("price", task.getPrice());
        collection.insertOne(doc);
    }

    @Override
    public void completeTask(int id){

    }

    @Override
    public List<Task> getAllTasks(){
        return execute();
    }

    private List<Task> execute(){
        List<Task> list = new ArrayList<>();
        for (Document doc : collection.find()){
            try {
                JSONObject object = (JSONObject) new JSONParser().parse(doc.toJson());
                String name = (String) object.get("name");
                String date = (String) object.get("date");
                int priority = Integer.parseInt(String.valueOf(object.get("priority")));
                boolean done = (boolean) object.get("done");
                Task task = new Task(name, date, priority, done);
                if (object.get("price") != null) {
                    task.setPrice(Double.parseDouble(String.valueOf(object.get("price"))));
                }
                list.add(task);
            } catch (Exception e) { e.printStackTrace(); }
        }
        return list;
    }

    private <T> List<Task> executeByCriteria(String criteria, T value){
        List<Task> list = new ArrayList<>();
        for (Document doc : collection.find()){
            try {
                JSONObject object = (JSONObject) new JSONParser().parse(doc.toJson());
                String name = (String) object.get("name");
                String date = (String) object.get("date");
                int priority = Integer.parseInt(String.valueOf(object.get("priority")));
                boolean done = (boolean) object.get("done");
                Task task = new Task(name, date, priority, done);
                if (object.get("price") != null)
                    task.setPrice(Double.parseDouble(String.valueOf(object.get("price"))));
                if (object.get(criteria) == value ||
                        (value instanceof Number && Integer.parseInt(String.valueOf(object.get(criteria))) ==
                                Integer.parseInt(String.valueOf(value))) ||
                        (value instanceof String && ((String) object.get(criteria)).equals(value)))
                    list.add(task);
            } catch (Exception e) { e.printStackTrace(); }
        }
        return list;
    }

    @Override
    public List<Task> getAllTasks(boolean done){
        return executeByCriteria("done", done);
    }

    @Override
    public List<Task> getTasksByPriority(int priority){
        return executeByCriteria("priority", priority);
    }

    @Override
    public List<Task> getTasksByName(String name){
        return executeByCriteria("name", name);
    }

    @Override
    public void deleteDoneTasks(){

    }

}
