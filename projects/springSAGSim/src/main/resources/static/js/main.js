// Definisci la variabile globale
window.chargeTaken = false;

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
            }, 1200);
        } else {
            //Saving animation end ERROR
            setTimeout(() => {
                const saveStatus = document.getElementById('save-status');
                saveStatus.removeAttribute('hidden');
                spinner.setAttribute('hidden', 'hidden');
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = "Error!";
            }, 1000);
        }
        return response;
    }).then(response => {
        response.text().then((resolvedValue) => {

            if (!response.ok) {
                showError(response.text());
            }
            else {
                if (resolvedValue.includes("loadaccepted")) {
                    showResponse("accepted")
                }
                else if (resolvedValue.includes("loadrejected")) {
                    showResponse("rejected")
                }
                else {
                    showResponse("KO")
                }
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
    if (response == "accepted") {
        responseText.innerHtml = "The request has been accepted! <br>Please wait for the service to take care of it."
        setTimeout(() => {
            checkChargeTaken();
        }, 15000); // timeout di 5 secondi
    }
    else if (response == "rejected") {
        responseText.innerHtml = "The request has been rejected! <br>The page will be restored shortly."
        setTimeout(() => {
            location.reload();
        }, 6000);
    }
    else {
        responseText.innerHtml = "Error during processing the deposit! <br>The page will be restored shortly."
        setTimeout(() => {
            location.reload();
        }, 6000);
    }
}


function validateInput() {
    const inputElement = document.getElementById('quantity');
    const inputValue = parseInt(inputElement.value);

    if (isNaN(inputValue) || inputValue <= 0) {
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
        setTimeout(() => {
            location.reload();
        }, 6000);
    }
}