import {useEffect, useState} from "react";
import Grid2 from "@mui/material/Unstable_Grid2";
import {List, ListItem, ListItemButton, ListItemText} from "@mui/material";

function getAccounts(setAccounts) {
    fetch(
        "https://localhost:8443/accounts",
        {
            headers: {
                "Access-Control-Allow-Origin": "https://localhost:8443"
            },
            method: "GET",
            credentials: "include"
        }
    ).then(async response => {
        let body = await response.json()
        setAccounts(body ? body : [])
    }).catch(error => {
        console.error(error)
    })
}

function getTasks(setTasks) {
    fetch(
        "https://localhost:8443/tasks",
        {
            headers: {
                "Access-Control-Allow-Origin": "https://localhost:8443"
            },
            method: "GET",
            credentials: "include"
        }
    ).then(async response => {
        let body = await response.json()
        setTasks(body ? body : [])
    })
}

export default function Dashboard(props) {
    let [accounts, setAccounts] = useState([])
    let [tasks, setTasks] = useState([])

    useEffect(() => {
        getAccounts(setAccounts)
        getTasks(setTasks)
    });

    return (
        <Grid2 container spacing={1} style={{paddingTop: 10}}>
            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <span>Number of accounts {accounts.length}</span>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <span>Number of tasks {tasks.length}</span>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <List>
                    <span>Accounts:</span>
                    {accounts.map(account =>
                        (<ListItem>
                            <ListItemButton component="button" onClick={(e) => {
                                e.preventDefault()
                                console.log("hello")
                            }}>
                                <ListItemText primary={account.name}/>
                            </ListItemButton>
                        </ListItem>)
                    )}
                </List>
            </Grid2>
        </Grid2>
    )
}