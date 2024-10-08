import { compileGridString } from "./utils.js";

document.addEventListener('DOMContentLoaded', () => {


    const saveButton = document.querySelector("#save");
    const clearButton = document.querySelector("#clear");

    //save functionality
    saveButton.addEventListener('click', () => {
        //Saving animation start

        const grid = document.querySelector('#grid');

        //creating grid string
        let mapCompact = compileGridString(grid);



        let mapHeight = localStorage.getItem("mapHeight");
        let mapWidth = localStorage.getItem("mapWidth");
        console.log(mapHeight, mapWidth)
        if (!mapWidth || !mapHeight) {
            console.log("test")
            mapHeight = document.querySelector("#height").value
            mapWidth = document.querySelector("#width").value
            localStorage.setItem("mapHeight", mapHeight)
            localStorage.setItem("mapWidth", mapWidth)
        }

        const mapName = document.querySelector("#mapname").value;

        if (mapName === "") {
            const toastElement = document.getElementById('errorToast');
            toastElement.querySelector('.toast-body').textContent = "Map name cannot be empty";
            if (toastElement) {
                const toast = new bootstrap.Toast(toastElement);
                toast.show();
            }
            return;
        }
        const button = document.querySelector("#save");
        button.firstElementChild.removeAttribute("hidden");
        button.lastElementChild.textContent = "Saving...";
        button.disabled = true;

        //data structure to send to server
        const requestBodyMap = {
            name: mapName,
            compact: mapCompact,
            width: mapWidth,
            height: mapHeight
        }

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
        // defaulting local storage
        // because values are somehow set to 8 at reload after clear
        localStorage.setItem("mapHeight", 5);
        localStorage.setItem("mapWidth", 7);

        let mapCompact = "";
        for (let i = 0; i < rows; i++) {
            for (let j = 0; j < cols; j++)
                // if at border set to 1
                if (i === 0 || i === rows - 1 || j === 0 || j === cols - 1) {
                    mapCompact += "1"
                }
                else {
                    mapCompact += "0"
                }
        }
        localStorage.setItem("mapCompact", mapCompact);
        location.reload();
    });
});

// setTimeout(setTime, 60000, toastElement);
// function setTime(toastElement) {
//     const content = toastElement.querySelector('#errorToastTime').textContent
//     toastElement.querySelector('#errorToastTime').textContent = (parseInt(content) || 0)  + 1 + " min"
//     setTimeout(setTime, 60000, toastElement)
// }

