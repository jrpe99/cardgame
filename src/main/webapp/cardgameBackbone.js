var ActionModel = Backbone.Model.extend({
    initialize: function(){
        this.on("change:time", function(){
            $("#timeID").val(this.get("time"));
        });
    }    
});

var GameStatusModel = Backbone.Model.extend({
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
        this.removeCardList();
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
    removeCardList: function() {
        $("#cardList").empty();
    }
});

var PlayerModel = Backbone.Model.extend({
    defaults: {
        playerId : '',
        playedCard : 'b1fv'
    }
});
var PlayerList = Backbone.Collection.extend({
    model: PlayerModel
});

var PlayerListView = Backbone.View.extend({
    el: $("#playerList"),
    collection: playerList,
    initialize: function() {
        this.collection = new PlayerList();
        this.reset();
    },
    // PUBLIC
    reset: function() {
        this.collection.reset();
        for(var i = 1;i<=5;i++) {
            this.collection.add(new PlayerModel({"playerId" : ""}));
        }
        this.showPlayerList();
    },
    updatePlayer: function(id, card) {
        var self = this;
        var playerFound = false;
        this.collection.each(function(player) {
            if(id === player.get("playerId")) {
                player.set({"playedCard":card});
                playerFound = true;
            }
        });
        if(!Boolean(playerFound)) {
            this.collection.each(function(player) {
                if(!Boolean(playerFound) && '' === player.get("playerId")) {
                    player.set({"playedCard":card});
                    player.set({"playerId":id});
                    playerFound = true;
                }
            });
        }
        this.showPlayerList();
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
    // PRIVATE
    createPlayer: function(player) {
        var template = _.template( $("#tpl-player").html(), 
            {
                playerId : player.get("playerId"), 
                playerCard : player.get("playedCard") + ".png"
            } 
        );
        this.$el.append(template);
    },
    removePlayerList: function() {
        $("#playerList").empty();
    }
});
