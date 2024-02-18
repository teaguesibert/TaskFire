import React, {useState} from 'react';
import {Button, TextField} from "@mui/material";
import Grid2 from "@mui/material/Unstable_Grid2";

function login(username, password, setAuthed, setAccountId) {
    fetch("https://localhost:8443/auth",
        {
            headers: {
                "Access-Control-Allow-Origin": "https://localhost:8443",
                "Content-Type": "application/json"
            },
            method: "POST",
            credentials: "include",
            body: JSON.stringify({
                name: username,
                password: password
            })
        }
    ).then(async response => {
        let body = await response.json()
        setAccountId(body.id)
        setAuthed(true)
    }).catch((error) => {
        console.error(error)
    })
}

export default function Login(props) {
    let [username, setUsername] = useState("")
    let [password, setPassword] = useState("")
    let setAuthed = props.setAuthed
    let setAccountId = props.setAccountId

    return (
        <Grid2 container spacing={1} style={{paddingTop: 10}}>
            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <TextField id="outlined-basic" label="Username" value={username}
                           onChange={(e) => setUsername(e.target.value)}/>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <TextField id="outlined-basic" label="Password" value={password}
                           onChange={(e) => setPassword(e.target.value)}/>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <Button onClick={() => login(username, password, setAuthed, setAccountId)}
                        variant="contained">Login</Button>
            </Grid2>
        </Grid2>
    )
}