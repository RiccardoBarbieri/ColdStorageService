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
        if (`${event.data}`.includes("updateColdRoom")) {
            var load = `${event.data}`.substring(`${event.data}`.indexOf("("), `${event.data}`.indexOf(")"))
            const coldRoomStatusText = document.getElementById('coldRoomStatusText');
            coldRoomStatusText.innerHTML = "Current ColdRoom load: " + load;
        }
        else if (`${event.data}`.includes("stomp")) {
            setTimeout(() => {
                location.reload();
            }, 5000);
        }

    };
}//connect
