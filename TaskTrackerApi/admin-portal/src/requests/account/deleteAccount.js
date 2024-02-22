import Constants from "../../Constants";

export default function deleteAccount(accountId, onSuccess) {
    fetch(
        `https://${Constants.domain}/accounts/${accountId}`,
        {
            headers: {
                "Access-Control-Allow-Origin": "https://localhost:8443"
            },
            method: "DELETE",
            credentials: "include"
        }).then(response => {
        onSuccess()
    }).catch(error => {
        console.error(error)
    })
}