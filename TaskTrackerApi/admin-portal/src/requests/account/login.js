import Constants from "../../Constants";

export default function login(username, password, onSuccess) {
    fetch(`https://${Constants.domain}/auth`,
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
        onSuccess(body)
    }).catch((error) => {
        console.error(error)
    })
}