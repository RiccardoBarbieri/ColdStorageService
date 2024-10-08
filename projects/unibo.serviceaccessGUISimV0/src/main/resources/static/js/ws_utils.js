var socket;

function connect() {
    var host = document.location.host;
    var pathname = "/"
    var addr = "ws://" + host + pathname + "socket";

    // Assicura che sia aperta un unica connessione
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);

    socket.onopen = function (event) {
        console.log("ws-onopen event.data:" + event.data);
    };

    socket.onmessage = function (event) {
        console.log("ws-onmessage:" + `${event.data}`);
        if (`${event.data}`.includes("chargetaken")) {
            window.chargeTaken = true;
            setTimeout(() => {
                const chargeTaken = document.getElementById('chargeTaken');
                chargeTaken.style.display = "block";
                countdownChargeTaken(reloadTime);
                setTimeout(() => {
                    location.reload();
                }, window.reloadTime);
            }, 3000);
        }
        else if (`${event.data}`.includes("stomp")) {
            setTimeout(() => {
                location.reload();
            }, 5000);
        }

    };
}//connect
