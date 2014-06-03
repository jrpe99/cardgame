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

var ActionModel = Backbone.Model.extend({
    initialize: function(){
        this.on("change:time", function(model){
            document.getElementById("timeID").value = this.get("time");
        });
    }    
});
var action = new ActionModel();    


var HandModel = Backbone.Model.extend({});
var hand = new HandModel();

var HandView = Backbone.View.extend({
    el: $("#hand"),
    initialize: function(){
        this.render();
    },
    render: function(){
        // Compile the template using underscore
        var template = _.template( $("#tpl-hand").html(), {} );
        // Load the compiled HTML into the Backbone "el"
        this.$el.html( template );
    }
});
var handView = new HandView();

var CardModel = Backbone.Model.extend({});
var CardList = Backbone.Collection.extend({
    model: CardModel
});
var CardListView = Backbone.View.extend({
    el: $("#cardList"),
    collection: cardList,
    initialize: function() {
        this.collection = new CardList();
        this.collection.add([
            {card : "b1fv"},
            {card : "b1fv"},
            {card : "b1fv"},
            {card : "b1fv"},
            {card : "b1fv"}
        ]);
        this.showCardList();
    },
    // PUBLIC
    updateCardList: function(cardList) {
        this.hideCardList();
        this.collection.reset();
        for(var i = 0; i < cardList.length; i++) {
            this.collection.add({card : cardList[i]});
        }
        this.showCardList();
        return this;
    },
    showCardList: function() {
        var self = this;
        this.collection.each(function(model) {
            self.createCard(model);
        });
        return this;
    },
    // PRIVATE
    createCard: function(model) {
        var template = _.template( $("#tpl-card").html(), {card : model.get("card") + ".png"} );
        this.$el.append(template);
    },
    removeCard: function(model) {
        var template = _.template( $("#tpl-cardClear").html(), {} );
        this.$el.html(template);
    },
    hideCardList: function() {
        var self = this;
        this.collection.each(function(model) {
            self.removeCard(model);
        });
    }
});
var cardListView = new CardListView();

function do_login() {
    username = getParam("name");
    writeToScreen("name: " + username);
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) {
        login();
    };
    websocket.onmessage = function(evt) {
        action.set(JSON.parse(evt.data));
        handleResponse();
    };
    websocket.onerror = function(evt) {
        onError(evt);
    };
}

function handleResponse() {
    var actionType = action.get("type");
    if (actionType === "handres") {
        //writeToScreen(evt.data);
        document.getElementById("scoreID").value = action.get("score");
        var cardList = [];
        cardList = action.get("cardList");
        cardListView.updateCardList(cardList);
/*
        for(var i = 1; i <= cardList.length; i++) {
            document.getElementById("card"+i).src = "cards/" + cardList[i-1] + ".png";
        }
        */
    }
    if (actionType === "loginres") {
        document.getElementById("loginId").value = action.get("loginId");
        document.getElementById("messageID").value = action.get("message");
    }
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
