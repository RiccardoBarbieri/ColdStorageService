// Definisci la variabile globale
window.chargeTaken = false;
window.reloadTime = 7000;
window.failTime = 15000;

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
        responseText.innerHTML = "The request has been accepted! <br>Please wait for the service to take care of it."
        setTimeout(() => {
            checkChargeTaken();
        }, window.failTime); // timeout di 15 secondi
    }
    else if (response === "rejected") {
        responseText.innerHTML = "The request has been rejected! <br>The page will be restored shortly."
        countdownFail(window.reloadTime);
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
    else {
        responseText.innerHTML = "Error during processing the deposit! <br>The page will be restored shortly."
        countdownFail(window.reloadTime)
        setTimeout(() => {
            location.reload();
        }, reloadTime);
    }
}


function validateInput() {
    const inputElement = document.getElementById('quantity');
    const inputValue = parseFloat(inputElement.value);

    if (isNaN(inputValue) || inputValue <= 0.0) {
        // Display an error message
        showError('Please enter a positive integer value!');
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

function countdownChargeTaken(time) {
    countdown(time - 1000, document.getElementById('countdownChargeTaken'));
}