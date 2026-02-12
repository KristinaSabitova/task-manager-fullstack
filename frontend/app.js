const input = document.getElementById("taskInput");
const button = document.getElementById("addTaskBtn");
const list = document.getElementById("taskList");

button.addEventListener("click", addTask);

function addTask() {
    const text = input.value.trim();
    if (text === "") return;

    const li = document.createElement("li");

    const span = document.createElement("span");
    span.textContent = text;

    span.addEventListener("click", () => {
        li.classList.toggle("completed");
    });

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "X";
    deleteBtn.classList.add("delete-btn");

    deleteBtn.addEventListener("click", () => {
        li.remove();
    });

    li.appendChild(span);
    li.appendChild(deleteBtn);

    list.appendChild(li);
    input.value = "";
}
