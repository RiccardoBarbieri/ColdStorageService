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
        var msg = `${event.data}`
        if (msg.includes("actualcurrent")) {
            var actual = msg.split("(")[1].split(")")[0];
            const actualElement = document.getElementById('actual');
            actualElement.innerHTML = actual + " KG";
        }
        else if (msg.includes("tempcurrent")) {
            var temp = msg.split("(")[1].split(")")[0];
            const tempElement = document.getElementById('temp');
            tempElement.innerHTML = temp + " KG";
        }
        else if (msg.includes("reqrejected")) {
            var num = msg.split("(")[1].split(")")[0];
            const tempElement = document.getElementById('requestsRejected');
            tempElement.innerHTML = num;
        }
        else if (msg.includes("ttstate")) {
            var both = msg.split("(")[1].split(")")[0];
            var stateTT = both.split(",")[0];
            const tempElement = document.getElementById('stateTT');
            tempElement.innerHTML = stateTT.toUpperCase();

            var posTT = both.split(",")[1];
            const tempElement2 = document.getElementById('posTT');
            if (stateTT === "stopped") {
                tempElement2.innerHTML = "AT " + posTT.toUpperCase();
            }
            else {
                tempElement2.innerHTML = "TO " + posTT.toUpperCase();
            }
        }
        else if (`${event.data}`.includes("stomp")) {
            setTimeout(() => {
                location.reload();
            }, 5000);
        }

    };
}//connect
