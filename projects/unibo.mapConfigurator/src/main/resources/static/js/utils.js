export function hasKey(map, value) {
    for (const key of map.keys()) {
        if (key === value) {
            return true;
        }
    }
    return false;
}

export function compileBlockMap(blocks) {
    const blockMap = new Map();
    for (let i = 0; i < blocks.length; i++) {
        const block = blocks[i].cloneNode(true);
        const blockId = block.firstElementChild.innerText;
        blockMap.set(blockId, block);
    }
    return blockMap;
}

export function restoreBlocks(grid, blockMap) {

    const mapCompact = localStorage.getItem('mapCompact');
    const newHeight = localStorage.getItem('mapHeight');
    const newWidth = localStorage.getItem('mapWidth');

    for (let i = 0; i < newHeight; i++) {
        for (let j = 0; j < newWidth; j++) {
            if (hasKey(blockMap, mapCompact[i * newWidth + j])) {
                grid.children[i].children[j].innerHTML = '';
                grid.children[i].children[j].appendChild(blockMap.get(mapCompact[i * newWidth + j]).cloneNode(true));
            }
        }
    }
}

export function newRow(row, template) {
    const gridItemTemplate = template.content.firstElementChild.cloneNode(true);
    const td = document.createElement('td');
    td.classList.add('grid');
    td.classList.add('droptarget');
    td.appendChild(gridItemTemplate);
    td.draggable = false;
    row.appendChild(td);
}

export function compileGridString(grid) {

    //scan grid and compile string to store or send to server

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