import React, { useState } from "react";

const AddTask = ({ onTaskAdded }) => {
  const [title, setTitle] = useState("");
  const [error, setError] = useState("");

  const handleAdd = (e) => {
    e.preventDefault();

    if (title.trim().length < 3) {
      setError("Task must be at least 3 characters.");
      return;
    }

    setError(""); // reset if all ok

    fetch("http://localhost:8080/tasks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ title, completed: false }),
    })
      .then((res) => res.json())
      .then((newTask) => {
        onTaskAdded(newTask);
        setTitle("");
      });
  };

  return (
    <form onSubmit={handleAdd}>
      <input
        type="text"
        placeholder="New task"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <button type="submit" className="add-button">Add</button>

      {/* error message */}
      {error && <div className="error-message">{error}</div>}
    </form>
  );
};

export default AddTask;
