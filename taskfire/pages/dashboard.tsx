import React, {useEffect, useState} from 'react';
import TasksList from '@/app/ui/TasksList';
import "../app/globals.css"
import Image from 'next/image';
import Clock from '@/app/ui/Clock';

const Dashboard:React.FC = () => {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [newTaskTitle, setNewTaskTitle] = useState<string>('');
    const [newTaskDescription, setNewTaskDescription] = useState<string>('');

    interface Task {
        title: string;
        description: string;
        created: number;
        modified?: number;  // Optional if not always present
        taskId: string;
        completed: boolean;
    }
    
    useEffect(() => {
    const storedUid = localStorage.getItem('accountUid');
        

        if(storedUid){
            //console.log(`${storedUid}`)
            fetchTasks(storedUid)
        }
        
    }, []);

    const fetchTasks = async (uid: string) => {
        try {
            const response = await fetch(`https://taskfireapi.jamesellerbee.com/tasks/${uid}`, {
                method: "GET",
                credentials: 'include',
                headers: {
                    "Access-Control-Allow-Origin": "https://taskfireapi.jamesellerbee.com",
                }
            });
            if(response.ok) {
                const data = await response.json();
                //console.log(tasks)
                setTasks(data);
            } else{

            }
            } catch(error) {
                console.log('Error fetching tasks:', error)
            }
        }
        
        const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setNewTaskTitle(e.target.value);
        };
    
        const handleDescriptionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
            setNewTaskDescription(e.target.value);
        };

        const handleToggleCompleted = async (taskId: string, completed: boolean) => {
            
            const taskToToggle = tasks.find(task => task.taskId === taskId);
            if (taskToToggle) {
                const updatedTask = { ...taskToToggle, completed };
                const storedUid = localStorage.getItem('accountUid') as string;
                try {
                    const response = await fetch(`https://taskfireapi.jamesellerbee.com/tasks/${storedUid}`, {
                        method: 'POST',
                        credentials: 'include',
                        headers: {
                            'Content-Type': 'application/json',
                            "Access-Control-Allow-Origin": "https://taskfireapi.jamesellerbee.com",
                        },
                        body: JSON.stringify(updatedTask)
                    });
        
                    if (response.ok) {
                        fetchTasks(storedUid);
                    } else {
                        // Handle errors
                    }
                } catch (error) {
                    console.log('Error toggling task:', error);
                }
            }
        };

        const handleDeleteTask = async (taskId: string) => {
            const storedUid = localStorage.getItem('accountUid');
            
            if (storedUid) {
                try {
                    const response = await fetch(`https://taskfireapi.jamesellerbee.com/tasks/${storedUid}/${taskId}`, {
                        method: 'DELETE',
                        credentials: 'include',
                        headers: {
                            "Access-Control-Allow-Origin": "https://taskfireapi.jamesellerbee.com",
                        }
                    });
        
                    if (!response.ok) {
                        // Handle errors
                        console.log('Error deleting task:', response.status);
                    }
                } catch (error) {
                    console.log('Error deleting task:', error);
                }
            }
        };
        
        const handleTaskSubmit = async (e: React.FormEvent<HTMLFormElement>): Promise<void> => {
            e.preventDefault();
            const storedUid = localStorage.getItem('accountUid');
    
            const task = {
                title: newTaskTitle,
                description: newTaskDescription,
                created: new Date().getTime(), 
                accountId: storedUid,
                completed: false,
            };

            
            if (storedUid) {
                try {
                    const response = await fetch(`https://taskfireapi.jamesellerbee.com/tasks/${storedUid}`, {
                        method: 'POST',
                        credentials: 'include',
                        headers: {
                            'Content-Type': 'application/json',
                            "Access-Control-Allow-Origin": "https://taskfireapi.jamesellerbee.com",
                        },
                        body: JSON.stringify(task)  
                    });
    
                    if (response.ok) {
                        setNewTaskTitle('');
                        setNewTaskDescription('');
                        fetchTasks(storedUid);
                    } else {
                        // Handle errors
                    }
                } catch (error) {
                    console.log('Error posting note:', error);
                }
            }
        };


        return (
            <div className="bg-gray-900 text-gray-100 h-screen  pt-4 px-4 flex flex-col justify-between">
                <div className="flex flex-row justify-between mb-3">
                    <Image
                    src="/NotesApp.svg"
                    width={30}
                    height={30}
                    alt="TaskFire Logo"
                    
                    />
                    <h1 className="text-2xl">Tasks</h1>
                    <Clock/>
                </div>
                <div className='overflow-y-auto mb-2'>
                    {tasks.length > 0 ? (
                    <TasksList 
                    tasks={tasks}
                    onToggleCompleted={handleToggleCompleted}
                    onDeleteTask={handleDeleteTask}
                    />
                    ) : (
                    <p>No tasks available.</p>
                    )}
                </div>
            <form onSubmit={handleTaskSubmit} className="mb-2">
                <div className='flex flex-row my-2'>
                    <input
                    type="text"
                    value={newTaskTitle}
                    onChange={handleTitleChange}
                    className="p-2 w-11/12 bg-gray-800 border border-gray-600 rounded focus:border-amber-700 focus:outline-none"
                    placeholder="Task title"
                    />
                    <button
                    type="submit"
                    className=" ml-4 bg-amber-700 hover:bg-amber-800 text-white font-semibold py-1 px-1 rounded"
                    >
                    <svg xmlns="http://www.w3.org/2000/svg" height="30" viewBox="0 -960 960 960" width="30" fill="rgb(255 255 255 / var(--tw-text-opacity)"><path d="M440-280h80v-160h160v-80H520v-160h-80v160H280v80h160v160Zm40 200q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Zm0-80q134 0 227-93t93-227q0-134-93-227t-227-93q-134 0-227 93t-93 227q0 134 93 227t227 93Zm0-320Z"/></svg>
                    </button>
                </div>
                    <textarea
                    value={newTaskDescription}
                    onChange={handleDescriptionChange}
                    className="w-full p-2 mt-2 bg-gray-800 border border-gray-600 rounded focus:border-amber-700 focus:outline-none"
                    placeholder="Task description (optional)"
                    />
                    
            </form>
      
    </div>
  );
};
       

export default Dashboard;