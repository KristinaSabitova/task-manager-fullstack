const input = document.getElementById("taskInput");
const button = document.getElementById("addTaskBtn");
const list = document.getElementById("taskList");

button.addEventListener("click", () => {
    const text = input.value;

    if (text === "") return;

    const li = document.createElement("li");
    li.textContent = text;

    list.appendChild(li);
    input.value = "";
});
