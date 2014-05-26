var wsUri = getRootUri() + "/cardgame/play";
var output;
var username = "";
var debug = true;
var websocket;
var separator = ":";
var id = 0;

function getRootUri() {
    return "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
            (document.location.port === "" ? "8080" : document.location.port);
}

function init() {
    output = document.getElementById("output");
    id = getParam("id");
    do_login();
}


function do_login() {
    username = getParam("name");
    writeToScreen("name: " + username);
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
    var json = JSON.parse(evt.data);
    if (json.type === "gameTimeRes") {
        document.getElementById("timeID").value = json.time;
    }
    if (json.type === "handres") {
        //writeToScreen(evt.data);
        document.getElementById("scoreID").value = json.score;
        var cardList = [];
        cardList = json.cardList;
        for(var i = 1; i <= cardList.length; i++) {
            document.getElementById("card"+i).src = "cards/" + cardList[i-1] + ".png";
        }
    }
    if (json.type === "loginres") {
        document.getElementById("loginId").value = json.loginId;
        document.getElementById("messageID").value = json.message;
    }

    //writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data + '</span>');
}

function login() {
    var json = "{\"type\":\"loginreq\",\"loginId\":\""+document.getElementById("loginId").value+"\"}";
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
