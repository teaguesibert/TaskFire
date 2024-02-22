import Constants from "../../Constants";

export default function getAccounts(onSuccess) {
    fetch(
        `https://${Constants.domain}/accounts`,
        {
            headers: {
                "Access-Control-Allow-Origin": "https://localhost:8443"
            },
            method: "GET",
            credentials: "include"
        }
    ).then(async response => {
        let body = await response.json()
        onSuccess(body)
    }).catch(error => {
        console.error(error)
    })
}