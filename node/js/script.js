const btn = document.getElementById('addButton');

btn.addEventListener("click", () => {
    var name = document.getElementById('nameTask').value;
    var priority = parseInt(document.getElementById('priority').value);
    var price = parseFloat(document.getElementById('price').value);
    const json = {
        name: name,
        priority: priority,
    }
    if (price > 0) json.price = price;
    console.log(json);
    $.ajax({
        url: "http://localhost:3000/task/new",
        type: "post",
        dataType: "application/json",
        data: json,
        success: (result) => {
            console.log(result);
        },
        error: (error) => {
            console.log(error);
        }
    })
});