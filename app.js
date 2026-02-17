const input = document.getElementById("taskInput");
const addBtn = document.getElementById("addTaskBtn");
const list = document.getElementById("taskList");

let tasks = JSON.parse(localStorage.getItem("tasks")) || [
  { id: 1, text: "Aprender Java", completed: false },
  { id: 2, text: "Crear API", completed: false },
  { id: 3, text: "Conectar frontend", completed: false }
];

function save() {
  localStorage.setItem("tasks", JSON.stringify(tasks));
}

function render() {
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

  save();
}

function addTask() {
  const text = input.value.trim();
  if (!text) return;

  tasks.push({
    id: Date.now(),
    text,
    completed: false
  });

  input.value = "";
  render();
}

function toggleTask(id) {
  tasks = tasks.map(t =>
    t.id === id ? { ...t, completed: !t.completed } : t
  );
  render();
}

function deleteTask(id) {
  tasks = tasks.filter(t => t.id !== id);
  render();
}

addBtn.onclick = addTask;

input.addEventListener("keydown", e => {
  if (e.key === "Enter") {
    e.preventDefault();
    addTask();
  }
});

render();