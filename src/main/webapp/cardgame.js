var wsUri = getRootUri() + "/cardgame/play";
var output;
var debug = true;
var websocket;

// Backbone objects
var action;
var cardListView;

function getRootUri() {
    return "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
            (document.location.port === "" ? "8080" : document.location.port);
}

function init() {
    action = new ActionModel();    
    cardListView = new CardListView();
    do_login();
}

function do_login() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) {
        login();
    };
    websocket.onmessage = function(evt) {
        handleResponse(evt);
    };
    websocket.onerror = function(evt) {
        onError(evt);
    };
}


function handleResponse(evt) {
    action.set(JSON.parse(evt.data));
    var actionType = action.get("type");
    if (actionType === "handres") {
        //writeToScreen(evt.data);
        document.getElementById("scoreID").value = action.get("score");
        var cardList = [];
        cardList = action.get("cardList");
        cardListView.updateCardList(cardList);
    }
    if (actionType === "loginres") {
        document.getElementById("messageID").value = action.get("message");
    }
}

function login() {
    var json = "{\"type\":\"loginreq\"}";
    websocket.send(json);
}

function startGame() {
    var json = "{\"type\":\"startreq\"}";
    websocket.send(json);
}

function deal() {
    var json = "{\"type\":\"dealreq\"}";
    websocket.send(json);
}

function stopGame() {
    var json = "{\"type\":\"stopreq\"}";
    websocket.send(json);
}


function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function isLoggedIn() {
    return (username !== "");
}

function writeToScreen(message) {
    if (debug) {
        var pre = document.createElement("p");
        pre.style.wordWrap = "break-word";
        pre.innerHTML = message;
        output.appendChild(pre);
    }
}

function getParam(sname) {
    var params = location.search.substr(location.search.indexOf("?") + 1);
    var sval = "";
    params = params.split("&");
    // split param and value into individual pieces
    for (var i = 0; i < params.length; i++) {
        var temp = params[i].split("=");
        if (temp[0] === sname) {
            sval = temp[1];
        }
    }
    return sval;
}

window.addEventListener("load", init, false);
