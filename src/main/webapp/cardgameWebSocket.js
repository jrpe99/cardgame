function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    $("#output").val(message);
}

function initWebSocket(cardListView, playerListView) {
    var uri = "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
                    (document.location.port === "" ? "8080" : document.location.port);

    var action = new models.ActionModel();
    var wsUri = uri + "/cardgame/play";
    websocket = new WebSocket(wsUri);

    websocket.onopen = function(evt) {
    };
    websocket.onmessage = function(evt) {
        action.set(JSON.parse(evt.data));
        var actionType = action.get("type");

        if (actionType === "handres") {
            $("#scoreID").val(action.get("score"));
            var cardList = [];
            cardList = action.get("cardList");
            cardListView.updateCardList(cardList);
            playerListView.reset();
        } else if (actionType === "loginres") {
            $("#messageID").val(action.get("message"));
        } else if (actionType === "playcardres") {
            playerListView.updatePlayer(action.get("gameId"), action.get("card"));
        }
    };
    websocket.onerror = function(evt) {
        onError(evt);
    };
}