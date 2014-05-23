var wsUri = getRootUri() + "/cardgame/play";
var output;
var debug = true;
var websocket;
var separator = ":";
var id = 0;

function getRootUri() {
    return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port == "" ? "8080" : document.location.port);
}

function init() {
    document.getElementById("loginMessage").value = "json.message";
    output = document.getElementById("output");
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        login();
    };
    websocket.onmessage = function (evt) {
        handleResponse(evt);
    };
    websocket.onerror = function (evt) {
        onError(evt);
    };
}

function handleResponse(evt) {
    writeToScreen(evt.data);
    var json = JSON.parse(evt.data);
    if (json.type === "loginres") {
        if(json.loggedIn === true) {
            to_cardgame();
        }
    }
}

function login() {
}

function doLogin() {
    var json = "{\"type\":\"loginreq\",\"loginId\":\""+document.getElementById("loginId").value+"\"}";
    websocket.send(json);
    //window.setTimeout('to_cardgame()', 10);
}

function to_cardgame() {
    var link = "cardgame.html?name=" + document.getElementById("loginId").value;
    window.location = link;
}


function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    if (debug) {
        var pre = document.createElement("p");
        pre.style.wordWrap = "break-word";
        pre.innerHTML = message;
        output.appendChild(pre);
    }
}

window.addEventListener("load", init, false);
