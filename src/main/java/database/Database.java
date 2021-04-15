package database;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entity.Task;
import org.bson.Document;


public class Database {
    private MongoClient client = new MongoClient();
    private MongoDatabase database = client.getDatabase("myFirstDb");
    private MongoCollection<Document> collection = database.getCollection("tasks");

    public void insertTask(Task task){
        collection.insertOne(new Document("task", new Gson().toJson(task)));
    }
}
