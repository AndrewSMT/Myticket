$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})
function myFunction() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", "/ticket/timeout", false ); // false for synchronous request
    return xmlHttp.responseText;
}

