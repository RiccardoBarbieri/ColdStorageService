export class DraggableBlock {

    element;

    constructor(element) {
        this.element = element;
        this.element.addEventListener('dragstart', (event) => {
            event.preventDefault();

            event.target.classList.add("dragging");
            event.dataTransfer.setData('application/json', JSON.stringify(this))
        });

        this.element.addEventListener('dragend', (event) => {
            event.preventDefault();

            if (event.target.classList.contains("dragging")) {
                event.target.classList.remove("dragging");
            }
        });
    }

    //declare method to print to console the name of the element
    test() {
        console.log(this)
    }
}