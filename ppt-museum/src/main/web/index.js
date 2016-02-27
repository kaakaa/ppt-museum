var container = document.getElementById("pdf-container");

var PDFController = require("pdf.js-controller");
var controller = new PDFController({
    container: container,
    pdfjsDistDir: "/ppt-museum/"
});

controller.loadDocument(PDFURL)
    .then(initializedEvent)
    .catch(function (error) {
    console.log(error);
});

container.addEventListener(PDFController.Events.before_pdf_rendering, function (event) {
    // before render
});
container.addEventListener(PDFController.Events.after_pdf_rendering, function (event) {
    // after render
});

function initializedEvent() {
    document.getElementById('js-prev').addEventListener('click', controller.prevPage.bind(controller));
    document.getElementById('js-next').addEventListener('click', controller.nextPage.bind(controller));

    window.addEventListener("resize", function (event) {
        controller.fitItSize();
    });
    document.onkeydown = function (event) {
        var kc = event.keyCode;
        if (event.shiftKey || event.ctrlKey || event.metaKey) {
            return;
        }
        if (kc === 37 || kc === 40 || kc === 75 || kc === 65) {
            // left, down, K, A
            event.preventDefault();
            controller.prePage();
        } else if (kc === 38 || kc === 39 || kc === 74 || kc === 83) {
            // up, right, J, S
            event.preventDefault();
            controller.nextPage();
        }
    };
}

