import React, { useState, useEffect } from "react";
import AddTask from "./AddTask";
import TaskList from "./TaskList";
import "./App.css";

const App = () => {
  const [refreshFlag, setRefreshFlag] = useState(false);
  const [darkMode, setDarkMode] = useState(false); // ⬅️ сюда!

  useEffect(() => {
    document.body.className = darkMode ? "dark" : "light";
  }, [darkMode]);

  const refreshTasks = () => setRefreshFlag((prev) => !prev);

  return (
    <div className="App">
      <div className="theme-toggle">
      <button onClick={() => setDarkMode(!darkMode)} className="theme-toggle-button">
  {darkMode ? "🌞 Light Mode" : "🌙 Dark Mode"}
</button>
      </div>

      <h1>My ToDo List</h1>
      <AddTask onTaskAdded={refreshTasks} />
      <TaskList refresh={refreshFlag} />
    </div>
  );
};

export default App;
