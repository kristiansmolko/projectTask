//$(document).ready( () => { })

$(() => {
    console.log("Ready");
    $.ajax({
        url: "http://localhost:3000/task",
        type: "get",

        dataType: "json",

        statusCode: {
            200: (result) => {
                // console.log(result);
                for(var index in result){
                    const id = result[index]._id;
                    const name = result[index].name;
                    const priority = result[index].priority;
                    const date = result[index].date;
                    const done = result[index].done;
                    const price = result[index].price;
                    var text = "<label class=\"task_text\"> <b>" + name + "</b> <u>(" + date + ")</u> ";
                    text = text + "Priority: " + priority + "<br>";
                    if (result[index].price) text = text + "Price: " + price + "<br>";
                    text = text + "Done: " + done + "<br>";
                    if (!done) text = text + "<button id=" + id + " onClick=\"complete(this.id)\"> Complete";
                    text = text + "</label>";
                    $("#parent").append(text);
                }
            },
            400: (err) => {
                console.log("Bad request");
            },
            404: (err) => {
                console.log("Not found");
            }
        },
        
    })
});

function complete(id){
    id = {"_id": id};
    $.ajax({
        url: "http://localhost:3000/task/done",
        type: "put",
        data: id,
        success: (result) => {
        },
        error: (err) => {
            console.log("Error: " + err);
        }
    });
    
    location.reload();
}