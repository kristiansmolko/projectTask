const express = require('express');

const app = express();

const mongodb = require('mongodb');

const mongoClient = mongodb.MongoClient;

var url = "mongodb://localhost:27017/";

app.use(
    express.urlencoded({
        extended: true
    })
);

app.use(express.json());

function connect(req, res, query) {
    mongoClient.connect(url, function(err, db) {
        if (err) throw err;
        var dbo = db.db("myFirstDb");
        dbo.collection("tasks").find(query).toArray(function(err, result) {
            if (err) throw err;
            res.send(result);
        })
        db.close();
    })
}

function insert(req, res, doc) {
    mongoClient.connect(url, function(err, db) {
        if (err) throw err;
        var dbo = db.db("myFirstDb");
        dbo.collection("tasks").insertOne(doc, (err, result) => {
            if (err) throw err;
            console.log("Document inserted");
            db.close();
        })
    })
}

function getDate() {
    var today = new Date();
    let year = today.getFullYear();
    let month = ("0" + (today.getMonth() + 1)).slice(-2);
    let day = ("0" + today.getDate()).slice(-2);
    let hours = ("0" + today.getHours()).slice(-2);
    let minutes = ("0" + today.getMinutes()).slice(-2);
    let seconds = ("0" + today.getSeconds()).slice(-2);
    var result = year + "-" + month + "-" + day + " " 
    + hours + ":" + minutes + ":" + seconds
    return result;
}

app.get('', (req, res) => {
    res.send('Hello there, General Kenobi');
})

app.get('/about', (req, res) => {
    res.send('<h1>Server: task manager </h1>');
})

app.get('/author', (req, res) => {
    res.send({'first name':'Kristian', 'last name':'Smolko'});
})

app.get('/task', (req, res) => {
    if (req.query.priority && !req.query.done){
        var prior = req.query.priority;
        var query = {priority: parseInt(prior, 10)};
        if (prior < 0 || prior > 3){
            throw new Error("Wrong value");
        }
        connect(req, res, query);
    } else if (req.query.done && !req.query.priority){
        var bool = req.query.done;
        let query;
        if (bool === "true" || bool === "True"){
            query = {done: true};
        } else if (bool === "false" || bool === "False"){
            query = {done: false};
        } else {
            throw new Error("Wrong value");
        }
        connect(req, res, query);
    } else if (req.query.done && req.query.priority){
        var prior = parseInt(req.query.priority)
        if (req.query.done !== "true" && req.query.done !== "false" || prior > 3 ||
        prior < 0) throw new Error("Wrong value");
        var query = {$and : [
        {done: req.query.done=="true"?true:false},
        {priority: prior}
        ]}
        connect(req, res, query);
    } else {
        connect(req, res, {});
        // mongoClient.connect(url, function(err, db) {
        //     if (err) throw err;
        //     var dbo = db.db("myFirstDb");
        //     dbo.collection("tasks").find().toArray(function(err, result) {
        //         if (err) throw err;
        //         res.send(result);
        //         db.close();
        //     })
        // })
    }
})

app.post('/task', (req, res) => {
    if (!req.body.task && !req.body.done && !req.body.priority) throw new Error("Wrong values");
    let date = getDate();
    var name = req.body.task;
    var prior = parseInt(req.body.priority);
    var done = req.body.done=="true"?true:false;
    var price = parseFloat(req.body.price);
    var doc = {
        name: name,
        priority: prior,
        date: date,
        done: done,
    };
    if (price > 0) doc.price = price;
    console.log(date);
    insert(req, res, doc);
    res.send("Document inserted");
})

app.listen(3000, () => {
    console.log('Server runs on port 3000');
})