var websocket;

function initWebSocket() {
    var uri = "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
                    (document.location.port === "" ? "8080" : document.location.port);

    var wsUri = uri + "/cardgame/play";

    websocket = new WebSocket(wsUri);

    var self = this;
    websocket.onopen = function(evt) {
    };
    websocket.onmessage = function(evt) {
        self.cardGame.handleServerResponse(evt);
    };
    websocket.onerror = function(evt) {
        onError(evt);
    };
}

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    $("#output").val(message);
}


var moduleCache = {
    // Create this closure to contain the cached modules
    module: function() {
        // Internal module cache.
        var modules = {};

        // Create a new module reference scaffold or load an
        // existing module.
        return function(name) {
            // If this module has already been created, return it.
            if (modules[name]) {
                return modules[name];
            }

            // Create a module and save it under this name
            return modules[name] = {Views: {}};
        };
    }()
};

(function(models) {
    models.ActionModel = Backbone.Model.extend({
        initialize: function() {
            this.on("change:time", function() {
                $("#timeID").val(this.get("time"));
            });
        }
    });
    models.GameStatusModel = Backbone.Model.extend({
        urlRoot: '/cardgame/webresources/status',
        defaults: {
            status: ''
        },
        initialize: function() {
            this.fetch({
                success: function(status) {
                    alert(status.get("status"));
                }
            });
        }
    });

    models.PlayerModel = Backbone.Model.extend({
        defaults: {
            playerId: '',
            playedCard: 'b1fv',
            currentPlayer: false
        }
    });

    models.CardModel = Backbone.Model.extend({});

    models.CardList = Backbone.Collection.extend({
        model: models.CardModel
    });

    models.PlayerList = Backbone.Collection.extend({
        model: models.PlayerModel
    });


})(moduleCache.module("models"));

(function(views) {
    views.CardListView = Backbone.View.extend({
        el: $("#cardList"),
        collection: cardList,
        initialize: function() {
            this.collection = new models.CardList();
            this.collection.add([
                {card: "b1fv"},
                {card: "b1fv"},
                {card: "b1fv"},
                {card: "b1fv"},
                {card: "b1fv"}
            ]);
            this.showCardList();
        },
        // PUBLIC
        updateCardList: function(cardList) {
            this.removeCardList();
            this.collection.reset();
            for (var i = 0; i < cardList.length; i++) {
                this.collection.add({card: cardList[i]});
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
            var template = _.template($("#tpl-card").html(), {card: model.get("card") + ".png"});
            this.$el.append(template);
        },
        removeCardList: function() {
            $("#cardList").empty();
        }
    });

    views.PlayerListView = Backbone.View.extend({
        el: $("#playerList"),
        collection: playerList,
        initialize: function() {
            this.collection = new models.PlayerList();
            this.reset();
        },
        reset: function() {
            this.collection.reset();
            for (var i = 1; i <= 5; i++) {
                this.collection.add(new models.PlayerModel({"playerId": ""}));
            }
            this.showPlayerList();
        },
        updatePlayer: function(id, card) {
            var playerFound = false;
            var currentPlayer;
            this.collection.each(function(player) {
                if (id === player.get("playerId")) {
                    player.set({"playedCard": card, "currentPlayer": true});
                    playerFound = true;
                    currentPlayer = player;
                }
            });
            if (!Boolean(playerFound)) {
                this.collection.each(function(player) {
                    if (!Boolean(playerFound) && '' === player.get("playerId")) {
                        player.set({"playedCard": card, "playerId": id, "currentPlayer": true});
                        playerFound = true;
                        currentPlayer = player;
                    }
                });
            }
            this.showPlayerList();
            currentPlayer.set({"currentPlayer": false})
            return this;
        },
        showPlayerList: function() {
            this.removePlayerList();
            var self = this;
            this.collection.each(function(player) {
                self.createPlayer(player);
            });
            return this;
        },
        createPlayer: function(player) {
            var currentPlayerStyle = '';
            if (Boolean(player.get("currentPlayer"))) {
                currentPlayerStyle = 'playerBold';
            }
            var template = _.template($("#tpl-player").html(),
                    {
                        playerId: player.get("playerId"),
                        playerCard: player.get("playedCard") + ".png",
                        playerStyle: currentPlayerStyle
                    }
            );
            this.$el.append(template);
        },
        removePlayerList: function() {
            $("#playerList").empty();
        }
    });
})(moduleCache.module("views"));



var models = moduleCache.module("models");
var views = moduleCache.module("views");

var CardGameRouter = Backbone.Router.extend({
    initialize: function() {
        this.action = new models.ActionModel();
        this.cardListView = new views.CardListView();
        this.playerListView = new views.PlayerListView();
    },
    login: function() {
        var json = "{\"type\":\"loginreq\"}";
        websocket.send(json);
    },
    join: function() {
        var gameId = document.getElementById("gameID").value;
        if (gameId === '') {
            alert("Add a game ID");
        } else {
            var json = "{\"type\":\"joinreq\",\"gameId\":\"" + gameId + "\"}";
            websocket.send(json);
        }
    },
    startGame: function() {
        var json = "{\"type\":\"startreq\"}";
        websocket.send(json);
    },

    deal: function() {
        var json = "{\"type\":\"dealreq\"}";
        websocket.send(json);
    },
    stopGame: function() {
        var json = "{\"type\":\"stopreq\"}";
        websocket.send(json);
    },
    playCard: function(img) {
        var json = "{\"type\":\"playcardreq\",\"card\":\""+img.src+"\"}";
        websocket.send(json);
        $(img).hide();
    },
    status: function() {
        new models.GameStatusModel();
    },
    handleServerResponse: function(evt) {
        this.action.set(JSON.parse(evt.data));
        var actionType = this.action.get("type");
        if (actionType === "handres") {
            $("#scoreID").val(this.action.get("score"));
            var cardList = [];
            cardList = this.action.get("cardList");
            this.cardListView.updateCardList(cardList);
            this.playerListView.reset();
        } else if (actionType === "loginres") {
            $("#messageID").val(this.action.get("message"));
        } else if (actionType === "playcardres") {
            this.playerListView.updatePlayer(this.action.get("gameId"), this.action.get("card"));
        }
    }
});

Backbone.history.start();


var cardGame = new CardGameRouter();

initWebSocket();



