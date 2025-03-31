import React, { useEffect, useState, useCallback } from "react";
import TaskItem from "./TaskItem";

const TaskList = ({ refresh }) => {
  const [tasks, setTasks] = useState([]);
  const [filter, setFilter] = useState("all"); // all | active | completed
  const [sortOrder, setSortOrder] = useState("desc"); // asc | desc
  const [searchTerm, setSearchTerm] = useState("");
  const [activeTab, setActiveTab] = useState("filter:all");

  const fetchTasks = useCallback(() => {
  fetch(`http://localhost:8080/tasks?sort=createdAt&order=${sortOrder}`)
    .then((res) => res.json())
    .then(setTasks)
    .catch((err) => console.error("Ошибка при загрузке задач:", err));
}, [sortOrder]);


  useEffect(() => {
    fetchTasks();
  }, [refresh, fetchTasks]);

  const handleToggle = (taskId) => {
    fetch(`http://localhost:8080/tasks/${taskId}/completed?completed=true`, {
      method: "PATCH",
    })
      .then((res) => {
        if (res.ok) {
          setTasks((prev) =>
            prev.map((task) =>
              task.id === taskId ? { ...task, completed: true } : task
            )
          );
        }
      })
      .catch((err) => console.error("Ошибка при обновлении задачи:", err));
  };

  const handleDelete = (taskId) => {
    fetch(`http://localhost:8080/tasks/${taskId}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (res.ok) {
          setTasks((prev) => prev.filter((task) => task.id !== taskId));
        }
      })
      .catch((err) => console.error("Ошибка при удалении задачи:", err));
  };

  const filteredTasks = tasks.filter((task) => {
    const matchesFilter =
      filter === "all" ||
      (filter === "completed" && task.completed) ||
      (filter === "active" && !task.completed);

    const matchesSearch = task.title
      .toLowerCase()
      .includes(searchTerm.toLowerCase());

    return matchesFilter && matchesSearch;
  });

  return (
    <div>
      <h2>Task List</h2>

      <div className="filter-buttons">
        <button
          className={activeTab === "filter:all" ? "active" : ""}
          onClick={() => {
            setFilter("all");
            setActiveTab("filter:all");
          }}
        >
          All
        </button>
        <button
          className={activeTab === "filter:active" ? "active" : ""}
          onClick={() => {
            setFilter("active");
            setActiveTab("filter:active");
          }}
        >
          Active
        </button>
        <button
          className={activeTab === "filter:completed" ? "active" : ""}
          onClick={() => {
            setFilter("completed");
            setActiveTab("filter:completed");
          }}
        >
          Completed
        </button>
        <button
          className={activeTab === "sort:asc" ? "active" : ""}
          onClick={() => {
            setSortOrder("asc");
            setActiveTab("sort:asc");
          }}
        >
          ↑ Oldest first
        </button>
        <button
          className={activeTab === "sort:desc" ? "active" : ""}
          onClick={() => {
            setSortOrder("desc");
            setActiveTab("sort:desc");
          }}
        >
          ↓ Newest first
        </button>
      </div>

      <input
        type="text"
        placeholder="Search by title..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        style={{ marginBottom: "1rem", padding: "4px", width: "100%" }}
      />

      <ul>
        {filteredTasks.map((task) => (
          <TaskItem
            key={task.id}
            task={task}
            onToggle={handleToggle}
            onDelete={handleDelete}
          />
        ))}
      </ul>
    </div>
  );
};

export default TaskList;
