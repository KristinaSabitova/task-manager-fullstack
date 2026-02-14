const API = "http://localhost:8080/tasks";

const input = document.getElementById("taskInput");
const addBtn = document.getElementById("addTaskBtn");
const list = document.getElementById("taskList");

async function loadTasks() {
  const res = await fetch(API);
  const tasks = await res.json();
  render(tasks);
}

function render(tasks) {
  list.innerHTML = "";

  tasks.forEach(task => {
    const li = document.createElement("li");

    const text = document.createElement("span");
    text.textContent = task.text;
    text.style.textDecoration = task.completed ? "line-through" : "none";
    text.style.cursor = "pointer";

    text.onclick = () => toggleTask(task.id);

    const del = document.createElement("button");
    del.textContent = "❌";
    del.onclick = () => deleteTask(task.id);

    li.appendChild(text);
    li.appendChild(del);
    list.appendChild(li);
  });
}

async function addTask() {
  const text = input.value.trim();
  if (!text) return;

  await fetch(API, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ text })
  });

  input.value = "";
  loadTasks();
}

async function toggleTask(id) {
  await fetch(`${API}/${id}`, { method: "PUT" });
  loadTasks();
}

async function deleteTask(id) {
  await fetch(`${API}/${id}`, { method: "DELETE" });
  loadTasks();
}

addBtn.onclick = addTask;

input.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    e.preventDefault();
    addTask();
  }
});

loadTasks();