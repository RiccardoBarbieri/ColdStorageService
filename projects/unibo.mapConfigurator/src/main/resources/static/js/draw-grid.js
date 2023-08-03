import {compileBlockMap, restoreBlocks, newRow, compileGridString} from "./utils.js";

document.addEventListener('DOMContentLoaded', () => {

    const heightInput = document.querySelector('#height');
    const widthInput = document.querySelector('#width');


    const template = document.querySelector('#empty-item-template');

    //compiling block map
    const blocks = document.querySelector('#blocks').children;
    const blockMap = compileBlockMap(blocks);

    //restore from local storage, if exists
    const mapCompact = localStorage.getItem('mapCompact');
    const mapHeight = localStorage.getItem('mapHeight');
    const mapWidth = localStorage.getItem('mapWidth');


    if (mapCompact && mapHeight && mapWidth) {
        // draw grid first time
        redrawGrid(0, mapWidth, mapHeight, mapWidth, template);

        const grid = document.querySelector('#grid');
        restoreBlocks(grid, blockMap);
    }
    else {
        // draw grid first time
        redrawGrid(0, widthInput.value, heightInput.value, widthInput.value, template);
    }


    // sets listeners for change on height and width
    let oldHeight = heightInput.value;
    let oldWidth = widthInput.value;
    heightInput.addEventListener('input', (event) => {
        const newHeight = event.target.value;

        const grid = document.querySelector('#grid');

        redrawGrid(oldHeight, oldWidth, newHeight, oldWidth, template);

        localStorage.setItem("mapWidth", oldWidth)
        localStorage.setItem("mapHeight", newHeight)
        localStorage.setItem("mapCompact", compileGridString(grid))

        // restoreBlocks(grid, blockMap);

        oldHeight = newHeight;
    });

    widthInput.addEventListener('input', (event) => {
        const newWidth = event.target.value;

        const grid = document.querySelector('#grid');

        redrawGrid(oldHeight, oldWidth, oldHeight, newWidth, template);

        localStorage.setItem("mapWidth", newWidth)
        localStorage.setItem("mapHeight", oldHeight)
        localStorage.setItem("mapCompact", compileGridString(grid))

        // restoreBlocks(grid, blockMap);

        oldWidth = newWidth;
    });

});

// redraws grid adding or removing rows or columns
// to maintain changes at grid items when changing dimensions
function redrawGrid(prevHeight, prevWidth, newHeight, newWidth, template) {
    const grid = document.querySelector('#grid');

    const newCols = newWidth - prevWidth;
    const newRows = newHeight - prevHeight;

    if (newRows > 0) {
        for (let i = 0; i < newRows; i++) {
            const row = document.createElement('tr');
            row.classList.add('grid');

            for (let j = 0; j < newWidth; j++) {
                newRow(row, template);
            }

            grid.appendChild(row);
        }
    } else if (newRows < 0) {
        for (let i = 0; i < Math.abs(newRows); i++) {
            grid.removeChild(grid.lastChild);
        }
    }

    if (newCols > 0) {
        for (let i = 0; i < newHeight; i++) {
            const row = grid.children[i];

            for (let j = 0; j < newCols; j++) {
                newRow(row, template);
            }
        }
    } else if (newCols < 0) {
        for (let i = 0; i < newHeight; i++) {
            const row = grid.children[i];
            for (let j = 0; j < Math.abs(newCols); j++) {
                row.removeChild(row.lastChild);
            }
        }
    }

    // set border to 1
    for (let i = 0; i < newHeight; i++) {
        for (let j = 0; j < newWidth; j++) {
            const row = grid.children[i];
            const td = row.children[j];
            const span = td.querySelector('span')
            if (span.innerText !== '1' && span.innerText !== '0') {
                continue
            }
            if (i === 0 || i === newHeight - 1 || j === 0 || j === newWidth - 1) {
                td.querySelector('span').innerText = '1'
            } else {
                td.querySelector('span').innerText = '0'
            }
        }
    }

    clearAndAttachListeners();

}

function clearAndAttachListeners() {

    const sources = document.querySelectorAll(".draggable");

    sources.forEach(source => {
        if (source.eventListeners) {
            source.eventListeners.clear();
        }
    });

    sources.forEach(source => {
        source.addEventListener(
            "dragstart",
            (event) => {
                // prevent default to allow drop

                event.dataTransfer.setData("text/html", event.target.outerHTML);
                event.target.classList.add("dragging");
            }
        );
    });

    sources.forEach(source => {
        source.addEventListener(
            "dragend",
            (event) => {
                // prevent default to allow drop
                event.preventDefault();

                console.log(event.target.parentNode)
                if (event.target.classList.contains("dragging")) {
                    event.target.classList.remove("dragging");
                }
            }
        )
    });


    const targets = document.querySelectorAll(".droptarget");
    targets.forEach(target => {
        if (target.eventListeners) {
            target.eventListeners.clear();
        }
    });

    targets.forEach(target => {
        target.addEventListener(
            "dragover",
            (event) => {
                // prevent default to allow drop
                event.preventDefault();
            }
        )
    });

    targets.forEach(target => {
        target.addEventListener(
            "drop",
            (event) => {
                // prevent default to allow drop
                event.preventDefault();

                if (event.target.classList.contains("highlight")) {
                    event.target.classList.remove("highlight");
                }

                const divString = event.dataTransfer.getData("text/html");
                let div = new DOMParser().parseFromString(divString, "text/html").body.firstElementChild;

                if (div) {
                    div.addEventListener(
                        "dragstart",
                        (event) => {

                            event.dataTransfer.setData("text/html", event.target.outerHTML);
                            event.target.classList.add("dragging");
                        }
                    );
                    div.addEventListener(
                        "dragend",
                        (event) => {
                            // prevent default to allow drop
                            event.preventDefault();

                            const parent = event.target.parentNode;
                            parent.innerHTML = "";
                            const templateElement = document.querySelector("#empty-item-template").content.firstElementChild.cloneNode(true);
                            parent.appendChild(templateElement);

                            const row = parent.closest("tr").rowIndex;
                            const col = parent.cellIndex;
                            const maxRow = document.querySelector("#grid").rows.length;
                            const maxCol = document.querySelector("#grid").rows[0].cells.length;
                            if (row === 0 || row === maxRow - 1 || col === 0 || col === maxCol - 1) {
                                parent.querySelector('span').innerText = '1'
                            } else {
                                parent.querySelector('span').innerText = '0'
                            }

                            if (event.target.classList.contains("dragging")) {
                                event.target.classList.remove("dragging");
                            }

                            const grid = document.querySelector("#grid");

                            let mapCompact = compileGridString(grid);
                            console.log("final:" + mapCompact);

                            localStorage.setItem("mapCompact", mapCompact);
                            localStorage.setItem("mapHeight", document.querySelector('#height').value);
                            localStorage.setItem("mapWidth", document.querySelector('#width').value);
                        }
                    )

                    event.currentTarget.innerHTML = "";
                    event.currentTarget.appendChild(div);
                }
            }
        )
    });

    //assigning drop target event listener to compile the string at each drop
    targets.forEach(target => {
        target.addEventListener('drop', (event) => {

            const grid = document.querySelector("#grid");

            let mapCompact = compileGridString(grid);

            localStorage.setItem("mapCompact", mapCompact);
            localStorage.setItem("mapHeight", document.querySelector('#height').value);
            localStorage.setItem("mapWidth", document.querySelector('#width').value);
        });
    });
}