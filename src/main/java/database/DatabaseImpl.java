package database;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import entity.Task;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;


public class DatabaseImpl implements Database {
    private final MongoClient client = new MongoClient();
    private final MongoDatabase database = client.getDatabase("myFirstDb");
    private final MongoCollection<Document> collection = database.getCollection("tasks");

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
    public void completeTask(String id){
        collection.updateOne(Filters.eq("_id", new ObjectId(id)), Updates.set("done", true));
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
                Task task = getTaskOutOfJSON(object);
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
                Task task = getTaskOutOfJSON(object);
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

    public List<Task> getTasksByName(String name){
        List<Task> list = new ArrayList<>();
        Document doc = new Document();
        Document regex = new Document();
        regex.put("$regex", name);
        regex.put("$options", "\"$i\"");
        doc.put("name", regex);
        FindIterable<Document> find = collection.find(doc);
        for (Document d: find){
            try {
                JSONObject object = (JSONObject) new JSONParser().parse(d.toJson());
                Task task = getTaskOutOfJSON(object);
                list.add(task);
            } catch (ParseException e) { e.printStackTrace(); }
        }
        return list;
    }

    @Override
    public Task getTaskByName(String name){
        Document doc = new Document();
        doc.put("name", name);
        FindIterable<Document> find = collection.find(doc);
        for (Document d: find){
            try {
                JSONObject object = (JSONObject) new JSONParser().parse(d.toJson());
                return getTaskOutOfJSON(object);
            } catch (ParseException e) { e.printStackTrace(); }
        }
        return null;
    }

    @Override
    public void deleteDoneTasks(){
        collection.deleteMany(new Document().append("done", true));
    }

    private Task getTaskOutOfJSON(JSONObject object){
        JSONObject idJSON = (JSONObject) object.get("_id");
        String id = (String) idJSON.get("$oid");
        String name = (String) object.get("name");
        String date = (String) object.get("date");
        int priority = Integer.parseInt(String.valueOf(object.get("priority")));
        boolean done = (boolean) object.get("done");
        Task task = new Task(id, name, date, priority, done);
        if (object.get("price") != null) {
            task.setPrice(Double.parseDouble(String.valueOf(object.get("price"))));
        }
        return task;
    }
}
