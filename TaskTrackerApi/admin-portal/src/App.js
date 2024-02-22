import React, {useState} from 'react';
import Login from "./Login";
import {createTheme, CssBaseline, ThemeProvider, useMediaQuery} from "@mui/material";
import Dashboard from "./Dashboard";

function App() {
    const prefersDarkMode = useMediaQuery('(prefers-color-scheme: dark)');
    const theme = React.useMemo(
        () => createTheme({
            palette: {
                mode: prefersDarkMode ? 'dark' : 'light'
            },
        }),
        [prefersDarkMode]
    )

    let [authed, setAuthed] = useState(false)
    let [accountId, setAccountId] = useState("")

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <div>
                {
                    !authed ? (<Login setAuthed={setAuthed} setAccountId={setAccountId}/>) : (
                        <Dashboard/>
                    )
                }
            </div>
        </ThemeProvider>
    )
}

export default App;
