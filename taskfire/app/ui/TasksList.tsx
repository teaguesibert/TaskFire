import React from 'react';

interface Task {
    title: string;
    description: string;
    created: number;
    modified: number;
    taskId: string;
}


interface TaskListProps {
    tasks: Task[];
}

const TasksList: React.FC<TaskListProps> = ({ tasks }) => {
    return (
        <div>
            {tasks.map((task) => (
                <div key={task.taskId} className="text-white">
                    <h3>{task.title}</h3>
                    <p>{task.description}</p>
                    {/* Render other note properties */}
                </div>
            ))}
        </div>
    );
};

export default TasksList;
