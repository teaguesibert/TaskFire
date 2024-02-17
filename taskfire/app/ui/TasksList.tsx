import React from 'react';
import { SwipeableList, SwipeableListItem, SwipeAction, TrailingActions, LeadingActions } from 'react-swipeable-list';
import 'react-swipeable-list/dist/styles.css'; // default styles
import Image from 'next/image';

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
    
    const trailingActions = (taskId: string) => (
        <TrailingActions>
            <SwipeAction
                onClick={() => onDeleteTask(taskId)}
                destructive={true}
            >
                 <Image
                    src="/delete_sweep.svg"
                    width={20}
                    height={20}
                    alt="TaskFire Logo"
                    />
            </SwipeAction>
        </TrailingActions>
    );

    const leadingActions = (taskId: string, completed: boolean) => (
        <LeadingActions>
            <SwipeAction
                onClick={() => onToggleCompleted(taskId, !completed)}
            >
                {completed ?  <Image
                    src="/task_alt.svg"
                    width={20}
                    height={20}
                    alt="TaskFire Logo"
                    className='mb-4 p-3'
                    /> : 
                    <Image
                    src="/radio_button_unchecked.svg"
                    width={20}
                    height={20}
                    alt="TaskFire Logo"
                    className='mb-4 p-3'
                    />}
            </SwipeAction>
        </LeadingActions>
    );

    return (
        <SwipeableList>
            {tasks.map((task) => (
                <SwipeableListItem
                    key={task.taskId}
                    leadingActions={leadingActions(task.taskId, task.completed)}
                    trailingActions={trailingActions(task.taskId)}
                    className="bg-gray-800 p-2 rounded mb-2 "
                >
                    <div key={task.taskId} className="bg-gray-800 p-2 rounded mb-2 ">
                    <div className='flex justify-between'>
                        <div className="flex items-center">
                            <h3 className={`text-lg font-semibold ${task.completed ? 'line-through' : ''}`}>
                                {task.title}
                            </h3>
                        </div>
                        <span className="text-sm text-gray-400">
                            {formatDate(task.created)}
                        </span>
                    </div>
                    
                    <p className={`${task.completed ? 'line-through' : ''}`}>{task.description}</p>
                </div>
                </SwipeableListItem>
            ))}
        </SwipeableList>
    );
};

export default TasksList;

