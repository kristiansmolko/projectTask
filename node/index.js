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

app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');
    next();
});

app.use(express.json());

function connect(req, res, query) {
    mongoClient.connect(url, function(err, db) {
        if (err) throw err;
        var dbo = db.db("myFirstDb");
        dbo.collection("tasks").find(query).toArray(function(err, result) {
            if (err) throw err;
            res.status(200).send(result);
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

function update(req, res, query, set){
    mongoClient.connect(url, function(err, db) {
        if (err) throw err;
        var dbo = db.db("myFirstDb");
        dbo.collection("tasks").updateOne(query, set, (err, result) => {
            if (err) throw err;
            console.log("Document updated");
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
        if (prior < 0 || prior > 3) res.status(404).send("Wrong value");
        else connect(req, res, query);
    } else if (req.query.done && !req.query.priority){
        var bool = req.query.done;
        let query = 'undefined';
        if (bool === "true" || bool === "True") query = {done: true};
        else if (bool === "false" || bool === "False") query = {done: false};
        if (query === 'undefined') res.status(404).send("Wrong value");
        else connect(req, res, query);
    } else if (req.query.done && req.query.priority){
        var prior = parseInt(req.query.priority)
        if (req.query.done !== "true" && req.query.done !== "false" || prior > 3 ||
        prior < 0) res.status(404).send("Wrong value");
        else {
            var query = {$and : [
            {done: req.query.done=="true"?true:false},
            {priority: prior}
            ]}
            connect(req, res, query);
        }
    } else if (req.query.find){
        if (typeof req.query.find !== "string") res.status(404).send("Wrong value");
        else {
            let query = {$regex: req.query.find};
            query.$options = "$i";
            query = {name: query};
            connect(req, res, query);
        }
    }else connect(req, res, {});
})

app.post('/task/new', (req, res) => {
    if (!req.body.task && !req.body.done && !req.body.priority) res.status(404).send("Wrong values");
    if ((typeof req.body.task != "string") || (typeof req.body.priority != "number")
    || (typeof req.body.done != 'boolean')) res.status(404).send("Wrong values");
    let date = getDate();
    var name = req.body.name;
    var prior = parseInt(req.body.priority);
    var done = req.body.done=="true"?true:false;
    var price = parseFloat(req.body.price);
    var doc = {
        name: name,
        priority: prior,
        date: date,
        done: done,
    };
    if (req.body.price && (typeof price != "number")) res.status(404).send("Wrong values");
    if (price > 0) doc.price = price;
    console.log(doc);
    insert(req, res, doc);
    res.status(201).send("Document inserted");
})

app.put('/task/done', (req, res) => {
    if (!req.body._id) res.status(400).send("Wrong value");
    console.log(req.body._id);
    var query = {_id: new mongodb.ObjectID(req.body._id)};
    var set = {done: true};
    set = {$set: set}
    update(req, res, query, set);
})

app.listen(3000, () => {
    console.log('Server runs on port 3000');
})