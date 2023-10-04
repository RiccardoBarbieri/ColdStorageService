// Definisci la variabile globale
window.chargeTaken = false;
window.reloadTime = 7000;
window.depositRequestIdentifier = "";
window.ticketNumber = -1;

function sendRequest(quantityFw) {
    const saveButton = document.querySelector("#save");

    saveButton.firstElementChild.removeAttribute("hidden");
    saveButton.disabled = true

    const spinner = document.querySelector('.spinner-border');
    spinner.removeAttribute('hidden');

    //data structure to send to server
    const requestBodyMap = {
        fw: quantityFw
    }

    //request to server
    const relativeEndpoint = '/sendStorageRequest';
    fetch(
        relativeEndpoint,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBodyMap)
        }
    ).then(response => {
        if (response.ok) {
            //Saving animation end OK
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = 'Request sent';
                saveButton.disabled = true;
            }, 1700);
        } else {
            //Saving animation end ERROR
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = "Error!";
            }, 1700);
        }
        return response;
    }).then(response => {
        response.text().then((resolvedValue) => {

            if (!response.ok) {
                showError(response.text());
            }
            else {
                setTimeout(() => {
                    if (resolvedValue.includes("loadaccepted")) {
                        const stringaccepted = resolvedValue.split("loadaccepted(")[1].split(")")[0];
                        depositRequestIdentifier = stringaccepted.split(",")[0];
                        ticketNumber = stringaccepted.split(",")[1];
                        showResponse("accepted")
                    }
                    else if (resolvedValue.includes("loadrejected")) {
                        showResponse("rejected")
                    }
                    else {
                        showResponse("KO")
                    }
                }, 2000);
            }
        });
        
    }).catch(error => {
        console.log(error);
    });
}

function showResponse(response) {
    const responseBody = document.getElementById('responseBody');
    responseBody.style.display = "block";
    const responseText = document.getElementById('responseText');
    if (response === "accepted") {
        const ticketButton = document.getElementById('ticketButton');
        ticketButton.style.display = "block";
        const ticketNumberField = document.getElementById('ticketNumberField');
        ticketNumberField.value = ticketNumber
        responseText.innerHTML = "The request has been accepted! <br>Your ticket code is " + ticketNumber
    }
    else if (response === "rejected") {
        responseText.innerHTML = "The request has been rejected! <br>The page will be restored shortly."
        countdownFail(reloadTime);
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
    else {
        responseText.innerHTML = "Error during processing the deposit! <br>The page will be restored shortly."
        countdownFail(reloadTime)
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
}


function validateInput() {
    const inputElement = document.getElementById('quantity');
    const inputValue = parseInt(inputElement.value);

    if (isNaN(inputValue) || inputValue < 0) {
        // Display an error message
        showError('Please enter a positive integer value greater than 0.');
    }
    else {
        sendRequest(inputValue);
    }
}

function showError(message) {
    const errorToast = document.getElementById('errorToast');
    const toastBody = errorToast.querySelector('.toast-body');
    toastBody.textContent = message;
    errorToast.classList.add('show');
    setTimeout(() => {
        errorToast.classList.remove('show');
    }, 2000);
}


document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.getElementById('quantity');
    inputElement.value = ''; // Imposta il campo di input a una stringa vuota
});

function checkChargeTaken() {
    if (!window.chargeTaken) {
        const timeout = document.getElementById('timeout');
        timeout.style.display = "block";
        countdownChargeTaken(reloadTime)
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
}


function countdown(time, element) {
    const countdownDate = new Date().getTime() + time - 1000;

    // Aggiorna il conto alla rovescia ogni secondo
    const countdownInterval = setInterval(() => {
        const now = new Date().getTime();
        const timeLeft = countdownDate - now;

        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
            element.innerHTML = "Updating the page ...";
        } else {
            const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
            element.innerHTML = `${seconds} seconds left ...`;
        }
    }, 1000);
}

function countdownFail(time) {
    countdown(time - 1000, document.getElementById('countdown'));
}

function countdownTicketFail(time) {
    countdown(time - 1000, document.getElementById('countdownTicket'));
}

function countdownChargeTaken(time) {
    countdown(time - 1000, document.getElementById('countdownChargeTaken'));
}

function showTicketField() {
     const ticketField = document.getElementById('ticketField');
     ticketField.style.display = "block";
     const ticketButton = document.getElementById('ticketButton');
     ticketButton.style.display = "none";
}


function enterTicketRequest() {

    const saveButton = document.querySelector("#sendTicketNumber");

    saveButton.firstElementChild.removeAttribute("hidden");
    saveButton.disabled = true

    const spinner = document.querySelector('#spinner-border-ticket');
    spinner.removeAttribute('hidden');
        const inputElement = document.getElementById('ticketNumberField');
        const inputValue = inputElement.value;

        //data structure to send to server
        const requestBodyMap = {
            ticketCode: inputValue
        }

        //request to server
        const relativeEndpoint = '/enterTicketRequest';
        fetch(
            relativeEndpoint,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBodyMap)
            }
        ).then(response => {
            if (response.ok) {
                //Saving animation end OK
                setTimeout(() => {
                    const saveStatus = document.getElementById('save-status-ticket');
                    saveStatus.removeAttribute('hidden');
                    spinner.setAttribute('hidden', 'hidden');
                    saveButton.firstElementChild.hidden = "hidden";
                    saveButton.lastElementChild.textContent = 'Ticket entered';
                    saveButton.disabled = true;
                }, 1700);
            } else {
                //Saving animation end ERROR
                setTimeout(() => {
                    const saveStatus = document.getElementById('save-status-ticket');
                    saveStatus.removeAttribute('hidden');
                    spinner.setAttribute('hidden', 'hidden');
                    saveButton.firstElementChild.hidden = "hidden";
                    saveButton.lastElementChild.textContent = "Error!";
                }, 1700);
            }
            return response;
        }).then(response => {
            response.text().then((resolvedValue) => {

                if (!response.ok) {
                    showError(response.text());
                }
                else {
                    setTimeout(() => {
                        if (resolvedValue.includes("ticketaccepted")) {
                            showResponseTicket("accepted")
                        }
                        else if (resolvedValue.includes("ticketrejected")) {
                            showResponseTicket("rejected")
                        }
                        else {
                            showResponseTicket("KO")
                        }
                    }, 2000);
                }
            });

        }).catch(error => {
            console.log(error);
        });
}


function showResponseTicket(response) {
    const responseBody = document.getElementById('responseTicketBody');
    responseBody.style.display = "block";
    const responseText = document.getElementById('responseTicketText');
    if (response === "accepted") {
        const ticketButton = document.getElementById('ticketButton');
        ticketButton.style.display = "block";
        responseText.innerHTML = "The request has been accepted! <br>Your ticket code is " + ticketNumber
        setTimeout(() => {
            checkChargeTaken();
        }, 15000); // timeout di 15 secondi
    }
    else if (response === "rejected") {
        responseText.innerHTML = "The ticket has been rejected! <br>The page will be restored shortly."
        countdownTicketFail(reloadTime);
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
    else {
        responseText.innerHTML = "Error during entering the ticket! <br>The page will be restored shortly."
        countdownTicketFail(reloadTime)
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
}