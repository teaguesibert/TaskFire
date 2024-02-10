import React, {useEffect, useState} from 'react';
import TasksList from '@/app/ui/TasksList';
import "../app/globals.css"

const Dashboard:React.FC = () => {
    const [tasks, setTasks] = useState([]);
    const [newTaskTitle, setNewTaskTitle] = useState<string>('');
    const [newTaskDescription, setNewTaskDescription] = useState<string>('');
    
    useEffect(() => {
        const storedToken = localStorage.getItem('authToken');
        const storedUid = localStorage.getItem('accountUid');
        

        if(storedToken && storedUid){
           
            //console.log(`${storedToken} // ${storedUid}`)
            
            fetchTasks(storedUid, storedToken)
            
        }
        
    }, []);
   
    interface Task {
        title: string;
        description: string;
        created: number;
        modified: number;
    }
    
    const fetchTasks = async (uid: string, authToken: string) => {
        try {
            const response = await fetch(`http://localhost:8080/tasks/${uid}`, {
                method: "GET",
                headers: {
                    'Authorization':`Bearer ${authToken}`,
                    "Access-Control-Allow-Origin": "*",
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


        const handleTaskSubmit = async (e: React.FormEvent<HTMLFormElement>): Promise<void> => {
            e.preventDefault();
            const storedToken = localStorage.getItem('authToken');
            const storedUid = localStorage.getItem('accountUid');
    
            const task = {
                title: newTaskTitle,
                description: newTaskDescription,
                created: 0, 
                accountId: storedUid,
                completed: false,
            };

            
            if (storedToken && storedUid) {
                try {
                    const response = await fetch(`http://localhost:8080/tasks/${storedUid}`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${storedToken}`,
                        },
                        body: JSON.stringify(task)  
                    });
    
                    if (response.ok) {
                        setNewTaskTitle('');
                        setNewTaskDescription('');
                        fetchTasks(storedUid, storedToken);
                    } else {
                        // Handle errors
                    }
                } catch (error) {
                    console.log('Error posting note:', error);
                }
            }
        };


        return(
            <div className="container mx-auto p-4 text-gray-800">
            <h1 className="text-2xl text-white">Your Tasks</h1>
            <form onSubmit={handleTaskSubmit}>
                <input
                    type="text"
                    value={newTaskTitle}
                    onChange={handleTitleChange}
                    className="w-full p-2 border rounded"
                    placeholder="Note title"
                />
                <textarea
                    value={newTaskDescription}
                    onChange={handleDescriptionChange}
                    className="w-full p-2 border rounded mt-2"
                    placeholder="Note description (optional)"
                />
                <button
                    type="submit"
                    className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                >
                    Add Note
                </button>
            </form>
            <div className="container mx-auto p-4 text-gray-800">
            <h1 className="text-2xl text-white">Your Tasks</h1>
            {tasks.length > 0 ? (
                <TasksList tasks={tasks} />
            ) : (
                <p>No tasks available.</p>
            )}
        </div>
        </div>
        )
    }

export default Dashboard;