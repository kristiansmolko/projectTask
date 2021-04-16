package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.Task;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Database {
    private MongoClient client = new MongoClient();
    private MongoDatabase database = client.getDatabase("myFirstDb");
    private MongoCollection<Document> collection = database.getCollection("tasks");

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

    public void completeTask(int id){

    }

    public List<Task> getAllTasks(){
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
                list.add(task);
            } catch (Exception e) { e.printStackTrace(); }
        }
        return list;
    }

    public List<Task> getAllTasks(boolean done){
        List<Task> list = new ArrayList<>();


        return list;
    }

    public List<Task> getTasksByPriority(){
        List<Task> list = new ArrayList<>();


        return list;
    }

    public List<Task> getTasksByName(String name){
        List<Task> list = new ArrayList<>();


        return list;
    }

    public void deleteDoneTasks(){

    }

}
