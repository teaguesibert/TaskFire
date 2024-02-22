import {useEffect, useState} from "react";
import Grid2 from "@mui/material/Unstable_Grid2";
import {Button, List, ListItem, ListItemButton, ListItemText} from "@mui/material";
import getAccounts from "./requests/account/getAccounts"
import getTasks from "./requests/task/getTasks"
import updateAccount from "./requests/account/updateAccount";
import deleteAccount from "./requests/account/deleteAccount";

function makeTempPassword() {
    let result = ""
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    for (let i = 0; i < 10; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length))
    }

    return result
}

export default function Dashboard() {
    let [selectedAccount, setSelectedAccount] = useState(null)

    return (
        <div>
            {selectedAccount ? (
                <AccountDetails selectedAccount={selectedAccount} setSelectedAccount={setSelectedAccount}/>
            ) : (
                <Overview setSelectedAccount={setSelectedAccount}/>
            )
            }
        </div>
    )
}

function Overview(props) {
    let setSelectedAccount = props.setSelectedAccount
    let [accounts, setAccounts] = useState([])
    let [tasks, setTasks] = useState([])
    let [showAccounts, setShowAccounts] = useState(false)

    useEffect(() => {
        getAccounts(body => {
            setAccounts(body ? body.filter(account => account.name !== "admin") : [])
        })
        getTasks(setTasks)
    }, []);

    return (
        <Grid2 container spacing={1} style={{paddingTop: 10}}>
            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <h1>Overview</h1>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <span>Number of accounts: {accounts.length}</span>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <span>Number of tasks: {tasks.length}</span>
            </Grid2>

            {accounts.length > 0 ? (
                <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                    <Button variant="contained" onClick={() => {
                        setShowAccounts(!showAccounts)
                    }}>{showAccounts ? "Hide" : "Show"} accounts</Button>
                </Grid2>
            ) : null}

            {showAccounts ?
                <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                    <List>
                        <span>Accounts:</span>
                        {accounts.map(account =>
                            (<ListItem key={account.id}>
                                <ListItemButton component="button" onClick={(e) => {
                                    e.preventDefault()
                                    setSelectedAccount(account)
                                }}>
                                    <ListItemText primary={account.name}/>
                                </ListItemButton>
                            </ListItem>)
                        )}
                    </List>
                </Grid2>
                : null}
        </Grid2>
    )
}

function AccountDetails(props) {
    let selectedAccount = props.selectedAccount
    let setSelectedAccount = props.setSelectedAccount
    let [newTemporaryPassword, setNewTemporaryPassword] = useState("")

    return (
        <Grid2 container spacing={1} style={{paddingTop: 10}}>
            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <div style={{paddingRight: 10}}>
                    <Button variant="contained" onClick={() => {
                        setSelectedAccount(null)
                    }}>Back</Button>
                </div>

                <h1>Account details for {selectedAccount.name} </h1>

            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <span>Id: {selectedAccount.id}</span>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <span>Created on: {(new Date(selectedAccount.created)).toDateString()}</span>
            </Grid2>

            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <h2>Actions:</h2>
            </Grid2>

            {/*New password*/}
            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <Button variant="contained" onClick={() => {
                    let newTemporaryPassword = makeTempPassword()
                    setNewTemporaryPassword(newTemporaryPassword)
                    updateAccount(selectedAccount.id, {
                        name: selectedAccount.name,
                        password: newTemporaryPassword
                    }, () => {
                        setNewTemporaryPassword(newTemporaryPassword)
                    })
                }}>Reset password</Button>
            </Grid2>

            {newTemporaryPassword ? (
                <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                    <span>Success! Temporary password: {newTemporaryPassword}</span>
                </Grid2>
            ) : null}

            {/*// delete account*/}
            <Grid2 display="flex" justifyContent="center" alignItems="center" xs={12}>
                <Button variant="contained" onClick={() => {
                    deleteAccount(selectedAccount.id, () => {
                        setSelectedAccount(null)
                    })
                }}>Delete</Button>
            </Grid2>
        </Grid2>
    )
}