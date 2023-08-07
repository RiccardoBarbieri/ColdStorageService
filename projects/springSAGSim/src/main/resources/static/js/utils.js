const saveButton = document.querySelector("#save");

function sendRequest() {
    saveButton.firstElementChild.removeAttribute("hidden");
    saveButton.disabled = true

    const spinner = document.querySelector('.spinner-border');
    spinner.removeAttribute('hidden');

    //data structure to send to server
    const requestBodyMap = {
        fw: document.getElementById("quantity")
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
                showResponse()
            }, 2000);
        } else {
            //Saving animation end ERROR
            setTimeout(() => {
                saveButton.firstElementChild.hidden = "hidden";
                saveButton.lastElementChild.textContent = "Error!";
                setTimeout(() => {
                    button.lastElementChild.textContent = "Send request";
                    button.disabled = false;
                }, 1500);
            }, 1000);
        }
        return response;
    }).then(response => {
        //Toast on error
        if (!response.ok) {
            const toastElement = document.getElementById('errorToast');
            toastElement.querySelector('.toast-body').textContent = response.text();
            if (toastElement) {
                const toast = new bootstrap.Toast(toastElement);
                toast.show();
            }
        }
    }).catch(error => {
        console.log(error);
    });
}
function showResponse() {
    const attesaBody = document.getElementById('attesaBody');
    attesaBody.style.display = "block";
    setTimeout(() => {
        const chargeTaken = document.getElementById('chargeTaken');
        chargeTaken.style.display = "block";
        setTisetTimeout(() => {
            location.reload();
        }, 6000);
    }, 6000);
    // TODO MOCK
}


function validateInput() {
    const inputElement = document.getElementById('quantity');
    const inputValue = parseInt(inputElement.value);

    if (isNaN(inputValue) || inputValue <= 0) {
        // Display an error message
        showError('Please enter a positive integer value greater than 0.');
    }
    sendRequest();
}

function showError(message) {
    const errorToast = document.getElementById('errorToast');
    const toastBody = errorToast.querySelector('.toast-body');
    toastBody.textContent = message;
    errorToast.classList.add('show');
    setTimeout(() => {
        errorToast.classList.remove('show');
    }, 3000);
}


document.addEventListener("DOMContentLoaded", function () {
    const inputElement = document.getElementById('quantity');
    inputElement.value = ''; // Imposta il campo di input a una stringa vuota
});