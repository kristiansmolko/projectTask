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
});