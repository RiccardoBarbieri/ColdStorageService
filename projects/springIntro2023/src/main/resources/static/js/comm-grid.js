document.addEventListener('DOMContentLoaded', () => {


    const saveButton = document.querySelector("#save");
    const clearButton = document.querySelector("#clear");

    //save functionality
    saveButton.addEventListener('click', () => {
        //Saving animation start
        const button = document.querySelector("#save");
        button.firstElementChild.removeAttribute("hidden");
        button.lastElementChild.textContent = "Saving...";
        button.disabled = true;


        //creating grid string
        let mapCompact = compileGridString();

        //TODO test functionality with input taken from local storage
        // const mapHeight = document.querySelector('#height').value;
        // const mapWidth = document.querySelector('#width').value;
        const mapHeight = localStorage.getItem("mapHeight");
        const mapWidth = localStorage.getItem("mapWidth");

        //data structure to send to server
        const requestBodyMap = {
            name: "test", //TODO aggiungere input per nome mappa
            compact: mapCompact,
            width: mapWidth,
            height: mapHeight
        }
// ____________________________________________________________
        //request to server
        const relativeEndpoint = '/savegrid';
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
                    button.firstElementChild.hidden = "hidden";
                    button.lastElementChild.textContent = "Saved!";
                    setTimeout(() => {
                        button.lastElementChild.textContent = "Save";
                        button.disabled = false;
                    }, 1500);
                }, 1000);
            } else {
                //Saving animation end ERROR
                setTimeout(() => {
                    button.firstElementChild.hidden = "hidden";
                    button.lastElementChild.textContent = "Error!";
                    setTimeout(() => {
                        button.lastElementChild.textContent = "Save";
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

    });
    // _________________________________________________________________________


    //clear functionality
    clearButton.addEventListener('click', () => {
        //clearing grid
        const grid = document.querySelector("#grid");
        const rows = grid.children.length;
        const cols = grid.children[0].children.length;
        const template = document.querySelector("#empty-item-template");
        for (let i = 0; i < rows; i++) {
            for (let j = 0; j < cols; j++) {
                const td = grid.children[i].children[j];
                const gridItemTemplate = template.content.firstElementChild.cloneNode(true);
                td.replaceChildren(gridItemTemplate);
                if (i === 0 || i === rows - 1 || j === 0 || j === cols - 1) {
                    td.querySelector('span').innerText = '1'
                }
                else {
                    td.querySelector('span').innerText = '0'
                }
            }
        }
        //clearing local storage
        localStorage.removeItem("mapCompact");
        localStorage.removeItem("mapHeight");
        localStorage.removeItem("mapWidth");
    });
});

// setTimeout(setTime, 60000, toastElement);
// function setTime(toastElement) {
//     const content = toastElement.querySelector('#errorToastTime').textContent
//     toastElement.querySelector('#errorToastTime').textContent = (parseInt(content) || 0)  + 1 + " min"
//     setTimeout(setTime, 60000, toastElement)
// }

function compileGridString() {

    //scan grid and compile string to store or send to server

    const grid = document.querySelector("#grid");

    const rowLength = grid.children.length;
    const colLength = grid.children[0].children.length;

    let mapCompact = "";

    for (let i = 0; i < rowLength; i++) {
        for (let j = 0; j < colLength; j++) {
            const cell = grid.children[i].children[j];

            const spanValue = cell.querySelector("span").innerHTML;
            mapCompact += spanValue.trim();
        }
    }
    return mapCompact;
}