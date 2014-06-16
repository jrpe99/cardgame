(function(views) {
    views.GameStatusView = Backbone.View.extend({
        el: $("#timeID"),
        updateTime: function(time) {
            $("#timeID").html("<b>Time: " + time + "</b>");
        },
        updateMessage: function(msg) {
            $("#messageID").html("<b>" + msg + "</b>");
        }
    });
    
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
            return this;
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
            return this;
        },
        removePlayerList: function() {
            $("#playerList").empty();
        }
    });

})(moduleCache.module("views"));


// Action views (buttons)
(function (views) {
    views.JoinActionView = Backbone.View.extend({
        el: $("#joinAction"),
        initialize: function(){
            this.joinView();
        },
        events: {
            "click #joinButton": "joinAction"
        },
        joinAction: function() {
            new models.ActionModel().joinAction();
            return this;
        },
        joinView: function() {
            $(this.el).html("<button id='joinButton' value='join'>Join</button>");            
        }
    });
    views.StartGameActionView = Backbone.View.extend({
        el: $("#startGameAction"),
        initialize: function(){
            this.startGameView();
        },
        events: {
            "click #startGameButton": "startGameAction"
        },
        startGameAction: function() {
            new models.ActionModel().startGameAction();
            return this;
        },
        startGameView: function() {
            $(this.el).html("<button id='startGameButton' value='start'>Start</button>");            
        }
    });
    views.DealActionView = Backbone.View.extend({
        el: $("#dealAction"),
        initialize: function(){
            this.dealView();
        },
        events: {
            "click #dealButton": "dealAction"
        },
        dealAction: function() {
            new models.ActionModel().dealAction();
            return this;
        },
        dealView: function() {
            $(this.el).html("<button id='dealButton' value='deal'>Deal</button>");            
        }
    });
    views.StopGameActionView = Backbone.View.extend({
        el: $("#stopGameAction"),
        initialize: function(){
            this.stopView();
        },
        events: {
            "click #stopButton": "stopAction"
        },
        stopAction: function() {
            new models.ActionModel().stopAction();
            return this;
        },
        stopView: function() {
            $(this.el).html("<button id='stopButton' value='stop'>Stop</button>");            
        }
    });
    views.StatusActionView = Backbone.View.extend({
        el: $("#statusAction"),
        initialize: function(){
            this.statusView();
        },
        events: {
            "click #statusButton": "statusAction"
        },
        statusAction: function() {
            new models.GameStatusModel().getStatus();
            return this;
        },
        statusView: function() {
            $(this.el).html("<button id='statusButton' value='status'>Status</button>");            
        }
    });
    
})(moduleCache.module("views"));