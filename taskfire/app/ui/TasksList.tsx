import React from 'react';

interface Task {
    title: string;
    description: string;
    created: number;
    modified?: number;
    taskId: string;
    completed: boolean;
}

interface TaskListProps {
  tasks: Task[];
  onToggleCompleted: (taskId: string, completed: boolean) => void;
  onDeleteTask: (taskId: string) => void;
}

const formatDate = (createdEpochMillis: number) => {
    const date = new Date(createdEpochMillis);
    const today = new Date();
    const isToday =
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear();
  
    if (isToday) {
      return `Today, ${date.toLocaleString('en-US', { hour: '2-digit', minute: '2-digit' })}`;
    } else {
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    }
  };


  const TasksList: React.FC<TaskListProps> = ({ tasks, onToggleCompleted, onDeleteTask }) => {
    return (
        <>
            {tasks.map((task) => (
                <div key={task.taskId} className="bg-gray-800 p-2 rounded mb-2 ">
                    <div className='flex justify-between'>
                        <div className="flex items-center">
                            <input
                                type="checkbox"
                                checked={task.completed}
                                onChange={(e) => onToggleCompleted(task.taskId, e.target.checked)}
                                className="form-checkbox h-5 w-5 text-green-600 mr-2"
                            />
                            <h3 className={`text-lg font-bold ${task.completed ? 'line-through' : ''}`}>
                                {task.title}
                            </h3>
                        </div>
                        <span className="text-sm text-gray-400">
                            {formatDate(task.created)}
                        </span>
                    </div>
                    
                    <p>{task.description}</p>
                    <button
                        onClick={() => onDeleteTask(task.taskId)}
                        className="text-red-500 hover:text-red-700"
                    >
                        Delete
                    </button>
                </div>
            ))}
        </>
    );
};

export default TasksList;
