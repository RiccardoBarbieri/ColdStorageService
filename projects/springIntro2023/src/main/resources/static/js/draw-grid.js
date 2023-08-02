import { hasKey } from "./utils.js";

document.addEventListener('DOMContentLoaded', () => {

    const heightInput = document.querySelector('#height');
    const widthInput = document.querySelector('#width');

    const template = document.querySelector('#empty-item-template');

    //compiling block map
    const blockMap = new Map();
    const blocks = document.querySelector('#blocks').children;

    for (let i = 0; i < blocks.length; i++) {
        const block = blocks[i];
        const blockId = block.firstElementChild.innerText;
        blockMap.set(blockId, block.cloneNode(true));
    }
    // const templateContent = template.content.firstElementChild;
    // blockMap.set('0', templateContent.cloneNode(true));
    // const modifiedTemplate = templateContent.cloneNode(true);
    // modifiedTemplate.cloneNode(true).firstElementChild.innerText = '1'
    // blockMap.set('1', modifiedTemplate);


    //restore from local storage, if exists
    const mapCompact = localStorage.getItem('mapCompact');
    const mapHeight = localStorage.getItem('mapHeight');
    const mapWidth = localStorage.getItem('mapWidth');

    console.log(mapCompact)

    if (mapCompact && mapHeight && mapWidth) {
        // draw grid first time
        redrawGrid(0, mapWidth, mapHeight, mapWidth, template);

        const grid = document.querySelector('#grid');
        for (let i = 0; i < mapHeight; i++) {
            for (let j = 0; j < mapWidth; j++) {
                if (hasKey(blockMap, mapCompact[i * mapWidth + j])) {
                    grid.children[i].children[j].innerHTML = '';
                    grid.children[i].children[j].appendChild(blockMap.get(mapCompact[i * mapWidth + j]).cloneNode(true));
                }
            }
        }
    }
    else {
        // draw grid first time
        redrawGrid(0, widthInput.value, heightInput.value, widthInput.value, template);
    }

    // sets listeners change of height and width
    let oldHeight = heightInput.value;
    let oldWidth = widthInput.value;
    heightInput.addEventListener('input', (event) => {
        const newHeight = event.target.value;

        redrawGrid(oldHeight, oldWidth, newHeight, oldWidth, template);

        oldHeight = newHeight;
    });

    widthInput.addEventListener('input', (event) => {
        const newWidth = event.target.value;

        redrawGrid(oldHeight, oldWidth, oldHeight, newWidth, template);

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
                const gridItemTemplate = template.content.firstElementChild.cloneNode(true);
                const td = document.createElement('td');
                td.classList.add('grid');
                td.classList.add('droptarget');
                td.appendChild(gridItemTemplate);
                td.draggable = false;
                row.appendChild(td);
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
                const gridItemTemplate = template.content.firstElementChild.cloneNode(true);
                const td = document.createElement('td');
                td.classList.add('grid');
                td.classList.add('droptarget');
                td.appendChild(gridItemTemplate);
                td.draggable = false;
                row.appendChild(td);
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

    attachListeners();

}

function attachListeners() {


    const sources = document.querySelectorAll(".draggable");
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

                if (event.target.classList.contains("dragging")) {
                    event.target.classList.remove("dragging");
                }
            }
        )
    });

    const targets = document.querySelectorAll(".droptarget");
    targets.forEach(target => {
        target.addEventListener(
            "dragover",
            (event) => {
                // prevent default to allow drop
                event.preventDefault();
            }
        )
    });
    //
    // targets.forEach(target => {
    //     target.addEventListener(
    //         "dragleave",
    //         (event) => {
    //             // prevent default to allow drop
    //             event.preventDefault();
    //             if (event.target !== event.currentTarget) {
    //                 return;
    //             }
    //             setTimeout(
    //                 (div) => {
    //                     div.classList.remove("highlight");
    //
    //                 },
    //                 800,
    //                 event.currentTarget.firstElementChild
    //             )
    //         }
    //     )
    // });

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

                // div.draggable = false;
                // div.classList.remove("draggable")
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

                        const parent = event.target.parentElement;
                        parent.innerHTML = "";
                        const templateElement = document.querySelector("#empty-item-template").content.firstElementChild.cloneNode(true);;

                        const row = parent.closest("tr").rowIndex;
                        const col = parent.cellIndex;
                        const maxRow = document.querySelector("#grid").rows.length;
                        const maxCol = document.querySelector("#grid").rows[0].cells.length;

                        parent.appendChild(templateElement);
                        if (row === 0 || row === maxRow - 1 || col === 0 || col === maxCol - 1) {
                            parent.querySelector('span').innerText = '1'
                        } else {
                            parent.querySelector('span').innerText = '0'
                        }

                        if (event.target.classList.contains("dragging")) {
                            event.target.classList.remove("dragging");
                        }
                    }
                )


                event.currentTarget.innerHTML = "";
                event.currentTarget.appendChild(div);
            }
        )
    });

    //assigning drop target event listener to compile the string at each drop
    targets.forEach(target => {
        target.addEventListener('drop', (event) => {

            let mapCompact = compileGridString();

            console.log(mapCompact);
            localStorage.setItem("mapCompact", mapCompact);
            localStorage.setItem("mapHeight", document.querySelector('#height').value);
            localStorage.setItem("mapWidth", document.querySelector('#width').value);

        });
    });
}