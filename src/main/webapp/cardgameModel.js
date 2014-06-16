(function(models) {
    models.ActionModel = Backbone.Model.extend({
        initialize: function() {
            this.on("change:time", function() {
                $("#timeID").val(this.get("time"));
            });
        },
        joinAction: function() {
            var gameId = $("#gameID").val();
            if (gameId === '') {
                alert("Add a game ID");
            } else {
                this.set({"type":"joinreq", "gameId":gameId});
                websocket.send(JSON.stringify(this));
            }
            return this;
        },
        dealAction: function() {
            this.set({"type":"dealreq"});
            websocket.send(JSON.stringify(this));
            return this;
        },
        startGameAction: function() {
            this.set({"type":"startreq"});
            websocket.send(JSON.stringify(this));
            return this;
        },
        stopAction: function() {
            this.set({"type":"stopreq"});
            websocket.send(JSON.stringify(this));
            return this;
        },
        playCard: function(img) {
            this.set({"type":"playcardreq","card":img.src});
            websocket.send(JSON.stringify(action));
            $(img).hide();
            return this;
        }
    });
    
    models.GameStatusModel = Backbone.Model.extend({
        urlRoot: '/cardgame/webresources/status',
        defaults: {
            status: ''
        },
        getStatus: function() {
            this.fetch({
                success: function(status) {
                    alert(status.get("status"));
                }
            });
            return this;
        }
    });

    models.PlayerModel = Backbone.Model.extend({
        defaults: {
            playerId: '',
            playedCard: 'empty',
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
