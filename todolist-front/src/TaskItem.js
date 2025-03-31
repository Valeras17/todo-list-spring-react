import React from "react";

const TaskItem = ({ task, onToggle, onDelete }) => {
  const date = new Date(task.createdAt);
  const formattedDate = date.toLocaleString("en-US", {
    year: "numeric",
    month: "short",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });

  return (
    <li>
      <strong>{task.title}</strong> —{" "}
      {task.completed ? (
        <>
          done  ✅ <span className="createdAt">({formattedDate})</span>{" "}
          <button onClick={() => onDelete(task.id)}>❌</button>
        </>
      ) : (
        <>
          not done ❌ <span className="createdAt">({formattedDate})</span>{" "}
          <button onClick={() => onToggle(task.id)}>Mark as done</button>{" "}
          <button onClick={() => onDelete(task.id)}>❌</button>
        </>
      )}
    </li>
  );
};

export default TaskItem;
