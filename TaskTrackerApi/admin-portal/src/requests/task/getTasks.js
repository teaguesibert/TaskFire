export default function getTasks(setTasks) {
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
    }).catch(error => {
        console.error(error)
    })
}