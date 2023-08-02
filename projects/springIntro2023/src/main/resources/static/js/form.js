document.addEventListener('DOMContentLoaded', () => {
    const heightLabel = document.querySelector('[for=height]')
    const widthLabel = document.querySelector('[for=width]')

    const heightInput = document.querySelector('#height')
    const widthInput = document.querySelector('#width')

    heightLabel.innerText = "Height: " + heightInput.value
    widthLabel.innerText = "Width: " + widthInput.value

    localStorage.setItem('mapHeight', heightInput.value)
    localStorage.setItem('mapWidth', widthInput.value)

    heightInput.addEventListener('input', () => {
        heightLabel.innerText = "Height: " + heightInput.value
        localStorage.setItem('mapHeight', heightInput.value)
    });

    widthInput.addEventListener('input', () => {
        widthLabel.innerText = "Width: " + widthInput.value
        localStorage.setItem('mapWidth', widthInput.value)
    });
});