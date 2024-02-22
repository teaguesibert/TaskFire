import Constants from "../../Constants";

export default function updateAccount(accountId, account, onSuccess) {
    fetch(
        `https://${Constants.domain}/accounts/${accountId}`,
        {
            headers: {
                "Access-Control-Allow-Origin": "https://localhost:8443",
                "Content-Type": "application/json"
            },
            method: "POST",
            credentials: "include",
            body: JSON.stringify(account)
        }).then(response => {
            onSuccess()
    }).catch(error => {
        console.error(error)
    })
}